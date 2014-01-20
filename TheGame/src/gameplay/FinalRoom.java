package gameplay;

import java.awt.Graphics;

import utilities.SoundPlayer;
import entities.Hero;
import entities.Item;
import entities.Monster;
import entities.Position;

/**
 * The last room of the labyrinth, the one with the treasure and the boss.<p>
 * You cannot open the treasure prior to having killed the boss monster.<p>
 * Once you have done that, the chest will automatically open, and the game will end ! Congratulations !
 */
public class FinalRoom extends Room{

	private Position center;
	private Item chest;
	private static boolean chestOpened = false;
	private Monster[] monster = new Monster[1];

	/**
	 * Creates the Treasure and the Monster2 boss.
	 * @param player the Hero
	 */
	public FinalRoom(Hero player) {
		super(player, 0);
		center = new Position(75*4,75*4);
		chest = new Item("chest", center, null);
		monster[0] = new Monster(280, 280, "Monster2", 4){
			@SuppressWarnings("unused")
			private boolean canAttack(){

				int dx = position.getX() - target.getPosition().getX();
				int dy = position.getY() - target.getPosition().getY();

				if(Math.abs(dx)<35 && Math.abs(dy)<35) return true;
				else return false;
			}
		};
		monster[0].start();
	}

	/**
	 * Update the graphics of the final room.
	 */
	@Override
	public void updateGraphics(Graphics g) {
		bg.updateGraphic(g);
		chest.updateGraphics();
		if(monster[0].isDead() && !chestOpened)
			//player.getPosition().getX()<80*4 
			//	&& player.getPosition().getX()>70*4 
			//	&& player.getPosition().getY()<90*4 
			//	&& player.getPosition().getY()>80*4
			{
			chestOpened = true;
			sound.stopSound();
			chest.actionItem();
			new SoundPlayer("final").playSoundOnce();
		}
	}

	/**
	 * Stops the room, hides the monster and stops the sound when the Hero leaves the room.
	 */
	@Override
	public void stop() {
		sound.stopSound();
		monster[0].setDisplay(false);
	}

	/**
	 * Starts the room, displays the monster and play the sound when the Hero enters the room.
	 */
	@Override
	public void start() {
		sound.playSound();
		monster[0].setDisplay(true);
		player.setMonsters(monster);
	}
	
	/**
	 * When the chest is opened, the game is won !!!!
	 * @return true if the game is won, false otherwise.
	 */
	public static boolean winGame(){
		return chestOpened;
	}

}
