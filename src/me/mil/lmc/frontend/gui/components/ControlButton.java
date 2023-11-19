package me.mil.lmc.frontend.gui.components;

import me.mil.lmc.backend.Processor;
import me.mil.lmc.backend.ProcessorObserverNotification;
import me.mil.lmc.frontend.gui.AbstractGraphicalInterface;
import me.mil.lmc.frontend.gui.ControlFunction;
import me.mil.lmc.frontend.gui.LMCProcessorObserver;

import javax.swing.*;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.Function;

import static me.mil.lmc.backend.ProcessorObserverNotificationType.SET_HALTING;
import static me.mil.lmc.backend.ProcessorObserverNotificationType.SET_RUNNING;


public class ControlButton extends JButton {

	private static final Function<AbstractGraphicalInterface, Boolean> CONDITION_PROGRAM_NOT_RUNNING_OR_NULL = (lmc) -> {
		if (lmc.getProcessor() == null) return true;
		return !lmc.getProcessor().isRunning();
	};
	private static final Function<AbstractGraphicalInterface, Boolean> CONDITION_PROGRAM_RUNNING_AND_NOT_NULL = (lmc) -> {
		if (lmc.getProcessor() == null) return false;
		return lmc.getProcessor().isRunning();
	};

	protected static ControlButton RUN(AbstractGraphicalInterface lmcInterface) {
		return new ControlButton("Run", lmcInterface, () -> CONDITION_PROGRAM_NOT_RUNNING_OR_NULL.apply(lmcInterface), ControlFunction.RUN);
	}

	protected static ControlButton LOAD_INTO_RAM(AbstractGraphicalInterface lmcInterface) {
		return new ControlButton("Load into Memory", lmcInterface, () -> CONDITION_PROGRAM_NOT_RUNNING_OR_NULL.apply(lmcInterface), ControlFunction.LOAD_INTO_RAM);
	}

	protected static ControlButton CLEAR_RAM(AbstractGraphicalInterface lmcInterface) {
		return new ControlButton("Clear Memory", lmcInterface, () -> CONDITION_PROGRAM_NOT_RUNNING_OR_NULL.apply(lmcInterface), ControlFunction.CLEAR_RAM);
	}

	protected static ControlButton STOP(AbstractGraphicalInterface lmcInterface) {
		return new ControlButton("Stop", lmcInterface, () -> CONDITION_PROGRAM_RUNNING_AND_NOT_NULL.apply(lmcInterface), ControlFunction.STOP);
	}

	private ControlButton(String text, AbstractGraphicalInterface lmcInterface, Callable<Boolean> enabledCondition, ControlFunction... functions) {
		setText(text);
		addActionListener((e) -> Arrays.stream(functions).forEach(lmcInterface::performControlFunction));

		try {
			setEnabled(enabledCondition.call());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		new LMCProcessorObserver(lmcInterface) {
			@Override
			public void onUpdate(Processor processor, ProcessorObserverNotification notification) {
				if (notification.getType() == SET_RUNNING || notification.getType() == SET_HALTING) {
					try {
						setEnabled(enabledCondition.call());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		};

	}

}
