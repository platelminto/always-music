package com.platelminto.alwaysmusic.graphics;

import java.io.*;

import com.platelminto.alwaysmusic.commandline.CommandLineUtil;

public class DisplayUtil {

	private final static File SNIPPET_LOCATION = new File("/private/var/tmp/screenasleep.m");
	private static final File COMPILED_LOCATION = new File("/private/var/tmp/screenasleep");

	static {

		createSnippet();
		compileSnippet();
		
		CommandLineUtil.runCommand("chmod", "+x", COMPILED_LOCATION.toString());
	}

	public static boolean isDisplayAsleep() {

		final String[] output =CommandLineUtil.runCommand(COMPILED_LOCATION.toString()); // C executable that detects whether screen is asleep

		return Boolean.parseBoolean(output[0]);
	}

	public static boolean isScreenSaverOn() {

		final String[] output = CommandLineUtil.runCommand(
				"osascript", "-e", 
				"tell application \"System Events\"", "-e", 
				"get running of screen saver preferences", "-e", 
				"end tell"
				);

		return Boolean.parseBoolean(output[0]);
	}

	public static boolean cleanUp() {

		return SNIPPET_LOCATION.delete() && COMPILED_LOCATION.delete();
	}

	private static boolean createSnippet() { // Copy the snippet from the resource to an external, temporary location to then by compiled

		try {

			final BufferedReader reader = new BufferedReader(new InputStreamReader(DisplayUtil.class.getResourceAsStream("/com/platelminto/alwaysmusic/graphics/screenasleep.m")));
			final StringBuilder builder = new StringBuilder();

			String line;

			while((line = reader.readLine()) != null)

				builder.append(line + System.lineSeparator());

			reader.close();

			final PrintWriter writer = new PrintWriter(SNIPPET_LOCATION);
			writer.print(builder.toString());

			writer.close();

		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}

		return true;
	}

	private static void compileSnippet() {

		CommandLineUtil.runCommand("cc", "-framework", "Foundation", "-framework", "CoreGraphics", SNIPPET_LOCATION.toString(), "-o", COMPILED_LOCATION.toString());
	}
}