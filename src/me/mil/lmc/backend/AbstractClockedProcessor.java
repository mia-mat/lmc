package me.mil.lmc.backend;

import me.mil.lmc.LMCReader;
import me.mil.lmc.LMCWriter;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractClockedProcessor extends AbstractProcessor implements ClockedProcessor {

	private int clockSpeed;
	LinkedBlockingQueue<Runnable> runnableQueue;

	private boolean currentlyRunningRunnable;

	public AbstractClockedProcessor(ProcessorInstruction[] instructions, int memorySize, int clockSpeed, LMCReader reader, LMCWriter writer) {
		super(instructions, memorySize, reader, writer);
		this.clockSpeed = clockSpeed;
		this.runnableQueue = new LinkedBlockingQueue<>();
	}

	@Override
	public void run() {
		super.prepareRun();
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(isHalting()) {
					this.cancel();
					AbstractClockedProcessor.super.finalizeRun();
					return;
				}

				if(getRunnableQueue().isEmpty()) {
					performCurrentInstructionStep();
				}

				if(currentlyRunningRunnable) return;

				if(!getRunnableQueue().isEmpty()) {
					currentlyRunningRunnable = true;
					runnableQueue.poll().run();
					currentlyRunningRunnable = false;
				}

			}
		}, 0L, (1000 / clockSpeed)); // todo setclockspeed reflect here


	}

	@Override
	public void addToRunnableQueue(Runnable r) {
		try {
			runnableQueue.put(r);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Queue<Runnable> getRunnableQueue() {
		return runnableQueue;
	}

	@Override
	public int getClockSpeed() {
		return clockSpeed;
	}

	@Override
	public void setClockSpeed(int newValue) {
		this.clockSpeed = newValue;
	}
}
