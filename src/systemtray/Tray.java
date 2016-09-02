package systemtray;

import java.awt.*;
import java.awt.event.MouseAdapter;

public class Tray {

	private final TrayIcon trayIcon;
	private int width, height;
	private final PopupMenu menu;
	private final SystemTray tray = SystemTray.getSystemTray();

	public Tray(Image image, int width, int height) {

		this.width = width;
		this.height = height;

		if(SystemTray.isSupported()) {

			menu = new PopupMenu();
			trayIcon = new TrayIcon(correctImage(image, width, height), "TrayIcon", menu);

		} else {

			menu = null;
			trayIcon = null;
			System.exit(1);
		}
	}

	public void addItems(MenuItem...items) {

		for(MenuItem item : items)

			menu.add(item);
	}

	public void addSeparator() {

		menu.addSeparator();
	}
	
	public void remove() {
		
		tray.remove(trayIcon);
	}

	public void addMouseListener(MouseAdapter e) {

		trayIcon.addMouseListener(e);
	}

	public boolean pushTray() {

		try {

			tray.add(trayIcon);

		} catch (AWTException e) {

			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void setTrayImage(Image image) {

		trayIcon.setImage(correctImage(image, width, height));
	}

	private static Image correctImage(Image image, int width, int height) { // Set image to preferred size with correct hints

		return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
}