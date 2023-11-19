package me.mil.lmc.frontend.shell;

import me.mil.lmc.LMCReader;

import java.util.Scanner;

public class ShellReader implements LMCReader {
	private final Scanner scanner;

	public ShellReader() {
		scanner = new Scanner(System.in);
	}

	@Override
	public int nextInt() {
		return nextInt("Input: ");
	}

	private int nextInt(String prompt) {
		try {
			System.out.print(prompt);
			return Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException ignored) {
			return nextInt("Please enter an integer: ");
		}
	}
}
