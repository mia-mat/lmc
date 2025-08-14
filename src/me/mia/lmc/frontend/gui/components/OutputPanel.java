package me.mia.lmc.frontend.gui.components;

import me.mia.lmc.backend.ProcessorObserverNotification;
import me.mia.lmc.backend.ProcessorObserverNotificationType;
import me.mia.lmc.backend.util.Observable;
import me.mia.lmc.frontend.gui.AbstractGraphicalInterface;
import me.mia.lmc.frontend.gui.LMCProcessorObserver;
import me.mia.lmc.frontend.gui.util.GBCBuilder;
import me.mia.lmc.frontend.gui.util.StyleConstants;

import javax.swing.*;
import java.awt.*;

public final class OutputPanel extends LMCSubPanel {

	private JTextArea outputTextArea;

	public OutputPanel(AbstractGraphicalInterface lmcInterface) {
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
		new LMCProcessorObserver(getInterface()) {
			@Override
			public void onUpdate(Observable processor, ProcessorObserverNotification notification) {
				if (notification.getType() == ProcessorObserverNotificationType.SET_HALTING && getInterface().getProcessor() != null && getInterface().getProcessor().isHalting()) {
					writeOutput("Program has finished executing.");
					// Scroll to the bottom
					SwingUtilities.invokeLater(() -> scrollPane.getViewport().setViewPosition(new Point(0, textArea.getDocument().getLength())));
				}
			}
		};
	}

	public JTextArea getOutputTextArea() {
		return outputTextArea;
	}

	void writeOutput(String out) {
		getOutputTextArea().setText(getOutputTextArea().getText() + out + "\n");
	}

	public void writeOutput(int out) {
		writeOutput(Integer.toString(out));
	}

}
