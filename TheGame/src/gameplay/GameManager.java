package gameplay;

import java.util.concurrent.atomic.AtomicInteger;

import display.CanvasGame;
import entities.Hero;
import entities.StatePlayer;

public class GameManager{

	RoomManager roomManager;
	static Hero player = null; 
	static Hero otherPlayer = null;
	public static final int WIDTH = 340, HEIGHT = 340, SCALE = 2;
	CanvasGame canvas;
	
	public GameManager(CanvasGame canvas){
		this.canvas = canvas;
		player = new Hero(new AtomicInteger(50*4),new AtomicInteger(50*4),"Link");
		player.start();
		roomManager = new RoomManager(player, 5);
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
