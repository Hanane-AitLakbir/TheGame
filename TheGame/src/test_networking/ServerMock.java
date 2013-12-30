package test_networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import networking.Communicator;
import networking.TurnManager;

public class ServerMock implements Communicator {

	private ServerSocket serverSocket;
	private TurnManager turnManager;
	private Socket connexion;
	DataOutputStream output;
	DataInputStream input; 

	public ServerMock(int port) throws IOException{
		serverSocket = new ServerSocket(port);
		turnManager = new TurnManager();
		System.out.println("server created");
	}

	public void run(){
//		try {
//			while(true){
//				new TaskThread(serverSocket.accept(),turnManager).start();
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

						try {
							connexion = serverSocket.accept();
							System.out.println("server accepted a connexion");
							output = new DataOutputStream(connexion.getOutputStream());
							input = new DataInputStream(connexion.getInputStream());
				
							while(true){
								if(turnManager.getTurn()){
									output.writeInt(GameManagerMock.playerAction()); // sends performed action by the player
									System.out.println("\t\t\t server says : My turn !!");
								} 			
								else{
									output.writeInt(28792);
									GameManagerMock.updateOtherPlayers(input.readInt());
									System.out.println("\t\t\t server says : I don't play");
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
		this.setName("3");
	}

	public void run(){
		while(true){
			try {
				if(turnManager.getTurn()){

					output.writeInt(GameManagerMock.playerAction());
					// sends performed action by the player
					System.out.println("\t\t\t server says : My turn !!");
				} 			
				else{
					output.writeInt(28792);
					GameManagerMock.updateOtherPlayers(input.readInt());
					System.out.println("\t\t\t server says : I don't play");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}



