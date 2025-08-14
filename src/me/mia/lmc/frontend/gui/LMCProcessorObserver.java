package me.mia.lmc.frontend.gui;

import me.mia.lmc.backend.ProcessorObserver;

// For integration with LMCInterface
public class LMCProcessorObserver extends ProcessorObserver {

	public LMCProcessorObserver(AbstractGraphicalInterface lmcInterface) {
		super(lmcInterface.getProcessor());
	}


}
