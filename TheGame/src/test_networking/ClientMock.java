package test_networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import networking.Communicator;

public class ClientMock implements Communicator {

	private Socket socket;

	public ClientMock(String address, int port) throws IOException{
		socket = new Socket(address, port);
		System.out.println("client created");
	}

	public void run(){
		DataOutputStream output;
		DataInputStream input;

		try {
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());

			while(true){

				if(input.readInt()!=28792){
					GameManagerMock.updateOtherPlayers(input.readInt());
					System.out.println("client says :  I don't play");
				}
				else {
					output.writeInt(GameManagerMock.playerAction()); //sends the performed action by the player
					System.out.println("client says : My turn !!");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
