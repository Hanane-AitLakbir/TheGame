package networking;

import java.util.concurrent.Semaphore;

/**
 * A buffer to stock the messages sent through the network. Follows the desing pattern Producer/Consumer.<p>
 * You can change its size here.
 */
public class BufferMessage {
	int[] buffer;
	int n=2000; //the space in the buffer.
	Semaphore nbMess = new Semaphore(0),nbCases = new Semaphore(n); //Tells the number of messages and room in the buffer.
	Semaphore mutexProd = new Semaphore(1), mutexCon=new Semaphore(0);
	int cursorIn = 0, cursorOut = 0;
	
	public BufferMessage() {
		buffer = new int[n];
	}
	
	public int[] consume() throws InterruptedException {
		int[] message = new int[2];
		nbMess.acquire(2); //Takes 2 messages
		
		message[0] = buffer[cursorOut];
		message[1] = buffer[cursorOut+1]; //Registers the messages.
		cursorOut = (cursorOut +2)%n; //Moves the cursor.
		
		nbCases.release(2); //There is 2 new empty spaces.
		//System.out.println("\t\t\t consume " + nbMess.availablePermits()); //Test line.
		return message;
	}
	
	public void produce(int[] message) throws InterruptedException {
		nbCases.acquire(2); //Takes 2 empty spaces.
		mutexProd.acquire(); //Prevents another producer from producing. 
		//Critical Part Start.
		
		buffer[cursorIn] = message[0]; 
		buffer[cursorIn +1] = message[1]; /*Stocks the messages. 
											(+1 is fine because the rooms in the buffer is even, and so in the number of messages each time */
		cursorIn = (cursorIn +2)%n; //Moves the cursor.
		
		//Critical Part End.
		mutexProd.release(); //Allows the other producers to produce. 
		nbMess.release(2); //There are 2 new messages in the buffer.
		//System.out.println("\t\t\t produce " + nbMess.availablePermits()); //Test line.
	}
	
	/**
	 * @return the number of messages in the buffer.
	 */
	public int getMess(){
		return nbMess.availablePermits();
	}

}
