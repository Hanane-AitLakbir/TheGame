package networking;

import gameplay.GameManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Communicator {

	private Socket socket;

	public Client(String serverName, int port) throws IOException{
		socket = new Socket(serverName, port);
		System.out.println("client connected");
	}

	public void run(){
		DataOutputStream output;
		DataInputStream input;
		
		//int message;
		int[] message = new int[2]; 
		int[] action = new int[2];
		
		try {
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
			GameManager.endRoom = input.readInt(); //reads the endRoom
			
			while(true){
				//message = input.readInt();
				message[0]= input.readInt();
				message[1] = input.readInt();
				
				//USELESS
//				if(message!=28792){
//					TurnManager.turn = false;
//					GameManager.updateActor(message);
//					//GameManager.updateOtherPlayers(message);
//				}
				System.out.println("client : " + message[0]);
				if(message[0]!=28792){
					TurnManager.turn = false;
					//GameManager.updateOtherPlayers(message[1]);
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
					
					//USELESS
					//output.writeInt(GameManager.playerAction()); //sends the action performed by the player
				}
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
