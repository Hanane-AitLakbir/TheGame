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

		try {
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());

			while(true){
				if(input.readInt()!=28792){
					GameManager.updateOtherPlayers(input.readInt());
				}
				else {
					output.writeInt(GameManager.playerAction()); //sends the action performed by the player
				}
			}

		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	}

}
