package me.mil.lmc.backend;

import java.util.ArrayList;

// Could do this through reflection. But it's simpler like this.
public class TimedProcessorHandler {

	private static ArrayList<ProcessorObserver> observers;

	private static TimedProcessorHandler instance;

	public <T extends AbstractObservableProcessor & TimedProcessor> TimedProcessorHandler(T processor) {
		this();
		addTimedProcessor(processor);
	}

	private TimedProcessorHandler() {
		if (instance != null) return;
		instance = this;
		observers = new ArrayList<>();

		System.out.println("B"); // TODO find out why this is called twice on compile
	}

	public static <T extends AbstractObservableProcessor & TimedProcessor> void addTimedProcessor(T p) {
		observers.add(new ProcessorObserver(p) {
			@Override
			public void update(AbstractObservableProcessor processor, ProcessorObserverNotification notification) {
				if (!p.isRespectingClockSpeed()) return;
				if (!p.isRunning()) return;
				if (!p.getTimingCatalysts().contains(notification.getNotificationType())) return;

				try {
					p.waitForClock();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

			}
		});
	}

	public static ArrayList<ProcessorObserver> getObservers() {
		return observers;
	}


}
