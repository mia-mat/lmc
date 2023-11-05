package me.mil.lmc.backend;

import java.util.Observable;

public class Register extends AbstractValueContainer<Integer>{

	public Register(Integer value) {
		super(value);
	}
	public Register() {
		this(0);
	}

}
