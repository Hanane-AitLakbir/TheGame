package gameplay;

import java.awt.Graphics;
import java.util.ArrayList;

import entities.Hero;
import entities.Monster;
import entities.Position;
import entities.StateActor;

/**
 * RoomManager manages the rooms (number, order, difficulty, ...)<p>
 * It knows the starting room, the ending room the size and the global layout of the labyrinth.
 */
public class RoomManager {

	private Room start;
	private Room current;
	private int end;
	private int size;

	private ArrayList<Room> rooms;

	/**
	 * Creates the labyrinth with a random layout in solo mode, and a known one in multiplayer.
	 * @param player
	 * @param difficulty
	 */
	public RoomManager(Hero player, int difficulty){

		if(!GameManager.multiplayer){
			switch(difficulty){ 
			case 1 :
				size = 5;
				break;
			case 2 : 
				size = 6;
				break;
			case 3 : 
				size = 7; //May cause OutOfMemory on "slow and old" computers.
				break;
			default : 
				size = 5;
				break;
			}
		}
		else{
			size = 5;
		}

		if(GameManager.clientMode) { //If you're the client, get the endRoom number.
			end = GameManager.endRoom;
			//System.out.println(end);
		}
		else{ //If you're the server, create the endRoom, and store the number.
			end = generateEndRoom(); 
			GameManager.endRoom = end;
			//System.out.println(end);
		}

		rooms = new ArrayList<Room>();

		//Creates the first room of the dungeon, may it be the final room, 
		for(int i = 0;i<size*size;i++){
			if(i==Math.floor(size*size/2.)){
				rooms.add(new StartRoom(player));
			}
			else if(i==end){
				rooms.add(new FinalRoom(player));
			}
			else{
				if(!GameManager.multiplayer){
					rooms.add(new MonsterRoom(player,randomMonstersNumber(difficulty),difficulty,i)); //changes the number of monsters => more fun
				}
				else{
					rooms.add(new MonsterRoom(player,(i+2)%3+1,difficulty,i)); //The number of monsters is capped at 3 in multiplayer mode.
				}
			}
		} 

		//Creates the neighbours of the first room.
		int r,q;
		for(int k = 0; k<rooms.size();k++){
			
			r = k % size;
			q = k/size; // Integer division !
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

		start = rooms.get((int) Math.floor(size*size/2.)); // starts in the middle room
		current = start;
		current.start();
	}

	public void updateGraphics(Graphics g){
		current.updateGraphics(g);
	}

	/**
	 * If a player is trying to go to another room, stops the current one and starts the new room.
	 * @param player
	 */
	public void changeRoom(Hero player){

		Position pos = player.getPosition();
		int x = pos.getX();
		int y = pos.getY();

		if (x>80*GameManager.SCALE*2 && x<90*2*GameManager.SCALE && y<18*2*GameManager.SCALE){ //If the player is at the north door
			current.stop();
			current=rooms.get(current.up); //north door
			current.start();
			player.getPosition().setXY(66*GameManager.SCALE*2, 123*2*GameManager.SCALE);
		} 
		else if (x>60*GameManager.SCALE*2 && x<73*2*GameManager.SCALE && y>128*2*GameManager.SCALE){//If the player is at the south door
			current.stop();
			current=rooms.get(current.down); //south door
			current.start();
			player.getPosition().setXY(82*GameManager.SCALE*2, 29*2*GameManager.SCALE);
		} 
		else if (y>56*GameManager.SCALE*2 && y<71*2*GameManager.SCALE && x<21*2*GameManager.SCALE){//If the player is at the west door
			current.stop();
			current=rooms.get(current.left); //west door
			current.start();
			player.getPosition().setXY(125*GameManager.SCALE*2, 80*2*GameManager.SCALE);
		} 
		else if (y>73*GameManager.SCALE*2 && y<87*2*GameManager.SCALE && x>129*2*GameManager.SCALE){//If the player is at the east door
			current.stop();
			current=rooms.get(current.right); // east door
			current.start();
			player.getPosition().setXY(27*GameManager.SCALE*2, 63*2*GameManager.SCALE);
		}
	}

	/**
	 * Creates the number which determines the final room of the dungeon.
	 * @return the number corresponding to the final room.
	 */
	private int generateEndRoom(){
		int random = (int) Math.floor(Math.random()*size*size);
		if(random!=Math.floor(size*size/2.)){
			return random;
		}else{
			return (random+1<size*size) ? random+1 : random-1;
		}
	}

	/**
	 * Creates a maximum of 4, 5, or 6 monsters depending on the difficulty of the game.
	 * @param difficulty
	 * @return a random number of monsters
	 */
	private int randomMonstersNumber(int difficulty){

		int random = (int) Math.floor(Math.random()*(3+difficulty)+1);
		if(random == 7) return 6;
		else return random;

	}

	public synchronized void controlMonster(int id, int message, Graphics g){
		
		Monster monster =((MonsterRoom) rooms.get(id/10)).getMonster(id%10);  //Looks at a monster
		
		int action = Integer.parseInt(String.valueOf(message%100));
		int y = Integer.parseInt(String.valueOf(message/(1000*1000))); //Turns the action into a movement.
		int x = Integer.parseInt(String.valueOf((message/1000)%1000));
		
		monster.setState(StateActor.convertToState(action)); //Tells him which way to face
		monster.getPosition().setXY(x, y); //Tells him his new position.
		monster.actionMultiplayer(); //Acts.
		
		monster.updateGraphic(g); //Change the graphics.
		
	}
}
