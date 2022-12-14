import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * This is the Splat assignment.
 * <Description here>
 *
 * @author Nathan Brewer
 * <p>
 * CS1131, 12/8/2022
 * The SplatProject project.
 */
public class Splat {
	
	private static String[] planetNames = {"Earth","Venus","Saturn","Jupiter","Neptune","Uranus","Pluto","Mercury","Mars"};
	
	public Planet planet;
	public String name;
	public ArrayList<Score> Scoreboard = new ArrayList<>();
	public static SplatFX fx = new SplatFX();
	
	public void readFile(){
		try(Scanner scan = new Scanner( new File( "Scoreboard.txt" ) )) {
		while ( scan.hasNextLine() ){
			String[] line = scan.nextLine().split( "," );
			Scoreboard.add( new Score(
					line[0],
					line[1],
					Double.parseDouble( line[2] ),
					Double.parseDouble( line[3] ),
					Double.parseDouble( line[4] ),
					Double.parseDouble( line[5] )) );
		}
		} catch ( FileNotFoundException e ) {
			throw new RuntimeException( "Please Create a Scoreboard.txt file in directory." );
		}
	}
	
	/**
	 * @return a planet
	 */
	public Planet randomizePlanet(){
		return new Planet(
				planetNames[ (int) (Math.abs( new Random().nextDouble() * planetNames.length) % planetNames.length-1) ],
				Math.abs( new Random().nextDouble() * 500 ),
				Math.abs( new Random().nextDouble() * 25 ),
				Math.abs( new Random().nextDouble() * 10000 ));
	}
	
	public void run(){
		readFile( );
		Scanner scan = new Scanner( System.in );
		
		boolean alive;
		boolean again = false;
		double timer;
		
		System.out.print("Welcome to SPLAT! -- The game that simulates a parachute jump.\n" +
				"Try to open your chute at the last possible moment without going splat.\n" +
				"\n" +
				"Enter your name: ");
		
		
		name = scan.nextLine();
		
		do {
			planet = randomizePlanet();
			Score[] score = {new Score()};
			System.out.printf( "You are skydiving on %s%n"+
					"Altitude = %f meters.%n" +
					"Terminal Velocity = %f meters/second.%n" +
					"Acceleration = %f meters/second/second.%n" +
					"%n" +
					"Set the timer for your freefall.%n" +
					"How many seconds? (floating point) ", planet.planetName, planet.Altitude, planet.terminalVelocity, planet.Acceleration );
			timer = scan.nextDouble();
			
			System.out.println( "Here we go." );
			
			System.out.printf( "%15s%15s%15s%n", "TIME s", "SPEED m/s", "HEIGHT m" );
			
			
			alive = parachute( timer, score );
			
			if(alive){
				updateScoreboard( score[0] );
				for(int i = 1; i <= Scoreboard.size(); i++){
					Score s = Scoreboard.get( i-1 );
					if(s.equals( score[0] ))
						System.out.printf( "You ranked %d%n%n", i );
						
				}
			}
			
			System.out.println("Play again? (Y/N) ");
			fx.output.setText( "Play again? (Y/N) " );
			
			String inp = scan.next().toLowerCase();
			inp = fx.inputS.toLowerCase();
			if(inp.contains("y"))
				again = true;
			else if(inp.contains( "n" ))
				again=false;
			
		}while(again);
		
		System.out.printf( "Top Jumps:%n%15s%15s%10s%15s%15s%15s%15s%n","#","NAME","PLANET","INITIAL_HEIGHT","TIME","VELOCITY","FINAL_HEIGHT" );
		fx.output.setText( String.format( "Top Jumps:%n%15s%15s%10s%15s%15s%15s%15s%n","#","NAME","PLANET","INITIAL_HEIGHT","TIME","VELOCITY","FINAL_HEIGHT" ) );
		
		for(int i = 1 ; i <= Scoreboard.size(); i++){
			Score score = Scoreboard.get( i-1 );
			System.out.printf( "%15d%15s%10s%15.2f%15.3f%15.3f%15.3f%n",i,score.name,score.planet,score.Initial_Height,score.Time,score.Velocity,score.Final_Height);
			fx.output.setText( String.format( "%15d%15s%10s%15.2f%15.3f%15.3f%15.3f%n",i,score.name,score.planet,score.Initial_Height,score.Time,score.Velocity,score.Final_Height ) );
		}
		
		outputScoreboard();
		
		scan.close();
	}
	
	/**
	 * Updates the scoreboard arraylist.
	 * @param score score to add to scoreboard.
	 */
	public void updateScoreboard(Score score){
		Scoreboard.add( score );
		selectionsort( Scoreboard,0, Scoreboard.size(),SortOrder.ASCENDING );
	}
	
