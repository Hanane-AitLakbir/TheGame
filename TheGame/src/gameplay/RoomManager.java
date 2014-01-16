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
	private int end;
	private int size;

	private ArrayList<Room> rooms;

	public RoomManager(Hero player, int difficulty){

		size = 5; //default size
		end = generateEndRoom();
		System.out.println(end);
		rooms = new ArrayList<Room>();

		//creation of rooms
		for(int i = 0;i<size*size;i++){
			if(i==12){
				rooms.add(new StartRoom(player));
			}
			else if(i==end){
				rooms.add(new FinalRoom(player));
			}
			else{
				rooms.add(new MonsterRoom(player,((i+2)%3)+1,difficulty)); // changes the number of monsters => more fun
			}
		}

		//creation of the neighbours
		int r,q;
		for(int k = 0; k<rooms.size();k++){
			r = k % size;
			q = k/size; // attention division d'entier = quotient de la division euclidienne

			if(k-size>=0){
				rooms.get(k).up = (k-size);
			}else{
				rooms.get(k).up = (size*(size-1)+r);
			}

			if(k+size<size*size){
				rooms.get(k).down =(k+size);
			}else{
				rooms.get(k).down = (r);}


			if(k+1<=(q+1)*size-1){
				rooms.get(k).right = (k+1);
			}else{
				rooms.get(k).right = (q*size);
			}

			if(k-1>=q*size){
				rooms.get(k).left = (k-1);
			}else{
				rooms.get(k).left = ((q+1)*size-1);
			}

		}

		start = rooms.get(12); // starts in the middle room
		current = start;
		current.start();
	}


	public void updateGraphics(Graphics g){
		current.updateGraphics(g);
	}

	public void changeRoom(Hero player){

		Position pos = player.getPosition();
		int x = pos.getX();
		int y = pos.getY();

		if (x>80*GameManager.SCALE*2 && x<90*2*GameManager.SCALE && y<18*2*GameManager.SCALE){
			current.stop();
			current=rooms.get(current.up); //north door
			current.start();
			player.getPosition().setXY(66*GameManager.SCALE*2, 123*2*GameManager.SCALE);
		} 
		else if (x>60*GameManager.SCALE*2 && x<73*2*GameManager.SCALE && y>128*2*GameManager.SCALE){
			current.stop();
			current=rooms.get(current.down); //south door
			current.start();
			player.getPosition().setXY(82*GameManager.SCALE*2, 29*2*GameManager.SCALE);
		} 
		else if (y>56*GameManager.SCALE*2 && y<71*2*GameManager.SCALE && x<21*2*GameManager.SCALE){
			current.stop();
			current=rooms.get(current.left); //west door
			current.start();
			player.getPosition().setXY(125*GameManager.SCALE*2, 80*2*GameManager.SCALE);
		} 
		else if (y>73*GameManager.SCALE*2 && y<87*2*GameManager.SCALE && x>129*2*GameManager.SCALE){
			current.stop();
			current=rooms.get(current.right); // east door
			current.start();
			player.getPosition().setXY(27*GameManager.SCALE*2, 63*2*GameManager.SCALE);
		}
	}

	private int generateEndRoom(){
		int random = (int) Math.floor(Math.random()*size*size);
		if(random!=12){
			return random;
		}else{
			return (random+1<size*size) ? random+1 : random-1;
		}
	}

}
