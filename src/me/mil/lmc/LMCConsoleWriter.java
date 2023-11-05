package me.mil.lmc;

public class LMCConsoleWriter implements LMCWriter{
	@Override
	public void write(int out) {
		System.out.println("# Output: " + out);
	}
}
