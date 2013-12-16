package gameplay;

import java.awt.Graphics;

import display.Position;

import main.Game;

import entities.Hero;

/**
 * La classe RoomManager gere la gestion des salles (ie leur creation et les changements de salle au cours du jeu).
 * @author hanane
 *
 */
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

	public Room goingOut(){

		Position pos = player.getPosition();
		int x = pos.getX();
		int y = pos.getY();

		if (x>80*GameManager.SCALE*2 && x<95*2*GameManager.SCALE && y<16*2*GameManager.SCALE){
			return current.up; //porte nord
		} 
		else if (x>64*GameManager.SCALE*2 && x<79*2*GameManager.SCALE && y>138*2*GameManager.SCALE){
			return current.down; //porte sud
		} 
		else if (y>60*GameManager.SCALE*2 && y<75*2*GameManager.SCALE && x<16*2*GameManager.SCALE){
			return current.left; //porte ouest
		} 
		else if (y>80*GameManager.SCALE*2 && y<95*2*GameManager.SCALE && x>140*2*GameManager.SCALE){
			return current.right; // porte est
		}
		return null;	
	}

}
