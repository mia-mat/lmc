package me.mil.lmc.backend;

import me.mil.lmc.LMCReader;
import me.mil.lmc.LMCWriter;
import me.mil.lmc.backend.exceptions.LMCCompilationException;

import java.util.ArrayList;
import java.util.List;

public class LMCProcessor extends AbstractObservableProcessorImplementation {

	private LMCProcessor(ProcessorInstruction[] instructions, int memorySize, int clockSpeed, LMCReader reader, LMCWriter writer) {
		super(instructions, memorySize, clockSpeed, reader, writer);

	}

	@Override
	public void run() {
		super.run();
		System.out.println("Finished Executing Program");
	}

	@Override
	protected void fetchInstructions() { // todo impl. clockspeed
		setRegister(RegisterType.MEMORY_ADDRESS_REGISTER, getRegisterValue(RegisterType.PROGRAM_COUNTER));
		setRegister(RegisterType.PROGRAM_COUNTER, getRegisterValue(RegisterType.PROGRAM_COUNTER)+1);
		setRegister(RegisterType.CURRENT_INSTRUCTION_REGISTER, getRegisterValue(RegisterType.MEMORY_ADDRESS_REGISTER));
	}

	@Override
	protected Pair<Instruction, Integer> decodeInstructions() {
		return Instruction.fromCode(getMemorySlotValue(getRegisterValue(RegisterType.CURRENT_INSTRUCTION_REGISTER)));

	}

	@Override
	protected void executeInstructions(Pair<Instruction, Integer> instructionParameterPair) {

		instructionParameterPair.getA().execute(this, instructionParameterPair.getB());
	}


	public static LMCProcessor compileInstructions(String input, int memorySize, int clockSpeed, LMCReader reader, LMCWriter writer) throws LMCCompilationException {
		if (memorySize < 1) throw new LMCCompilationException("Invalid RAM Size. (" + memorySize + ")");

		// convert tabs to spaces
		input = input.replaceAll("\t", " ");

		{ // eliminate unnecessary spaces
			StringBuilder stringBuilder = new StringBuilder();
			for (String s : input.split(" ")) {
				if (!s.isEmpty()) stringBuilder.append(s).append(" ");
			}
			input = stringBuilder.toString();
		}

		// loop over each line to extract program
		String[] lines = input.split("\n");
		List<ProcessorInstruction> instructions = new ArrayList<>();
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].replaceAll(" ", "").isEmpty())
				continue; // check if isEmpty, but also include empty lines with spaces.
			if (lines[i].startsWith(" ")) lines[i] = lines[i].substring(1); // remove leading space, if it exists.
			// separate into label, instruction, and parameter
			String[] lineComponents = lines[i].split(" ");

			switch (lineComponents.length) {
				case 1: // Only 'INSTRUCTION'
					if (!Instruction.exists(lineComponents[0].toUpperCase()))
						throw new LMCCompilationException(i + 1, "Instruction \"" + lineComponents[0].toUpperCase() + "\" not found");
					Instruction instruction = Instruction.valueOf(lineComponents[0].toUpperCase());
					if (instruction.requiresParameter())
						throw new LMCCompilationException(i + 1, "No parameter provided when one was required!");

					instructions.add(new ProcessorInstruction(null, instruction, null));
					break;
				case 2: // Could be 'INSTRUCTION parameter', or 'label INSTRUCTION'
					if (Instruction.exists(lineComponents[0].toUpperCase()) && Instruction.valueOf(lineComponents[0].toUpperCase()).requiresParameter()) { // INSTRUCTION parameter
						instructions.add(new ProcessorInstruction(null, Instruction.valueOf(lineComponents[0]), lineComponents[1]));
						break;
					}

					if (Instruction.exists(lineComponents[1].toUpperCase()) &&
							(Instruction.valueOf(lineComponents[1].toUpperCase()).equals(Instruction.DAT) || !Instruction.valueOf(lineComponents[1].toUpperCase()).requiresParameter())) { // label INSTRUCTION
						instructions.add(new ProcessorInstruction(lineComponents[0], Instruction.valueOf(lineComponents[1]), null));

						break;
					}

					throw new LMCCompilationException(i + 1);
				case 3: // Only 'label INSTRUCTION parameter'
					if (Instruction.exists(lineComponents[1].toUpperCase()) && Instruction.valueOf(lineComponents[1].toUpperCase()).requiresParameter()) {
						instructions.add(new ProcessorInstruction(lineComponents[0], Instruction.valueOf(lineComponents[1]), lineComponents[2]));
						break;
					}
					throw new LMCCompilationException(i + 1);
				default:
					throw new LMCCompilationException(i + 1, "Invalid number of parameters");
			}
		}

		return new LMCProcessor(instructions.toArray(new ProcessorInstruction[0]), memorySize, clockSpeed, reader, writer);
	}

}
