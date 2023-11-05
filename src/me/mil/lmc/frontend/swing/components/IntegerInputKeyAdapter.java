package me.mil.lmc.frontend.swing.components;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class IntegerInputKeyAdapter extends KeyAdapter {

	public static final int NO_LIMIT = -1;

	private final JTextField field;
	private final int limit; // -1 = no limit
	private final int emptyValue;

	public IntegerInputKeyAdapter(JTextField field, int charLimit, int emptyValue) {
		this.field = field;
		this.limit = charLimit;
		this.emptyValue = emptyValue;

		// disable method of getting around the filters (and other annoying things)
		disableAction(DefaultEditorKit.pasteAction);
		disableAction(DefaultEditorKit.beepAction);
	}

	public IntegerInputKeyAdapter(JTextField field, int charLimit) {
		this(field, charLimit, 0);
	}

	public IntegerInputKeyAdapter(JTextField field) {
		this(field, NO_LIMIT, 0);
	}

	public JTextField getField() {
		return field;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		e.consume(); // don't pass on
		char keyChar = e.getKeyChar();
		String text = field.getText();

		if (text.isEmpty()) field.setText(String.valueOf(emptyValue));

		if ((keyChar < '0' || keyChar > '9')) return; // accept only 1-9 and backspace

		// Add input with respect to caret position (and length of selection)
		String newText = text.substring(0, field.getSelectionStart()) + keyChar + text.substring(field.getSelectionEnd());

		if (limit != NO_LIMIT && newText.length() > limit) return; // limit num of chars.

		int newCaretPosition = field.getSelectionStart() + 1; // calculate new caret position before modifying text

		field.setText(String.valueOf(Integer.parseInt(newText))); // parseInt to remove leading 0s.

		field.setSelectionStart(newCaretPosition);
		field.setSelectionEnd(newCaretPosition);
	}


	private void disableAction(String actionKey) {
		field.getActionMap().get(actionKey).setEnabled(false);
	}
}
