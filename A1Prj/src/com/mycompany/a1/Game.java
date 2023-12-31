/**
 * File: Game.java
 * Author: Marc Lejano
 * Class: CSC 133-02
 * Professor: Dr. Pınar Muyan-Özçelik
 * Date: 9/24/2023
 */

package com.mycompany.a1;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import java.lang.String;

public class Game extends Form{
	private GameWorld gw;
	public boolean isExiting = false;
	
	public Game() {
		gw = new GameWorld();
		gw.init();
		play();
	}

	private void play() {
	Label myLabel=new Label("Enter a Command:");
	this.addComponent(myLabel);
	final TextField myTextField=new TextField();
	this.addComponent(myTextField);
	this.show();
	myTextField.addActionListener(new ActionListener(){
		
		public void actionPerformed(ActionEvent evt) {
		String sCommand=myTextField.getText().toString();
		myTextField.clear();
		if(sCommand.length() != 0)
			
			if (isExiting) {
				// Handle exit confirmation
                if (sCommand.equals("y")) {
                    System.exit(0);
                } else if (sCommand.equals("n")) {
                	System.out.println("Attempt to exit canceled");
                    isExiting = false; // User canceled exit
                } else {
                    System.out.println("Invalid input, please enter 'y' or 'n'.");
                }
			} else {
			switch (sCommand.charAt(0)) {
			
					// ant speed + accelerationRate(5)
				case 'a':
					gw.accelerate();
					break;
					
					// ant speed - brakeRate(5)
				case 'b':
					gw.brake();
					break;
					
					// -5 ant heading
				case 'l':
					gw.leftTurn();
					break;
					
					// +5 ant heading
				case 'r':
					gw.rightTurn();
					break;
					
					// Sets food consumption rate of ant
				case 'c':
					gw.setFC();
					break;
					
					// '0-9' Pretend ant hits flag of #
				case '1':
					gw.flagHit(1);
					break;
				case '2':
					gw.flagHit(2);
					break;
				case '3':
					gw.flagHit(3);
					break;
				case '4':
					gw.flagHit(4);
					break;
				case '5':
					gw.flagHit(5);
					break;
				case '6':
					gw.flagHit(6);
					break;
				case '7':
					gw.flagHit(7);
					break;
				case '8':
					gw.flagHit(8);
					break;
				case '9':
					gw.flagHit(9);
					break;
					
					// Pretend ant hit food station
				case 'f':
					gw.foodHit();
					break;
					
					// Pretend ant collide spider
				case 'g':
					gw.spidHit();
					break;

					// Advance game clock by 1 tick
				case 't':
					gw.tick();
					break;

					// Display current game/ant state values
				case 'd':
					gw.display();
					break;
					
					// Map current world to console
				case 'm':
					gw.map();
					break;
					
					// Attempt and confirm exit
				case 'x':
					isExiting = gw.exit();
					break;
					
				// Handles unknown commands
                default:
                    System.out.println("Unknown command: " + sCommand);
                    break;
					
				//add code to handle rest of the commands
	} //switch
	
	}// else
	
	} //actionPerformed
		
	} //new ActionListener()
	
	); //addActionListener
	
	} //play

}
