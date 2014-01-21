
package com.teotigraphix.caustk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompress {

    private List<String> fileList;

    private File sourceDirectory;

    /**
     * Compresses a directory and extra files into a zip archive.
     * 
     * @param sourceDirectory The source directory to recursivly travers to find
     *            file.
     */
    public ZipCompress(File sourceDirectory) {
        this.sourceDirectory = sourceDirectory;

        fileList = new ArrayList<String>();
        generateFileList(sourceDirectory);
    }

    /**
     * Zip it
     * 
     * @param zipFile output ZIP file location
     */
    public void zip(File zipFile) {

        byte[] buffer = new byte[1024];

        try {

            zipFile.getParentFile().mkdirs();

            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + zipFile);

            for (String file : fileList) {

                System.out.println("File Added : " + file);
                ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);

                FileInputStream in = new FileInputStream(new File(sourceDirectory, file));

                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }

                in.close();
            }

            zos.closeEntry();
            //remember close it
            zos.close();

            System.out.println("Done");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Traverse a directory and get all files, and add the file into fileList
     * 
     * @param node file or directory
     */
    public void generateFileList(File node) {

        //add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote) {
                generateFileList(new File(node, filename));
            }
        }

    }

    /**
     * Format the file path for zip
     * 
     * @param file file path
     * @return Formatted file path
     */
    private String generateZipEntry(String file) {
        return file.substring(sourceDirectory.toString().length() + 1, file.length());
    }
}
