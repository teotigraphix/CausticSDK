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

package com.teotigraphix.caustic.desktop;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*

 Compress compress = new Compress(files, zipLocation);
 compress.zip();

 */

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Compress {
    private static final int BUFFER = 2048;

    private Collection<File> _files;

    private String _zipFile;

    public Compress(Collection<File> files, String zipFile) {
        _files = files;
        _zipFile = zipFile;
    }

    public void zip() {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(_zipFile);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            for (File file : _files) {

                //XXX Log.v("Compress", "Adding: " + file.getAbsolutePath());
                FileInputStream fi = new FileInputStream(file.getAbsolutePath());
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(file.getAbsolutePath().substring(
                        file.getAbsolutePath().lastIndexOf(File.separator) + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
