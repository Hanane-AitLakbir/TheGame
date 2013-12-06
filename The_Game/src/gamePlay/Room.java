package gamePlay;
import sound.SoundPlayer;
import entities.*;
import graphics.Background;

public class Room {
	Background bg;
	Hero player; 
	SoundPlayer sound;
	
	public Room(Hero player, int difficulty){
		this.player = player;
		bg = new Background(difficulty);
		sound = new SoundPlayer("room"+difficulty);
	}
	
	public Background getBackground(){
		return bg;
	}
	
	public SoundPlayer getSound(){
		return sound;
	}
	
	public boolean playerIsOut(){
		return player.isOutOfRoom();
	}
	
}
