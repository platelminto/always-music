package main;

import java.io.IOException;

public abstract class PlayUtility {
	
	public static boolean playMusic(String playerType) {

		switch(playerType) {

		case "Spotify":

			return playViaCommand(defaultPlayCommand(playerType));
			
		case "iTunes":
			
			return playViaCommand(defaultPlayCommand(playerType));

		default:

			return false;
		}
	}

	private static boolean playViaCommand(String[] command) { // Plays music using a terminal command

		try {

			final ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();

		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	private static String[] defaultPlayCommand(String applicationName) { 
		
		return new String[] {"osascript", "-e", "tell application \"" + applicationName + "\" to play"}; // Default command to play music - most applications use this format
	}
}
