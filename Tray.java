package main;

import java.awt.*;

public class Tray {

	private final TrayIcon trayIcon;
	private int width, height;
	private final PopupMenu menu;

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
	
	public void addItemsToTray(MenuItem...items) {
		
		for(MenuItem item : items)
			
			menu.add(item);
	}
	
	public void addSeparatorToTray() {
		
		menu.addSeparator();
	}
	
	public boolean pushTray() {
		
		try {

			SystemTray.getSystemTray().add(trayIcon);

		} catch (AWTException e) {

			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public void setTrayImage(Image image) {

		trayIcon.setImage(correctImage(image, width, height));
	}

	private static Image correctImage(Image image, int width, int height) {

		return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
	
	public PopupMenu getMenu() {
		
		return menu;
	}
}