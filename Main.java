import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		for (File folder : new File(System.getenv("HAXEPATH") + "/lib/").listFiles()) {
			if (folder.isDirectory()) {
				try {
					String libPath = "C:/HaxeToolkit/haxe/lib/" + folder.getName();
					Scanner s = new Scanner(new File(libPath + "/.current"));
					String currentVersion = s.nextLine();
					String formattedFolder = folder.getName().replace(',', '.');
					System.out.println("Checking " + formattedFolder);
					for (File libVersion : new File(libPath).listFiles()) {
						if (libVersion.isDirectory()) {
							if (!libVersion.getName().contains(currentVersion.replace('.', ','))) {
								String strippedVersion = libVersion.getName().replace(libPath, "");
								String formattedVersion = strippedVersion.replace(',', '.');
								System.out
										.println("\nFound unused version: " + formattedVersion);
								System.out.println("Would you like to remove it? y/n");
								if (System.console().readLine().toLowerCase().equals("y")) {
									runCommand("haxelib", "remove", formattedFolder, formattedVersion);
								} else {
									System.out.println("Skipping.");
								}

							}
						}
					}
					s.close();
				} catch (FileNotFoundException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Runs a system command 
	 * I got this off a random website lol
	 * 
	 * @see https://javapointers.com/java/java-core/how-to-run-a-command-using-java-in-linux-or-windows/
	 * @param command The command to run
	 */
	public static void runCommand(String... command) {
		ProcessBuilder processBuilder = new ProcessBuilder().command(command);

		try {
			Process process = processBuilder.start();

			// read the output
			InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String output = null;
			while ((output = bufferedReader.readLine()) != null) {
				System.out.println(output);
			}

			// wait for the process to complete
			process.waitFor();

			// close the resources
			bufferedReader.close();
			process.destroy();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
