import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		for (File folder : new File(System.getenv("HAXEPATH") + "/lib/").listFiles()) { // Read all folders in the
																						// haxelib folder.
			if (folder.isDirectory()) { // Make sure that the found folder is actually a folder.
				try {
					String folderName = folder.getName();
					String libPath = "C:/HaxeToolkit/haxe/lib/" + folderName;
					File currentVersionFile = new File(libPath + "/.current");
					if (new File(libPath + "/.dev").exists()) {
						System.out.println("\u001B[33mWARNING: Development library deteched. Skipping.\u001B[0m");
					} else {
						Scanner currentVersionRaw = new Scanner(currentVersionFile);
						String currentVersion = currentVersionRaw.nextLine().replace('.', ',');
						currentVersionRaw.close(); // Close scanner since we don't need it anymore.
						String formattedFolder = folderName.replace(',', '.');
						System.out.println("Checking " + formattedFolder);
						for (File libVersion : new File(libPath).listFiles()) { // Read all the versions of the current
																				// library.
							if (libVersion.isDirectory()
									&& !libVersion.getName().contains(currentVersion)) {
								String strippedVersion = libVersion.getName().replace(libPath, "");
								String formattedVersion = strippedVersion.replace(',', '.');
								System.out.println("\nFound unused version: " + formattedVersion);
								System.out.println("Would you like to remove it? \u001B[32my\u001B[0m/\u001B[31mn\u001B[0m"); // y\n with color codes
								if (System.console().readLine().toLowerCase().equals("y")) {
									runCommand("haxelib", "remove", formattedFolder, formattedVersion);
								} else {
									System.out.println("Skipping.");
								}
							}
						}
					}
				} catch (FileNotFoundException e) { // If an error happens
					System.out
							.println("\u001B[31mERROR: Could not find library "+ folder.getName().replace(',', '.') +" or an unknown error occured. Skipping");
					e.printStackTrace();
					System.out.println("\u001B[0m");
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
