package me.mil.lmc;

import me.mil.lmc.backend.Program;
import me.mil.lmc.backend.exceptions.LMCException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LMC {

	public static void main(String[] args) throws FileNotFoundException, LMCException {
		while(true) {
			System.out.println("Provide the directory of a LMC program (.txt) to execute.");
			Scanner scanner = new Scanner(System.in);

			Scanner fileScanner = new Scanner(new File(scanner.nextLine()));
			StringBuilder str = new StringBuilder();
			while(fileScanner.hasNext()) str.append(fileScanner.nextLine()).append("\n");

			Program program = Program.loadFromString(str.toString());
			program.run();

			System.out.println("\n");
		}



	}

}
