package me.mil.lmc.frontend.swing.components;

import me.mil.lmc.frontend.LMCInterface;

import javax.swing.*;

public abstract class LMCPanel extends JPanel {

	private final LMCInterface lmcInterface;

	public LMCPanel(LMCInterface lmcInterface, boolean autoGenerate) {
		this.lmcInterface = lmcInterface;

		// Sometimes it may be necessary to generate after calling super(), as some other values may be passed in which are needed in the generate method, and super() needs to be called first in the constructor (annoyingly).
		if(autoGenerate) generate();
	}
	public LMCPanel(LMCInterface lmcInterface) {
		this(lmcInterface, true);
	}


	protected abstract void generate();

	protected LMCInterface getInterface() {
		return lmcInterface;
	}

}
