package gameplay;

import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

import entities.Hero;
import entities.Monster;

public class MonsterRoom extends Room {

	Monster[] monsters;

	public MonsterRoom(Hero player, int nbMonster,int difficulty) {
		super(player, nbMonster);
		monsters = new Monster[nbMonster];
		for(int i = 0 ; i<monsters.length;i++){
			monsters[i] = new Monster((50*4 + i*20),(50*4 + i*20),"Monster1",difficulty);
			// ne pas placer tous les monstres au même endroit (normalement en diagonale mais les monstres se téléportent !!!)
			monsters[i].start();
		}
	}
	
	public Monster[] getMonsters(){
		return monsters;
	}
	
	@Override
	public void updateGraphics(Graphics g){
		bg.updateGraphic(g);
		//DONE in run() (Thread) => 
//		for(Monster m : monsters)
//		{
//			m.updateGraphics(g);
//		}
		//player.updateGraphic(g);
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

}
