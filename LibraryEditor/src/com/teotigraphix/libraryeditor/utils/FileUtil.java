
package com.teotigraphix.libraryeditor.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class FileUtil {

    /**
     * The character set. UTF-8 works good for windows, mac and Umlaute.
     */
    private static final Charset CHARSET = Charset.forName("UTF-8");

    /**
     * Reads the specified file and returns the content as a String.
     * 
     * @param file
     * @return
     * @throws IOException thrown if an I/O error occurs opening the file
     */
    public static String readFile(File file) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();

        BufferedReader reader = Files.newBufferedReader(file.toPath(), CHARSET);

        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }

        reader.close();

        return stringBuffer.toString();
    }

    /**
     * Saves the content String to the specified file.
     * 
     * @param content
     * @param file
     * @throws IOException thrown if an I/O error occurs opening or creating the
     *             file
     */
    public static void saveFile(String content, File file) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(file.toPath(), CHARSET);
        writer.write(content, 0, content.length());
        writer.close();
    }

    public static DirectoryChooser createDefaultDirectoryChooser(String initialPath) {
        DirectoryChooser chooser = new DirectoryChooser();
        File directory = getParentOrUserDirectory(initialPath);
        chooser.setInitialDirectory(directory);
        return chooser;
    }

    /**
     * @param initialPath
     * @param display IE (XML files (*.xml))
     * @param extension IE (*.xml)
     * @return
     */
    public static FileChooser createDefaultFileChooser(String initialPath, String display,
            String extension) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(display, extension);
        fileChooser.getExtensionFilters().add(extFilter);

        File directory = getParentOrUserDirectory(initialPath);
        fileChooser.setInitialDirectory(directory);
        return fileChooser;
    }

    public static FileChooser createDefaultPersonFileChooser(String initialPath) {

        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File directory = getParentOrUserDirectory(initialPath);
        fileChooser.setInitialDirectory(directory);
        return fileChooser;
    }

    /**
     * Returns the parent directory of the path OR the user's home directory.
     * 
     * @param path The initial path to construct a parent path.
     */
    public static File getParentOrUserDirectory(String path) {
        if (path != null) {
            path = new File(path).getParent();
        }
        if (path == null) {
            path = System.getProperty("user.home");
        }
        File directory = new File(path);
        return directory;
    }
}
