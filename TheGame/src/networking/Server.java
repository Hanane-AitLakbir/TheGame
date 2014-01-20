package networking;

import gameplay.GameManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The server of the game, creates the true GameManager that manages the monsters and everything in the game.<p>
 * Sends the monsters' position to the client<p>
 * Receive the other hero's position.
 */
public class Server implements Communicator {

	private ServerSocket serverSocket;
	private TurnManager turnManager;

	public Server(int port) throws IOException{
		serverSocket = new ServerSocket(port);
		turnManager = new TurnManager();
	}

	public void run(){

		try {
			Socket connexion = serverSocket.accept(); //Creates the connection.
			//System.out.println("server accepted a connexion");
			GameManager.gameIsRunning = true;
			DataOutputStream output = new DataOutputStream(connexion.getOutputStream());
			DataInputStream input = new DataInputStream(connexion.getInputStream());
			int[] message = new int[2];

			output.writeInt(GameManager.endRoom); //sends the position of the endRoom

			while(true){

				if(turnManager.getTurn()){
					try {
						message = GameManager.buffer.consume(); //Reads the messages available.
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					output.writeInt(message[0]);
					output.writeInt(message[1]); //Writes the messages to send.

				}
				
				if(!TurnManager.turn){
					output.writeInt(28792); //It's the turn of the "server Hero"
					output.writeInt(28792);
					try {
						message = GameManager.buffer.consume();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					output.writeInt(message[0]);
					output.writeInt(message[1]);

					message[0] = input.readInt();
					message[1] = input.readInt();
					GameManager.updateActor(message);
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}

