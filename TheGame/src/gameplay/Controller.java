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
			//Game.getPlayer().state = StatePlayer.ATTACKING;
			GameManager.getPlayer().attack = true;
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
			Game.getPlayer().state = StatePlayer.NONE;
			//Game.getPlayer().up = false;
		}		
		if(event.getKeyCode()== KeyEvent.VK_DOWN){
			Game.getPlayer().state = StatePlayer.NONE;
			//Game.getPlayer().dw = false;
		}		
		if(event.getKeyCode()== KeyEvent.VK_RIGHT){
			Game.getPlayer().state = StatePlayer.NONE;
			//Game.getPlayer().rt = false;
		}		
		if(event.getKeyCode()== KeyEvent.VK_LEFT){
			Game.getPlayer().state = StatePlayer.NONE;
			//Game.getPlayer().lt = false;
		}
		if(event.getKeyCode()== KeyEvent.VK_SPACE){
			Game.getPlayer().state = StatePlayer.NONE;
			Game.getPlayer().attack = false;
		}
		if(event.getKeyCode()== KeyEvent.VK_P){
			Game.getPlayer().state = StatePlayer.NONE;
			Game.getPlayer().protect = false;
		}
		
		action = -1;
	}

	@Override
	public void keyTyped(KeyEvent event) {
		Game.endIntro = true; // pour la fin de l'intro

	}
	
}
