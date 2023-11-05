package me.mil.lmc.frontend.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StyleUtil {

	public static Font FONT_LARGE() { return UIManager.getFont("large.font"); }

	public static final Color COLOR_REGISTER_VIEW_BACKGROUND = new Color(44, 45, 47);
	public static final Color COLOR_MEMORY_VIEW_BACKGROUND = new Color(30, 31, 34);

	public static final Color COLOR_MEMORY_UNIT_ID_BACKGROUND = new Color(44, 45, 47);
	public static final Color COLOR_MEMORY_UNIT_ID_FOREGROUND = new Color(150, 150, 150);

	public static final Color COLOR_MEMORY_UNIT_OPCODE_FOREGROUND = new Color(195, 195, 195);

	public static final Border BORDER_EMPTY = new EmptyBorder(0, 0, 0, 0);

}
