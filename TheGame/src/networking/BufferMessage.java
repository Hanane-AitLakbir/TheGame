package networking;

import java.util.concurrent.Semaphore;

public class BufferMessage {
	int[] buffer;
	int n=50;
	Semaphore nbMess = new Semaphore(0),nbCases = new Semaphore(n);
	Semaphore mutexProd = new Semaphore(1), mutexCon=new Semaphore(0);
	int cursorIn = 0, cursorOut = 0;
	
	public BufferMessage(){
		buffer = new int[n];
	}
	public int[] consume() throws InterruptedException {
		int[] message = new int[2];
		nbMess.acquire(2);
		message[0] = buffer[cursorOut];
		message[1] = buffer[cursorOut+1];
		cursorOut = (cursorOut +2)%n;
		nbCases.release(2);
		return message;
	}
	
	public void produce(int[] message) throws InterruptedException{
		nbCases.acquire(2);
		mutexProd.acquire();
		buffer[cursorIn] = message[0];
		buffer[cursorIn +1] = message[1];
		cursorIn = (cursorIn +2)%n;
		mutexProd.release();
		nbMess.release(2);
	}

}
