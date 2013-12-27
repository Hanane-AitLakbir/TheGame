package gameplay;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import networking.*;
import display.CanvasGame;
import display.Window;
import entities.Hero;
import entities.StateActor;

/**
 * GameManager creates the display objects (a JPanel object and a CanvasGame), the Heroes, a RoomManager and
 * a communicator (server or client) if multiplayer mode. 
 * This class contains the game loop (??) 
 * 
 * @author hanane
 *
 */
public class GameManager extends Thread{
	
	private RoomManager roomManager;
	private CanvasGame canvas;
	private Communicator communicator;
	
	private static Hero player = null; 
	private static Hero otherPlayer = null;
	
	public static boolean multiplayer; // multiplayer or solo mode : instanced in Menu (choice gameMode listener)
	public static int difficulty; // game difficulty : instanced in Menu (choice difficulty listener)
	public static final int WIDTH = 340, HEIGHT = 340, SCALE = 2;
	
	
	/**
	 * Constructor for a server or in solo mode
	 */
	public GameManager(){
		this.canvas = new CanvasGame();
		Window window = new Window();
		window.add(canvas);
		player = new Hero(new AtomicInteger(50*4),new AtomicInteger(50*4),"Link");
		player.start();
		roomManager = new RoomManager(player, difficulty);
		if(multiplayer){
			try {
				communicator = new Server(3956);
				new Thread(communicator).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Constructor for client in multiplayer mode to Join a game
	 * @param nameServer 
	 * @param serverPort
	 */
	public GameManager(String nameServer, int serverPort){
		this.canvas = new CanvasGame();
		Window window = new Window();
		window.add(canvas);
		player = new Hero(new AtomicInteger(50*4),new AtomicInteger(50*4),"Link");
		player.start();
		roomManager = new RoomManager(player, difficulty);
	}

	public void run(){

		while(true){
			try {
				roomManager.updateGraphics(canvas.getGraphics());
				sleep(14);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Updates the other hero(es) following the action received by the communicator
	 * @param action the action received by the communicator
	 */
	public static void updateOtherPlayers(int action){

		StateActor state = StateActor.convertToState(action);
		otherPlayer.setState(state);
		otherPlayer.action();

	}

	/**
	 * Gives the player's moves. 
	 * ~used by the communicator to send them.
	 * @return an Integer corresponding with the player's moves 
	 */
	public static int playerAction(){

		int action = StateActor.convertToInt(player.getHeroState());
		return action;

	}

	public static void setOtherPlayer(Hero otherPlayer){
		otherPlayer = new Hero(new AtomicInteger(50*4),new AtomicInteger(50*4),"");
	}

	public static Hero getPlayer(){
		return player;
	}

	//main de test : affichage
	public static void main(String[] args){
		multiplayer = false; 
		difficulty = 1;
		new GameManager().start();
	}
}
