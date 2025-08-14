package me.mia.lmc.frontend.gui.components;

import me.mia.lmc.backend.Processor;
import me.mia.lmc.backend.ProcessorObserverNotification;
import me.mia.lmc.backend.ProcessorObserverNotificationType;
import me.mia.lmc.backend.RegisterType;
import me.mia.lmc.frontend.gui.AbstractGraphicalInterface;
import me.mia.lmc.frontend.gui.LMCProcessorObserver;
import me.mia.lmc.frontend.gui.util.GBCBuilder;
import me.mia.lmc.frontend.gui.util.InterfaceUtils;
import me.mia.lmc.frontend.gui.util.StyleConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class RegisterViewPanel extends LMCSubPanel {

	Map<RegisterType, RegisterPanel> registerObjects;

	public RegisterViewPanel(AbstractGraphicalInterface lmcInterface) {
		super(lmcInterface);
	}

	private static class RegisterPanel extends LMCPanel {

		private final RegisterType registerType;
		private JLabel registerLabel; // 'label label'
		private JLabel valueLabel;

		public RegisterPanel(AbstractGraphicalInterface lmcInterface, RegisterType registerType) {
			super(lmcInterface, false);
			this.registerType = registerType;
			generate();
		}

		@Override
		protected void generate() {
			setLayout(new FlowLayout(FlowLayout.LEFT));
			setBackground(StyleConstants.COLOR_REGISTER_VIEW_BACKGROUND);
			setBorder(new EmptyBorder(0, 0, 0, 20));

			JLabel labelRegister = new JLabel(this.registerType.getUiName());

			JLabel labelValue = new JLabel("000");
			labelValue.setFont(StyleConstants.FONT_H4);

			add(labelRegister);
			add(labelValue);

			this.registerLabel = labelRegister;
			this.valueLabel = labelValue;
		}

		public RegisterType getRegisterType() {
			return registerType;
		}

		public JLabel getRegisterLabel() {
			return registerLabel;
		}

		public JLabel getValueLabel() {
			return valueLabel;
		}

		public void setValueLabelText(int newText) {
			valueLabel.setText(InterfaceUtils.padInteger(newText));
			valueLabel.paintImmediately(valueLabel.getVisibleRect()); // refresh
		}
	}

	@Override
	protected void addToRoot(RootPanel root) {
		root.add(this, new GBCBuilder().setAnchor(GBCBuilder.Anchor.NORTH).setFill(GBCBuilder.Fill.BOTH)
				.setWeight(0.65, 0).setPosition(1, 1).build());
	}

	@Override
	protected void generate() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBorder(StyleConstants.BORDER_EMPTY);
		setBackground(StyleConstants.COLOR_REGISTER_VIEW_BACKGROUND);

		registerObjects = new HashMap<>();
		Arrays.stream(RegisterType.values()).forEach(type -> { // Generate a RegisterPanel for each RegisterType
			if (!type.isDisplayed()) return;
			RegisterPanel reg = new RegisterPanel(getInterface(), type);
			add(reg);

			registerObjects.put(type, reg);
		});

		new LMCProcessorObserver(getInterface()) {
			@Override
			public void onUpdate(Processor processor, ProcessorObserverNotification notification) {
				if (notification.getType() == ProcessorObserverNotificationType.SET_REGISTER
						|| notification.getType() == ProcessorObserverNotificationType.CLEAR_REGISTERS) {
					SwingUtilities.invokeLater(() -> registerObjects.keySet().forEach(type -> registerObjects.get(type).setValueLabelText((int) processor.getRegisterValue(type))));
				}
			}
		};
	}

}
