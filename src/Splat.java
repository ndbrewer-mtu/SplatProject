import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

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
	
	private Planet planet;
	
	private ArrayList<String> readFile(String path){
	return null;
	}
	
	public void run(){
		Scanner inp = new Scanner( System.in );
		String name;
		float timer;
		
		System.out.println("Welcome to SPLAT! -- The game that simulates a parachute jump.\n" +
				"Try to open your chute at the last possible moment without going splat.\n" +
				"\n" +
				"Enter your name: ");
		
		name = inp.nextLine();
		
		System.out.printf("%n" +
				"You are skydiving on %s.%n" +
				"Altitude = %f meters.%n" +
				"Terminal Velocity = %f meters/second.%n" +
				"Acceleration = %f meters/second/second.%n" +
				"%n" +
				"Set the timer for your freefall.%n" +
				"How many seconds? (floating point) ");
		
		timer = inp.nextFloat();
		
		System.out.println("Here we go.");
		
		boolean alive = parachute(timer);
		
		inp.close();
	}
	
	private boolean parachute( float timer ) {
		return false;
	}
	
	public static void main( String[] args ) {
		new Splat().run();
	}
	
	private class Planet {
		public Planet(  String planetName, float terminalVelocity, float acceleration, float altitude ) {
			this.terminalVelocity = terminalVelocity;
			Acceleration = acceleration;
			Altitude = altitude;
			this.planetName = planetName;
		}
		public String planetName;
		public float terminalVelocity;
		public float Acceleration;
		public float Altitude;
		
		
		
	}
}
