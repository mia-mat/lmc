package me.mil.lmc.frontend.swing.components;

import me.mil.lmc.frontend.LMCInterface;
import me.mil.lmc.frontend.util.GBCBuilder;
import me.mil.lmc.frontend.util.InterfaceUtils;
import me.mil.lmc.frontend.util.StyleConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.KeyEvent;

public final class ControlPanel extends LMCSubPanel {

	private JTextField textFieldMemorySize;
	private JTextField textFieldClockSpeed;

	public ControlPanel(LMCInterface lmcInterface) {
		super(lmcInterface);
	}

	@Override
	protected void addToRoot(RootPanel root) {
		root.add(this, new GBCBuilder().setAnchor(GBCBuilder.Anchor.LINE_START).setFill(GBCBuilder.Fill.BOTH)
				.setWeight(0.65, 0)
				.setPosition(1, 0)
				.build());
	}

	@Override
	protected void generate() {
		setLayout(new GridBagLayout());

		JPanel panelRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		InterfaceUtils.addAll(panelRow1, ControlButton.RUN(getInterface()), ControlButton.LOAD_INTO_RAM(getInterface()),
				ControlButton.CLEAR_RAM(getInterface()), ControlButton.STOP(getInterface()));

		JPanel panelRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		generateMemorySizeComponents(panelRow2);
		generateClockSpeedComponents(panelRow2);

		this.add(panelRow1, new GBCBuilder().setAnchor(GBCBuilder.Anchor.NORTH).setFill(GBCBuilder.Fill.HORIZONTAL)
				.setWeight(1, 0).setPosition(0, 0).setCellsConsumed(1, 1).build());
		this.add(panelRow2, new GBCBuilder().setAnchor(GBCBuilder.Anchor.NORTH).setFill(GBCBuilder.Fill.HORIZONTAL)
				.setWeight(1, 1).setPosition(0, 1).setCellsConsumed(1,1).build());
	}

	private void generateMemorySizeComponents(JPanel parent) {
		JLabel labelMemorySize = new JLabel("RAM Size");
		JTextField textFieldMemorySize = new JTextField("100");
		textFieldMemorySize.addKeyListener(new IntegerInputKeyAdapter(textFieldMemorySize, 4));

		parent.add(labelMemorySize);
		parent.add(textFieldMemorySize);
		this.textFieldMemorySize = textFieldMemorySize;
	}

	private void generateClockSpeedComponents(JPanel parent) {
		JLabel labelClockSpeed = new JLabel("Clock Speed (Hz)");
		labelClockSpeed.setBorder(new EmptyBorder(0, 5, 0, 0)); // Align

		JLabel labelClockSpeedUnrestricted = new JLabel("(Unrestricted)"); // Show when clock speed is set to "0", to inform the user that it's unrestricted.

		labelClockSpeedUnrestricted.setForeground(StyleConstants.COLOR_CONTROL_PANEL_UNRESTRICTED_CLOCK_SPEED_FOREGROUND);

		JTextField textFieldClockSpeed = new JTextField("0");

		// todo make this more elegant
		textFieldClockSpeed.addKeyListener(new IntegerInputKeyAdapter(textFieldClockSpeed, 5) { // Set restrictions
			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				labelClockSpeedUnrestricted.setVisible(Integer.parseInt(textFieldClockSpeed.getText()) == 0); // Correct visibility of unrestricted text
			}
		});
		this.textFieldClockSpeed = textFieldClockSpeed;

		parent.add(labelClockSpeed);
		parent.add(textFieldClockSpeed);
		parent.add(labelClockSpeedUnrestricted);
	}

	public int getRequestedMemorySize() {
		return Integer.parseInt(textFieldMemorySize.getText());
	}

	public int getRequestedClockSpeed() {
		return Integer.parseInt(textFieldClockSpeed.getText());
	}

}
