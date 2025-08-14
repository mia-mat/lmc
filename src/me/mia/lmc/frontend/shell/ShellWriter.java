package me.mia.lmc.frontend.shell;

import me.mia.lmc.LMCWriter;

public class ShellWriter implements LMCWriter {
	@Override
	public void write(int out) {
		System.out.println("# Output: " + out);
	}
}
