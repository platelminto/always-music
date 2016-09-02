package main;

import java.awt.*;
import java.io.*;
import java.util.Arrays;

public class AlwaysMusic {

	private final static String SLEEP_LINE_STRING = "sleep"; // Name of line which lists why device isn't going to sleep
	private final static String AUDIO_PREVENT_SLEEP_STRING = "coreaudiod"; // coreaudiod prevents device from sleeping if audio is playing/has played recently
	private final static String PREFERRED_MUSIC_PLAYER = "Spotify";
	private final static String ENABLE_STRING = "Enable", DISABLE_STRING = "Disable";
	private final Image ENABLED_IMAGE = Toolkit.getDefaultToolkit().getImage(AlwaysMusic.class.getResource("/enabled.png"));
	private final Image DISABLED_IMAGE = Toolkit.getDefaultToolkit().getImage(AlwaysMusic.class.getResource("/disabled.png"));
	private static int CHECK_INTERVAL_SECONDS = 60;
	private boolean isEnabled = true;
	private Tray tray;

	public static void main(String...args) {
		
		System.setProperty("apple.awt.UIElement", "true"); // Prevent java icon from appearing in dock
		
		new AlwaysMusic();
	}

	AlwaysMusic() {

		setUpTray();

		while(true) {

			final String sleepLine = getLineFromArray(SLEEP_LINE_STRING, getOutputFromCommand("pmset", "-g")); // Lists various system properties

			if(isEnabled && !isAudioPlaying(sleepLine) && !DisplayUtility.isDisplayAsleep()) { // Only start music if no audio is currently being played by the device, screen is on & program is enabled

				if(PlayUtility.playMusic(PREFERRED_MUSIC_PLAYER))

					System.out.println("Now playing...");

				else 

					System.err.println("ERROR");
			}

			sleep(CHECK_INTERVAL_SECONDS * 1000);
		}
	}

	void setUpTray() {

		final MenuItem item = new MenuItem(isEnabled ? DISABLE_STRING : ENABLE_STRING);
		item.addActionListener(e -> {

			if(isEnabled) {

				setStatus(item, ENABLE_STRING, DISABLED_IMAGE);
				isEnabled = false;

			} else {

				setStatus(item, DISABLE_STRING, ENABLED_IMAGE);
				isEnabled = true;
			}
		});

		
		tray = new Tray(ENABLED_IMAGE, 16, 15);
		tray.addItemsToTray(item);
		
		tray.pushTray();
	}

	void setStatus(MenuItem item, String label, Image image) { // Sets properties for menu item when toggling state

		item.setLabel(label);
		tray.setTrayImage(image);
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
