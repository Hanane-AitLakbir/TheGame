package networking;

import gameplay.GameManager;

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
			Socket connexion = serverSocket.accept();
			System.out.println("server accepted a connexion");
			DataOutputStream output = new DataOutputStream(connexion.getOutputStream());
			DataInputStream input = new DataInputStream(connexion.getInputStream());

			while(true){
				if(turnManager.getTurn()){
					output.writeInt(GameManager.playerAction()); // sends performed action by the player
				} 			
				else{
					output.writeInt(28792);
					GameManager.updateOtherPlayers(input.readInt());
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

class TaskThread extends Thread{
	DataOutputStream output;
	DataInputStream input; 
	TurnManager turnManager;

	TaskThread(Socket connexion, TurnManager turnManager) throws IOException{
		output = new DataOutputStream(connexion.getOutputStream());
		input = new DataInputStream(connexion.getInputStream());
		this.turnManager = turnManager;
	}

	public void run(){
		while(true){
			try {
				if(turnManager.getTurn()){
					output.writeInt(GameManager.playerAction()); // sends the action performed by the player
					System.out.println("\t\t\t server says : My turn !!");
				}
				else{
					output.writeInt(28792);
					GameManager.updateOtherPlayers(input.readInt());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}
