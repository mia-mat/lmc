package me.mil.lmc;

import me.mil.lmc.frontend.gui.util.InterfaceUtils;
import me.mil.lmc.frontend.shell.ShellInterface;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LMC {

	public static final String ARGUMENT_SHELL_START = "-minimal";
	public static final String ARGUMENT_SHELL_CLOCK_SPEED = "-clock";
	public static final String ARGUMENT_SHELL_MEMORY_SIZE = "-memory";

	public static void main(String[] args) throws Exception {
		List<String> argsList = Arrays.asList(args);
		if (argsList.contains(ARGUMENT_SHELL_START)) {
			startMinimal(argsList);
			return;
		}

		InterfaceUtils.createInterface();
	}

	private static void startMinimal(List<String> args) throws IOException {
		int clockSpeed = ShellInterface.DEFAULT_CLOCK_SPEED;
		int memorySize = ShellInterface.DEFAULT_MEMORY_SIZE;

		try {
			if (args.contains(ARGUMENT_SHELL_CLOCK_SPEED))
				clockSpeed = Integer.parseInt(args.get(args.lastIndexOf(ARGUMENT_SHELL_CLOCK_SPEED) + 1));
		} catch (NumberFormatException ignored) {
			System.out.println("Invalid clock speed, using default of " + clockSpeed);
		}

		try {
			if (args.contains(ARGUMENT_SHELL_MEMORY_SIZE))
				memorySize = Integer.parseInt(args.get(args.lastIndexOf(ARGUMENT_SHELL_MEMORY_SIZE) + 1));
		} catch (NumberFormatException ignored) {
			System.out.println("Invalid memory size, using default of " + memorySize);
		}

		ShellInterface.create(clockSpeed, memorySize);
	}

}
