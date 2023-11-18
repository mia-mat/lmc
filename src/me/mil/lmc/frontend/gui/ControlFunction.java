package me.mil.lmc.frontend.gui;

import me.mil.lmc.backend.exceptions.LMCCompilationException;
import me.mil.lmc.backend.exceptions.LMCRuntimeException;
import me.mil.lmc.frontend.gui.util.DialogMessageType;

import java.util.*;
import java.util.function.Consumer;

public enum ControlFunction {
	COMPILE(3, (lmcInterface) -> {
		try {
			lmcInterface.getProcessor().compileAndSetInstructions(lmcInterface.getInputPanel().getText());
			if(lmcInterface.getControlPanel().getRequestedMemorySize() < 1) {
				lmcInterface.showMessageDialog("Oops", "Memory Size must be greater than 0.\nProceeding without updating processor specifications.", DialogMessageType.WARNING_MESSAGE);
				return;
			}
			lmcInterface.getProcessor().setMemorySize(lmcInterface.getControlPanel().getRequestedMemorySize());
			lmcInterface.getProcessor().setClockSpeed(lmcInterface.getControlPanel().getRequestedClockSpeed());
			lmcInterface.getProcessor().setReader(lmcInterface.getReader());
			lmcInterface.getProcessor().setWriter(lmcInterface.getWriter());

		} catch (LMCCompilationException e) {
			lmcInterface.showErrorDialog(e);
		}
	}),
	LOAD_INTO_RAM(1, lmcInterface -> {
		try {
			lmcInterface.getProcessor().loadInstructionsIntoMemory();
		} catch (LMCRuntimeException e) {
			lmcInterface.showErrorDialog(e);
		}
	}, COMPILE),
	CLEAR_RAM(2, lmcInterface -> lmcInterface.getProcessor().clearMemory(), COMPILE),
	RUN(0, lmcInterface -> {
		try {
			lmcInterface.getProcessor().run();
		} catch (Exception e) {
			lmcInterface.showErrorDialog(new LMCRuntimeException("Unknown Runtime Exception."));
			throw new RuntimeException(e);
		}
	}, CLEAR_RAM, LOAD_INTO_RAM),
	STOP(-1, (lmc) -> lmc.getProcessor().forceHalt());

	private final Consumer<AbstractGraphicalInterface> action;
	private final ControlFunction[] inheritedFunctions;
	private final int priority;

	ControlFunction(int priority, Consumer<AbstractGraphicalInterface> action, ControlFunction... inheritedFunctions) {
		this.action = action;
		this.inheritedFunctions = inheritedFunctions;
		this.priority = priority;
	}


	public void executeAction(AbstractGraphicalInterface lmcInterface) {
		getAllFunctions().stream().sorted(Comparator.comparingInt(ControlFunction::getPriority).reversed()).forEach(a -> a.action.accept(lmcInterface));
	}

	private Set<ControlFunction> getAllFunctions() {
		Set<ControlFunction> set = new HashSet<>();
		set.add(this);

		for (ControlFunction function : inheritedFunctions) {
			set.addAll(function.getAllFunctions());
		}
		return set;
	}

	private int getPriority() {
		return priority;
	}
}
