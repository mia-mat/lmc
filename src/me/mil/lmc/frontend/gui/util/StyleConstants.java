package me.mil.lmc.frontend.gui.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StyleConstants {

	public static Font FONT_LARGE;
	public static Font FONT_MONOSPACED;
	public static Font FONT_H4;

	public static final Color COLOR_REGISTER_VIEW_BACKGROUND = new Color(44, 45, 47);
	public static final Color COLOR_MEMORY_VIEW_BACKGROUND = new Color(30, 31, 34);
	public static final Color COLOR_CONTROL_PANEL_UNRESTRICTED_CLOCK_SPEED_FOREGROUND = new Color(128, 255, 119);
	public static final Color COLOR_CONTROL_PANEL_UNRESTRICTED_CLOCK_SPEED_FOREGROUND_B = new Color(255, 119, 119);

	public static final Color COLOR_MEMORY_UNIT_ID_BACKGROUND = new Color(44, 45, 47);
	public static final Color COLOR_MEMORY_UNIT_ID_FOREGROUND = new Color(150, 150, 150);

	public static final Color COLOR_MEMORY_UNIT_OPCODE_FOREGROUND = new Color(195, 195, 195);

	public static final Border BORDER_EMPTY = new EmptyBorder(0, 0, 0, 0);

	protected static void initLFDependentFields() {
		FONT_LARGE = UIManager.getFont("large.font");
		FONT_H4 = UIManager.getFont("h4.font");
		FONT_MONOSPACED = new Font("Consolas", Font.PLAIN, 16);
	}

}
