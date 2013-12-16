package networking;

import java.io.IOException;

public interface Communicator {

	public void communicate() throws IOException;
	public void stopCommunication();
	
}