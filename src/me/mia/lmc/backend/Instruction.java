package me.mia.lmc.backend;

import me.mia.lmc.backend.exceptions.LMCRuntimeException;
import me.mia.lmc.backend.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public enum Instruction {
	ADD("1XX", ((processor, parameter) -> { // Add the input to the accumulator
		processor.setRegister(RegisterType.ACCUMULATOR, (int) processor.getRegisterValue(RegisterType.ACCUMULATOR) + processor.getMemorySlotValue(parameter));
	})),
	SUB("2XX", ((processor, parameter) -> { // Subtract the input from the accumulator
		processor.setRegister(RegisterType.ACCUMULATOR, (int) processor.getRegisterValue(RegisterType.ACCUMULATOR) - processor.getMemorySlotValue(parameter));
	})),
	STA("3XX", ((processor, parameter) -> { // Store the contents of the accumulator in RAM location 'input'
		processor.setMemorySlot(parameter, (int) processor.getRegisterValue(RegisterType.ACCUMULATOR));
	})),
	LDA("5XX", ((processor, parameter) -> { // Load the value from RAM location 'input' into the accumulator
		processor.setRegister(RegisterType.ACCUMULATOR, processor.getMemorySlotValue(parameter));
	})),
	BRA("6XX", ((processor, parameter) -> { // Set the Program Counter to 'input'
		processor.setRegister(RegisterType.PROGRAM_COUNTER, parameter);
	})),
	BRZ("7XX", ((processor, parameter) -> { // Set the Program Counter to 'input' if the accumulator is 0
		if ((int) processor.getRegisterValue(RegisterType.ACCUMULATOR) == 0) {
			processor.setRegister(RegisterType.PROGRAM_COUNTER, parameter);
		}
	})),
	BRP("8XX", ((processor, parameter) -> { // Set the Program Counter to 'input' if the accumulator is greater than or equal to 0
		if ((int) processor.getRegisterValue(RegisterType.ACCUMULATOR) >= 0) {
			processor.setRegister(RegisterType.PROGRAM_COUNTER, parameter);
		}
	})),
	INP("901", ((processor, parameter) -> { // Fetch a value from the user into the accumulator
		processor.setRegister(RegisterType.ACCUMULATOR, processor.getReader().nextInt());
	})),
	OUT("902", ((processor, parameter) -> { // Output the value in the accumulator
		processor.getWriter().write((int) processor.getRegisterValue(RegisterType.ACCUMULATOR));
	})),
	HLT("0", ((processor, parameter) -> processor.setHalting(true))),
	DAT(null, null);

	private final String code;
	private final BiConsumer<Processor, Integer> function;

	Instruction(String code, BiConsumer<Processor, Integer> function) {
		this.code = code;
		this.function = function;
	}

	public void execute(Processor processor, Integer parameter) {
		execute(processor, parameter, false);
	}

	public void execute(Processor processor, Integer parameter, boolean ignoreClock) {
		if (!ignoreClock) {
			if (processor instanceof ClockedProcessor) {
				((ClockedProcessor) processor).addToRunnableQueue(() -> execute(processor, parameter, true));
			} else execute(processor, parameter, true);

			return;
		}

		function.accept(processor, parameter);
	}

	public boolean requiresParameter() {
		if (this == DAT) return true;
		return this.code.contains("X");
	}

	public int getCode(int parameter) {
		if (!requiresParameter()) Logger.getGlobal().warning("Unnecessary parameter provided");
		return Integer.parseInt(code.replaceAll("XX", String.valueOf(parameter)));
	}

	public int getCode() throws LMCRuntimeException {
		if (requiresParameter()) throw new LMCRuntimeException("No parameter provided when one was required");
		return Integer.parseInt(code);
	}

	// not creating these dynamically because I like my performance (and brevity).
	private static final Map<String, Instruction> codeToInstruction = Arrays.stream(Instruction.values()).collect(Collectors.toMap(i -> i.code, i -> i));
	private static final Map<Short, Instruction> firstDigitToInstruction = Arrays.stream(Instruction.values()).filter(i -> (i.code != null && i.code.contains("X"))).collect(Collectors.toMap(i -> Short.parseShort(String.valueOf(i.code.toCharArray()[0])), i -> i)); // Only for instructions with parameters

	public static Pair<Instruction, Integer> fromCode(int code) { // Pair of Instruction, Parameter
		String codeStr = String.valueOf(code);
		if (codeToInstruction.containsKey(codeStr))
			return new Pair<>(codeToInstruction.get(codeStr), null); // no parameter

		char[] codeChars = codeStr.toCharArray();
		// not the greatest method, since it is hardcoding, but it works well; since every instruction that has a parameter has the 2 slots on the right reserved for it, just use that to our advantage.
		// first digit indicates the instruction:
		Instruction instruction = firstDigitToInstruction.get(Short.parseShort(String.valueOf(codeChars[0])));
		// other 2 (sometimes 1) digits indicate parameter:
		if (codeStr.length() == 2) { // replace IX with I0X
			codeStr = codeChars[0] + "0" + codeChars[1];
		}

		return new Pair<>(instruction, Integer.parseInt(codeStr.substring(1)));
	}

	private static final List<String> stringValues = Arrays.stream(Instruction.values()).map(Enum::toString).collect(Collectors.toList());

	public static boolean exists(String name) {
		return stringValues.contains(name);
	}

}
