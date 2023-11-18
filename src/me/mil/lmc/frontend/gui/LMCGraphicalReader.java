package me.mil.lmc.frontend.gui;

import me.mil.lmc.LMCReader;

public class LMCGraphicalReader implements LMCReader {

	private final AbstractGraphicalInterface lmcInterface;

	public LMCGraphicalReader(AbstractGraphicalInterface lmcInterface) {
		this.lmcInterface = lmcInterface;
	}

	@Override
	public int nextInt() {

		return promptForInt();
	}

	private int promptForInt() {
		return promptForInt("");
	}

	private int promptForInt(String msg) {
		try{
			return Integer.parseInt(getInterface().showInputDialog(msg));
		}catch (NumberFormatException e) {
			return promptForInt("Please provide an integer.");
		}
	}

	private AbstractGraphicalInterface getInterface() {
		return lmcInterface;
	}
}
