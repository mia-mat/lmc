package me.mil.lmc.frontend.util;

import com.formdev.flatlaf.FlatDarculaLaf;
import me.mil.lmc.frontend.LMCInterface;

import java.awt.*;
import java.util.Arrays;

public class InterfaceUtils {

	public static LMCInterface createInterface() {
		initLookAndFeel();
		return new LMCInterface();
	}

	protected static void initLookAndFeel() {
		FlatDarculaLaf.setup(); // L&F
		StyleConstants.initLFDependentFields();
	}

	public static void addAll(Container container, Component... components) {
		Arrays.stream(components).forEach(container::add);
	}

	public static String padInteger(int input, char ch, int length) {
		return ((input<0) ? "-" : "") + String.format("%" + ch + length + "d", Math.abs(input)); // TODO fix bug wherein negative nums are cut off in RAM Units
	}

	public static String padInteger(int input) { // Default values
		return padInteger(input, '0', 3);
	}

}
