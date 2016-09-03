package main;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;

import graphics.DisplayUtil;
import systemtray.Tray;

public class AlwaysMusic {

	private final static String SLEEP_LINE_STRING = "sleep"; // Name of line which lists why device isn't going to sleep
	private final static String AUDIO_PREVENT_SLEEP_STRING = "coreaudiod"; // coreaudiod prevents device from sleeping if audio is playing/has played recently
	private final static String PREFERRED_MUSIC_PLAYER = "Spotify";
	private final static String ENABLE_STRING = "Enable", DISABLE_STRING = "Disable";
	private final Image ENABLED_IMAGE = Toolkit.getDefaultToolkit().getImage(AlwaysMusic.class.getResource("/enabled.png"));
	private final Image DISABLED_IMAGE = Toolkit.getDefaultToolkit().getImage(AlwaysMusic.class.getResource("/disabled.png"));
	private MenuItem statusMenuItem;
	private static int INTERVAL_SECONDS_SHORT = 10, INTERVAL_SECONDS_MEDIUM = 5 * 60, INTERVAL_SECONDS_LONG = 45 * 60;
	private static String INTERVAL_SHORT_STRING = "Short", INTERVAL_MEDIUM_STRING = "Medium", INTERVAL_LONG_STRING = "Long";
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

			if(isEnabled && !isAudioPlaying(sleepLine) && !DisplayUtil.isDisplayAsleep() && !DisplayUtil.isScreenSaverOn()) { // Only start music if no audio is currently being played by the device, screen is on & program is enabled

				if(PlayUtil.playMusic(PREFERRED_MUSIC_PLAYER))

					System.out.println("Now playing...");

				else 

					System.err.println("ERROR");
			}

			sleep(INTERVAL_SECONDS_SHORT * 1000);
		}
	}

	void setUpTray() {

		statusMenuItem = new MenuItem(isEnabled ? DISABLE_STRING : ENABLE_STRING);
		statusMenuItem.addActionListener(e -> toggleStatus());

		tray = new Tray(ENABLED_IMAGE, 16, 15);
		tray.addItems(statusMenuItem);
		tray.addMouseListener(new MouseAdapter() { // Toggle enabled status on right click

			@Override
			public void mousePressed(MouseEvent e) {

				if(e.getButton() == MouseEvent.BUTTON3)
					
					toggleStatus();
			}
		});
		
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(e -> quit());
		
		tray.addSeparator();
		tray.addItems(exitItem);

		tray.pushTray();
	}

	void quit() {
		
		tray.remove();
		System.exit(0);
	}
	
	void toggleStatus() {

		if(isEnabled) {

			setStatus(statusMenuItem, ENABLE_STRING, DISABLED_IMAGE);
			isEnabled = false;

		} else {

			setStatus(statusMenuItem, DISABLE_STRING, ENABLED_IMAGE);
			isEnabled = true;
			PlayUtil.playMusic(PREFERRED_MUSIC_PLAYER); // When re-enabled, music immediately starts to play
		}
	}

	void setStatus(MenuItem item, String label, Image image) { // Sets properties for menu item when toggling state

		item.setLabel(label);
		tray.setTrayImage(image);
	}

	public static void sleep(int milliseconds) {

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