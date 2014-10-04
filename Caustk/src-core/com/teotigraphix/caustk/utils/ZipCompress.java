////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

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
        //        Collection<File> listFiles = FileUtils.listFiles(sourceDirectory, new IOFileFilter() {
        //            @Override
        //            public boolean accept(File arg0, String arg1) {
        //                return false;
        //            }
        //
        //            @Override
        //            public boolean accept(File arg0) {
        //                return false;
        //            }
        //        }, new IOFileFilter() {
        //            @Override
        //            public boolean accept(File arg0, String arg1) {
        //                return true;
        //            }
        //
        //            @Override
        //            public boolean accept(File arg0) {
        //                return true;
        //            }
        //        });
        //
        //        for (File file : listFiles) {
        //            fileList.add(generateZipEntry(file.getAbsolutePath()));
        //        }
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
            fileList.add(generateZipEntry(node.getAbsolutePath()));
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
        return file.substring(sourceDirectory.getAbsoluteFile().getAbsolutePath().length() + 1,
                file.length());
    }
}
