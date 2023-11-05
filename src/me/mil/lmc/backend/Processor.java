package me.mil.lmc.backend;

import me.mil.lmc.LMCReader;
import me.mil.lmc.LMCWriter;
import me.mil.lmc.backend.exceptions.LMCRuntimeException;

public interface Processor {

	void clearMemory();

	void clearRegisters();

	void forceHalt();

	MemorySlot getMemorySlot(int id);

	int getMemorySlotValue(int id);

	Register getRegister(RegisterType registerType);

	int getRegisterValue(RegisterType registerType);

	void run();

	void loadInstructionsIntoMemory() throws LMCRuntimeException;

	int getMaybeLabelledMemorySlotID(String label) throws LMCRuntimeException;

	void setMemorySlot(int id, int newValue);

	void setRegister(RegisterType registerType, int newValue);

	int getClockSpeed();

	void setClockSpeed(int clockSpeed);

	int getMemorySize();

	void setMemorySize(int memorySize);

	LMCReader getReader();

	void setReader(LMCReader reader);

	LMCWriter getWriter();

	void setWriter(LMCWriter writer);

	boolean isHalting();

	boolean isRunning();

	void setHalting(boolean halting);

	void setRunning(boolean running);

	ProcessorInstruction[] getRawInstructions();

	void setRawInstructions(ProcessorInstruction[] instructions);

	boolean isRespectingClockSpeed();

}
