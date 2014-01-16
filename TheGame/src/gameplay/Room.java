package gameplay;
import java.awt.Graphics;

import sound.SoundPlayer;
import entities.*;
import graphics.Background;

public abstract class Room {
	Background bg;
	Hero player; 
	SoundPlayer sound;
	int up, down, left, right;

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

	public abstract void updateGraphics(Graphics g); //draws the background
	public abstract void stop();
	public abstract void start();

}
