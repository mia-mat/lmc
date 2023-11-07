package me.mil.lmc.backend;

import me.mil.lmc.LMCReader;
import me.mil.lmc.LMCWriter;
import me.mil.lmc.backend.exceptions.LMCRuntimeException;
import me.mil.lmc.backend.util.Pair;

import java.util.*;

public abstract class AbstractObservableProcessorImplementation extends AbstractObservableProcessor {

	// todo cleanup
	private LMCWriter writer;
	private LMCReader reader;

	private int memorySize;

	private MemorySlot[] memory;
	private Register[] registers;

	private final List<Observer> observers;
	private final Map<String, Integer> labels;
	ProcessorInstruction[] instructions;

	private boolean halting;
	private boolean running;

	protected AbstractObservableProcessorImplementation(ProcessorInstruction[] instructions, int memorySize, LMCReader reader, LMCWriter writer) {
		this.reader = reader;
		this.writer = writer;

		this.memorySize = memorySize;
		this.memory = new MemorySlot[this.memorySize];
		clearMemory();

		this.registers = new Register[RegisterType.values().length];
		clearRegisters();

		this.observers = new ArrayList<>();
		this.labels = new HashMap<>();
		this.instructions = instructions;

		this.running = false;
		this.halting = false;
	}


	@Override
	public void run() {
		setHalting(false);
		setRunning(true);
		while (!halting) {
			fetchInstructions();
			executeInstructions(decodeInstructions());
		}
		setRunning(false);
		setHalting(false);
	}

	@Override
	public void loadInstructionsIntoMemory() throws LMCRuntimeException {
		System.out.println("Loading instructions into memory");
		for (int i = 0; i < instructions.length * 2; i++) {
			int correctedI = i < instructions.length ? i : i - instructions.length;
			Instruction instruction = instructions[correctedI].getInstruction();
			String label = instructions[correctedI].getLabel();
			String parameter = instructions[correctedI].getParameter();


			// init all the DAT instructions first, so that they can be used, even if they're not chronologically defined.
			if (i < instructions.length) { // search only DAT
				if (instruction.equals(Instruction.DAT)) {
					if (parameter != null) {
						setMemorySlot(i, getMaybeLabelledMemorySlotID(parameter));
					}
				}
			}

			if (i >= instructions.length) { // search only non-DAT.
				if (instruction.equals(Instruction.DAT)) continue;
				int code = instruction.requiresParameter()
						? instruction.getCode(getMaybeLabelledMemorySlotID(parameter))
						: instruction.getCode();
				setMemorySlot(correctedI, code);
			}

			if (label != null) {
				labels.put(label, correctedI);
			}
		}

		notifyObservers(new ProcessorObserverNotification(ProcessorObserverNotificationType.LOAD_INTO_RAM, null, null));
	}

	/**
	 * If <code>label</code> is a valid memory location and not a label, returns that ID. <br>
	 * If <code>label</code> is a valid label, returns the corresponding memory location. <br>
	 * If neither, throws a fun exception.
	 */
	@Override
	public int getMaybeLabelledMemorySlotID(String potentialLabel) throws LMCRuntimeException {
		try {
			return Integer.parseInt(potentialLabel);
		} catch (NumberFormatException e) {    // not a number
			if (labels.containsKey(potentialLabel)) {
				return labels.get(potentialLabel);
			} else {
				throw new LMCRuntimeException("Invalid label (\"" + potentialLabel + "\")");
			}
		}
	}

	@Override
	public void clearMemory() {
		MemorySlot[] old = memory.clone();
		for(int i = 0; i < memorySize; i++) {
			memory[i] = new MemorySlot(0);
		}
		if(!Arrays.equals(old, memory.clone())) notifyObservers(new ProcessorObserverNotification(ProcessorObserverNotificationType.CLEAR_MEMORY, old, memory.clone()));
	}

