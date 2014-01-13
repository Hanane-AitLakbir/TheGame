package networking;

import gameplay.GameManager;

import java.util.Timer;
import java.util.TimerTask;

public class TurnManager {
	static public boolean turn=true; 
	private Timer timer;
	private TimerTask task;
	
	public TurnManager(){
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				turn = !turn;
				GameManager.turn = turn;
				
			}
		};
		timer.scheduleAtFixedRate(task, 0, 30000); //turn lasts 30 sec
	}
	
	/**
	 * Returns true if it's the user's turn to play, false otherwise.
	 */
	public boolean getTurn(){
		return turn;
	}
	
//	public static void main(String[] args){
//		
//		while(true){
//		TurnManager man = new TurnManager();
//		}
//	}

}
