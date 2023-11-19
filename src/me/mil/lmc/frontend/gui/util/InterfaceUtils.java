package me.mil.lmc.frontend.gui.util;

import com.formdev.flatlaf.FlatDarculaLaf;
import me.mil.lmc.frontend.gui.AbstractGraphicalInterface;
import me.mil.lmc.frontend.gui.LMCGraphicalInterface;

import java.awt.*;
import java.util.Arrays;

public class InterfaceUtils {

	public static AbstractGraphicalInterface createInterface() {
		initLookAndFeel();
		return new LMCGraphicalInterface();
	}

	protected static void initLookAndFeel() {
		FlatDarculaLaf.setup(); // L&F
		StyleConstants.initLFDependentFields();
	}

	public static void addAll(Container container, Component... components) {
		Arrays.stream(components).forEach(container::add);
	}

	public static String padInteger(int input, char ch, int length) {
		return ((input < 0) ? "-" : "") + String.format("%" + ch + length + "d", Math.abs(input));
	}

	public static String padInteger(int input) { // Default values
		return padInteger(input, '0', 3);
	}

}
