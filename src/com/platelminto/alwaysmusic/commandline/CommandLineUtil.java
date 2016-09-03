package com.platelminto.alwaysmusic.commandline;

import java.io.*;

public class CommandLineUtil {

	public static String[] runCommand(String...command) {

		final ProcessBuilder builder = new ProcessBuilder(command);

		try {

			final Process process = builder.start();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); // Hook into the output from running the command

			return reader.lines().toArray(String[]::new);

		} catch (IOException e) {

			e.printStackTrace();
		}

		return null;
	}
}