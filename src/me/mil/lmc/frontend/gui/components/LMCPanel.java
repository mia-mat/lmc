package me.mil.lmc.frontend.gui.components;

import me.mil.lmc.frontend.gui.AbstractGraphicalInterface;

import javax.swing.*;

public abstract class LMCPanel extends JPanel {

	private final AbstractGraphicalInterface lmcInterface;

	public LMCPanel(AbstractGraphicalInterface lmcInterface, boolean autoGenerate) {
		this.lmcInterface = lmcInterface;

		// Sometimes it may be necessary to generate after calling super(), as some other values may be passed in which are needed in the generate method, and super() needs to be called first in the constructor (annoyingly).
		if(autoGenerate) generate();
	}
	public LMCPanel(AbstractGraphicalInterface lmcInterface) {
		this(lmcInterface, true);
	}


	protected abstract void generate();

	protected AbstractGraphicalInterface getInterface() {
		return lmcInterface;
	}

}
