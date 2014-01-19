package utilities;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class SoundPlayer{
	private AudioClip song; // Sound player
	private URL songPath; // Sound path
	private boolean isPlaying = false;
	public SoundPlayer(String name)
	{
		try
		{
			songPath = getClass().getResource(("/sounds/" + name + ".wav")); // Get the Sound URL

			song = Applet.newAudioClip(songPath); // Load the Sound

		}

		catch(Exception e){} // Satisfy the catch

	}

	public void playSound()

	{
		song.loop(); // Play
		isPlaying = true;
	}

	public void stopSound()

	{

		song.stop(); // Stop
		isPlaying = false;
	}

	public void playSoundOnce()

	{
		song.play(); // Play only once
	}

	public boolean isPlaying(){
		return isPlaying;
	}

}
