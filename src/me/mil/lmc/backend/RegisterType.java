package me.mil.lmc.backend;

public enum RegisterType {
	PROGRAM_COUNTER("PC"),
	MEMORY_ADDRESS_REGISTER("MAR"),
	CURRENT_INSTRUCTION_REGISTER("CIR"),
	ACCUMULATOR("ACC");

	private final String uiName;

	RegisterType(String uiName) {
		this.uiName = uiName;
	}

	public String getUiName() {
		return uiName;
	}
}
