package entities;

/**
 * Represents the different stats an actor can be in.<p>
 * UP, DOWN, LEFT and RIGHT are for the movements<p>
 * ATTACKINGUP, ATTACKINGDOWN, ATTACKINGLEFT and ATTACKINGRIGHT are for the attacks<p>
 * NONE is for when they do nothing.
 */
public enum StateActor {
	UP,DOWN,LEFT,RIGHT,ATTACKINGUP,ATTACKINGDOWN,ATTACKINGLEFT,ATTACKINGRIGHT,NONE;

	/**
	 * Converts the action code received through the network into a state.
	 * @param action the action code
	 * @return the state
	 */
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
		default : return NONE;
		
		}
		
	}

	/**
	 * Converts a state into an action code to send through the network
	 * @param state the state of the actor
	 * @return the action integer code
	 */
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
		default : return 5;
		
		}
		
	}
	
}
