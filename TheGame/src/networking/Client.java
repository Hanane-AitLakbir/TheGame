package networking;

import gameplay.GameManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The Client of the game in multiplayer mode. Communicates with a preexisting server.<p>
 * Reads messages sent by the Server (the position of the monsters<p>
 * Sends the moves of the hero.
 */
public class Client implements Communicator {

	private Socket socket;

	public Client(String serverName, int port) throws IOException{
		socket = new Socket(serverName, port);
		//System.out.println("client connected");
	}

	public void run(){
		DataOutputStream output;
		DataInputStream input;
		GameManager.gameIsRunning = true;
		
		int[] message = new int[2]; 
		int[] action = new int[2];
		
		try {
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
			GameManager.endRoom = input.readInt(); //reads the endRoom, to put it at the same spot.
			
			while(true){
				
				message[0]= input.readInt();
				message[1] = input.readInt(); //Reads the message from the server.
				
				//System.out.println("client : " + message[0]);
				if(message[0]!=28792){
					TurnManager.turn = false;
					
					GameManager.buffer.consume();
					GameManager.updateActor(message);
				}else{
					TurnManager.turn = true;
					
					//receives monsters moves
					message[0]= input.readInt();
					message[1] = input.readInt();
					GameManager.updateActor(message);
					
					//sends hero's moves
					action = GameManager.buffer.consume();
					output.writeInt(action[0]);
					output.writeInt(action[1]);
					
				}
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
