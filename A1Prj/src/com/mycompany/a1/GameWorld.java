/**
 * File: GameWorld.java
 * Author: Marc Lejano
 * Class: CSC 133-02
 * Professor: Dr. Pınar Muyan-Özçelik
 * Date: 9/24/2023
 */

package com.mycompany.a1;
import java.lang.Math;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import com.codename1.charts.models.Point;
import com.codename1.charts.util.ColorUtil;

public class GameWorld {
	//Declare objects as instance variables
	//private Ant player;
	private Random rand = new Random();
	private List<GameObject> gameObjects; // List to store different types of game objects
	
	// Clock used to keep track of game time ticks
	public int clock = 0;
	public int lives;
	public void init() {
		lives = 3 ;
		int numFoodStations = 5;
		int numSpiders = 5;
		
		// Code here to create the initial game objects/setup
		gameObjects = new ArrayList<>(); // Initializes the List
		
		// Create and initialize different types of game objects and add them to the List
        Ant player = new Ant();
        
        // Initialized 4 flags at set locations with a size of 7 and succeeding sequence numbers
        	//flag1 set at (250,250)
        Point Location1 = new Point(250, 250);
        Flag flag1 = new Flag(7, Location1, 1);
        
      //flag1 set at (500,250)
        Point Location2 = new Point(500, 250);
        Flag flag2 = new Flag(7, Location2, 2);
        
        //flag1 set at (250,500)
        Point Location3 = new Point(250, 500);
        Flag flag3 = new Flag(7, Location3, 3);
        
        //flag1 set at (250,700)
        Point Location4 = new Point(250, 700);
        Flag flag4 = new Flag(7, Location4, 4);
        
        // Generate food stations with random sizes, random locations and capacity is based on size. Adds them to gameObject array list
        for (int i = 0; i < numFoodStations; i++) {
        	float randomX = rand.nextInt(1001); // Random X coord
        	float randomY = rand.nextInt(1001); // Random Y coord 
        	int randomSize = (10 + rand.nextInt(51));  // Random Size 10-50
        	
        	Point location = new Point(randomX, randomY);
        	FoodStation foodstation = new FoodStation(randomSize, location);
        	gameObjects.add(foodstation);
        }

        // Generate spiders with random sizes and random locations. Adds them to gameObject array list
        for (int i = 0; i < numSpiders; i++) {
        	int randomX = rand.nextInt(1001); // Random X coord
        	int randomY = rand.nextInt(1001); // Random Y coord 
        	int randomSize = (15 + rand.nextInt(31));  // Random Size 10-30
        	
        	Point location = new Point(randomX, randomY);
        	Spiders spider = new Spiders(randomSize, location);
        	gameObjects.add(spider);
        }

        
        
        
        
        
        
        
        // Adds objects to arrayList
        gameObjects.add(player);
        gameObjects.add(flag1);
        gameObjects.add(flag2);
        gameObjects.add(flag3);
        gameObjects.add(flag4);
        
	}
	// Main GameObject class, Grandfather of most
	public abstract class GameObject{		
		private int size;
		private int shape;
		private Point location = new Point();
		private int color;
		
		// Constructor Set up for pass through
		public GameObject(int inSize) {
			this.size = inSize;
		}
		
		public int getSize() {
			return size;
		}
		
		public int getShape() {
			shape = this.size * this.size;
			return shape;
		}
		public void setShape(int shape) {
			this.shape = shape;
		}
		
				
		public Point getLocation() {
			return location;
		}
		public void setLocation(Point location) {
			this.location = location;
		}

		public int getcolor() {
			return color;
		}

		public void setColor(int color) {
			this.color = color;
		}
		
		// toString Override
		public String toString() {
			// Round the digits for the locations on output
			double roundedX = Math.round(location.getX()*10.0) / 10.0;
			double roundedY = Math.round(location.getY()*10.0) / 10.0;
			
			String colorString = "color=["+ ColorUtil.red(color) + "," + ColorUtil.green(color) + "," + ColorUtil.blue(color) + "]";
			String locationString = "Loc=" + roundedX + ", " + roundedY + " ";
			
			return locationString + colorString;
		}
		

	}
	
