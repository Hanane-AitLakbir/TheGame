package gamePlay;

import entities.Hero;
import entities.Monster;

public class MonsterRoom extends Room {

	Monster[] monsters;
	
	public MonsterRoom(Hero player, int difficulty) {
		super(player, difficulty);
		monsters = new Monster[difficulty];
		for(int i = 0 ; i<difficulty;i++){
			monsters[i] = new Monster(50,50,"Monster1",difficulty);
		}
	}

}
