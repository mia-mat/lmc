package me.mia.lmc.backend;

import me.mia.lmc.LMCReader;
import me.mia.lmc.LMCWriter;
import me.mia.lmc.backend.exceptions.LMCRuntimeException;

import java.util.ArrayList;

public interface Processor {

	ArrayList<Runnable> getInstructionCycle();

	int getInstructionCycleProgress();

	void performNextInstructionStep();

	void resetInstructionCycle();

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

	int getMemoryID(String potentialLabel) throws LMCRuntimeException;

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

	void clearInstructions();

}
