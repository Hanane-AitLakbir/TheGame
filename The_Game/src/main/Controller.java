package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entities.StatePlayer;


public class Controller implements KeyListener{

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode()== KeyEvent.VK_UP){
			Game.getPlayer().state = StatePlayer.UP;
		}		
		if(event.getKeyCode()== KeyEvent.VK_DOWN){
			Game.getPlayer().state = StatePlayer.DOWN;
		}		
		if(event.getKeyCode()== KeyEvent.VK_RIGHT){
			Game.getPlayer().state = StatePlayer.RIGHT;
		}		
		if(event.getKeyCode()== KeyEvent.VK_LEFT){
			Game.getPlayer().state = StatePlayer.LEFT;
		}
		if(event.getKeyCode()== KeyEvent.VK_SPACE){
			//Game.getPlayer().state = StatePlayer.ATTACKING;
			Game.getPlayer().attack = true;
		}
		if(event.getKeyCode()== KeyEvent.VK_P){
			Game.getPlayer().state = StatePlayer.PROTECTED;
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
		
	}

	@Override
	public void keyTyped(KeyEvent event) {
		Game.endIntro = true; // pour la fin de l'intro

	}
	
}