	// Fixed subclass, can not set location post constructor
	public abstract class Fixed extends GameObject{
		public Fixed(int inSize, Point location) {
			super(inSize);	// Passes size up to parent object
			super.setLocation(location); // Passes location up to parent object
		}
		//null body override. Removes ability to set location for fixed objects
		public final void setLocation(Point na) {
			// method left empty so child classes can not change location
		}
		
	}

	// Movable subclass main differences are Heading, Speed, FoodLevel, Move(), and a changeable location
	public abstract class Movable extends GameObject{
		private int heading;
		private int speed;
		private int foodLevel;
		
		//Constructor sets up grandparent constructor pass through
		public Movable(int inSize) {
			super(inSize); // Passes size up to parent object
		}
		
		public int getHeading() {
			return heading;
		}
		public void setHeading(int heading) {
			this.heading = heading;
		}
		
		public int getSpeed() {
			return speed;
		}
		public void setSpeed(int speed) {
			this.speed = speed;
		}
		
		public int getFoodLevel() {
			return foodLevel;
		}
		public void setFoodLevel(int foodLevel) {
			this.foodLevel = foodLevel;
		} 
		
		//move method available to all movable sub objects
		public void move(Point oldLocation, int heading, int speed) {
			
			double deltaX = Math.cos(Math.toRadians(90 - heading))*speed;
			double deltaY = Math.sin(Math.toRadians(90 - heading))*speed;			
			
			float newCoordX = (float) (oldLocation.getX() + deltaX);
			float newCoordY = (float) (oldLocation.getY() + deltaY);
			
			Point newLocation = new Point();
			newLocation.setX(newCoordX);
			newLocation.setY(newCoordY);
			
			// Sets location of object to new location
			this.setLocation(newLocation);
			
		}
		
	}
	
	public interface iFoodie{
		public void setFoodConsumption(int amount);
	}
	
	public class Ant extends Movable implements iFoodie{
		private int maximumSpeed;
		private int foodConsumptionRate;
		private int healthLevel;
		private int lastFlagReached;
		
		// Default starting values. Notice constants
		private static final int antSize = 4;
		private static final int antFoodConsumptionRate = 2;
		private static final int startingHealth = 10;
		private static final int initialAntMaxSpeed = 100;
		private static final int initialFlag = 1;
		private static final int initialHeading = 0;
		private static final int initialSpeed = 4;
		private static final int initialFoodLevel = 15;
		private int antColor = ColorUtil.rgb(153, 0, 0);
		
		// Constructor initializes Ant object
		public Ant() {
			// Sets initial values
			super(antSize);
			this.foodConsumptionRate=antFoodConsumptionRate;
			this.setMaximumSpeed(initialAntMaxSpeed);
			this.setHealthLevel(startingHealth);
			this.setLastFlagReached(initialFlag);
			this.setHeading(initialHeading);
			this.setSpeed(initialSpeed);
			this.setColor(antColor);
			this.setFoodLevel(initialFoodLevel);
		}
		
		public int getMaximumSpeed() {
			return maximumSpeed;
		}
		
		public void setMaximumSpeed(int maximumSpeed) {
			this.maximumSpeed = maximumSpeed;
		}
		
		public int getFoodConsumptionRate() {
			return foodConsumptionRate;
		}

		public int getHealthLevel() {
			return healthLevel;
		}

		public void setHealthLevel(int healthLevel) {
			this.healthLevel = healthLevel;
		}

		public int getLastFlagReached() {
			return lastFlagReached;
		}

		public void setLastFlagReached(int lastFlagReached) {
			this.lastFlagReached = lastFlagReached;
		}

		@Override
		public void setFoodConsumption(int amount) {
			this.foodConsumptionRate=amount;
			
		}
		
		
		
	}
	
