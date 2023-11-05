package me.mil.lmc.frontend.swing.components;

import me.mil.lmc.frontend.LMCInterface;
import me.mil.lmc.frontend.util.GBCBuilder;
import me.mil.lmc.frontend.util.StyleUtil;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;

public final class InputPanel extends LMCSubPanel {

	private JTextArea inputTextArea;

	public InputPanel(LMCInterface lmcInterface) {
		super(lmcInterface);
	}

	@Override
	protected void addToRoot(RootPanel root) { // Adding to InputOutputPanel instead of root.
		getInterface().getInputOutputPanel().add(this, new GBCBuilder().setAnchor(GBCBuilder.Anchor.CENTRE).setFill(GBCBuilder.Fill.BOTH)
				.setWeight(1, 0.6).setPosition(0, 0)
				.setCellsConsumed(1, 1).build());
	}

	@Override
	protected void generate() {
		setLayout(new GridBagLayout());

		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(false);
		textArea.setFocusable(true);
		textArea.setFont(StyleUtil.FONT_LARGE());

		// Prevent beep sound when pressing backspace at start of text area
		Action deleteAction = textArea.getActionMap().get(DefaultEditorKit.deletePrevCharAction);
		textArea.getActionMap().put(DefaultEditorKit.deletePrevCharAction, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textArea.getSelectionEnd() != 0) deleteAction.actionPerformed(e);
			}
		});

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Fix input panel being non-assertive when resizing
		scrollPane.setPreferredSize(new Dimension(500, (int) (getHeight() * 0.6)));
		scrollPane.setMinimumSize(new Dimension(300, (int) (getHeight() * 0.6)));

		this.add(scrollPane, new GBCBuilder().setAnchor(GBCBuilder.Anchor.CENTER).setFill(GBCBuilder.Fill.BOTH).
				setWeight(1, 1).setPosition(0, 0).setCellsConsumed(1, 1).build());

		inputTextArea = textArea;
	}

	public JTextArea getInputTextArea() {
		return inputTextArea;
	}

	public String getText() {
		return getInputTextArea().getText();
	}
}