	/**
	 * overwrites Scoreboard.txt file with data in Scoreboard.
	 */
	public void outputScoreboard(){
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter( "Scoreboard.txt" );
		} catch ( FileNotFoundException e ) {
			throw new RuntimeException( e );
		}
		for(int i = 1 ; i <= Scoreboard.size(); i++){
			Score score = Scoreboard.get( i-1 );
			pw.println( String.format( "%s,%s,%f,%f,%f,%f",score.name,score.planet,score.Initial_Height,score.Time,score.Velocity,score.Final_Height));
		}
		pw.close();
	}
	
	/**
	 * Does the calculations for parachuting.
	 * @param timer the time you open your parachute.
	 * @param score the score you got if you lived.
	 * @return if you survived your fall.
	 */
	public boolean parachute( double timer, Score[] score) {
		double height = planet.Altitude;
		double time = 0;
		double speed = 0;
		int i = 1;
		while(height > 0 && time <= timer){
			System.out.printf("%15f%15f%15f%n",time,speed,height);
			fx.output.setText( fx.output.getText() + String.format( "%15f%15f%15f%n",time,speed,height ) );
			time = (timer / 8 * i);
			speed = time * planet.Acceleration;
			if(speed > planet.terminalVelocity)
				speed = planet.terminalVelocity;
			height = (double)(planet.Altitude - (0.5 * planet.Acceleration * Math.pow(time,2.0)));
			i++;
			
			if(height < 0 && time <= timer){
				System.out.printf("%15f%15f%15s%n",time,speed,"SPLAT!");
				fx.output.setText( fx.output.getText() + String.format( "%15f%15f%15s%n",time,speed,"SPLAT!" ) );
				return false;
			}
			
		}
		i-=2;
		time = (timer / 8 * i);
		speed = time * planet.Acceleration;
		if(speed > planet.terminalVelocity)
			speed = planet.terminalVelocity;
		height = (double)(planet.Altitude - (0.5 * planet.Acceleration * Math.pow(time,2.0)));
		score[0] = new Score(name, planet.planetName, planet.Altitude, timer, planet.terminalVelocity, height);
		return true;
	}
	
	public static void main( String[] args ) {
		new Splat().run();
	}
	
	public class Planet {
		public Planet(  String planetName, double terminalVelocity, double acceleration, double altitude ) {
			this.terminalVelocity = terminalVelocity;
			Acceleration = acceleration;
			Altitude = altitude;
			this.planetName = planetName;
		}
		public String planetName;
		public double terminalVelocity;
		public double Acceleration;
		public double Altitude;
		
		
		public Planet( String planetName, double terminalVelocity, double acceleration ) {
			this.planetName = planetName;
			this.terminalVelocity = terminalVelocity;
			Acceleration = acceleration;
		}
	}
	
	public static class Score implements Comparable {
		public String name;
		public String planet;
		public double Initial_Height;
		public double Time;
		public double Velocity;
		public Double Final_Height;
		
		public Score(String name, String planet, double initial_Height, double time, double velocity, double final_Height ) {
			this.name = name;
			this.planet = planet;
			Initial_Height = initial_Height;
			Time = time;
			Velocity = velocity;
			Final_Height = final_Height;
		}
		
		public Score() {
		
		}
		
		@Override
		public int compareTo( Object o ) {
			Score score = (Score) o;
			return this.Final_Height.compareTo( score.Final_Height );
		}
		
	}

	/**
 	* Order to sort in.
 	* <br><code>Ascending = -1</code>
 	* <br><code>Descending = 1</code>
 	*/
	public enum SortOrder {
		ASCENDING( -1 ),
		DESCENDING( 1 );
		public final int order;
		
		SortOrder( int order ) {
			this.order = order;
		}
		
	}
	
	/**
	 * Sorts a List using Selection sort Algorithm standard.
	 * @param list List of elements to sort.
	 * @param lowindex index to start sorting from. list[lowindex...highindex]. (inclusive)
	 * @param highindex index to stop sorting at. list[lowindex...highindex]. (exclusive)
	 * @param sortOrder order to sort in. [ASCENDING, DESCENDING].
	 * @param <E> Generic type that extends Comparable.
	 */
	public static <E extends Comparable<E>> void selectionsort ( List< E > list, int lowindex, int highindex, SortOrder sortOrder ) {
		if(list == null  || sortOrder == null || (highindex < lowindex)) throw new IllegalArgumentException();
		
		for (int currentIndex = lowindex; currentIndex < highindex - 1; currentIndex++)
		{
			int minIndex = currentIndex;
			for (int i = currentIndex + 1; i < highindex; i++)
				if (list.get( i ).compareTo(list.get( minIndex )) == sortOrder.order)
					minIndex = i;
			if (minIndex != currentIndex)
			{
				E temp = list.get( currentIndex );
				list.set( currentIndex,list.get( minIndex ) );
				list.set( minIndex, temp );
			}
		}
	}
	
	
}
