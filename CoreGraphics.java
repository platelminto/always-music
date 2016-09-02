package main;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;

public interface CoreGraphics extends Library {

	CoreGraphics INSTANCE = (CoreGraphics) Native.loadLibrary("CoreGraphics", CoreGraphics.class);

	public static class CGError extends IntegerType {

		private static final long serialVersionUID = 1L;

		public CGError() { 

			this(0); 
		}

		public CGError(int value) { 
			
			super(4, value, false); 
		}
	};

	CGError CGGetActiveDisplayList(int maxDisplays, int[] activeDisplays, IntByReference displayCount);

	boolean CGDisplayIsAsleep(int display);
}