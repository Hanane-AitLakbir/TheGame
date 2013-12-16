package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import main.Controller;

public class Client implements Communicator {

	private Socket socket;
	private boolean isWorking;

	public Client(InetAddress address, int port) throws IOException{
		socket = new Socket(address, port);
		isWorking = true;
	}

	public void communicate() throws IOException{
		while(isWorking){
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			DataInputStream input = new DataInputStream(socket.getInputStream());
			if(Controller.action!=-1){
				output.writeInt(Controller.action);
			}
			
			System.out.println(input.readInt());
		}

	}

	@Override
	public void stopCommunication() {
		isWorking = false;
		
	}


}