package gameplay;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import networking.*;
import display.CanvasGame;
import display.Position;
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
	public static boolean turn = true;
	private static Graphics graphics;


	/**
	 * Constructor for a server or in solo mode
	 */
	public GameManager(){
		canvas = new CanvasGame();
		Window window = new Window();
		window.add(canvas);
		graphics = canvas.getGraphics();
		graphics.setColor(Color.RED);

		player = new Hero(75*4,75*4,"Link");
		player.start();

		roomManager = new RoomManager(player, difficulty);

		if(multiplayer){
			otherPlayer = new Hero(75*4, 75*4,"Link2");
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

		player = new Hero(75*4,75*4,"Link");
		player.start();
		otherPlayer = new Hero(75*4,75*4,"Link2");

		roomManager = new RoomManager(player, difficulty);

		try {
			communicator = new Client(nameServer,serverPort);
			new Thread(communicator).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run(){

		while(true){
			try {
				if(turn){
					roomManager.changeRoom(player);
				}
				else{
					roomManager.changeRoom(otherPlayer);
					otherPlayer.updateGraphic(canvas.getGraphics());
				}
				roomManager.updateGraphics(canvas.getGraphics());
				sleep(100);
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

	//USELESS
	//	public static void setOtherPlayer(Hero otherPlayer){
	//		otherPlayer = new Hero(new AtomicInteger(50*4),new AtomicInteger(50*4),"");
	//	}

	public static Hero getPlayer(){
		return player;
	}

	public static synchronized void updateGraphics(BufferedImage image, Position position, double rateLife){
		graphics.drawImage(image, position.getX(), position.getY(), 40*GameManager.SCALE, 40*GameManager.SCALE,null);
		if(rateLife!=0){
			graphics.fillRect(position.getX()+15, position.getY(), (int) (50*rateLife), 3);
		}
	}

	//main de test : affichage
	public static void main(String[] args){
		multiplayer = false; 
		difficulty = 1;
		new GameManager().start();
	}
}
