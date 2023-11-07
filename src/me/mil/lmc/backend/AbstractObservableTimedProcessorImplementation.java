package me.mil.lmc.backend;

import me.mil.lmc.LMCReader;
import me.mil.lmc.LMCWriter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractObservableTimedProcessorImplementation extends AbstractObservableProcessorImplementation implements TimedProcessor{

	private static final ArrayList<ProcessorObserverNotificationType> timingCatalysts = new ArrayList<ProcessorObserverNotificationType>(){{
		add(ProcessorObserverNotificationType.SET_MEMORY);
		add(ProcessorObserverNotificationType.SET_REGISTER);
		add(ProcessorObserverNotificationType.CLEAR_REGISTERS);
		add(ProcessorObserverNotificationType.SET_HALTING);
	}};

	private int clockSpeed;

	protected AbstractObservableTimedProcessorImplementation(ProcessorInstruction[] instructions, int memorySize, int clockSpeed, LMCReader reader, LMCWriter writer) {
		super(instructions, memorySize, reader, writer);

		this.clockSpeed = clockSpeed;

		//noinspection InstantiationOfUtilityClass
		new TimedProcessorHandler(this);
	}

	@Override
	public void waitForClock() throws InterruptedException {
		Thread.sleep(1000 / getClockSpeed()); // TODO don't freeze the whole UI
	}

	@Override
	public int getClockSpeed() {
		return clockSpeed;
	}

	@Override
	public void setClockSpeed(int clockSpeed) {
		int old = getClockSpeed();
		this.clockSpeed = clockSpeed;
		if(old!=clockSpeed) notifyObservers(new ProcessorObserverNotification(ProcessorObserverNotificationType.SET_CLOCK_SPEED, old, clockSpeed));
	}

	@Override
	public boolean isRespectingClockSpeed() {
		return clockSpeed > 0;
	}

	@Override
	public List<ProcessorObserverNotificationType> getTimingCatalysts() {
		return timingCatalysts;
	}
}
