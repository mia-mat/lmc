package me.mil.lmc.frontend.shell;

import me.mil.lmc.LMCWriter;

public class ShellWriter implements LMCWriter {
	@Override
	public void write(int out) {
		System.out.println("# Output: " + out);
	}
}
