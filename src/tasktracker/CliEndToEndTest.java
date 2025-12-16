package tasktracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Executes the real CLI (tasktracker.Main) in a temporary directory and asserts the output.
 * Run with: {@code java -cp out tasktracker.CliEndToEndTest}
 */
public class CliEndToEndTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        Path tempDir = Files.createTempDirectory("tasktracker-cli-test");
        try {
            CliEndToEndTest test = new CliEndToEndTest(tempDir);
            test.runFullScenario();
            System.out.println("CLI end-to-end test passed.");
        } finally {
            deleteRecursively(tempDir);
        }
    }

    private final Path workingDir;
    private final String classpath;

    private CliEndToEndTest(Path workingDir) {
        this.workingDir = workingDir;
        this.classpath = resolveClasspath();
    }

    private void runFullScenario() throws IOException, InterruptedException {
        String addOutput = runCli("add", "End-to-end task");
        requireContains(addOutput, "Created task with id 1");

        String listOutput = runCli("list");
        requireContains(listOutput, "[1] (TODO) End-to-end task");

        String inProgressOutput = runCli("mark-in-progress", "1");
        requireContains(inProgressOutput, "Task 1 is now IN_PROGRESS");

        String inProgressList = runCli("list", "in-progress");
        requireContains(inProgressList, "[1] (IN_PROGRESS) End-to-end task");

        String doneOutput = runCli("mark-done", "1");
        requireContains(doneOutput, "Task 1 is now DONE");

        String deleteOutput = runCli("delete", "1");
        requireContains(deleteOutput, "Deleted task 1");

        String emptyListOutput = runCli("list");
        requireContains(emptyListOutput, "No tasks.");
    }

    private String runCli(String... args) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(classpath);
        command.add("tasktracker.Main");
        command.addAll(List.of(args));

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir.toFile());
        Process process = builder.start();
        String stdout = readStream(process.inputReader());
        String stderr = readStream(process.errorReader());
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("CLI exited with " + exitCode + "\nSTDOUT:\n" + stdout + "\nSTDERR:\n" + stderr);
        }
        return stdout;
    }

    private static String readStream(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    private static void requireContains(String actual, String expectedFragment) {
        if (!actual.contains(expectedFragment)) {
            throw new IllegalStateException("Expected output to contain '" + expectedFragment + "' but was:\n" + actual);
        }
    }

    private static void deleteRecursively(Path root) throws IOException {
        if (!Files.exists(root)) {
            return;
        }
        Files.walk(root)
                .sorted((a, b) -> b.compareTo(a)) // delete children first
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to delete " + path, e);
                    }
                });
    }

    private String resolveClasspath() {
        String raw = System.getProperty("java.class.path");
        if (raw == null || raw.isEmpty()) {
            throw new IllegalStateException("java.class.path is empty");
        }
        String separator = File.pathSeparator;
        return Arrays.stream(raw.split(separator))
                .map(entry -> {
                    if (entry.isEmpty()) {
                        return entry;
                    }
                    Path path = Path.of(entry);
                    if (path.isAbsolute()) {
                        return path.toString();
                    }
                    return path.toAbsolutePath().toString();
                })
                .collect(Collectors.joining(separator));
    }
}
