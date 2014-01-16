package test_networking;

import java.io.IOException;

public class GameManagerMock {

	public static int playerAction(){
		return Integer.parseInt(Thread.currentThread().getName());
	}

	public static void updateOtherPlayers(int action){
		System.out.println("\t\t\t\t\t\t player action updated " + action);
	}

	public static void main(String[] args){
		try {
			// TODO Change hostname and port !!!
			Thread server = new Thread(new ServerMock(1455));
			server.setName("1");
			Thread client = new Thread(new ClientMock("pc_de_hanane", 1455));
			client.setName("0");
			server.start();
			client.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
