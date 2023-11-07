package me.mil.lmc.backend;

import me.mil.lmc.backend.Processor;
import me.mil.lmc.backend.ProcessorObserverNotification;
import me.mil.lmc.backend.util.Observable;
import me.mil.lmc.backend.util.Observer;

public class ProcessorObserver<T extends Observable & Processor> implements Observer {
	/* no implicit generics allowed for variable type, so doing it this way, where setting T when instantiating is pointless,
	/ but makes the types here work better. (don't have to use Object for this field) */
	T processor;

	public ProcessorObserver(T processor) {
		setProcessor(processor);
	}

	public void setProcessor(T processor) {
		if (this.processor != null)  this.processor.deleteObserver(this);
		this.processor = processor;
		if (processor != null) this.processor.addObserver(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onUpdate(Observable caller, Object arg) {
		try {
			T ignored = ((T) caller); // Checking if caller instanceof T

			if(arg instanceof ProcessorObserverNotification) {
				onUpdate((T) caller, (ProcessorObserverNotification) arg);
				onUpdate((Processor) caller, (ProcessorObserverNotification) arg);
			}

		} catch (ClassCastException ignored) { }
	}

	public void onUpdate(T processor, ProcessorObserverNotification notification) { }

	public void onUpdate(Processor processor, ProcessorObserverNotification notification) { }
}
