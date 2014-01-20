package gameplay;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entities.StateActor;

/**
 * The controller listens to the keys pressed on the keyboard, and changes the state of the hero accordingly.
 */
public class Controller implements KeyListener{
	private int action = -1;

	@Override
	public void keyPressed(KeyEvent event) {
		//If you press UP
		if(event.getKeyCode()== KeyEvent.VK_UP){
			if(GameManager.getPlayer().get(0).getHeroState() != StateActor.UP){
				GameManager.getPlayer().get(0).setState(StateActor.UP);
				action = 8;
			}
		}		
		//If you press DOWN
		if(event.getKeyCode()== KeyEvent.VK_DOWN){
			if(GameManager.getPlayer().get(0).getHeroState() != StateActor.DOWN){
				GameManager.getPlayer().get(0).setState(StateActor.DOWN);
				action = 2;
			}
		}		
		//If you press RIGHT
		if(event.getKeyCode()== KeyEvent.VK_RIGHT){
			if(GameManager.getPlayer().get(0).getHeroState() != StateActor.RIGHT){
				GameManager.getPlayer().get(0).setState(StateActor.RIGHT);
				action = 6;
			}
		}		
		//If you press LEFT
		if(event.getKeyCode()== KeyEvent.VK_LEFT){
			if(GameManager.getPlayer().get(0).getHeroState() != StateActor.LEFT){
				GameManager.getPlayer().get(0).setState(StateActor.LEFT);
				action = 4;
			}
		}
		//If you press SPACE (to attack)
		if(event.getKeyCode()== KeyEvent.VK_SPACE){
			switch(action){
			case 8: 
				GameManager.getPlayer().get(0).setState(StateActor.ATTACKINGUP);
				break;
			case 4: 
				GameManager.getPlayer().get(0).setState(StateActor.ATTACKINGLEFT);
				break;
			case 2: 
				GameManager.getPlayer().get(0).setState(StateActor.ATTACKINGDOWN);
				break;
			case 6: 
				GameManager.getPlayer().get(0).setState(StateActor.ATTACKINGRIGHT);
				break;
			}
		}
	}

	/**
	 * When you release a movement Key, put the hero in a waiting state, at a stop.
	 */
	@Override
	public void keyReleased(KeyEvent event) {
		if(event.getKeyCode()== KeyEvent.VK_UP){
			GameManager.getPlayer().get(0).setState(StateActor.NONE);
		}		
		if(event.getKeyCode()== KeyEvent.VK_DOWN){
			GameManager.getPlayer().get(0).setState(StateActor.NONE);
		}		
		if(event.getKeyCode()== KeyEvent.VK_RIGHT){
			GameManager.getPlayer().get(0).setState(StateActor.NONE);
		}		
		if(event.getKeyCode()== KeyEvent.VK_LEFT){
			GameManager.getPlayer().get(0).setState(StateActor.NONE);
		}
	}

	@Override
	public void keyTyped(KeyEvent event) {

	}

}
