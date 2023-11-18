package me.mil.lmc.backend;

import me.mil.lmc.LMCReader;
import me.mil.lmc.LMCWriter;
import me.mil.lmc.backend.exceptions.LMCRuntimeException;
import me.mil.lmc.backend.util.Observable;
import me.mil.lmc.backend.util.Pair;

import java.util.Arrays;

/*
 AbstractClockedProcessor and AbstractObservableProcessor are unfortunately both abstract classes.
 Here, I'm just copying from AbstractObservableProcessor to have both abstract classes' benefits.
 This is not ideal; Better design decisions could have been made.
 TODO: create a better system that allows AbstractClockedProcessor and AbstractObservableProcessor to be combined.
*/
public abstract class AbstractObservableClockedProcessor extends AbstractClockedProcessor implements Observable {

	private final boolean observeUnchanged;

	public AbstractObservableClockedProcessor(ProcessorInstruction[] instructions, int memorySize, int clockSpeed, LMCReader reader, LMCWriter writer, boolean observeUnchangedValues) {
		super(instructions, memorySize, clockSpeed, reader, writer);
		this.observeUnchanged = observeUnchangedValues;
	}
	public AbstractObservableClockedProcessor(ProcessorInstruction[] instructions, int memorySize, int clockSpeed, LMCReader reader, LMCWriter writer) {
		this(instructions, memorySize, clockSpeed, reader, writer, false);
	}

	@Override
	public void clearMemory() {
		super.clearMemory();
		update(this, ProcessorObserverNotificationType.CLEAR_MEMORY);
	}

	@Override
	public void clearRegisters() {
		MemorySlot[] old = getMemory().clone();
		super.clearRegisters();
		if(observeUnchanged || !Arrays.equals(getMemory(), old)) update(new ProcessorObserverNotification(ProcessorObserverNotificationType.CLEAR_REGISTERS, old, getMemory()));
	}

	@Override
	public void loadInstructionsIntoMemory() throws LMCRuntimeException {
		super.loadInstructionsIntoMemory();
		update(new ProcessorObserverNotification(ProcessorObserverNotificationType.LOAD_INTO_RAM));
	}

	@Override
	public void setMemorySlot(int id, int newValue) {
		int old = getMemorySlotValue(id);
		super.setMemorySlot(id, newValue);
		if(observeUnchanged || old!=getMemorySlotValue(id)) update(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_MEMORY, new Pair<>(id, old), new Pair<>(id, getMemorySlot(id))));
	}

	@Override
	public void setRegister(RegisterType registerType, Object newValue) {
		Object old = getRegister(registerType);
		super.setRegister(registerType, newValue);
		if(observeUnchanged || !getRegister(registerType).equals(old)) update(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_REGISTER, new Pair<>(registerType, old), new Pair<>(registerType, getRegisterValue(registerType))));
	}

	@Override
	public void setMemorySize(int memorySize) {
		System.out.println(memorySize);
		int old = getMemorySize();
		super.setMemorySize(memorySize);
		if(observeUnchanged || old!=getMemorySize()) update(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_MEMORY_SIZE, old, getMemorySize()));
	}

	@Override
	public void setReader(LMCReader reader) {
		LMCReader old = getReader();
		super.setReader(reader);
		if(observeUnchanged || !getReader().equals(old)) update(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_READER, old, getReader()));
	}

	@Override
	public void setWriter(LMCWriter writer) {
		LMCWriter old = getWriter();
		super.setWriter(writer);
		if(observeUnchanged || !old.equals(getWriter())) update(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_WRITER, old, getWriter()));
	}

	@Override
	public void setHalting(boolean halting) {
		boolean old = isHalting();
		super.setHalting(halting);
		if(observeUnchanged || isHalting()!=old) update(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_HALTING, old, isHalting()));
	}

	@Override
	public void setRunning(boolean running) {
		boolean old = isRunning();
		super.setRunning(running);
		if(observeUnchanged || isRunning()!=old) update(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_RUNNING, old, isRunning()));
	}

	@Override
	public void setInstructions(ProcessorInstruction[] instructions) {
		ProcessorInstruction[] old = getInstructions().clone();
		super.setInstructions(instructions);
		if(observeUnchanged || !Arrays.equals(getInstructions(), old)) update(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_INSTRUCTIONS, old, getInstructions()));
	}
}
