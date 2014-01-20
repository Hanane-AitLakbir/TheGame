package gameplay;

import java.awt.Graphics;

import entities.Hero;
import entities.Monster;

/**
 * A room with monsters inside.<p>
 * The number of monsters depends on the difficulty.<p>
 * Their strenght will also vary greatly depending on the level.
 * Novice -> 1-4 Monsters (LIFE_MAX = 50, power = 5, speed = 1) <p>
 * Normal -> 1-5 Monsters (LIFE_MAX = 75, power = 10, speed = 2) <p>
 * Expert -> 1-6 Monsters (LIFE_MAX = 100, power = 15, speed = 3)
 */
public class MonsterRoom extends Room {

	Monster[] monsters;

	public MonsterRoom(Hero player, int nbMonster,int difficulty,int id) {
		
		super(player, nbMonster);
		roomId = id;
		monsters = new Monster[nbMonster];
		
		for(int i = 0 ; i<monsters.length;i++){
			monsters[i] = new Monster((50*4 + i*30),(50*4 + i*30),"Monster1",difficulty);
			monsters[i].setId(((roomId*10) + i));
			if(!GameManager.clientMode) monsters[i].start();
		}
		
	}

	public Monster[] getMonsters(){
		return monsters;
	}

	@Override
	public void updateGraphics(Graphics g){
		bg.updateGraphic(g);
	}

	@Override
	public void stop() {
		sound.stopSound();
		for(Monster m : monsters){
			m.setDisplay(false); //Hide the monsters so that they cannot do anything.
		}
	}

	@Override
	public void start() {

		sound.playSound();
		for(Monster m : monsters){
			m.setDisplay(true); //Shows the monsters so that they can act.
		}
		player.setMonsters(monsters);

	}

	public Monster getMonster(int i) {
		return monsters[i];
	}

}
