package gameplay;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entities.StatePlayer;


public class Controller implements KeyListener{
	public static int action = -1;

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode()== KeyEvent.VK_UP){
			GameManager.getPlayer().setState(StatePlayer.UP);
			action = 8;
		}		
		if(event.getKeyCode()== KeyEvent.VK_DOWN){
			GameManager.getPlayer().setState(StatePlayer.DOWN);
			action = 2;
		}		
		if(event.getKeyCode()== KeyEvent.VK_RIGHT){
			GameManager.getPlayer().setState(StatePlayer.RIGHT);
			action = 6;
		}		
		if(event.getKeyCode()== KeyEvent.VK_LEFT){
			GameManager.getPlayer().setState(StatePlayer.LEFT);
			action = 4;
		}
		if(event.getKeyCode()== KeyEvent.VK_SPACE){
			switch(GameManager.getPlayer().getHeroState()){

			case UP : 
				GameManager.getPlayer().setState(StatePlayer.ATTACKINGUP);
				break;
			case DOWN : 
				GameManager.getPlayer().setState(StatePlayer.ATTACKINGDOWN);
				break;
			case LEFT : 
				GameManager.getPlayer().setState(StatePlayer.ATTACKINGLEFT);
				break;
			case RIGHT :
				GameManager.getPlayer().setState(StatePlayer.ATTACKINGRIGHT);
				break;

			}
			//GameManager.getPlayer().attack = true;
			action = 10;
		}
		if(event.getKeyCode()== KeyEvent.VK_P){
			GameManager.getPlayer().setState(StatePlayer.PROTECTED);
			action = 100;
		}


	}

	@Override
	public void keyReleased(KeyEvent event) {
		if(event.getKeyCode()== KeyEvent.VK_UP){
			GameManager.getPlayer().setState(StatePlayer.NONE);
		}		
		if(event.getKeyCode()== KeyEvent.VK_DOWN){
			GameManager.getPlayer().setState(StatePlayer.NONE);
		}		
		if(event.getKeyCode()== KeyEvent.VK_RIGHT){
			GameManager.getPlayer().setState(StatePlayer.NONE);
		}		
		if(event.getKeyCode()== KeyEvent.VK_LEFT){
			GameManager.getPlayer().setState(StatePlayer.NONE);
		}
		if(event.getKeyCode()== KeyEvent.VK_SPACE){
			GameManager.getPlayer().setState(StatePlayer.NONE);
		}
		if(event.getKeyCode()== KeyEvent.VK_P){
			GameManager.getPlayer().setState(StatePlayer.NONE);
		}

		action = -1;
	}

	@Override
	public void keyTyped(KeyEvent event) {
		//GameManager.endIntro = true; // pour la fin de l'intro

	}

}
