package me.mil.lmc.frontend.gui.components;

import me.mil.lmc.frontend.gui.LMCInterface;

import java.awt.*;

public class RootPanel extends LMCPanel{

	public RootPanel(LMCInterface lmcInterface) {
		super(lmcInterface);

		lmcInterface.getFrame().add(this);
	}

	@Override
	protected void generate() {
		setLayout(new GridBagLayout());
	}

}
