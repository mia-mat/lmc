package me.mil.lmc.backend.util;

import java.util.HashMap;
import java.util.Map;

// a non-class-based implementation of observable
public interface Observable {
	Map<Observer, Observable> observers = new HashMap<>();

	default void update(Observable caller, Object arg) {
		observers.forEach((key, value) -> {
			if(value == caller) {
				key.onUpdate(caller, arg);
			}
		});
	}

	default void update(Object arg) {
		update(this, arg);
	}

	default void update() {
		update(null);
	}

	default void addObserver(Observer observer) {
		observers.put(observer, this);
	}

	default void deleteObserver(Observer observer) {
		observers.remove(observer);
	}
}
