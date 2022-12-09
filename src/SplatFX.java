import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.security.acl.Group;
import java.util.Optional;
import java.util.Scanner;

/**
 * This is the SplatFX assignment.
 * <Description here>
 *
 * @author Nathan Brewer
 * <p>
 * CS1131, 12/9/2022
 * The SplatProject project.
 */
public class SplatFX extends Application {
	public static Splat splat = new Splat();
	//TextField input = new TextField();
	Text output = new Text();
	String inputS;
	@Override
	public void start( Stage primaryStage ) throws Exception {
		Splat.fx = this;
		Pane root = new Pane();
		Scene scene = new Scene( root,800,600 );
		
		
		
		output.setLayoutX( 250 );
		output.setLayoutY( 150 );
		//input.setLayoutX( 100 );
		//input.setLayoutY( 450 );
		
		
		
		/*input.setOnKeyPressed( new EventHandler< KeyEvent >() {
			@Override
			public void handle( KeyEvent event ) {
				if(event.getCode() == KeyCode.ENTER ){inputS = input.getText();}
			}
		} );*/
		
		
		
		
		//root.getChildren().add( input );
		root.getChildren().add( output );
		primaryStage.setScene( scene );
		primaryStage.show();
		run();
	}
	
	public void run(){
		
		TextInputDialog dialog = new TextInputDialog("walter");
		
		
		splat.readFile( );
		Scanner scan = new Scanner( System.in );
		
		boolean alive;
		boolean again = false;
		double timer = 0;
		
		output.setText( "Welcome to SPLAT! -- The game that simulates a parachute jump.\n" +
				"Try to open your chute at the last possible moment without going splat.\n" +
				"\n" +
				"Enter your name: " );
		
		dialog.setTitle("Text Input Dialog");
		dialog.setHeaderText("Look, a Text Input Dialog");
		dialog.setContentText("Please enter name:");
		
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent())
		splat.name = result.get();
		
		do {
			Splat.Planet planet = splat.randomizePlanet();
			splat.planet = planet;
			Splat.Score[] score = {new Splat.Score()};
			output.setText( String.format( "You are skydiving on %s%n"+
					"Altitude = %f meters.%n" +
					"Terminal Velocity = %f meters/second.%n" +
					"Acceleration = %f meters/second/second.%n" +
					"%n" +
					"Set the timer for your freefall.%n" +
					"How many seconds? (floating point) ", planet.planetName, planet.Altitude, planet.terminalVelocity, planet.Acceleration ));
			
			dialog.setTitle("Text Input Dialog");
			dialog.setHeaderText("Look, a Text Input Dialog");
			dialog.setContentText("How many seconds?:");
			
			result = dialog.showAndWait();
			if(result.isPresent())
				timer = Double.parseDouble( result.get() );
			
			output.setText( String.format(  "Here we go.%n%15s%15s%15s%n", "TIME s", "SPEED m/s", "HEIGHT m"  ) );
			
			alive = splat.parachute( timer, score );
			
			if(alive){
				splat.updateScoreboard( score[0] );
				for(int i = 1; i <= splat.Scoreboard.size(); i++){
					Splat.Score s = splat.Scoreboard.get( i-1 );
					if(s.equals( score[0] ))
						output.setText( output.getText() + String.format( "You ranked %d%n%n", i  ) );
				}
			}
			
			output.setText( output.getText()+"Play again? (Y/N) " );
			
			dialog.setTitle("Text Input Dialog");
			dialog.setHeaderText("Look, a Text Input Dialog");
			dialog.setContentText("Play again?(y/n)");
			String inp = "";
			result = dialog.showAndWait();
			if(result.isPresent())
				inp = result.get();
			
			if(inp.contains("y"))
				again = true;
			else if(inp.contains( "n" ))
				again=false;
			
		}while(again);
		
		output.setText( output.getText() + String.format( "Top Jumps:%n%15s%15s%10s%15s%15s%15s%15s%n","#","NAME","PLANET","INITIAL_HEIGHT","TIME","VELOCITY","FINAL_HEIGHT" ) );
		
		for(int i = 1 ; i <= splat.Scoreboard.size(); i++){
			Splat.Score score = splat.Scoreboard.get( i-1 );
			output.setText( output.getText() + String.format( "%15d%15s%10s%15.2f%15.3f%15.3f%15.3f%n",i,score.name,score.planet,score.Initial_Height,score.Time,score.Velocity,score.Final_Height ) );
		}
		
		splat.outputScoreboard();
		
		scan.close();
		
	}

}
