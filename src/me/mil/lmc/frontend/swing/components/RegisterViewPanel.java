package me.mil.lmc.frontend.swing.components;

import me.mil.lmc.backend.ProgramState;
import me.mil.lmc.backend.RegisterType;
import me.mil.lmc.frontend.ProgramObserver;
import me.mil.lmc.frontend.LMCInterface;
import me.mil.lmc.frontend.util.GBCBuilder;
import me.mil.lmc.frontend.util.InterfaceUtils;
import me.mil.lmc.frontend.util.StyleUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public final class RegisterViewPanel extends LMCSubPanel {

	Map<RegisterType, RegisterPanel> registerObjects;

	public RegisterViewPanel(LMCInterface lmcInterface) {
		super(lmcInterface);
	}

	private static class RegisterPanel extends LMCPanel {

		private final RegisterType registerType;
		private JLabel registerLabel; // 'label label'
		private JLabel valueLabel;

		public RegisterPanel(LMCInterface lmcInterface, RegisterType registerType) {
			super(lmcInterface, false);
			this.registerType = registerType;
			generate();
		}

		@Override
		protected void generate() {
			setLayout(new FlowLayout(FlowLayout.LEFT));
//			setBackground(getParent().getBackground()); // tODO fix
			setBorder(new EmptyBorder(0, 0, 0, 20));

			JLabel labelRegister = new JLabel(this.registerType.getUiName());

			JLabel labelValue = new JLabel("000");
			labelValue.setFont(UIManager.getFont("h4.font"));

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
			valueLabel.setText(InterfaceUtils.padInteger(newText, '0', 3));
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
		setBorder(StyleUtil.BORDER_EMPTY);
		setBackground(StyleUtil.COLOR_REGISTER_VIEW_BACKGROUND);

		registerObjects = new HashMap<>();
		Arrays.stream(RegisterType.values()).forEach(type -> { // Generate a RegisterPanel for each RegisterType
			RegisterPanel reg = new RegisterPanel(getInterface(), type);
			add(reg);

			registerObjects.put(type, reg);
		});

		new ProgramObserver(getInterface()) {
			@Override
			public void update(Observable o, Object arg) {
				registerObjects.keySet().forEach(type -> registerObjects.get(type).setValueLabelText(((ProgramState) arg).getRegister(type).getValue()));
			}
		};
	}

}
