package me.mil.lmc.backend;


import me.mil.lmc.backend.exceptions.LMCCompilationException;
import me.mil.lmc.backend.exceptions.LMCRuntimeException;

import java.util.*;

public class Program {
	enum RegisterType {
		PROGRAM_COUNTER,
		MEMORY_ADDRESS_REGISTER,
		MEMORY_DATA_REGISTER,
		CURRENT_INSTRUCTION_REGISTER,
		ACCUMULATOR
	}

	private Register[] registers = new Register[RegisterType.values().length];
	private int[] ram;
	private Map<String, Integer> labels = new HashMap<>();
	private final Triplet<String, Instruction, String>[] instructions;
	private boolean halting = false;


	public Program(int ramSize, Triplet<String, Instruction, String>[] instructions) {
		this.ram = new int[ramSize];
		this.instructions = instructions;
		reset();
	}

	public int getRamSize() {
		return ram.length;
	}
	public int getValue(int ramLocation) {
		return ram[ramLocation];
	}

	public Register getRegister(RegisterType registerType) {
		return registers[registerType.ordinal()];
	}

	private void setRegister(RegisterType register, int newValue) {
		registers[register.ordinal()].setValue(newValue);
	}

	private void copyFromRegister(RegisterType from, RegisterType to) {
		setRegister(to, getRegister(from).getValue());
	}

	private void reset() {
		Arrays.stream(RegisterType.values()).forEach(r -> registers[r.ordinal()] = new Register(0));
		Arrays.fill(ram, 0);
		labels.clear();
	}

	/**
	 * Gets the integer value of the parameter, as some might be a label, etc.
	 */
	private int getParameterValue(String parameter) throws LMCRuntimeException {
		try{
			return Integer.parseInt(parameter);
		}catch (NumberFormatException e) {	// not a number
			if(labels.containsKey(parameter)) {
				return labels.get(parameter);
			}else {
				throw new LMCRuntimeException("Invalid parameter (\"" + parameter + "\")");
			}
		}
	}

	// load program into RAM
	private void load() throws LMCRuntimeException {
		System.out.println("Loading program into RAM...");
		for(int i = 0; i < instructions.length*2; i++) {
			int correctedI = i < instructions.length ? i : i-instructions.length;
			Instruction instruction = instructions[correctedI].getB();
			String label = instructions[correctedI].getA();
			String parameter = instructions[correctedI].getC();


			// init all the DAT instructions first, so that they can be used, even if they're not chronologically defined.
			if(i < instructions.length) { // search only DAT
				if(instruction.equals(Instruction.DAT)){
					if(parameter != null) {
						ram[i] = getParameterValue(parameter);
					}
				}
			}

			if(i >= instructions.length) { // search only non-DAT.
				if(instruction.equals(Instruction.DAT)) continue;
				int code = instruction.requiresParameter()
						? instruction.getCode(getParameterValue(parameter))
						: instruction.getCode();
				ram[correctedI] = code;
			}



			if(label != null) {
				labels.put(label, correctedI);
			}


		}
	}

	/**
	 * Resets the program, loads it into RAM, and runs it.
	 */
	public void run() throws LMCRuntimeException {
		reset();
		load();
		System.out.println("Attempting to run program...");

		do {
			// Fetch
			copyFromRegister(RegisterType.PROGRAM_COUNTER, RegisterType.MEMORY_ADDRESS_REGISTER); // Copy value of PC to MAR
			setRegister(RegisterType.PROGRAM_COUNTER, getRegister(RegisterType.PROGRAM_COUNTER).getValue() + 1); // Increment Program Counter
			copyFromRegister(RegisterType.MEMORY_ADDRESS_REGISTER, RegisterType.CURRENT_INSTRUCTION_REGISTER); // Copy value of MAR to CIR

			// Decode
			Pair<Instruction, Integer> instructionParameterPair = Instruction.fromCode(ram[getRegister(RegisterType.CURRENT_INSTRUCTION_REGISTER).getValue()]);
			Instruction instruction = instructionParameterPair.getA();
			Integer parameter = instructionParameterPair.getB();

			// Execute
			ProgramState state = instruction.execute(generateProgramState(), parameter);
			ram = state.getRam();
			registers = state.getRegisters();
			halting = state.isHalting();

		} while (!halting);
		System.out.println("Finished Executing Program.");


	}

	public ProgramState generateProgramState() {
		return new ProgramState(registers, ram, halting);
	}

	public static Program loadFromString(int ramSize, String str) throws LMCCompilationException {
		// convert tabs to spaces
		str = str.replaceAll("\t", " ");

		{ // eliminate unnecessary spaces
			StringBuilder stringBuilder = new StringBuilder();
			for (String s : str.split(" ")) {
				if(s.length()>0) stringBuilder.append(s).append(" ");
			}
			str = stringBuilder.toString();
		}


		System.out.println("Compiling program... \n---");

		// loop over each line to extract program
		String[] lines = str.split("\n");
		List<Triplet<String, Instruction, String>> program = new ArrayList<>();
		for(int i = 0; i < lines.length; i++) {
			if(lines[i].replaceAll(" ", "").length()==0) continue; // check if isEmpty, but also include empty lines with spaces.
			if(lines[i].startsWith(" ")) lines[i] = lines[i].substring(1); // remove leading space, if it exists.
			// separate into label, instruction, and parameter
			String[] lineComponents = lines[i].split(" ");

			switch(lineComponents.length) {
				case 1: // Only 'INSTRUCTION'
					if(!Instruction.exists(lineComponents[0].toUpperCase())) throw new LMCCompilationException(i+1, "Instruction \"" + lineComponents[0].toUpperCase() + "\" not found");
					Instruction instruction = Instruction.valueOf(lineComponents[0].toUpperCase());
					if(instruction.requiresParameter()) throw new LMCCompilationException(i+1, "No parameter provided when one was required!");

					program.add(new Triplet<>(null, instruction, null));
					break;
				case 2: // Could be 'INSTRUCTION parameter', or 'label INSTRUCTION'
					if(Instruction.exists(lineComponents[0].toUpperCase()) && Instruction.valueOf(lineComponents[0].toUpperCase()).requiresParameter()) { // INSTRUCTION parameter
						program.add(new Triplet<>(null, Instruction.valueOf(lineComponents[0]), lineComponents[1]));
						break;
					}

					if(Instruction.exists(lineComponents[1].toUpperCase()) &&
							(Instruction.valueOf(lineComponents[1].toUpperCase()).equals(Instruction.DAT) || !Instruction.valueOf(lineComponents[1].toUpperCase()).requiresParameter())) { // label INSTRUCTION
						program.add(new Triplet<>(lineComponents[0], Instruction.valueOf(lineComponents[1]), null));
						break;
					}

					throw new LMCCompilationException(i+1);
				case 3: // Only 'label INSTRUCTION parameter'
						if(Instruction.exists(lineComponents[1].toUpperCase()) && Instruction.valueOf(lineComponents[1].toUpperCase()).requiresParameter()) {
							program.add(new Triplet<>(lineComponents[0], Instruction.valueOf(lineComponents[1]), lineComponents[2]));
							break;
						}
						throw new LMCCompilationException(i+1);
				default: throw new LMCCompilationException(i+1, "Invalid number of parameters");
			}

			System.out.println(lines[i]);
		}
		System.out.println("---");

		return new Program(ramSize, program.toArray(new Triplet[0]));
	}

	public static Program loadFromString(String str) throws LMCCompilationException {
		return loadFromString(100, str);
	}
}
