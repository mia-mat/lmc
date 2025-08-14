package me.mia.lmc.frontend.gui;

import me.mia.lmc.LMCReader;
import me.mia.lmc.LMCWriter;
import me.mia.lmc.backend.LMCProcessor;
import me.mia.lmc.backend.exceptions.LMCException;
import me.mia.lmc.backend.exceptions.LMCRuntimeException;
import me.mia.lmc.frontend.gui.components.*;
import me.mia.lmc.frontend.gui.util.DialogMessageType;

import javax.swing.*;

public abstract class AbstractGraphicalInterface {

	private final JFrame frame;

	// Panels
	private final RootPanel rootPanel;

	private final InputOutputPanel inputOutputPanel;
	private final InputPanel inputPanel;
	private final OutputPanel outputPanel;

	private final ControlPanel controlPanel;
	private final RegisterViewPanel registerViewPanel;
	private final MemoryViewPanel memoryViewPanel;
	///

	private LMCReader reader;
	private LMCWriter writer;

	private final LMCProcessor processor;

	public AbstractGraphicalInterface() {
		this.frame = generateFrame();

		this.reader = new LMCGraphicalReader(this);
		this.writer = new LMCGraphicalWriter(this);

		this.processor = LMCProcessor.create(101, 0, reader, writer);

		// Panels
		this.rootPanel = new RootPanel(this);

		this.inputOutputPanel = new InputOutputPanel(this);
		this.inputOutputPanel.setInputPanel(new InputPanel(this));
		this.inputOutputPanel.setOutputPanel(new OutputPanel(this));
		this.inputPanel = inputOutputPanel.getInputPanel();
		this.outputPanel = inputOutputPanel.getOutputPanel();

		this.controlPanel = new ControlPanel(this);
		this.registerViewPanel = new RegisterViewPanel(this);
		this.memoryViewPanel = new MemoryViewPanel(this);

		frame.setVisible(true);
	}

	protected abstract JFrame generateFrame();

	public void performControlFunction(ControlFunction function) {
		function.executeAction(this);
	}

	public void showMessageDialog(String title, String description, DialogMessageType messageType) {
		JOptionPane.showMessageDialog(getFrame(), description, title, messageType.getValue());
	}

	protected String showInputDialog(String description) {
		return JOptionPane.showInputDialog(getFrame(), description);
	}

	protected void showErrorDialog(LMCException error) {
		showMessageDialog((error instanceof LMCRuntimeException) ? "Runtime Error" : "Compilation Error",
				error.getMessage(), DialogMessageType.ERROR_MESSAGE);
	}

	// -- Getters / Setters -- //

	public JFrame getFrame() {
		return frame;
	}

	public RootPanel getRootPanel() {
		return rootPanel;
	}

	public InputOutputPanel getInputOutputPanel() {
		return inputOutputPanel;
	}

	public InputPanel getInputPanel() {
		return inputPanel;
	}

	public OutputPanel getOutputPanel() {
		return outputPanel;
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	public RegisterViewPanel getRegisterViewPanel() {
		return registerViewPanel;
	}

	public MemoryViewPanel getMemoryViewPanel() {
		return memoryViewPanel;
	}

	public LMCReader getReader() {
		return reader;
	}

	public LMCWriter getWriter() {
		return writer;
	}

	public LMCProcessor getProcessor() {
		return processor;
	}

}
