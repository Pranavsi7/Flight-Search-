package com.searchflights.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
/**
 * Utility class for basic file operations.
 * <p>
 * Provides helper methods to read from and write to files using standard
 * {@code java.nio.file} APIs.
 * </p>
 *
 * <p>
 * Supported operations:
 * <ul>
 *     <li>Read file content as a {@link String}</li>
 *     <li>Write a {@link String} content to a file</li>
 * </ul>
 * </p>
 *
 * <p>
 * All file paths are expected to be valid and accessible on the file system.
 * Exceptions may be thrown if the file is not found or accessible.
 * </p>
 *
 * @author Pranav Singh
 */

public class FileUtil {
    public static String readFile(String path) throws IOException {
        return Files.readString(Paths.get(path));
    }

    public static void writeFile(String path, String content) throws IOException {
        Files.writeString(Paths.get(path), content);
    }
}
