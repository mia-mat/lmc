package me.mil.lmc.frontend.gui;

import javax.swing.*;

public class LMCGraphicalInterface extends AbstractGraphicalInterface {
	@Override
	protected JFrame generateFrame() {
		JFrame frame = new JFrame();
		frame.setTitle("LMC");
		frame.setSize(1400, 800);
		frame.setLocationRelativeTo(null); // Centre
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.setJMenuBar(LMCMenuBar.generate(this));
		return frame;
	}
}
