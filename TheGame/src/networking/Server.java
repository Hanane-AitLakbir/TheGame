package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Communicator {

	private ServerSocket serverSocket;

	public Server(int port) throws IOException{
		serverSocket = new ServerSocket(port);
	}

	public void run(){
		Socket connexion;
		try {
			connexion = serverSocket.accept();
			DataOutputStream output = new DataOutputStream(connexion.getOutputStream());
			DataInputStream input = new DataInputStream(connexion.getInputStream());

			//boucle de traitement de l'info => display
			while(true){

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

}
