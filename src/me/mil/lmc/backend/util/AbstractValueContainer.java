package me.mil.lmc.backend.util;

public abstract class AbstractValueContainer<T> {
	private T value;

	public AbstractValueContainer(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
