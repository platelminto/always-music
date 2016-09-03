package graphics;

import java.io.*;

public class DisplayUtil {

	public static boolean isDisplayAsleep() {

		final String[] output = runCommand(System.getProperty("user.dir") + "/src/graphics/screenasleep"); // C executable that detects whether screen is asleep
		
		return Boolean.parseBoolean(output[0]);
	}

	public static boolean isScreenSaverOn() {

		final String[] output = runCommand(
				"osascript", "-e", 
				"tell application \"System Events\"", "-e", 
				"get running of screen saver preferences", "-e", 
				"end tell"
				);

		return Boolean.parseBoolean(output[0]);
	}
	
	private static String[] runCommand(String...command) {
		
		final ProcessBuilder builder = new ProcessBuilder(command);

		try {

			final Process process = builder.start();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			return reader.lines().toArray(String[]::new);

		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return null;
	}
}