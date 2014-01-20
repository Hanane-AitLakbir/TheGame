package gameplay;

import java.awt.Graphics;

import display.Background;
import utilities.SoundPlayer;
import entities.*;

/**
 * An abstraction to represent the rooms in the labyrinth, whether they may be StartRooms, FinalRooms or MonsterRooms.<p>
 * Each room has an ID, a music, a hero, a background and knows its neighbours.
 */
public abstract class Room {
	Background bg;
	Hero player; 
	SoundPlayer sound;
	int up, down, left, right;
	int roomId;

	public Room(Hero player, int difficulty){
		
		this.player = player;
		bg = new Background(difficulty);
		
		if(difficulty<=3){
			sound = new SoundPlayer("room"+difficulty);
		}
		else{
			int diff = 3-difficulty%3;
			sound = new SoundPlayer("room"+diff);
		}
	}

	public void setId(int id){
		roomId = id;
	}
	
	/**
	 * Draws the background.
	 * @param g
	 */
	public abstract void updateGraphics(Graphics g); 
	
	public abstract void stop();
	
	public abstract void start();

}
