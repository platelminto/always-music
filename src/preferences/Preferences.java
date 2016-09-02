package preferences;

import java.io.File;

public class Preferences {

	private final static String PREF_NAME = "com.platelminto.AlwaysMusicPreferences";
	private final static File PREF_LOCATION = new File(System.getProperty("user.home") + "Library/Preferences/" + PREF_NAME); 
	
	public static boolean setIntervalTime(int intervalTimeSeconds) {
		
		
	}
}