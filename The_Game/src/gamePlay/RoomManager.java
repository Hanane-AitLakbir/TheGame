package gamePlay;

import java.awt.Graphics;

import main.Game;

import entities.Hero;

public class RoomManager {

	private Room start;
	private Room current;
	private int size;
	private Hero player;

	public RoomManager(Hero player, int size){
		this.player = player;
		this.size = size;

		start = new MonsterRoom(player, 0);
		start.createNeighbours(size);
	}

	public void updateGraphics(Graphics g){
		current.updateGraphics(g);
	}

	public void changeRoom(){
//		if(player.getY()>80*Game.SCALE*2 && player.getY()<95*2*Game.SCALE && player.getX()>140*2*Game.SCALE)
//		{
//			current = current.right;
//			player.setXY(18*2*Game.SCALE, 67*2*Game.SCALE);
//		}
		
		
		
		
	}
}