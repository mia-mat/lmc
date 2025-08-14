package me.mia.lmc.frontend.gui.components;

import me.mia.lmc.frontend.gui.AbstractGraphicalInterface;

public abstract class LMCSubPanel extends LMCPanel {

	public LMCSubPanel(AbstractGraphicalInterface lmcInterface) {
		super(lmcInterface);

		if (lmcInterface.getRootPanel() == null) {
			throw new RuntimeException("LMC Root Panel must be assigned before creating any other panels!");
		}
		addToRoot(lmcInterface.getRootPanel());
	}

	protected abstract void addToRoot(RootPanel root);
}
