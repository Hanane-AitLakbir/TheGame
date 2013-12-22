package gameplay;

import java.util.concurrent.atomic.AtomicInteger;

import display.CanvasGame;
import entities.Hero;
import entities.StatePlayer;

public class GameManager extends Thread{

	// TODO Ajouter un attribut difficulty à GameManager + paramètre String difficulty ou int size en fct de difficulty à RoomManager
	// TODO Ajouter un param string difficulty aux constructors de Hero et de Monster
	
	RoomManager roomManager;
	static Hero player = null; 
	static Hero otherPlayer = null;
	public static boolean multiplayer; // multiplayer or solo mode : instanced in Menu (choice gameMode listener)
	public static String difficulty; // game difficulty : instanced in Menu (choice difficulty listener)
	public static final int WIDTH = 340, HEIGHT = 340, SCALE = 2;
	CanvasGame canvas;

	//constructor for server or solo mode
	public GameManager(){
		this.canvas = new CanvasGame();
		player = new Hero(new AtomicInteger(50*4),new AtomicInteger(50*4),"Link");
		player.start();
		roomManager = new RoomManager(player, 5);
	}

	//constructor for client in multiplayer mode to Join a game
	public GameManager(String nameServer, int serverPort){

	}

	public void run(){

		while(true){

			try {
				//updateGraphic();
				sleep(14);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	public static void updateOtherPlayers(int action){

		StatePlayer state = StatePlayer.convertToState(action);
		otherPlayer.setState(state);
		otherPlayer.action();

	}

	public static int playerAction(){

		int action = StatePlayer.convertToInt(player.getHeroState());
		return action;

	}

	public static void setOtherPlayer(Hero otherPlayer){
		otherPlayer = new Hero(new AtomicInteger(50*4),new AtomicInteger(50*4),"");
	}

	public static Hero getPlayer(){
		return player;
	}

}
