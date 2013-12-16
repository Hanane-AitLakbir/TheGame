package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Communicator {

	private ServerSocket serverSocket;
	private boolean isWorking;
	
	public Server(int port) throws IOException{
		serverSocket = new ServerSocket(port);
		isWorking = true;
	}

	public void communicate() throws IOException{
		Socket connexion = serverSocket.accept();
		DataOutputStream output = new DataOutputStream(connexion.getOutputStream());
		DataInputStream input = new DataInputStream(connexion.getInputStream());
		
		while(isWorking){
			
		}
	}

	public void stopCommunication() {
		// TODO Auto-generated method stub
		isWorking = false;
	}
}