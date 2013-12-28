package networking;

import gameplay.GameManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Communicator {

	private Socket socket;

	public Client(String serverName, int port) throws IOException{
		socket = new Socket(serverName, port);
	}

	public void run(){
		DataOutputStream output;
		DataInputStream input;

			try {
				output = new DataOutputStream(socket.getOutputStream());
				input = new DataInputStream(socket.getInputStream());
				
				while(true){
					if(input.readInt()==28792){
						output.writeInt(GameManager.playerAction()); //sends the performed action by the player
					}
					else{
						GameManager.updateOtherPlayers(input.readInt());
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

}
