package me.mil.lmc.frontend.swing.components;

import me.mil.lmc.backend.AbstractObservableProcessor;
import me.mil.lmc.backend.ProcessorObserverNotification;
import me.mil.lmc.backend.ProcessorObserverNotificationType;
import me.mil.lmc.frontend.LMCProcessorObserver;
import me.mil.lmc.frontend.LMCInterface;
import me.mil.lmc.frontend.swing.WrapLayout;
import me.mil.lmc.frontend.util.GBCBuilder;
import me.mil.lmc.frontend.util.InterfaceUtils;
import me.mil.lmc.frontend.util.StyleConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;

public final class MemoryViewPanel extends LMCSubPanel {

	private Map<Integer, MemoryUnit> memoryUnits = new HashMap<>();

	private Container memoryUnitContainer;

	public MemoryViewPanel(LMCInterface lmcInterface) {
		super(lmcInterface);
	}

	@Override
	protected void addToRoot(RootPanel root) {
		root.add(this, new GBCBuilder().setAnchor(GBCBuilder.Anchor.LINE_START).setFill(GBCBuilder.Fill.BOTH)
				.setWeight(0.65, 0.96).
				setCellsConsumed(1,2)
				.setPosition(1, 2).build());
	}

	@Override
	protected void generate() {
		setLayout(new BorderLayout());
		setBackground(StyleConstants.COLOR_MEMORY_VIEW_BACKGROUND);

		JPanel memoryUnitContainer = new JPanel(new WrapLayout(FlowLayout.LEFT, 10, 10));
		memoryUnitContainer.setBackground(getBackground());
		memoryUnitContainer.setBorder(new EmptyBorder(15, 10, 15, 10));

		JScrollPane scrollPane = new JScrollPane(memoryUnitContainer);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		this.memoryUnitContainer = memoryUnitContainer;

		add(scrollPane);

		new LMCProcessorObserver(getInterface()){
			@Override
			public void update(AbstractObservableProcessor processor, ProcessorObserverNotification notification) {
				if(notification.getNotificationType() == ProcessorObserverNotificationType.SET_MEMORY
				|| notification.getNotificationType() == ProcessorObserverNotificationType.CLEAR_MEMORY) {
					memoryUnits.forEach((id, unit) -> unit.setOpCode(processor.getMemorySlotValue(id)));
					return;
				}

				if(notification.getNotificationType() == ProcessorObserverNotificationType.SET_MEMORY_SIZE) {
					resetMemoryUnits();
				}

			}
		};

	}

	// clear, then instantiate
	public void resetMemoryUnits() {
		clearMemoryUnits();
		instantiateMemoryUnits();
	}

	public void instantiateMemoryUnits() {
		for (int i = 0; i < getInterface().getProcessor().getMemorySize(); i++) { // Instantiate Memory Units
			addMemoryUnit(i);
		}
	}

	public void addMemoryUnit(int id) {
		MemoryUnit memUnit = new MemoryUnit(getInterface(), id);
		getMemoryUnitContainer().add(memUnit);
		memoryUnits.put(id, memUnit);
		getMemoryUnitContainer().revalidate(); // refresh
	}

	public void removeMemoryUnit(int id) {
		getMemoryUnitContainer().remove(memoryUnits.get(id));
		memoryUnits.remove(id);
	}

	public void clearMemoryUnits() {
		new HashSet<>(memoryUnits.keySet()).forEach(this::removeMemoryUnit);
	}

	protected Container getMemoryUnitContainer() {
		return memoryUnitContainer;
	}

	private static class MemoryUnit extends LMCPanel {

		private final int id;
		private JLabel labelOpCode;

		public MemoryUnit(LMCInterface lmcInterface, int id) {
			super(lmcInterface, false);
			this.id = id;
			generate();
		}

		@Override
		protected void generate() {
			setLayout(new GridBagLayout());

			JPanel panelID = new JPanel(new GridBagLayout());
			panelID.setBorder(StyleConstants.BORDER_EMPTY);
			panelID.setBackground(StyleConstants.COLOR_MEMORY_UNIT_ID_BACKGROUND);
			panelID.setPreferredSize(new Dimension(50, 20));
			panelID.setSize(panelID.getPreferredSize());

			JLabel labelID = new JLabel(InterfaceUtils.padInteger(id));

			labelID.setForeground(StyleConstants.COLOR_MEMORY_UNIT_ID_FOREGROUND);
			panelID.add(labelID, new GridBagConstraints()); // Places label centred in both axes

			JPanel panelOpCode = new JPanel(new GridBagLayout()); // Unfortunately, you can't .clone() swing components, so some duplication here
			panelOpCode.setBorder(StyleConstants.BORDER_EMPTY);
			panelOpCode.setPreferredSize(panelID.getPreferredSize());
			panelOpCode.setSize(panelOpCode.getPreferredSize());

			JLabel labelOpCode = new JLabel("000");
			labelOpCode.setForeground(StyleConstants.COLOR_MEMORY_UNIT_OPCODE_FOREGROUND);
			panelOpCode.add(labelOpCode, new GridBagConstraints());

			GBCBuilder gbcBuilder = new GBCBuilder().setAnchor(GBCBuilder.Anchor.NORTH).setFill(GBCBuilder.Fill.BOTH)
					.setWeight(1, 0.5).setPositionX(0);
			add(panelID, gbcBuilder.setPositionY(0).build());
			add(panelOpCode, gbcBuilder.setPositionY(1).build());

			this.labelOpCode = labelOpCode;
		}

		public void setOpCode(int newOpCode) {
			String oldText = labelOpCode.getText();
			labelOpCode.setText(InterfaceUtils.padInteger(newOpCode));
			if(!Objects.equals(oldText, labelOpCode.getText())){
				playUpdateAnimation();

			}
			labelOpCode.paintImmediately(labelOpCode.getVisibleRect()); // refresh
		}

		private boolean animationPlaying = false;
		public void playUpdateAnimation() { // TODO refactor, also, still buggy.
			Color initColor = StyleConstants.COLOR_MEMORY_UNIT_OPCODE_FOREGROUND;
			Color updateColor = new Color(128, 255, 119);

			if(animationPlaying) return;
			animationPlaying = true;

			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					for(int i = 0; i <= 100; i++) {
						labelOpCode.setForeground(mixColours(initColor, updateColor, (double)i/100));
						Thread.sleep(10);
					}
					return null;
				}
			};
			worker.execute();


			animationPlaying = false;

		}

		private Color mixColours(Color color1, Color color2, double percent){
			double inversePerc = 1.0 - percent;
			int red = (int) (color1.getRed()*percent + color2.getRed()*inversePerc);
			int green = (int) (color1.getGreen()*percent + color2.getGreen()*inversePerc);
			int blue = (int) (color1.getBlue()*percent + color2.getBlue()*inversePerc);
			return new Color(red, green, blue);
		}

	}
}
