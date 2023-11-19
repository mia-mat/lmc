package me.mil.lmc.frontend.shell;

import me.mil.lmc.LMCReader;
import me.mil.lmc.LMCWriter;
import me.mil.lmc.backend.LMCProcessor;
import me.mil.lmc.backend.Processor;
import me.mil.lmc.backend.exceptions.LMCCompilationException;
import me.mil.lmc.backend.exceptions.LMCException;
import me.mil.lmc.backend.exceptions.LMCRuntimeException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.Scanner;

// Minimal shell interface
public class ShellInterface {
	public static final int DEFAULT_CLOCK_SPEED = 0;
	public static final int DEFAULT_MEMORY_SIZE = 100;

	private static final Scanner scanner = new Scanner(System.in);

	private final LMCReader reader;
	private final LMCWriter writer;

	Processor processor;

	private final int clockSpeed;
	private final int memorySize;

	public static ShellInterface create() throws IOException {
		return create(DEFAULT_CLOCK_SPEED, DEFAULT_MEMORY_SIZE);
	}

	public static ShellInterface create(int clockSpeed, int memorySize) throws IOException {
		return new ShellInterface(new ShellReader(), new ShellWriter(), clockSpeed, memorySize);
	}


	private ShellInterface(LMCReader reader, LMCWriter writer, int clockSpeed, int memorySize) throws IOException {
		this.reader = reader;
		this.writer = writer;

		this.clockSpeed = clockSpeed;
		this.memorySize = memorySize;

		init();
	}

	private void handleException(LMCException e) throws IOException {
		final String RED = "\033[0;31m";
		final String RESET = "\033[0m";
		if (e instanceof LMCCompilationException) {
			System.out.println(RED + e.getMessage());
		}
		if (e instanceof LMCRuntimeException) {
			System.out.println(RED + e.getMessage());
		}

		System.out.println("Restarting..." + RESET);
		create();
	}

	private void loadFromFile(File file) throws IOException {
		try {
			setProcessor(LMCProcessor.compileIntoProcessor(
					String.join("\n", Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)),
					memorySize, clockSpeed, getReader(), getWriter()));
			System.out.println("Loading instructions into memory...");
			getProcessor().loadInstructionsIntoMemory();
			System.out.println("Executing program...");
			getProcessor().run();
			while (getProcessor().isRunning()) {
				Thread.sleep(10);
			}
			System.out.println("Finished executing program.");
			if (processor instanceof LMCProcessor) {
				System.out.println("Instruction cycle count: " + ((LMCProcessor) processor).getInstructionCycleCount());
			}
			System.out.println();


		} catch (MalformedInputException ignored) {
			promptLoad("(Valid) Path of program: ");
		} catch (AccessDeniedException ignored) {
			System.out.println("File Access Denied.");
			promptLoad("(Valid and Accessible) Path of program: ");
		} catch (LMCException e) {
			handleException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	private void promptLoad() throws IOException {
		promptLoad("Path of program: ");
	}

	private void promptLoad(String prompt) throws IOException {
		System.out.print(prompt);
		File programFile = new File(scanner.nextLine());
		if (!programFile.exists() || programFile.isDirectory()) {
			promptLoad("(Valid) Path of program: ");
			return;
		}

		loadFromFile(programFile);
	}

	private void init() throws IOException {
		System.out.println("Initializing minimal interface...");
		System.out.println("Clock Speed = " + clockSpeed);
		System.out.println("Memory Size = " + memorySize);
		System.out.println();

		while (true) {
			promptLoad();
		}
	}

	public LMCReader getReader() {
		return reader;
	}

	public LMCWriter getWriter() {
		return writer;
	}

	public Processor getProcessor() {
		return processor;
	}

	private void setProcessor(Processor processor) {
		this.processor = processor;
	}
}
