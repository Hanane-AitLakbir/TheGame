package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Communicator {

	private ServerSocket serverSocket;
	private TurnManager turnManager;

	public Server(int port) throws IOException{
		serverSocket = new ServerSocket(port);
		turnManager = new TurnManager();
	}

	public void run(){
		try {

			while(true){
				new TaskThread(serverSocket.accept(), turnManager);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

class TaskThread extends Thread{
	DataOutputStream output;
	DataInputStream input; 
	TurnManager turnManager;

	TaskThread(Socket connexion, TurnManager turnManager) throws IOException{
		output = new DataOutputStream(connexion.getOutputStream());
		input = new DataInputStream(connexion.getInputStream());
	}

	public void run(){
		while(true){
			if(turnManager.getTurn()){
				// TODO A decommenter
//				try {
//					output.writeInt(GameManager.playerAction()); // sends performed action by the player
//				} catch (IOException e) {
//					e.printStackTrace();
//				} 
			}
			else{
				// TODO A decommenter
				//output.writeInt(28792);
				//GameManager.updateOtherPlayer(input.writeInt());

			}
		}
	}


}
