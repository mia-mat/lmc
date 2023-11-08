package me.mil.lmc.backend;

import me.mil.lmc.LMCReader;
import me.mil.lmc.LMCWriter;
import me.mil.lmc.backend.exceptions.LMCRuntimeException;
import me.mil.lmc.backend.util.Pair;

import java.util.*;

// Adds basic capabilities of a processor
public abstract class AbstractProcessor implements Processor {

	private int memorySize;
	private MemorySlot[] memory;

	private Register[] registers;

	private final Map<String, Integer> labels;
	ProcessorInstruction[] instructions;

	private LMCReader reader;
	private LMCWriter writer;

	private boolean halting;
	private boolean running;

	private int instructionCycleProgress;

	public AbstractProcessor(ProcessorInstruction[] instructions, int memorySize, LMCReader reader, LMCWriter writer) {
		this.memorySize = memorySize;
		this.memory = new MemorySlot[this.memorySize];
		clearMemory();

		this.registers = new Register[RegisterType.values().length];
		clearRegisters();

		this.labels = new HashMap<>();
		this.instructions = instructions;

		this.reader = reader;
		this.writer = writer;

		this.running = false;
		this.halting = false;

		instructionCycleProgress = 0;
	}

	@Override
	public void run() {
		prepareRun();

		while(!isHalting()) {
			performNextInstructionStep();
		}

		finalizeRun();
	}

	@Override
	public void prepareRun() {
		setHalting(false);
		setRunning(true);
	}

	@Override
	public void finalizeRun() {
		setRunning(false);
		setHalting(false);
	}

	@Override
	public int getInstructionCycleProgress() {
		return instructionCycleProgress;
	}

	@Override
	public void performNextInstructionStep() {
		getInstructionCycle().get(getInstructionCycleProgress()).run();
		instructionCycleProgress++;
		if(getInstructionCycleProgress() >= getInstructionCycle().size()) {
			instructionCycleProgress = 0;
		}
	}

	@Override
	public ArrayList<Runnable> getInstructionCycle() {
		return new ArrayList<Runnable>(){{
			add(() -> { // Fetch
				setRegister(RegisterType.MEMORY_ADDRESS_REGISTER, getRegisterValue(RegisterType.PROGRAM_COUNTER));
				setRegister(RegisterType.PROGRAM_COUNTER, (int) getRegisterValue(RegisterType.PROGRAM_COUNTER)+1);
//				setRegister(RegisterType.CURRENT_INSTRUCTION_REGISTER, getRegisterValue(RegisterType.MEMORY_DATA_REGISTER)); // TODO make CIR useful again
			});

			add(() -> { // Decode
				setRegister(RegisterType.MEMORY_DATA_REGISTER, Instruction.fromCode(getMemorySlotValue((int) getRegisterValue(RegisterType.MEMORY_ADDRESS_REGISTER))));
			});

			add(() -> { // Execute
				Pair<Instruction, Integer> instruction = (Pair<Instruction, Integer>) getRegisterValue(RegisterType.MEMORY_DATA_REGISTER);
				instruction.getA().execute(AbstractProcessor.this, instruction.getB());
			});

		}};
	}

	@Override
	public void loadInstructionsIntoMemory() throws LMCRuntimeException {
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
	}

	/**
	 * If <code>label</code> is a valid memory location and not a label, returns that ID. <br>
	 * If <code>label</code> is a valid label, returns the corresponding memory location. <br>
	 * If neither, throws a fun exception.
	 */
	@Override
	public int getMaybeLabelledMemorySlotID(String potentialLabel) throws LMCRuntimeException { // TODO rename
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
	public void forceHalt() {
		setHalting(true);
	}

	@Override
	public MemorySlot[] getMemory() {
		return memory;
	}

	@Override
	public void clearMemory() {
		for(int i = 0; i < memorySize; i++) {
			memory[i] = new MemorySlot(0);
		}
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
		this.memory = new MemorySlot[memorySize];
		for(int i = 0; i < memorySize; i++) {
			memory[i] = new MemorySlot(0);
		}
		this.memorySize = memorySize;
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
	public void setMemorySlot(int id, int newValue) {
		this.memory[id].setValue(newValue);
	}

	@Override
	public Register[] getRegisters() {
		return registers;
	}

	@Override
	public void clearRegisters() {
		Arrays.stream(RegisterType.values()).forEach(registerType -> registers[registerType.ordinal()] = new Register(0));
	}

	@Override
	public Register getRegister(RegisterType registerType) {
		return registers[registerType.ordinal()];
	}

	@Override
	public Object getRegisterValue(RegisterType registerType) {
		return this.getRegister(registerType).getValue();
	}

	@Override
	public void setRegister(RegisterType registerType, Object newValue) {
		getRegister(registerType).setValue(newValue);
	}

	public ProcessorInstruction[] getInstructions() {
		return new ProcessorInstruction[0];
	}

	@Override
	public void setInstructions(ProcessorInstruction[] instructions) {
		this.instructions = instructions;
	}

	@Override
	public LMCReader getReader() {
		return reader;
	}

	@Override
	public void setReader(LMCReader reader) {
		this.reader = reader;
	}

	@Override
	public LMCWriter getWriter() {
		return writer;
	}

	@Override
	public void setWriter(LMCWriter writer) {
		this.writer = writer;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public boolean isHalting() {
		return halting;
	}

	@Override
	public void setHalting(boolean halting) {
		this.halting = halting;
	}

}
