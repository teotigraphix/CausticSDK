////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public final class ZipUtils {

    public static String readZipString(File archiveFile, File fileToRead) {
        ZipUncompress uncompress = new ZipUncompress(archiveFile);
        return uncompress.unzipString(fileToRead);
    }

    public static void writeZipEntryToFile(File archiveFile, String entryName, File outputFile)
            throws IOException {
        FileInputStream fis = new FileInputStream(archiveFile);
        ZipInputStream zis = new ZipInputStream(fis);

        ZipEntry entry = null;
        while ((entry = zis.getNextEntry()) != null) {
            System.out.println("Reading " + entry.getName());
            if (entry.getName().equals(entryName)) {
                FileOutputStream outputfile = new FileOutputStream(outputFile);
                int data = 0;
                while ((data = zis.read()) != -1) {
                    outputfile.write(data);
                }
                outputfile.close();
                break;
            }
        }

        zis.close();
        fis.close();
    }

    public static final void readZip(InputStream is) throws IOException {

        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
        try {
            @SuppressWarnings("unused")
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;
                while ((count = zis.read(buffer)) != -1) {
                    baos.write(buffer, 0, count);
                }
                // String filename = ze.getName();
                // byte[] bytes = baos.toByteArray();
                // do something with 'filename' and 'bytes'...
            }
        } finally {
            zis.close();
        }

    }

}
