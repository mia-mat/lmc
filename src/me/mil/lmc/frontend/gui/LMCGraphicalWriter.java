package me.mil.lmc.frontend.gui;

import me.mil.lmc.LMCWriter;

public class LMCGraphicalWriter implements LMCWriter {

	private final LMCInterface lmcInterface;

	public LMCGraphicalWriter(LMCInterface lmcInterface) {
		this.lmcInterface = lmcInterface;
	}

	@Override
	public void write(int out) {
		getInterface().getOutputPanel().writeOutput(out);
	}

	private LMCInterface getInterface() {
		return lmcInterface;
	}
}
