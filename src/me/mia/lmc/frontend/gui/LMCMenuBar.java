package me.mia.lmc.frontend.gui;

import me.mia.lmc.frontend.gui.util.DialogMessageType;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.function.Consumer;

public class LMCMenuBar {
	private enum FileChooserType {
		OPEN,
		SAVE
	}

	protected static JMenuBar generate(AbstractGraphicalInterface lmcInterface) {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(generateFileMenu(lmcInterface));

		return menuBar;
	}

	private static JMenu generateFileMenu(AbstractGraphicalInterface lmcInterface) {
		JMenu menu = new JMenu("File");

		menu.add(generateSaveMenuItem(lmcInterface));
		menu.add(generateLoadMenuItem(lmcInterface));

		return menu;
	}

	private static JMenuItem generateSaveMenuItem(AbstractGraphicalInterface lmcInterface) {
		JMenuItem itemSave = new JMenuItem("Save As");
		itemSave.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));

		itemSave.addActionListener((ignored) -> openFileChooser((file) -> {
			if (!file.getPath().endsWith(".txt")) file = new File(file.getPath() + ".txt");

			try (FileWriter writer = new FileWriter(file)) {
				writer.write(lmcInterface.getInputPanel().getText());
			} catch (IOException e) {
				lmcInterface.showMessageDialog("Oops", "Unable to save file.", DialogMessageType.ERROR_MESSAGE);
			}

		}, FileChooserType.SAVE));

		return itemSave;
	}

	private static JMenuItem generateLoadMenuItem(AbstractGraphicalInterface lmcInterface) {
		JMenuItem itemLoad = new JMenuItem("Load");
		itemLoad.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));

		itemLoad.addActionListener((ignored) -> openFileChooser((file) -> {
			try {
				lmcInterface.getInputPanel().getInputTextArea().setText(String.join("\n", Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)));
			} catch (IOException e) {
				lmcInterface.showMessageDialog("Oops", "Unable to load file.", DialogMessageType.ERROR_MESSAGE);
			}
		}, FileChooserType.OPEN));

		return itemLoad;
	}

	private static void openFileChooser(Consumer<File> action, FileChooserType type) {
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
		jfc.setFileFilter(new FileNameExtensionFilter("Text File", "txt"));
		int returnVal = (type == FileChooserType.SAVE) ? jfc.showSaveDialog(new JFrame()) : jfc.showOpenDialog(new JFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			action.accept(jfc.getSelectedFile());
		}
	}

}
