package me.mil.lmc.frontend.gui;

import me.mil.lmc.LMCWriter;

public class LMCGraphicalWriter implements LMCWriter {

	private final AbstractGraphicalInterface lmcInterface;

	public LMCGraphicalWriter(AbstractGraphicalInterface lmcInterface) {
		this.lmcInterface = lmcInterface;
	}

	@Override
	public void write(int out) {
		getInterface().getOutputPanel().writeOutput(out);
	}

	private AbstractGraphicalInterface getInterface() {
		return lmcInterface;
	}
}
