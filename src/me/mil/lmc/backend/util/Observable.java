package me.mil.lmc.backend.util;

import java.util.*;

// a non-class-based implementation of observable
public interface Observable {
	Map<Observable, Set<Observer>> observers = new HashMap<>();

	default void update(Observable caller, Object arg) {
		if(!observers.containsKey(this)) observers.put(this, new HashSet<>());
		observers.get(this).forEach(c -> c.onUpdate(caller, arg));
	}

	default void update(Object arg) {
		update(this, arg);
	}

	default void update() {
		update(null);
	}

	default void addObserver(Observer observer) {
		if(!observers.containsKey(this)) observers.put(this, new HashSet<>());

		observers.get(this).add(observer);
	}


	default void deleteObserver(Observer observer) {
		if(observers.containsKey(this)) observers.get(this).remove(observer);
	}
}
