package me.mia.lmc.backend.util;

public interface Observer<T extends Observable> {
	void onUpdate(Observable caller, Object arg);
}
