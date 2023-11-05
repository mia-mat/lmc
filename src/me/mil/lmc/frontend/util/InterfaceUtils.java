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
	}

	public static void addAll(Container container, Component... components) {
		Arrays.stream(components).forEach(container::add);
	}

	public static String padInteger(int input, char ch, int length) {
		return String.format("%" + ch + length + "d", input);
	}

}
