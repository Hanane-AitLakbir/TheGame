package gameplay;

import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

import sound.SoundPlayer;
import display.Position;
import entities.Hero;
import entities.Item;

public class FinalRoom extends Room{

	private Position center;
	private Item chest;
	private boolean chestOpened = false;

	public FinalRoom(Hero player) {
		super(player, 0);
		center = new Position(75*4,75*4);
		chest = new Item("chest", center);
	}

	@Override
	public void updateGraphics(Graphics g) {
		bg.updateGraphic(g);
		chest.updateGraphics();
		if(player.getPosition().getX()<80*4 
				&& player.getPosition().getX()>70*4 
				&& player.getPosition().getY()<90*4 
				&& player.getPosition().getY()>50*4
				&& chestOpened ==false){
			chestOpened = true;
			sound.stopSound();
			chest.actionItem();
			new SoundPlayer("final").playSoundOnce();
		}
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
