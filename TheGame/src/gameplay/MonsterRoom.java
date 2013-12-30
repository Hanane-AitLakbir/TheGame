package gameplay;

import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

import entities.Hero;
import entities.Monster;

public class MonsterRoom extends Room {

	Monster[] monsters;

	public MonsterRoom(Hero player, int difficulty) {
		super(player, difficulty);
		monsters = new Monster[difficulty];
		for(int i = 0 ; i<difficulty;i++){
			monsters[i] = new Monster(new AtomicInteger(50*(i+1)*4),new AtomicInteger(50*(i+1)*4),"Monster1",difficulty);
			// ne pas placer tous les monstres au même endroit (normalement en diagonale mais les monstres se téléportent !!!)
			monsters[i].start();
		}
	}

	public Monster[] getMonsters(){
		return monsters;
	}
	
	public void updateGraphics(Graphics g){
		bg.updateGraphic(g);
		for(Monster m : monsters)
		{
			m.updateGraphics(g);
		}
		player.updateGraphic(g);
	}

}
