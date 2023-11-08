package me.mil.lmc.backend;

import me.mil.lmc.LMCReader;
import me.mil.lmc.LMCWriter;
import me.mil.lmc.backend.exceptions.LMCRuntimeException;

import java.util.ArrayList;

public interface Processor {

	// TODO: In implementations, don't create new processors every time a new program is executed. Instead, clear things, set the instructions and re-run.

	ArrayList<Runnable> getInstructionCycle();

	int getInstructionCycleProgress();

	void performNextInstructionStep();

	MemorySlot[] getMemory();

	Register[] getRegisters();

	void clearMemory();

	void clearRegisters();

	void forceHalt();

	MemorySlot getMemorySlot(int id);

	int getMemorySlotValue(int id);

	Register getRegister(RegisterType registerType);

	Object getRegisterValue(RegisterType registerType);

	void run();
	void prepareRun();
	void finalizeRun();

	void loadInstructionsIntoMemory() throws LMCRuntimeException;

	int getMaybeLabelledMemorySlotID(String label) throws LMCRuntimeException; // TODO come up with a better name, or put this in a separate interface/up to implementer

	void setMemorySlot(int id, int newValue);

	void setRegister(RegisterType registerType, Object newValue);

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

	ProcessorInstruction[] getInstructions();

	void setInstructions(ProcessorInstruction[] instructions);

}
