package me.mil.lmc.backend.util;

public interface Observer<T extends Observable> {
	void onUpdate(Observable caller, Object arg);
}