	public class Spiders extends Movable{
		// Initial Values
		private int spiderHeading = rand.nextInt(360);
		private int spiderColor = ColorUtil.rgb(0, 0, 153);
		
		// Constructor
		public Spiders(int inSize, Point location) {
			super(inSize); // Passes size up to parent object
			
			// Initializes speed of 5 - 10
			this.setSpeed(5+rand.nextInt(6));
			
			//Initializes heading of 0-359
			this.setHeading(spiderHeading);
			
			this.setLocation(location);
			this.setColor(spiderColor);
		}
	}
	
	public class Flag extends Fixed{
		// Initial Values
		private int sequenceNumber;
		private int flagColor = ColorUtil.rgb(255, 128, 0);
		
		// Constructor
		public Flag(int inSize, Point location, int sequenceNumber){
			super(inSize, location);
			this.setColor(flagColor);
			this.sequenceNumber = sequenceNumber;
		}
		
		public int getSequenceNumber() {
			return sequenceNumber;
		}
		
		
	}
	
	public class FoodStation extends Fixed{
		// Initial Values
		private int capacity;
		private int stationColor = ColorUtil.rgb(0, 102, 0);
		
		// Constructor
		public FoodStation(int inSize, Point location){
			super(inSize, location);
			this.setColor(stationColor);
			this.setCapacity(inSize);
		}
		
		
		public int getCapacity() {
			return capacity;
		}
		
		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}
	}

	// Increase Ant speed
	public void accelerate() {
		int accelerationRate = 5;
		for(GameObject gameObject : gameObjects) {
			if (gameObject instanceof Ant) {
				 Ant player = (Ant)gameObject; // Cast the gameObject to Ant to directly access object on list
				 // Current status of ant object
				 int antCurrentSpeed = player.getSpeed();
				 int antCurrentFoodLevel = player.getFoodLevel();
				 int antCurrentHealthLevel = player.getHealthLevel();
				 
				 	// If ant not at max health lower maximum speed proportionately 
				 if(antCurrentHealthLevel != 10) {
					 player.setMaximumSpeed(antCurrentHealthLevel*10);
				 }
				 
				 	// If ant current foodLevel or healthLevel is zero set speed to 0
				 if (antCurrentFoodLevel == 0 || antCurrentHealthLevel == 0) {
					 player.setSpeed(0);
					 System.out.println("The current value of antSpeed: " + player.getSpeed());
					 System.out.println("Warning ant has either insufficient health: "+ antCurrentHealthLevel + "or food level: " + antCurrentFoodLevel);
					 
					 // If ant is at max speed stay at max speed
				 } else if (antCurrentSpeed >= player.getMaximumSpeed()-1) {
					 player.setSpeed(player.getMaximumSpeed());
					 System.out.println("The current value of antSpeed: " + player.getSpeed());
					 System.out.println("Warning: ant is at maximum speed");
					 
					 // Ant can accelerate, increase speed by acceleration rate
					 } else {
					 System.out.println("The prior value of antSpeed: " + player.getSpeed());
					 player.setSpeed((antCurrentSpeed + accelerationRate));
					 System.out.println("The current value of antSpeed: " + player.getSpeed());
					 
				 }
				 
			}
		}
	}
	
	// Decrease ant speed
	public void brake() {
		// Rate at which braking happens
		int brakingRate = 5;
		
		for(GameObject gameObject : gameObjects) {
			if (gameObject instanceof Ant) {
				 Ant player = (Ant)gameObject; // Cast the gameObject to Ant to directly access object on list
				 
				// Current status of ant object
				 int antCurrentSpeed = player.getSpeed();
				 int antCurrentFoodLevel = player.getFoodLevel();
				 int antCurrentHealthLevel = player.getHealthLevel();
				 
				 // If ant not at max health lower maximum speed proportionately 
				 if(antCurrentHealthLevel != 10) {
					 player.setMaximumSpeed(antCurrentHealthLevel*10);
				 }
				 
				 // If ant current foodLevel or healthLevel is zero set speed to 0
				 if (antCurrentFoodLevel == 0 || antCurrentHealthLevel == 0) {
					 player.setSpeed(0);
					 System.out.println("The current value of antSpeed: " + player.getSpeed());
					 System.out.println("Warning ant has either insufficient health: "+ antCurrentHealthLevel + "or food level: " + antCurrentFoodLevel);
					 
					 // If ant is at max speed stay at max speed
				 } else if (antCurrentSpeed < brakingRate) {
					 player.setSpeed(0);
					 System.out.println("The current value of antSpeed: " + player.getSpeed());
					 System.out.println("Warning: ant is at minimum speed");
					 
					 // Ant can decelerate, decrease speed by brakingRate
					 } else {
					 System.out.println("The prior value of antSpeed: " + player.getSpeed());
					 player.setSpeed((antCurrentSpeed - brakingRate));
					 System.out.println("The current value of antSpeed: " + player.getSpeed());
					 
				 }
				 
			}
		}
	}
	
	public void leftTurn() {
		// Rate at which turning happens
		int turnRate = 5;
		
		for(GameObject gameObject : gameObjects) {
			if (gameObject instanceof Ant) {
				 Ant player = (Ant)gameObject; // Cast the gameObject to Ant to directly access object on list
				 // Modifications on ant object
				 int antCurrentHeading = player.getHeading();
				 System.out.println("The prior value of antHeading: " + player.getHeading());
				 player.setHeading(antCurrentHeading-turnRate);
				 System.out.println("The current value of antHeading: " + player.getHeading());
				 
			}
		}
	}
	
	public void rightTurn() {
		// Rate at which turning happens
		int turnRate = 5;
		
		for(GameObject gameObject : gameObjects) {
			if (gameObject instanceof Ant) {
				 Ant player = (Ant)gameObject; // Cast the gameObject to Ant to directly access object on list
				 // Modifications on ant object
				 int antCurrentHeading = player.getHeading();
				 System.out.println("The prior value of antHeading: " + player.getHeading());
				 player.setHeading(antCurrentHeading+turnRate);
				 System.out.println("The current value of antHeading: " + player.getHeading());
				 
			}
		}
	}
	
	public void setFC() {
		// Sets random food consumption rate of ant from 1-4
		int randFoodConsumptionRate = (1+rand.nextInt(4));
		
		for(GameObject gameObject : gameObjects) {
			if (gameObject instanceof Ant) {
				 Ant player = (Ant)gameObject; // Cast the gameObject to Ant to directly access object on list
				 
				 // Sets food consumption to random value
				 player.setFoodConsumption(randFoodConsumptionRate);
				 System.out.println("The current ant food consumption rate is: " + player.getFoodConsumptionRate());
			}
		}
	}
	
	public void flagHit(int input) {
		
	    // Check if the flag number is exactly one greater than the lastFlagReached of the ant
	    for (GameObject gameObject : gameObjects) {
	        if (gameObject instanceof Ant) {
	            Ant ant = (Ant) gameObject;
	            int lastFlagReached = ant.getLastFlagReached();
	            System.out.println("Ant collided with flag: " + input);
	            
	            if (input == lastFlagReached + 1) {
	                // Update the lastFlagReached field of the ant by increasing it by one
	                ant.setLastFlagReached(input);
	                System.out.println("Last flag reached updated to: " + input);
	            }
	        }
	    }
	}
	
	// Ant collides with food station
	public void foodHit() {
		int hitStationCapacity = 0;
		int lightGreen = ColorUtil.rgb(102, 255, 102);
		
		for (GameObject gameObject : gameObjects) {
			if (gameObject instanceof FoodStation) {
				FoodStation foodStation = (FoodStation) gameObject;
				
				if (foodStation.getCapacity() != 0) {
					hitStationCapacity = foodStation.getCapacity();
					foodStation.setCapacity(0);
					foodStation.setColor(lightGreen);
					
					// adds a new foodStation with random size and location to the gameObjects array list
		        	int randomX = rand.nextInt(1001); // Random X coord
		        	int randomY = rand.nextInt(1001); // Random Y coord 
		        	int randomSize = (10 + rand.nextInt(51));  // Random Size 10-50
		        	
		        	Point location = new Point(randomX, randomY);
		        	FoodStation foodstation = new FoodStation(randomSize, location);
		        	gameObjects.add(foodstation);
		        	break; // Quit looping through food stations 
				}
				
			}
		}
		
		
	    for (GameObject gameObject : gameObjects) {
	        if (gameObject instanceof Ant) {
	            Ant player = (Ant) gameObject; // Cast the gameObject to Ant to directly access object on list
	            
	         // Modifications on ant object
	            int antCurrentFoodLevel = player.getFoodLevel();
	            System.out.println("Ant collided with a food station");
	            System.out.println("Ant previous food level: " + antCurrentFoodLevel);
	            
	            player.setFoodLevel((antCurrentFoodLevel + hitStationCapacity));
	            System.out.println("Ant current food level: " + player.getFoodLevel());  

	        }
	    }
	}
	
	// Ant collides with Spider
	public void spidHit() {
		Point pretendCollisionLocation = new Point();
		boolean isHit = false;
		
		// loop through spider list to check if hit
	    for (GameObject gameObject : gameObjects) {
	        if (gameObject instanceof Spiders) {
	            Spiders spider = (Spiders) gameObject; // Cast the gameObject to Ant to directly access object on list
	            
	            // status of spider
	            if (spider.getLocation()==pretendCollisionLocation) {
	            	isHit = true;
	            } else {
	            	isHit = true; // false here if actual collision
	            }
	            

	        }
	    }
		
	    // loop through ant list to check if sharing location with spider
	    for (GameObject gameObject : gameObjects) {
	        if (gameObject instanceof Ant) {
	            Ant player = (Ant) gameObject; // Cast the gameObject to Ant to directly access object on list
	            
	         // Modifications on ant object
	            int antCurrentHealthLevel = player.getHealthLevel();
	            
	            // True if spider and ant collided
	            if (isHit){
	            	System.out.println("Ant previous health level: " + antCurrentHealthLevel);  
	            	player.setHealthLevel(antCurrentHealthLevel - 1);
	            	System.out.println("Ant current health level after spider collision: " + player.getHealthLevel());
	            	
	             	// If ant not at max health lower maximum speed proportionately 
					 if(antCurrentHealthLevel != 10) {
						 player.setMaximumSpeed(antCurrentHealthLevel*10);
					 }
	            }
	            
	        }
	    }
	}
	
	// Game clock elapses by one tick
	public void tick() {
		// Elapse game clock
		clock++;
		
		//Loop through all spiders in array list
	    for (GameObject gameObject : gameObjects) {
	        if (gameObject instanceof Spiders) {
	            Spiders spider = (Spiders) gameObject; // Cast the gameObject to Ant to directly access object on list
	            int currentSpiderHeading = spider.getHeading();
	            System.out.println("Prior spider heading: " + currentSpiderHeading);
	            // Adds +-5 to spider heading
	            spider.setHeading(currentSpiderHeading + (-5 + rand.nextInt(11)));
	            System.out.println("Current spider heading after tick: " + currentSpiderHeading);

	        }
	    }
	    
		//Loop through all move able objects in array list
	    for (GameObject gameObject : gameObjects) {
	        if (gameObject instanceof Movable) {
	        	Movable mObj = (Movable)gameObject;
	        	mObj.move(mObj.getLocation(), mObj.getHeading(), mObj.getSpeed());
	        }
	    }
	    
	    for (GameObject gameObject : gameObjects) {
	        if (gameObject instanceof Ant) {
	            Ant player = (Ant) gameObject; // Cast the gameObject to Ant to directly access object on list
	            
	         // Modifications on ant object
	            int antCurrentFoodLevel = player.getFoodLevel();
	            int antCurrentFoodConsumptionRate = player.getFoodConsumptionRate();
	            System.out.println("Prior ant foodlevel: " + antCurrentFoodLevel);
	            
	            // Decreases food level by food consumption rate
	            player.setFoodLevel(antCurrentFoodLevel - antCurrentFoodConsumptionRate);
	            System.out.println("Current ant foodlevel after tick: " + player.getFoodLevel());
	            }   
	        }
	    }
	
	// Display current game/ant state values
	public void display() {
		// (1) prints number of lives left
		System.out.println("The current number of lives remaining: " + lives);
		
		// (2) prints the current clock value
		System.out.println("Elapsed time: " + clock);
		
	    for (GameObject gameObject : gameObjects) {
	        if (gameObject instanceof Ant) {
	            Ant player = (Ant) gameObject; // Cast the gameObject to Ant to directly access object on list
	            
	         // (3) prints the highest flag number the ant has reached
	            System.out.println("Ant latest flag reached: " + player.getLastFlagReached());
	            
	         // (4) prints the ant current food level
	            System.out.println("Ant current food level: " + player.getFoodLevel());
		     
	         // (5) prints the ant current health level
	            System.out.println("Ant current health level: " + player.getHealthLevel());
	        }
	    }
	}
	
	// Map current world to console
	public void map() {
		// toString() covers Location and Color
		
		//loop through flag
	    for (GameObject gameObject : gameObjects) {
	        if (gameObject instanceof Flag) {
	            Flag flag = (Flag) gameObject; // Cast the gameObject to flag to directly access object on list
	            System.out.println("Flag: " + flag.toString() + " size=" + flag.getSize() + " seqNum=" + flag.getSequenceNumber());
	            //Example output | Flag: Loc=250.0, 250.0 color=[255,128,0] size=7 seqNum=1
	        }
	    }
	    
	    //loop through ant
	    for (GameObject gameObject : gameObjects) {
	        if (gameObject instanceof Ant) {
	            Ant player = (Ant) gameObject; // Cast the gameObject to Ant to directly access object on list
	            System.out.println("Ant: " + player.toString() + " heading="+ player.getHeading() +" speed="+ player.getSpeed() +" size=" + player.getSize() + " maxSpeed=" + player.getMaximumSpeed() + " foodConsumptionRate=" + player.getFoodConsumptionRate());
	            // Example output | Ant: Loc=0.0, 0.0 color=[153,0,0] heading=0 speed=4 size=4 maxSpeed=100 foodConsumptionRate=2
	        }
	    }
	    
		//loop through spiders
	    for (GameObject gameObject : gameObjects) {
	        if (gameObject instanceof Spiders) {
	            Spiders spider = (Spiders) gameObject; // Cast the gameObject to spider to directly access object on list
	            System.out.println("Spider: " + spider.toString() + " heading="+ spider.getHeading() +" speed="+ spider.getSpeed() +" size=" + spider.getSize());
	            // Example output | Spider: Loc=748.0, 956.0 color=[0,0,153] heading=95 speed=5 size=21
	        }
	    }
	    
		//loop through Food station
	    for (GameObject gameObject : gameObjects) {
	        if (gameObject instanceof FoodStation) {
	            FoodStation foodstation = (FoodStation) gameObject; // Cast the gameObject to foodstation to directly access object on list
	            System.out.println("FoodStation: " + foodstation.toString() + " size=" + foodstation.getSize() + " capacity=" + foodstation.getCapacity());
	            // Example output | FoodStation: Loc=118.0, 651.0 color=[0,102,0] size=50 capacity=50
	        }
	    }
	}// end of function
	
	// Inform user and attempt to exit by setting flag
	public boolean exit() {
		System.out.println("Are you sure you want to exit? (y/n)");
		return true;
	}
}