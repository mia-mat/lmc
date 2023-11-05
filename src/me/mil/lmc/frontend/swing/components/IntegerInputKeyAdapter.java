package me.mil.lmc.frontend.swing.components;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class IntegerInputKeyAdapter extends KeyAdapter { // todo respect caret position

	private final JTextField field;
	private final int limit; // -1 -> no limit
	private final int emptyValue;

	public IntegerInputKeyAdapter(JTextField field, int charLimit, int emptyValue) {
		this.field = field;
		this.limit = charLimit;
		this.emptyValue = emptyValue;
	}

	public IntegerInputKeyAdapter(JTextField field, int charLimit) {
		this(field, charLimit, 0);
	}

	public IntegerInputKeyAdapter(JTextField field) {
		this(field, -1, 0);
	}

	public JTextField getField() {
		return field;
	}

	@Override
	public void keyTyped(KeyEvent e) { // todo bug: 100, bksp -> 0. should be 10
		e.consume(); // don't pass on
		char keyChar = e.getKeyChar();

		if((keyChar < '0' || keyChar > '9') && keyChar != KeyEvent.VK_BACK_SPACE) return; // accept only 1-9 and backspace

		String text = field.getText();
		String newText = "";
		if(keyChar == KeyEvent.VK_BACK_SPACE) {
			if(!text.isEmpty()) newText = text.substring(0, text.length()-1);
		}else newText = text + keyChar;

		if(newText.isEmpty()) { // set to emptyValue if empty
			field.setText("" + emptyValue);
			newText="" + emptyValue;
		}

		if(limit > -1 && newText.length() > limit) return; // limit num of chars.

		field.setText(Integer.parseInt(newText) + ""); // Remove leading 0's and set newText as text.
	}


}
