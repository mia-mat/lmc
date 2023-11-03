package me.mil.lmc.backend;

import me.mil.lmc.backend.exceptions.LMCRuntimeException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public enum Instruction {
	ADD("1XX", ((state, input) -> { // Add the input to the accumulator
		return state.setRegister(Program.RegisterType.ACCUMULATOR, state.getRegister(Program.RegisterType.ACCUMULATOR).getValue()+state.getValue(input));
	})),
	SUB("2XX", ((state, input) -> { // Subtract the input from the accumulator
		return state.setRegister(Program.RegisterType.ACCUMULATOR, state.getRegister(Program.RegisterType.ACCUMULATOR).getValue()-state.getValue(input));
	})),
	STA("3XX", ((state, input) -> { // Store the contents of the accumulator in RAM location 'input'
		return state.setValue(input, state.getRegister(Program.RegisterType.ACCUMULATOR).getValue());
	})),
	LDA("5XX", ((state, input) -> { // Load the value from RAM location 'input' into the accumulator
		return state.setRegister(Program.RegisterType.ACCUMULATOR, state.getValue(input));
	})),
	BRA("6XX", ((state, input) -> { // Set the Program Counter to 'input'
		return state.setRegister(Program.RegisterType.PROGRAM_COUNTER, input);
	})),
	BRZ("7XX", ((state, input) -> { // Set the Program Counter to 'input' if the accumulator is 0
		if(state.getRegister(Program.RegisterType.ACCUMULATOR).getValue() == 0) return state.setRegister(Program.RegisterType.PROGRAM_COUNTER, input);
		return state;
	})),
	BRP("8XX", ((state, input) -> {// Set the Program Counter to 'input' if the accumulator is greater than or equal to 0
		if(state.getRegister(Program.RegisterType.ACCUMULATOR).getValue() >= 0) return state.setRegister(Program.RegisterType.PROGRAM_COUNTER, input);
		return state;
	})),
	INP("901", ((state, input) -> { // Fetch a value from the user into the accumulator // TODO UI Input
		System.out.print("Awaiting Integer Input: ");
		return state.setRegister(Program.RegisterType.ACCUMULATOR, Integer.parseInt(new Scanner(System.in).nextLine()));
	})),
	OUT("902", ((state, input) -> { // Output the value in the accumulator // TODO UI Output
		System.out.println("# Output: " + state.getRegister(Program.RegisterType.ACCUMULATOR).getValue());
		return state;
	})),
	HLT("0", ((state, input) -> state.setHalting(true))),
	DAT(null, null);

	private final String code;
	private final BiFunction<ProgramState, Integer, ProgramState> function;

	Instruction(String code, BiFunction<ProgramState, Integer, ProgramState> function) {
		this.code = code;
		this.function = function;
	}

	public ProgramState execute(ProgramState state, Integer parameter) {
		return function.apply(state, parameter);
	}

	public boolean requiresParameter() {
		if(this == DAT) return true;
		return this.code.contains("X");
	}

	public int getCode(int parameter) {
		if(!requiresParameter()) Logger.getGlobal().warning("Unnecessary parameter provided");
		return Integer.parseInt(code.replaceAll("XX", String.valueOf(parameter)));
	}
	public int getCode() throws LMCRuntimeException {
		if(requiresParameter()) throw new LMCRuntimeException("No parameter provided when one was required");
		return Integer.parseInt(code);
	}

	// not creating these dynamically because I like my performance (and brevity).
	private static final Map<String, Instruction> codeToInstruction = Arrays.stream(Instruction.values()).collect(Collectors.toMap(i -> i.code, i -> i));
	private static final Map<Short, Instruction> firstDigitToInstruction = Arrays.stream(Instruction.values()).filter(i -> (i.code!= null && i.code.contains("X"))).collect(Collectors.toMap(i -> Short.parseShort(String.valueOf(i.code.toCharArray()[0])), i -> i)); // Only for instructions with parameters
	public static Pair<Instruction, Integer> fromCode(int code){ // Pair of Instruction, Parameter
		String codeStr = String.valueOf(code);
		if(codeToInstruction.containsKey(codeStr)) return new Pair<>(codeToInstruction.get(codeStr), null); // no parameter

		char[] codeChars = codeStr.toCharArray();
		// not the greatest method, since it is hardcoding, but it works well; since every instruction that has a parameter has the 2 slots on the right reserved for it, just use that to our advantage.
		// first digit indicates the instruction:
		Instruction instruction = firstDigitToInstruction.get(Short.parseShort(String.valueOf(codeChars[0])));
		// other 2 (sometimes 1) digits indicate parameter:
		if(codeStr.length()==2) { // replace IX with I0X
			codeStr = codeChars[0] + "0" + codeChars[1];
		}

		return new Pair<>(instruction, Integer.parseInt(codeStr.substring(1)));
	}

	private static final List<String> stringValues = Arrays.stream(Instruction.values()).map(Enum::toString).collect(Collectors.toList());
	public static boolean exists(String name) {
		return stringValues.contains(name);
	}

}
