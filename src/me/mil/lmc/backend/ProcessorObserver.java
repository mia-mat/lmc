package me.mil.lmc.backend;

import java.util.Observable;
import java.util.Observer;

public class ProcessorObserver implements Observer {

	AbstractObservableProcessor processor;

	public ProcessorObserver(AbstractObservableProcessor processor) {
		setProcessor(processor);
	}

	public void setProcessor(AbstractObservableProcessor processor) {
		if (this.processor != null) this.processor.deleteObserver(this);
		this.processor = processor;
		if (processor != null) this.processor.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof ProcessorObserverNotification) update((AbstractObservableProcessor) o, (ProcessorObserverNotification) arg);
	}

	public void update(AbstractObservableProcessor processor, ProcessorObserverNotification notification) { }
}
