package gameplay;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entities.StateActor;


public class Controller implements KeyListener{
	private int action = -1;

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode()== KeyEvent.VK_UP){
			if(GameManager.getPlayer().get(0).getHeroState() != StateActor.UP){
				GameManager.getPlayer().get(0).setState(StateActor.UP);
				action = 8;
			}
		}		
		if(event.getKeyCode()== KeyEvent.VK_DOWN){
			if(GameManager.getPlayer().get(0).getHeroState() != StateActor.DOWN){
				GameManager.getPlayer().get(0).setState(StateActor.DOWN);
				action = 2;
			}
		}		
		if(event.getKeyCode()== KeyEvent.VK_RIGHT){
			if(GameManager.getPlayer().get(0).getHeroState() != StateActor.RIGHT){
				GameManager.getPlayer().get(0).setState(StateActor.RIGHT);
				action = 6;
			}
		}		
		if(event.getKeyCode()== KeyEvent.VK_LEFT){
			if(GameManager.getPlayer().get(0).getHeroState() != StateActor.LEFT){
				GameManager.getPlayer().get(0).setState(StateActor.LEFT);
				action = 4;
			}
		}

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
		//		if(event.getKeyCode()== KeyEvent.VK_SPACE){
		//			GameManager.getPlayer().setState(StateActor.NONE);
		//		}
	}

	@Override
	public void keyTyped(KeyEvent event) {

	}

}
