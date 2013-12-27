package gameplay;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entities.StateActor;


public class Controller implements KeyListener{
	public static int action = -1;

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode()== KeyEvent.VK_UP){
			GameManager.getPlayer().setState(StateActor.UP);
			action = 8;
		}		
		if(event.getKeyCode()== KeyEvent.VK_DOWN){
			GameManager.getPlayer().setState(StateActor.DOWN);
			action = 2;
		}		
		if(event.getKeyCode()== KeyEvent.VK_RIGHT){
			GameManager.getPlayer().setState(StateActor.RIGHT);
			action = 6;
		}		
		if(event.getKeyCode()== KeyEvent.VK_LEFT){
			GameManager.getPlayer().setState(StateActor.LEFT);
			action = 4;
		}
		if(event.getKeyCode()== KeyEvent.VK_SPACE){
			switch(GameManager.getPlayer().getHeroState()){

			case UP : 
				GameManager.getPlayer().setState(StateActor.ATTACKINGUP);
				break;
			case DOWN : 
				GameManager.getPlayer().setState(StateActor.ATTACKINGDOWN);
				break;
			case LEFT : 
				GameManager.getPlayer().setState(StateActor.ATTACKINGLEFT);
				break;
			case RIGHT :
				GameManager.getPlayer().setState(StateActor.ATTACKINGRIGHT);
				break;

			}
			//GameManager.getPlayer().attack = true;
			action = 10;
		}
		if(event.getKeyCode()== KeyEvent.VK_P){
			GameManager.getPlayer().setState(StateActor.PROTECTED);
			action = 100;
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
		if(event.getKeyCode()== KeyEvent.VK_SPACE){
			GameManager.getPlayer().setState(StateActor.NONE);
		}
		if(event.getKeyCode()== KeyEvent.VK_P){
			GameManager.getPlayer().setState(StateActor.NONE);
		}

		action = -1;
	}

	@Override
	public void keyTyped(KeyEvent event) {
		//GameManager.endIntro = true; // pour la fin de l'intro

	}

}
