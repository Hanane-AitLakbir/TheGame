package gamePlay;
import java.awt.Graphics;

import sound.SoundPlayer;
import entities.*;
import graphics.Background;

public abstract class Room {
	Background bg;
	Hero player; 
	SoundPlayer sound;
	Room up, down, left, right;

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

	public void createNeighbours(int size){
		if(size>0)
		{
			if(up == null){
				up = new MonsterRoom(player, 1);
				up.down = this;
				up.createNeighbours(size-1);
			}
			if(down == null){
				down = new MonsterRoom(player, 1);
				down.up = this;
			}
			if(left == null){
				left = new MonsterRoom(player, 1);
				left.right = this;
				left.createNeighbours(size-1);
			}
			if(right == null){
				right = new MonsterRoom(player, 1);
				right.left = this;
			}
		}
		else
		{
		
		}
	}

	public void updateGraphics(Graphics g){

	}


}
