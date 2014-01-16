package entities;

public enum StateActor {
	UP,DOWN,LEFT,RIGHT,ATTACKINGUP,ATTACKINGDOWN,ATTACKINGLEFT,ATTACKINGRIGHT,PROTECTED,NONE;

	public static StateActor convertToState(int action){

		switch (action){

		case 8 : return UP;
		case 2 : return DOWN;
		case 4 : return LEFT;
		case 6 : return RIGHT;
		case 88 : return ATTACKINGUP;
		case 22 : return ATTACKINGDOWN;
		case 44 : return ATTACKINGLEFT;
		case 66 : return ATTACKINGRIGHT;

		}

		return NONE;

	}

	public static int convertToInt(StateActor state){

		switch (state){

		case UP : return 8;
		case DOWN : return 2;
		case LEFT : return 4;
		case RIGHT : return 6;
		case ATTACKINGUP : return 88;
		case ATTACKINGDOWN : return 22;
		case ATTACKINGLEFT : return 44;
		case ATTACKINGRIGHT : return 66;

		}

		return 5;
	}
}


