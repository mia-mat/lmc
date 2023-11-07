package me.mil.lmc.frontend.gui.components;

import me.mil.lmc.frontend.gui.LMCInterface;
import me.mil.lmc.frontend.gui.util.GBCBuilder;

import java.awt.*;

// Just encompasses the whole left side, so I can resize easier on the right.
public class InputOutputPanel extends LMCSubPanel {

	InputPanel inputPanel;
	OutputPanel outputPanel;

	public InputOutputPanel(LMCInterface lmcInterface) {
		super(lmcInterface);
	}

	@Override
	protected void generate() {
		setLayout(new GridBagLayout());
	}

	@Override
	protected void addToRoot(RootPanel root) {
		root.add(this, new GBCBuilder().setAnchor(GBCBuilder.Anchor.LINE_END).setFill(GBCBuilder.Fill.BOTH)
				.setWeight(0.35, 1).
				setPosition(0, 0).setCellsConsumed(1, 4).build());
	}

	public InputPanel getInputPanel() {
		return inputPanel;
	}

	public OutputPanel getOutputPanel() {
		return outputPanel;
	}

	public void setInputPanel(InputPanel inputPanel) {
		this.inputPanel = inputPanel;
	}

	public void setOutputPanel(OutputPanel outputPanel) {
		this.outputPanel = outputPanel;
	}
}
