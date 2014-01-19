package gameplay;

import java.awt.Graphics;
import entities.Hero;
import entities.Monster;

public class MonsterRoom extends Room {

	Monster[] monsters;

	public MonsterRoom(Hero player, int nbMonster,int difficulty,int id) {
		super(player, nbMonster);
		roomId = id;
		monsters = new Monster[nbMonster];
		for(int i = 0 ; i<monsters.length;i++){
			monsters[i] = new Monster((50*4 + i*20),(50*4 + i*20),"Monster1",difficulty);
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
			m.setDisplay(false);
		}
	}

	@Override
	public void start() {

		sound.playSound();
		for(Monster m : monsters){
			m.setDisplay(true);
		}
		player.setMonsters(monsters);

	}

	public Monster getMonster(int i) {
		return monsters[i];
	}

}
