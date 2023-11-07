package me.mil.lmc.frontend.gui;

import me.mil.lmc.backend.ProcessorObserver;

// For integration with LMCInterface
public class LMCProcessorObserver extends ProcessorObserver {

	public LMCProcessorObserver(LMCInterface lmcInterface) {
		super(lmcInterface.getProcessor());
		lmcInterface.addProcessorObserver(this);
	}


}