	@Override
	public void clearRegisters() {
		Register[] old = registers.clone();
		Arrays.stream(RegisterType.values()).forEach(registerType -> registers[registerType.ordinal()] = new Register(0));
		if(!Arrays.equals(old, registers.clone())) notifyObservers(new ProcessorObserverNotification(ProcessorObserverNotificationType.CLEAR_REGISTERS, old, registers.clone()));
	}

	@Override
	public void forceHalt() {
		setHalting(true);
	}

	@Override
	public MemorySlot getMemorySlot(int id) {
		return memory[id];
	}

	@Override
	public int getMemorySlotValue(int id) {
		return this.getMemorySlot(id).getValue();
	}

	@Override
	public Register getRegister(RegisterType registerType) {
		return registers[registerType.ordinal()];
	}

	@Override
	public int getRegisterValue(RegisterType registerType) {
		return this.getRegister(registerType).getValue();
	}

	@Override
	public void setMemorySlot(int id, int newValue) {
		int old = getMemorySlotValue(id);
		this.memory[id].setValue(newValue);
		if(old!=newValue) notifyObservers(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_MEMORY, new Pair<>(id, old), new Pair<>(id, newValue)));
	}

	@Override
	public void setRegister(RegisterType registerType, int newValue) {
		int old = getRegisterValue(registerType);
		getRegister(registerType).setValue(newValue);
		if(old!=newValue) notifyObservers(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_REGISTER, new Pair<>(registerType, old), new Pair<>(registerType, newValue)));
	}

	@Override
	public int getMemorySize() {
		return memorySize;
	}

	/**
	 * Destructively sets memory size.
	 */
	@Override
	public void setMemorySize(int memorySize) {
		int old = getMemorySize();
		this.memory = new MemorySlot[memorySize];
		for(int i = 0; i < memorySize; i++) {
			memory[i] = new MemorySlot(0);
		}
		this.memorySize = memorySize;
		if(old!=memorySize) notifyObservers(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_MEMORY_SIZE, old, memorySize));
	}

	@Override
	public LMCReader getReader() {
		return reader;
	}

	@Override
	public void setReader(LMCReader reader) {
		LMCReader old = getReader();
		this.reader = reader;
		if(!old.equals(reader)) notifyObservers(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_READER, old, reader));
	}

	@Override
	public LMCWriter getWriter() {
		return writer;
	}

	@Override
	public void setWriter(LMCWriter writer) {
		LMCWriter old = getWriter();
		this.writer = writer;
		if(!old.equals(writer)) notifyObservers(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_WRITER, old, writer));
	}

	@Override
	public boolean isHalting() {
		return halting;
	}

	@Override
	public void setHalting(boolean halting) {
		boolean old = this.halting;
		this.halting = halting;
		if(old != halting) notifyObservers(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_HALTING, old, halting));
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void setRunning(boolean running) {
		boolean old = this.running;
		this.running = running;
		if(old != running) notifyObservers(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_RUNNING, old, running));
	}

	@Override
	public ProcessorInstruction[] getRawInstructions() {
		return new ProcessorInstruction[0];
	}

	@Override
	public void setRawInstructions(ProcessorInstruction[] instructions) {
		ProcessorInstruction[] old = this.instructions.clone();
		this.instructions = instructions;
		if(!Arrays.equals(instructions,old)) notifyObservers(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_INSTRUCTIONS, old, this.instructions.clone()));
	}

	@Override
	public void notifyObservers(Object arg) {
		super.notifyObservers(arg);
		setChanged();
	}

	@Override
	public synchronized void addObserver(Observer o) {
		super.addObserver(o);
		observers.add(o);
	}

	@Override
	public synchronized void deleteObserver(Observer o) {
		super.deleteObserver(o);
		observers.remove(o);
	}

	public List<Observer> getObservers() {
		return observers;
	}

	protected abstract void fetchInstructions();

	protected abstract Pair<Instruction, Integer> decodeInstructions();

	protected abstract void executeInstructions(Pair<Instruction, Integer> instructionParameterPair);

}
