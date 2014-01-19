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
			GameManager.gameIsRunning = true;
			DataOutputStream output = new DataOutputStream(connexion.getOutputStream());
			DataInputStream input = new DataInputStream(connexion.getInputStream());
			int[] message = new int[2];

			output.writeInt(GameManager.endRoom); //sends endRoom

			while(true){

				if(turnManager.getTurn()){
					try {
						message = GameManager.buffer.consume();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
					}
					output.writeInt(message[0]);
					output.writeInt(message[1]);

					//output.writeInt(GameManager.playerAction()); // sends performed action by the player
				}
				//else
				if(!TurnManager.turn){
					output.writeInt(28792);
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
					//GameManager.updateOtherPlayers(message[1]);
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
