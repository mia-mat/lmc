package me.mil.lmc.backend;

import me.mil.lmc.backend.util.Pair;

public enum RegisterType {
	PROGRAM_COUNTER(Integer.class, "PC"),
	MEMORY_ADDRESS_REGISTER(Integer.class, "MAR"),
	MEMORY_DATA_REGISTER(Integer.class, "MDR"),
	CURRENT_INSTRUCTION_REGISTER(Pair.class, "CIR", false, new Pair<>(Instruction.HLT, null)), // Not displayed due to nature of data stored
	ACCUMULATOR(Integer.class, "ACC");

	private final String uiName;
	private final Class<?> dataTypeStored;
	private final Object defaultData;
	private final boolean display;

	RegisterType(Class<?> dataTypeStored, String uiName, boolean display, Object defaultData) {
		this.dataTypeStored = dataTypeStored;
		this.uiName = uiName;
		this.display = display;
		this.defaultData = defaultData;
	}

	RegisterType(Class<?> dataTypeStored, Object defaultData, String uiName) {
		this(dataTypeStored, uiName, true, defaultData);
	}

	RegisterType(Class<?> dataTypeStored, String uiName) {
		this(dataTypeStored, uiName, true, 0);
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

	public Object getDefault() {
		return defaultData;
	}

}
