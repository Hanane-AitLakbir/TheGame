package display;

import java.util.concurrent.atomic.AtomicInteger;

public class Position {
	
	AtomicInteger x,y;
	
	public Position(AtomicInteger x, AtomicInteger y){
		this.x = x;
		this.y = y;
	}
	
	public void setXY(int x, int y){
		this.x = new AtomicInteger(x);
		this.y = new AtomicInteger(y);
	}
	
	public int getX(){
		return x.get();
	}
	
	public int getY(){
		return y.get();
	}

}
