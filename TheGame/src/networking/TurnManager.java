package networking;

import java.util.Timer;
import java.util.TimerTask;

public class TurnManager {
	public static boolean turn; 
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
		timer.scheduleAtFixedRate(task, 0, 20000); // TODO turn lasts 15 sec 
	}

	/**
	 * Returns true if it's the user's turn to play, false otherwise.
	 */
	public boolean getTurn(){
		return turn;
	}

}
