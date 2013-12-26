package gameplay;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import display.Position;
import entities.Hero;

/**
 * La classe RoomManager gere les salles (ie leur creation et les changements de salle au cours du jeu).
 * @author hanane
 *
 */
public class RoomManager {

	private Room start;
	private Room current;
	private int size;
	private Hero player;

	public RoomManager(Hero player, int difficulty){
		this.player = player;
		switch(difficulty){
		case 1: 
			size=5;
			break;
		case 2:
			size = 6;
			break;
		case 3:
			size = 7;
			break;
		}
		ArrayList<Room> rooms = new ArrayList<Room>();
		
		for(int i = 0;i<size*size;i++){
			rooms.add(new MonsterRoom(player,difficulty));
		}

		int r,q;
		for(int k = 0; k<rooms.size();k++){
			r = k % size;
			q = k/size; // attention division d'entier = quotient de la division euclidienne

			if(k-size>0){
				rooms.get(k).up = rooms.get(k-size);
			}else{
				rooms.get(k).up = rooms.get(size*(size-1)+r);
			}
			
			
			if(k+size<size*size){
				rooms.get(k).down = rooms.get(k+size);
			}else{
				rooms.get(k).down = rooms.get(r);}

			
			if(k+1<(q+1)*size -1){
				rooms.get(k).right = rooms.get(k+1);
			}else{
				rooms.get(k).right = rooms.get(q*size);
			}
			
			if(k-1>q*size){
				rooms.get(k).left = rooms.get(k-1);
			}else{
				rooms.get(k).left = rooms.get((q+1)*size-1);
			}

		}
		//start = new MonsterRoom(player, 0);
		//start.createNeighbours(size);
		start = rooms.get((size/2)*(size/2)); // commence au milieu de la grille
		current = start;
		current.playSound();
	}

	public void updateGraphics(Graphics g){
		current.updateGraphics(g);
	}
	

	public Room goingOut(){

		Position pos = player.getPosition();
		int x = pos.getX();
		int y = pos.getY();

		if (x>80*GameManager.SCALE*2 && x<95*2*GameManager.SCALE && y<16*2*GameManager.SCALE){
			 current=current.up; //porte nord
			 current.playSound();
			 return current;
		} 
		else if (x>64*GameManager.SCALE*2 && x<79*2*GameManager.SCALE && y>138*2*GameManager.SCALE){
			 current=current.down; //porte sud
			 current.playSound();
			 return current;
		} 
		else if (y>60*GameManager.SCALE*2 && y<75*2*GameManager.SCALE && x<16*2*GameManager.SCALE){
			 current=current.left; //porte ouest
			 current.playSound();
			 return current;
		} 
		else if (y>80*GameManager.SCALE*2 && y<95*2*GameManager.SCALE && x>140*2*GameManager.SCALE){
			 current=current.right; // porte est
			 current.playSound();
			 return current;
		}else return null;
		
	}

	//main de test : outOfMemory
	public static void main(String[] args){
		RoomManager manager = new RoomManager(new Hero(new AtomicInteger(0),new AtomicInteger(0),"Link"), 2);
	}
}
