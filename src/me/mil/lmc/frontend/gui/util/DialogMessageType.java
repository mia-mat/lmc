package me.mil.lmc.frontend.gui.util;

import javax.swing.*;

public enum DialogMessageType {
	ERROR_MESSAGE(JOptionPane.ERROR_MESSAGE),
	INFORMATION_MESSAGE(JOptionPane.INFORMATION_MESSAGE),
	WARNING_MESSAGE(JOptionPane.WARNING_MESSAGE),
	QUESTION_MESSAGE(JOptionPane.QUESTION_MESSAGE),
	PLAIN_MESSAGE(JOptionPane.PLAIN_MESSAGE);

	private final int value;

	DialogMessageType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
