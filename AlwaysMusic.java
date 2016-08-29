package main;

import java.io.*;
import java.util.Arrays;

public class AlwaysMusic {

	private final static String SLEEP_LINE_STRING = "sleep"; // Name of line which lists why device isn't going to sleep
	private final static String AUDIO_PREVENT_SLEEP_STRING = "coreaudiod"; // coreaudiod prevents device from sleeping if audio is playing/has played recently
	private final static String PREFERRED_MUSIC_PLAYER = "iTunes";
	private final static int CHECK_INTERVAL_SECONDS = 60;

	public static void main(String...args) {

		new AlwaysMusic();
	}

	AlwaysMusic() {

		while(true) {

			final String sleepLine = getLineFromArray(SLEEP_LINE_STRING, getOutputFromCommand("pmset", "-g")); // Lists various system functions

			if(!isAudioPlaying(sleepLine)) { // Only start music if no audio is currently being played by the device

				if(PlayUtility.playMusic(PREFERRED_MUSIC_PLAYER))

					System.out.println("Now playing...");

				else 

					System.err.println("ERROR");
			}

			sleep(CHECK_INTERVAL_SECONDS * 1000);
		}
	}

	static void sleep(int milliseconds) {

		try {

			Thread.sleep(milliseconds);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	static boolean isAudioPlaying(String sleepLine) {
		
		return sleepLine.contains(AUDIO_PREVENT_SLEEP_STRING);
	}
	
	static String getLineFromArray(String initLine, String[] array) {

		return Arrays.stream(array)
				.parallel()
				.filter(line -> line.trim().startsWith(initLine))
				.findAny()
				.get();
	}

	static String[] getOutputFromCommand(String...command) {

		final ProcessBuilder builder = new ProcessBuilder(command);

		try {

			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); // Hook into the output from running the command

			return reader.lines().toArray(String[]::new);

		} catch (IOException e) {

			e.printStackTrace();
		}

		return null;
	}
}