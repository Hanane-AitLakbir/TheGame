package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import gameplay.Controller;

public class Client implements Communicator {

	private Socket socket;

	public Client(InetAddress address, int port) throws IOException{
		socket = new Socket(address, port);
	}

	public void run(){
		DataOutputStream output;
		DataInputStream input;

		while(true){


			try {
				output = new DataOutputStream(socket.getOutputStream());
				input = new DataInputStream(socket.getInputStream());
				if(Controller.action!=-1){

					output.writeInt(Controller.action);
					System.out.println(input.readInt());}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


	}


}
