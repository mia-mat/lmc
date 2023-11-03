package me.mil.lmc.backend.exceptions;


public class LMCCompilationException extends LMCException{

	public LMCCompilationException(int lineNum, String reason) {
		super("Error detected on line " + lineNum + " while attempting to compile the program! " + ((reason == null) ? "" : "("+reason+")"));
	}

	public LMCCompilationException(int lineNum) {
		this(lineNum, null);
	}

	public LMCCompilationException(String reason) {
		super("An error has occurred attempting to compile the program!" + ((reason == null) ? "" : "("+reason+")"));
	}

	public LMCCompilationException() {
		this(null);
	}

}
