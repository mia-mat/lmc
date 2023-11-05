package me.mil.lmc.frontend.swing.components;

import me.mil.lmc.frontend.LMCInterface;
import me.mil.lmc.frontend.util.GBCBuilder;
import me.mil.lmc.frontend.util.StyleConstants;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

public final class OutputPanel extends LMCSubPanel {

	private JTextArea outputTextArea;

	public OutputPanel(LMCInterface lmcInterface) {
		super(lmcInterface);
	}

	@Override
	protected void addToRoot(RootPanel root) { // Adding to InputOutputPanel instead of root.
		getInterface().getInputOutputPanel().add(this, new GBCBuilder().setAnchor(GBCBuilder.Anchor.SOUTH).setFill(GBCBuilder.Fill.BOTH)
				.setWeight(1, 0.3).setPosition(0, 2).build());
	}

	@Override
	protected void generate() {
		setLayout(new GridBagLayout());

		JTextArea textArea = new JTextArea();
		textArea.setFont(StyleConstants.FONT_LARGE);
		textArea.setEditable(false);
		textArea.setText("Output:\n");

		JScrollPane scrollPane = new JScrollPane(textArea);
		this.add(scrollPane, new GBCBuilder().setAnchor(GBCBuilder.Anchor.CENTRE).setFill(GBCBuilder.Fill.BOTH)
				.setWeight(1, 1).setPosition(0, 0).setCellsConsumed(1, 1).build());

		outputTextArea = textArea;
	}

	public JTextArea getOutputTextArea() {
		return outputTextArea;
	}

	public void writeOutput(int out) {
		getOutputTextArea().setText(getOutputTextArea().getText()+out+"\n");
	}

}
