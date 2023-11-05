package me.mil.lmc.backend;

public class ProcessorObserverNotification {
	private final Object newVal;
	private final Object oldVal;
	private final ProcessorObserverNotificationType type;

	ProcessorObserverNotification(ProcessorObserverNotificationType type, Object oldVal, Object newVal) {
		this.type = type;
		this.oldVal = oldVal;
		this.newVal = newVal;
	}

	public Object getNewValue() {
		return newVal;
	}

	public Object getOldValue() {
		return oldVal;
	}

	public ProcessorObserverNotificationType getNotificationType() {
		return type;
	}
}
