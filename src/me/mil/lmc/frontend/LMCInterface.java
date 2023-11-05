package me.mil.lmc.frontend;

import me.mil.lmc.LMCReader;
import me.mil.lmc.LMCWriter;
import me.mil.lmc.backend.Program;
import me.mil.lmc.backend.exceptions.LMCException;
import me.mil.lmc.backend.exceptions.LMCRuntimeException;
import me.mil.lmc.frontend.swing.components.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

// (not an interface)
public class LMCInterface {

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

	private Program compiledProgram;
	private final List<ProgramObserver> programObservers = new ArrayList<>();

	public LMCInterface() {
		this.frame = generateFrame();

		this.reader = new LMCGraphicalReader(this);
		this.writer = new LMCGraphicalWriter(this);

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
		//
		frame.setVisible(true);

	}

	private JFrame generateFrame() {
		JFrame frame = new JFrame();
		frame.setSize(1400, 800);
		frame.setLocationRelativeTo(null); // Centre
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.setJMenuBar(LMCMenuBar.generate(this));
		return frame;
	}

	private void updateProgramObservers() {
		programObservers.forEach(po -> po.setProgram(compiledProgram));
	}

	protected void addProgramObserver(ProgramObserver programObserver) {
		programObservers.add(programObserver);
	}

	public void performControlFunction(ControlFunction function) {
		function.executeAction(this);
	}

	public void showMessageDialog(String title, String description, MessageType messageType) {
		JOptionPane.showMessageDialog(getFrame(), description, title, messageType.getValue());
	}

	protected String showInputDialog(String description) {
		return JOptionPane.showInputDialog(getFrame(), description);
	}

	protected void showErrorDialog(LMCException error) {
		showMessageDialog((error instanceof LMCRuntimeException) ? "Runtime Error" : "Compilation Error",
				error.getMessage(), MessageType.ERROR_MESSAGE);
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

	public Program getCompiledProgram() {
		return compiledProgram;
	}

	protected void setCompiledProgram(Program newProgram) {
		this.compiledProgram = newProgram;
		updateProgramObservers();
	}

}
