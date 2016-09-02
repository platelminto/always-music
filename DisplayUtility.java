package main;

import com.sun.jna.ptr.IntByReference;

public class DisplayUtility {

	static final int DISPLAY_LIMIT = 16;

	public static boolean isDisplayAsleep() {

		IntByReference ibf = new IntByReference();
		int[] displayIds = new int[DISPLAY_LIMIT];
		boolean asleep = true;

		CoreGraphics.INSTANCE.CGGetActiveDisplayList(DISPLAY_LIMIT, displayIds, ibf);
		
		int i = ibf.getValue();

		while (--i >= 0)

			asleep &= CoreGraphics.INSTANCE.CGDisplayIsAsleep(displayIds[i]);

		return asleep;
	}
}