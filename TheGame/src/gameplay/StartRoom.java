package gameplay;

import java.awt.Graphics;

import entities.Hero;

public class StartRoom extends Room{

	public StartRoom(Hero player){
		super(player,0);
	}
	

	@Override
	public void updateGraphics(Graphics g) {
		bg.updateGraphic(g);
		
	}

	@Override
	public void stop() {
		sound.stopSound();
		
	}

	@Override
	public void start() {
		sound.playSound();
	}
}
