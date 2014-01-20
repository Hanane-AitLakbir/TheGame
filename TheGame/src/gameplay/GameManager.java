package gameplay;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import networking.*;
import display.CanvasGame;
import display.Window;
import entities.Hero;
import entities.Position;
import entities.StateActor;

/**
 * GameManager creates the display objects (a Jframe object and a CanvasGame), the Heroes, a RoomManager and
 * a communicator (server or client) for the multiplayer mode. 
 * This class contains the main game loop.
 */
public class GameManager extends Thread{

	private static RoomManager roomManager;
	private CanvasGame canvas;
	private Communicator communicator;
	private Window window;

	private static Hero player = null; 
	private static Hero otherPlayer = null;
	private static ArrayList<Hero> players = new ArrayList<Hero>();
	private static Graphics graphics;

	public static boolean multiplayer; // multiplayer or solo mode : instanced in Menu (choice gameMode listener)
	public static int difficulty; // game difficulty : instanced in Menu (choice difficulty listener)
	public static final int WIDTH = 340, HEIGHT = 340, SCALE = 2;
	public static int endRoom;

	public static BufferMessage buffer;
	public static boolean clientMode=false; 
	public static boolean gameIsRunning = false;


	/**
	 * Constructor for the GameManager in solo mode and for the server. It creates the graphical objects needed to display the game (canvas, window, ...)
	 */
	public GameManager(){
		canvas = new CanvasGame();
		window = new Window(); //The window of the game.
		window.add(canvas);

		graphics = canvas.getGraphics();
		Font font = new Font("Courier", Font.BOLD, 20);
		graphics.setFont(font);
		graphics.setColor(Color.RED);

		player = new Hero(75*4,75*4,"Link");
		players.add(player); //Adds the player to the game.

		if(multiplayer){
			otherPlayer = new Hero(75*4, 75*4,"Link2");
			players.add(otherPlayer); //Adds a second player to the game.
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
		
		player.start(); //Starts the player Thread.
	}

	/**
	 * Constructor for client in multiplayer mode to "Join a game"
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
	}

	public void run(){
		
		while(!player.isDead()){
			
			try {
				roomManager.changeRoom(player);
				if(multiplayer){
					roomManager.changeRoom(otherPlayer); //Look at the position of the players, in case they may move to another room.
				}

				roomManager.updateGraphics(graphics);

				if(otherPlayer!=null){
					otherPlayer.updateGraphic(graphics); //Updates the graphics.
					//System.out.println("update graphic other player"); //Test Line
				}
				//System.out.println("\t\t\t\t\t GM turn : " + TurnManager.turn); //Test Line

				if (multiplayer) {
					//System.out.println("GM nb mess buffer " + buffer.getMess()); //Test Line
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
		
		window.setVisible(false);
		JOptionPane.showMessageDialog(null, "You have LOST ! Mouahahahahah !");
		
		System.exit(0);
		
	}

	/**
	 * Updates the other hero(es) according to the action received by the communicator.
	 * @param action the action received by the communicator
	 */
	public static void updateOtherPlayers(int action){
		StateActor state = StateActor.convertToState(action); //Retrieve the action of the other player.

		otherPlayer.setState(state); //And change it on that gameManager.
		otherPlayer.action();
		//System.out.println("update de other player + action = "+ action); //Test line
		//System.out.println("other player position : " + otherPlayer.getPosition().getX() + " " + otherPlayer.getPosition().getY()); //Test line
	}

	/**
	 * Updates the actors according to what is in the message.
	 * @param message the action of an actor.
	 */
	public static void updateActor(int[] message){
		
		//System.out.println("message recu " + message[0] + " " + message[1]); //Test line
		
		if(message[0]==-1){ //If there is a valid message.
			int action = Integer.parseInt(String.valueOf(message[1]%100));
			int y = Integer.parseInt(String.valueOf(message[1]/(1000*1000)));
			int x = Integer.parseInt(String.valueOf((message[1]/1000)%1000));

			otherPlayer.getPosition().setXY(x, y);
			otherPlayer.setState(StateActor.convertToState(action));
			otherPlayer.action();
		}
		else{
			if(clientMode){
				roomManager.controlMonster(message[0], message[1], graphics);
			}
		}

		otherPlayer.updateGraphic(graphics);
		//System.out.println("update graphic other player"); /Test line

	}

	/**
	 * Gives the player's action. 
	 * ~used by the communicator to send them.
	 * @return an Integer corresponding to the player's action
	 */
	public static int playerAction(){
		int action = StateActor.convertToInt(player.getHeroState());
		return action;
	}

	/**
	 * @return the list of the players in the game.
	 */
	public static ArrayList<Hero> getPlayer(){
		return players;
	}

	/**
	 * Updates the graphics, and the life bars.
	 * @param image
	 * @param position
	 * @param rateLife
	 */
	public static synchronized void updateGraphics(BufferedImage image, Position position, double rateLife){
		graphics.drawImage(image, position.getX(), position.getY(), 40*GameManager.SCALE, 40*GameManager.SCALE,null);
		if(rateLife!=0){
			graphics.fillRect(position.getX()+15, position.getY(), (int) (50*rateLife), 3);
		}
	}

}
