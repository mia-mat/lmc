package me.mil.lmc.frontend.swing.components;

import me.mil.lmc.frontend.ControlFunction;
import me.mil.lmc.frontend.LMCInterface;
import me.mil.lmc.frontend.ProgramObserver;

import javax.swing.*;
import java.util.Arrays;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.function.Function;


public class ControlButton extends JButton {

	private static final Function<LMCInterface, Boolean> CONDITION_PROGRAM_NOT_RUNNING = (lmc) -> {
		if(lmc.getCompiledProgram() == null) return false;
		return !lmc.getCompiledProgram().isRunning();
	};

	protected static ControlButton RUN(LMCInterface lmcInterface) {
		return new ControlButton("Run", lmcInterface, () -> CONDITION_PROGRAM_NOT_RUNNING.apply(lmcInterface), ControlFunction.RUN);
	}
	protected static ControlButton LOAD_INTO_RAM(LMCInterface lmcInterface) {
		return new ControlButton("Load into Memory", lmcInterface, () -> CONDITION_PROGRAM_NOT_RUNNING.apply(lmcInterface), ControlFunction.LOAD_INTO_RAM);
	}
	protected static ControlButton CLEAR_RAM(LMCInterface lmcInterface) {
		 return new ControlButton("Clear Memory", lmcInterface, () -> CONDITION_PROGRAM_NOT_RUNNING.apply(lmcInterface), ControlFunction.CLEAR_RAM);
	}
	protected static ControlButton STOP(LMCInterface lmcInterface) {
		return new ControlButton("Stop", lmcInterface, () -> !CONDITION_PROGRAM_NOT_RUNNING.apply(lmcInterface), ControlFunction.STOP);
	}

	private ControlButton(String text, LMCInterface lmcInterface, Callable<Boolean> enabledCondition, ControlFunction... functions) {
		setText(text);
		addActionListener((e) -> Arrays.stream(functions).forEach(lmcInterface::performControlFunction));

		new ProgramObserver(lmcInterface){
			@Override
			public void update(Observable o, Object arg) {
				try {
					setEnabled(enabledCondition.call());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};

	}

}
