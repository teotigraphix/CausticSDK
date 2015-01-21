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

package com.teotigraphix.caustk.utils.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUncompress {

    private File sourceFile;

    public ZipUncompress(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * Unzip it
     * 
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    public void unzip(File outputFolder) {

        byte[] buffer = new byte[1024];

        try {
            //create output directory is not exists
            File directory = outputFolder;
            if (!directory.exists()) {
                directory.mkdir();
            }

            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(sourceFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);

                System.out.println("file unzip : " + newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            System.out.println("Done");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Unzips a file containing String data.
     * 
     * @param zipFile The relative path of the zip file to extract a String for.
     */
    public String unzipString(File zipFile) {
        try {
            //get the zip file content
            @SuppressWarnings("resource")
            ZipInputStream zin = new ZipInputStream(new FileInputStream(sourceFile));
            //get the zipped file list entry
            ZipEntry ze = zin.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                if (zipFile.getName().equals(fileName)) {
                    System.out.println("Found it");
                    StringBuilder sb = new StringBuilder();
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        sb.append((char)c);
                    }

                    return sb.toString();
                }

                ze = zin.getNextEntry();
            }

            zin.closeEntry();
            zin.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
