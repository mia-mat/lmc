package me.mil.lmc.frontend.swing.components;

import me.mil.lmc.frontend.LMCInterface;

public abstract class LMCSubPanel extends LMCPanel{

	public LMCSubPanel(LMCInterface lmcInterface) {
		super(lmcInterface);

		if(lmcInterface.getRootPanel() == null) {
			throw new RuntimeException("LMC Root Panel must be assigned before creating any other panels!");
		}
		addToRoot(lmcInterface.getRootPanel());
	}

	protected abstract void addToRoot(RootPanel root);
}
