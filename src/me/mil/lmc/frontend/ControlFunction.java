package me.mil.lmc.frontend;

import me.mil.lmc.backend.Program;
import me.mil.lmc.backend.exceptions.LMCCompilationException;
import me.mil.lmc.backend.exceptions.LMCException;
import me.mil.lmc.backend.exceptions.LMCRuntimeException;

import java.util.Arrays;
import java.util.function.Consumer;

public enum ControlFunction {
	COMPILE((lmcInterface) -> {
		try {
			lmcInterface.getMemoryViewPanel().clearMemoryUnits();

			for(int i = 0; i < lmcInterface.getControlPanel().getRequestedMemorySize(); i++) { // Instantiate Memory Units
				lmcInterface.getMemoryViewPanel().addMemoryUnit(i);
			}
			lmcInterface.setCompiledProgram(Program.compileProgram(lmcInterface.getReader(), lmcInterface.getWriter(), lmcInterface.getControlPanel().getRequestedMemorySize(), lmcInterface.getInputPanel().getText()));

		} catch (LMCCompilationException e) {
			lmcInterface.showErrorDialog(e);
		}
	}),
	LOAD_INTO_RAM(lmcInterface -> {
		try {
			lmcInterface.getCompiledProgram().loadIntoRAM();
		} catch (LMCRuntimeException e) {
			lmcInterface.showErrorDialog(e);
		}
	}, COMPILE),
	CLEAR_RAM(lmcInterface -> lmcInterface.getCompiledProgram().clearRAM(), COMPILE),
	RUN(lmcInterface -> {
		try {
			lmcInterface.getCompiledProgram().run(lmcInterface.getControlPanel().getRequestedClockSpeed(), false, false);
		} catch (Exception e) {
			if(e instanceof LMCException) {
				lmcInterface.showErrorDialog((LMCException) e);
			}else{
				throw new RuntimeException(e);
			}
		}
	}, CLEAR_RAM, LOAD_INTO_RAM),
	STOP((lmc) -> { System.out.println("stop!"); }); // TODO

	private final Consumer<LMCInterface> action;
	private final ControlFunction[] inheritedFunctions;
	ControlFunction(Consumer<LMCInterface> action, ControlFunction... inheritedFunctions) {
		this.action = action;
		this.inheritedFunctions = inheritedFunctions;
	}

	public void executeAction(LMCInterface lmcInterface) {
		Arrays.stream(inheritedFunctions).forEach(lmcInterface::performControlFunction);
		action.accept(lmcInterface);
	}
}
