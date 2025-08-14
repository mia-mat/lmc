package me.mia.lmc.frontend.gui.util;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.function.Consumer;

public class IntegerInputKeyAdapter extends KeyAdapter {

	public static final int NO_LIMIT = -1;

	private final JTextField textField;
	private final int charLimit;
	private final int emptyValue;
	private final Optional<Consumer<KeyEvent>> consumer;

	public IntegerInputKeyAdapter(JTextField textField, int charLimit, int emptyValue, Consumer<KeyEvent> consumer) {
		this.textField = textField;
		this.charLimit = charLimit;
		this.emptyValue = emptyValue;
		this.consumer = Optional.ofNullable(consumer);

		textField.setTransferHandler(null); // Disable Copy / Paste.
	}

	public IntegerInputKeyAdapter(JTextField textField, int charLimit, int emptyValue) {
		this(textField, charLimit, emptyValue, null);
	}

	public IntegerInputKeyAdapter(JTextField textField, int charLimit) {
		this(textField, charLimit, 0);
	}

	public IntegerInputKeyAdapter(JTextField textField) {
		this(textField, NO_LIMIT, 0);
	}

	public JTextField getTextField() {
		return textField;
	}

	private void process(KeyEvent e) {
		e.consume(); // don't pass on
		char keyChar = e.getKeyChar();
		String text = textField.getText();

		if (text.isEmpty()) textField.setText(String.valueOf(emptyValue));

		if ((keyChar < '0' || keyChar > '9')) return;

		// Add input character with respect to caret position (and length of selection).
		String newText = text.substring(0, textField.getSelectionStart()) + keyChar + text.substring(textField.getSelectionEnd());

		if (charLimit != NO_LIMIT && newText.length() > charLimit) return;

		int newCaretPosition = textField.getSelectionStart() + 1;

		textField.setText(String.valueOf(Integer.parseInt(newText))); // Parse as int to remove leading 0s.

		textField.setSelectionStart(newCaretPosition);
		textField.setSelectionEnd(newCaretPosition);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		process(e);
		consumer.ifPresent(c -> c.accept(e));
	}

}
