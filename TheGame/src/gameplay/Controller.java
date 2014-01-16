package gameplay;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entities.StateActor;


public class Controller implements KeyListener{
	private int action = -1;

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode()== KeyEvent.VK_UP){
			if(GameManager.getPlayer().getHeroState() != StateActor.UP){
				GameManager.getPlayer().setState(StateActor.UP);
				action = 8;
			}
		}		
		if(event.getKeyCode()== KeyEvent.VK_DOWN){
			if(GameManager.getPlayer().getHeroState() != StateActor.DOWN){
				GameManager.getPlayer().setState(StateActor.DOWN);
				action = 2;
			}
		}		
		if(event.getKeyCode()== KeyEvent.VK_RIGHT){
			if(GameManager.getPlayer().getHeroState() != StateActor.RIGHT){
				GameManager.getPlayer().setState(StateActor.RIGHT);
				action = 6;
			}
		}		
		if(event.getKeyCode()== KeyEvent.VK_LEFT){
			if(GameManager.getPlayer().getHeroState() != StateActor.LEFT){
				GameManager.getPlayer().setState(StateActor.LEFT);
				action = 4;
			}
		}

		if(event.getKeyCode()== KeyEvent.VK_SPACE){
			switch(action){
			case 8: 
				GameManager.getPlayer().setState(StateActor.ATTACKINGUP);
				break;
			case 4: 
				GameManager.getPlayer().setState(StateActor.ATTACKINGLEFT);
				break;
			case 2: 
				GameManager.getPlayer().setState(StateActor.ATTACKINGDOWN);
				break;
			case 6: 
				GameManager.getPlayer().setState(StateActor.ATTACKINGRIGHT);
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if(event.getKeyCode()== KeyEvent.VK_UP){
			GameManager.getPlayer().setState(StateActor.NONE);
		}		
		if(event.getKeyCode()== KeyEvent.VK_DOWN){
			GameManager.getPlayer().setState(StateActor.NONE);
		}		
		if(event.getKeyCode()== KeyEvent.VK_RIGHT){
			GameManager.getPlayer().setState(StateActor.NONE);
		}		
		if(event.getKeyCode()== KeyEvent.VK_LEFT){
			GameManager.getPlayer().setState(StateActor.NONE);
		}
		//		if(event.getKeyCode()== KeyEvent.VK_SPACE){
		//			GameManager.getPlayer().setState(StateActor.NONE);
		//		}
	}

	@Override
	public void keyTyped(KeyEvent event) {

	}

}
