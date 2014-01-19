package gameplay;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import networking.*;
import display.CanvasGame;
import display.Window;
import entities.Hero;
import entities.Position;
import entities.StateActor;

/**
 * GameManager creates the display objects (a Jframe object and a CanvasGame), the Heroes, a RoomManager and
 * a communicator (server or client) if multiplayer mode. 
 * This class contains the game loop (??) 
 * 
 * @author hanane
 *
 */
public class GameManager extends Thread{

	private static RoomManager roomManager;
	private CanvasGame canvas;
	private Communicator communicator;

	private static Hero player = null; 
	private static Hero otherPlayer = null;
	private static ArrayList<Hero> players = new ArrayList<Hero>();
	private static Graphics graphics;

	public static boolean multiplayer; // multiplayer or solo mode : instanced in Menu (choice gameMode listener)
	public static int difficulty; // game difficulty : instanced in Menu (choice difficulty listener)
	public static final int WIDTH = 340, HEIGHT = 340, SCALE = 2;
	//public static boolean turn = true;

	public static BufferMessage buffer;
	public static boolean clientMode=false; 
	public static int endRoom;
	public static boolean gameIsRunning = false;


	/**
	 * Constructor for a server or in solo mode
	 */
	public GameManager(){
		canvas = new CanvasGame();
		Window window = new Window();
		window.add(canvas);

		graphics = canvas.getGraphics();
		Font font = new Font("Courier", Font.BOLD, 20);
		graphics.setFont(font);
		graphics.setColor(Color.RED);

		player = new Hero(75*4,75*4,"Link");
		players.add(player);

		if(multiplayer){
			otherPlayer = new Hero(75*4, 75*4,"Link2");
			players.add(otherPlayer);
		}

		roomManager = new RoomManager(player, difficulty); //0 = null

		if(multiplayer){
			try {
				buffer = new BufferMessage();
				communicator = new Server(3956);
				new Thread(communicator).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			TurnManager.turn = true;
		}
		player.start();
	}

	/**
	 * Constructor for client in multiplayer mode to Join a game
	 * @param nameServer 
	 * @param serverPort
	 */
	public GameManager(String nameServer, int serverPort){
		clientMode = true;
		this.canvas = new CanvasGame();
		Window window = new Window();
		window.add(canvas);

		graphics = canvas.getGraphics();
		Font font = new Font("Courier", Font.BOLD, 20);
		graphics.setFont(font);
		graphics.setColor(Color.GREEN);
		otherPlayer = new Hero(75*4,75*4,"Link2");
		player = new Hero(75*4,75*4,"Link");
		try {
			buffer = new BufferMessage();
			communicator = new Client(nameServer,serverPort);
			new Thread(communicator).start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		players.add(player);
		players.add(otherPlayer);

		roomManager = new RoomManager(player, difficulty);
		player.start();
		//		try {
		//			buffer = new BufferMessage();
		//			communicator = new Client(nameServer,serverPort);
		//			new Thread(communicator).start();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
	}

	public void run(){
		while(true){
			try {
				roomManager.changeRoom(player);
				if(multiplayer) roomManager.changeRoom(otherPlayer);

				//if(turn){
				//else{
				//roomManager.changeRoom(otherPlayer);
				//}

				roomManager.updateGraphics(graphics);

				if(otherPlayer!=null){
					otherPlayer.updateGraphic(graphics);
					System.out.println("update graphic other player");
				}
				System.out.println("\t\t\t\t\t GM turn : " + TurnManager.turn);

				if (multiplayer) {
					System.out.println("GM nb mess buffer " + buffer.getMess());
					if (TurnManager.turn) {
						graphics.drawString("Your turn...", 10, 20);
					} else {
						graphics.drawString("The other is playing ...", 10, 20);
					}
				}
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
		//System.out.println(action);
		otherPlayer.setState(state);
		otherPlayer.action();
		System.out.println("update de other player + action = "+ action);
		System.out.println("other player position : " + otherPlayer.getPosition().getX() + " " + otherPlayer.getPosition().getY());
	}

	public static void updateActor(int[] message){
		System.out.println("message re�u " + message[0] + " " + message[1]);
		if(message[0]==-1){
			int action = Integer.parseInt(String.valueOf(message[1]%100));
			int y = Integer.parseInt(String.valueOf(message[1]/(1000*1000)));
			int x = Integer.parseInt(String.valueOf((message[1]/1000)%1000));

			//otherPlayer.setState(StateActor.convertToState(message[1]));
			otherPlayer.getPosition().setXY(x, y);
			otherPlayer.setState(StateActor.convertToState(action));
			//otherPlayer.getPosition().setXY(x, y);
			otherPlayer.action();
		}
		else{
			if(clientMode){
				roomManager.controlMonster(message[0], message[1], graphics);
			}
		}

		otherPlayer.updateGraphic(graphics);
		System.out.println("update graphic other player");

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

	//	public static Hero getPlayer(){
	//		return player;
	//	}

	public static ArrayList<Hero> getPlayer(){
		return players;
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
