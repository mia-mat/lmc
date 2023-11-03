package me.mil.lmc.backend.exceptions;

public abstract class LMCException extends Exception{

	public LMCException(String reason) {
		super(reason);
	}

}
