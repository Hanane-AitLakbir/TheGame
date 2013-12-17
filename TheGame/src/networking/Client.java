package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Communicator {

	private Socket socket;

	public Client(InetAddress address, int port) throws IOException{
		socket = new Socket(address, port);
	}

	public void run(){
		DataOutputStream output;
		DataInputStream input;

			try {
				output = new DataOutputStream(socket.getOutputStream());
				input = new DataInputStream(socket.getInputStream());
				
				while(true){
					if(input.readInt()==28792){
						// TODO A decommenter
						//output.writeInt(GameManager.playerAction(); //sends the performed action by the player
					}
					else{
						// TODO A decommenter
						//GameManager.updateOtherPlayer(input.writeInt());
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

}
