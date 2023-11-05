package me.mil.lmc.frontend;

import me.mil.lmc.backend.LMCProcessor;
import me.mil.lmc.backend.exceptions.LMCCompilationException;
import me.mil.lmc.backend.exceptions.LMCRuntimeException;

import java.util.Arrays;
import java.util.function.Consumer;

public enum ControlFunction {
	COMPILE((lmcInterface) -> {
		try {
			lmcInterface.setProcessor(LMCProcessor.compileInstructions(lmcInterface.getInputPanel().getText(),
					lmcInterface.getControlPanel().getRequestedMemorySize(), lmcInterface.getControlPanel().getRequestedClockSpeed(),
					lmcInterface.getReader(), lmcInterface.getWriter()));

			lmcInterface.getMemoryViewPanel().resetMemoryUnits();

		} catch (LMCCompilationException e) {
			lmcInterface.showErrorDialog(e);
		}
	}),
	LOAD_INTO_RAM(lmcInterface -> {
		try {
			lmcInterface.getProcessor().loadInstructionsIntoMemory();
		} catch (LMCRuntimeException e) {
			lmcInterface.showErrorDialog(e);
		}
	}, COMPILE),
	CLEAR_RAM(lmcInterface -> lmcInterface.getProcessor().clearMemory(), COMPILE),
	RUN(lmcInterface -> {
		try {
			lmcInterface.getProcessor().run();
		} catch (Exception e) {
			lmcInterface.showErrorDialog(new LMCRuntimeException("Unknown Runtime Exception."));
			throw new RuntimeException(e);
		}
	}, CLEAR_RAM, LOAD_INTO_RAM),
	STOP((lmc) -> { lmc.getProcessor().forceHalt(); });

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
