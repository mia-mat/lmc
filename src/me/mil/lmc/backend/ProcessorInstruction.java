package me.mil.lmc.backend;

public class ProcessorInstruction {
	private final String label;
	private final Instruction instruction;
	private final String parameter;

	public ProcessorInstruction(String label, Instruction instruction, String parameter) {
		this.label = label;
		this.instruction = instruction;
		this.parameter = parameter;
	}

	public String getLabel() {
		return label;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public String getParameter() {
		return parameter;
	}
}
