package me.mil.lmc.backend;

import java.util.List;

// A "timed" processor is one that has a clock speed.
public interface TimedProcessor extends Processor {

	int getClockSpeed();

	void setClockSpeed(int clockSpeed);

	boolean isRespectingClockSpeed();

	void waitForClock() throws InterruptedException;

	List<ProcessorObserverNotificationType> getTimingCatalysts();

}
