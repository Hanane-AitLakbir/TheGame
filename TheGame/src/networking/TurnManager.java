package networking;

import java.util.Timer;
import java.util.TimerTask;

/**
 * That object will manage the turn of the two players, and tell which can act, and which cannot.<p>
 * A turn lasts {@value #TURN_DURATION}ms. You can change it in this class.
 */
public class TurnManager {
	
	public static boolean turn; 
	private Timer timer;
	private TimerTask task;
	private final int TURN_DURATION = 15000; //ms, duration of the turn.

	public TurnManager(){
		turn = false;
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				turn = !turn;
			}
		};
		timer.scheduleAtFixedRate(task, 0, TURN_DURATION); // turn lasts 15 sec, you can change it here. 
	}

	/**
	 * Returns true if it's the user's turn to play, false otherwise.
	 */
	public boolean getTurn(){
		return turn;
	}

}
