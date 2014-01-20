package utilities;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**
 * Allows the other classes to load sounds and then play then anytime.
 */
public class SoundPlayer{
	
	private AudioClip song; // Sound player
	private URL songPath; // Sound path
	private boolean isPlaying = false;
	
	public SoundPlayer(String name){
		try
		{
			songPath = getClass().getResource(("/sounds/" + name + ".wav")); // Get the Sound URL
			song = Applet.newAudioClip(songPath); // Load the Sound
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Plays the sound in a loop.
	 */
	public void playSound(){
		song.loop(); // Play
		isPlaying = true;
	}

	/**
	 * Stops the sound.
	 */
	public void stopSound(){
		song.stop(); // Stop
		isPlaying = false;
	}

	/**
	 * Plays the sound only once.
	 */
	public void playSoundOnce(){
		song.play(); // Play only once
	}

	/**
	 * Tells if a sound is being played.
	 * @return true if a sound is being played, false otherwise.
	 */
	public boolean isPlaying(){
		return isPlaying;
	}

}
