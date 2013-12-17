package networking;

import java.util.Timer;
import java.util.TimerTask;

public class TurnManager {
	private boolean turn; 
	private Timer timer;
	private TimerTask task;
	
	public TurnManager(){
		turn = true;
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				turn = !turn;
				
			}
		};
		timer.scheduleAtFixedRate(task, 0, 10000);
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
