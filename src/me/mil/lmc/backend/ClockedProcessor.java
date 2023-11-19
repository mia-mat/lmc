package me.mil.lmc.backend;

import java.util.Queue;

public interface ClockedProcessor {

	void addToRunnableQueue(Runnable r);
	Queue<Runnable> getRunnableQueue();
	int getClockSpeed();
	void setClockSpeed(int newValue);

}
