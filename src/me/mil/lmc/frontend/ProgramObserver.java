package me.mil.lmc.frontend;

import me.mil.lmc.backend.Program;

import java.util.Observable;
import java.util.Observer;

public class ProgramObserver implements Observer {

	Program program = null;

	public ProgramObserver(LMCInterface lmcInterface) {
		setProgram(lmcInterface.getCompiledProgram());
		lmcInterface.addProgramObserver(this);
	}

	public ProgramObserver(Program program) {
		setProgram(program);
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		if(this.program != null) this.program.deleteObserver(this);
		this.program = program;
		if(program != null) this.program.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) { }
}
