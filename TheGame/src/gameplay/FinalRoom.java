package gameplay;

import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

import sound.SoundPlayer;
import display.Position;
import entities.Hero;
import entities.Item;
import entities.Monster;

public class FinalRoom extends Room{

	private Position center;
	private Item chest;
	private boolean chestOpened = false;
	private Monster[] monster = new Monster[1];

	public FinalRoom(Hero player) {
		super(player, 0);
		center = new Position(75*4,75*4);
		chest = new Item("chest", center, null);
		monster[0] = new Monster(280, 280, "Monster2", 3){
			private boolean canAttack(){

				int dx = position.getX() - target.getPosition().getX();
				int dy = position.getY() - target.getPosition().getY();

				if(Math.abs(dx)<35 && Math.abs(dy)<35) return true;
				else return false;
			}
		};
		monster[0].start();
	}

	@Override
	public void updateGraphics(Graphics g) {
		bg.updateGraphic(g);
		chest.updateGraphics();
		if(player.getPosition().getX()<80*4 
				&& player.getPosition().getX()>70*4 
				&& player.getPosition().getY()<90*4 
				&& player.getPosition().getY()>80*4
				&& chestOpened ==false
				&& monster==null){
			chestOpened = true;
			sound.stopSound();
			chest.actionItem();
			new SoundPlayer("final").playSoundOnce();
		}
	}

	@Override
	public void stop() {
		sound.stopSound();
		monster[0].setDisplay(false);
	}

	@Override
	public void start() {
		sound.playSound();
		monster[0].setDisplay(true);
		player.setMonsters(monster);
	}
	
	public boolean winGame(){
		return chestOpened;
	}

}
