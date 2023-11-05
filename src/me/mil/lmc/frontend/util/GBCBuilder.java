package me.mil.lmc.frontend.util;

import java.awt.*;

public class GBCBuilder {
	public enum Anchor {
		CENTRE(10),
		CENTER(10),
		NORTH(11),
		NORTH_EAST(12),
		EAST(13),
		SOUTH_EAST(14),
		SOUTH(15),
		SOUTH_WEST(16),
		WEST(17),
		NORTH_WEST(18),
		PAGE_START(19),
		PAGE_END(20),
		LINE_START(21),
		LINE_END(22),
		FIRST_LINE_START(23),
		FIRST_LINE_END(24),
		LAST_LINE_START(25),
		LAST_LINE_END(26);

		private final int value;
		Anchor(int value) {
			this.value = value;
		}

		private int getValue() {
			return value;
		}
	}

	public enum Fill {
		NONE(0),
		BOTH(1),
		HORIZONTAL(2),
		VERTICAL(3);

		private final int value;
		Fill(int value) {
			this.value = value;
		}

		private int getValue() {
			return value;
		}
	}

	private final GridBagConstraints gridBagConstraints;

	public GBCBuilder() {
		this.gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.gridwidth = 1;
		setFill(Fill.BOTH);
	}

	public GBCBuilder setAnchor(Anchor anchor) {
		gridBagConstraints.anchor = anchor.getValue();
		return this;
	}

	public GBCBuilder setFill(Fill fill) {
		gridBagConstraints.fill = fill.getValue();
		return this;
	}

	public GBCBuilder setWeightX(double weightX) {
		gridBagConstraints.weightx = weightX;
		return this;
	}

	public GBCBuilder setWeightY(double weightY) {
		gridBagConstraints.weighty = weightY;
		return this;
	}

	public GBCBuilder setWeight(double x, double y) {
		setWeightX(x);
		setWeightY(y);
		return this;
	}

	public GBCBuilder setPositionX(int positionX) {
		gridBagConstraints.gridx = positionX;
		return this;
	}

	public GBCBuilder setPositionY(int positionY) {
		gridBagConstraints.gridy = positionY;
		return this;
	}

	public GBCBuilder setPosition(int x, int y) {
		setPositionX(x);
		setPositionY(y);
		return this;
	}

	public GBCBuilder setCellsConsumedX(int cellsConsumedX) {
		gridBagConstraints.gridwidth = cellsConsumedX;
		return this;
	}

	public GBCBuilder setCellsConsumedY(int cellsConsumedY) {
		gridBagConstraints.gridheight = cellsConsumedY;
		return this;
	}

	public GBCBuilder setCellsConsumed(int x, int y) {
		setCellsConsumedX(x);
		setCellsConsumedY(y);
		return this;
	}

	public GBCBuilder setInternalPaddingX(int ipadX) {
		gridBagConstraints.ipadx = ipadX;
		return this;
	}

	public GBCBuilder setInternalPaddingY(int ipadY){
		gridBagConstraints.ipady = ipadY;
		return this;
	}

	public GBCBuilder setInternalPadding(int x, int y) {
		setInternalPaddingX(x);
		setInternalPaddingY(y);
		return this;
	}

	public GridBagConstraints build() {
		return gridBagConstraints;
	}
}
