package gamePlay;

import java.awt.Graphics;

import entities.Hero;
import entities.Monster;

public class MonsterRoom extends Room {

	Monster[] monsters;

	public MonsterRoom(Hero player, int difficulty) {
		super(player, difficulty);
		monsters = new Monster[difficulty];
		for(int i = 0 ; i<difficulty;i++){
			monsters[i] = new Monster(80*4,80*4,"Monster1",difficulty);
			monsters[i].start();
		}
	}

	public void updateGraphics(Graphics g){
		for(Monster m : monsters)
		{
			m.updateGraphics(g);
		}
	}

}
