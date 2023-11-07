package me.mil.lmc.backend;

import me.mil.lmc.backend.util.Pair;

public enum RegisterType {
	PROGRAM_COUNTER(Integer.class, "PC"),
	MEMORY_ADDRESS_REGISTER(Integer.class, "MAR"),
	CURRENT_INSTRUCTION_REGISTER(Integer.class, "CIR"),
	ACCUMULATOR(Integer.class, "ACC"),
	MEMORY_DATA_REGISTER(Pair.class, false);

	private final String uiName;
	private final Class<?> dataTypeStored;
	private final boolean display;

	RegisterType(Class<?> dataTypeStored, String uiName, boolean display) {
		this.dataTypeStored = dataTypeStored;
		this.uiName = uiName;
		this.display = display;
	}
	RegisterType(Class<?> dataTypeStored, boolean display) {
		this(dataTypeStored, "", display);
	}
	RegisterType(Class<?> dataTypeStored, String uiName) {
		this(dataTypeStored, uiName, true);
	}

	public Class<?> getDataTypeStored() {
		return dataTypeStored;
	}

	public String getUiName() {
		return uiName;
	}

	public boolean isDisplayed() {
		return display;
	}
}
