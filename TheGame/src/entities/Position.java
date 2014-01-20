package entities;

/**
 * An object to represent the spatial position of the actors in the pixel grid.
 */
public class Position {

	int x,y;

	public Position(int x,int y){
		this.x = x;
		this.y = y;
	}
	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

}