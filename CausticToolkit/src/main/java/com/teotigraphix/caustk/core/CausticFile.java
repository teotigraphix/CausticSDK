////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class CausticFile {

    private File file;

    public File getFile() {
        return file;
    }

    private String artist;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String linkText;

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    private String linkUrl;

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public CausticFile(File file) {
        this.file = file;

        try {
            readFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readFile(File file) throws FileNotFoundException {
        RandomAccessFile accessFile = new RandomAccessFile(file, "r");

        try {
            long len = accessFile.length();
            accessFile.seek(len - 8);

            byte[] DESC = new byte[4];
            accessFile.read(DESC, 0, 4);
            String desc = new String(DESC);
            if (!desc.equals("DESC"))
                return;

            int size = accessFile.read();

            accessFile.seek(0);
            accessFile.seek(len - size - 8);

            byte[] bytes = new byte[size];
            accessFile.read(bytes, 0, size);

            String s = new String(bytes, Charset.forName("UTF-8"));

            String[] split = s.split("\\|");
            artist = split[0];
            title = split[1];
            description = split[2];
            linkText = split[3];
            linkUrl = split[4];

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (accessFile != null)
                    accessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void save() throws IOException {
        StringBuilder desc = new StringBuilder();
        desc.append(artist);
        desc.append("|");
        desc.append(title);
        desc.append("|");
        desc.append(description);
        desc.append("|");
        desc.append(linkText);
        desc.append("|");
        desc.append(linkUrl);
        desc.append("|");

        String info = desc.toString();

        StringBuilder sb = new StringBuilder();
        //sb.append("DESC");
        //sb.append(info.getBytes().length);
        sb.append(info);
        //sb.append("DESC");

        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        long len = accessFile.length();
        accessFile.seek(len - 1);
        byte[] d = new byte[] {
                68, 69, 83, 67
        };
        accessFile.write(d);
        int infoLen = desc.toString().getBytes().length;
        //byte[] byteArray = BigInteger.valueOf(infoLen).toByteArray();
        byte[] intToByteArray = intToByteArray(infoLen);
        accessFile.write(intToByteArray);
        accessFile.write(desc.toString().getBytes());
        accessFile.write(d);
        accessFile.write(intToByteArray);
        accessFile.close();
    }

    public static final byte[] intToByteArray(int value) {
        return ByteBuffer.allocate(Integer.SIZE / 8).order(ByteOrder.LITTLE_ENDIAN).putInt(value)
                .array();
    }

    /*

    The data is tacked on at the end of the file so it looks like this

    [binary caustic data]
    DESC (4 bytes) <-- Riff tag for the block
    size of the description (4 bytes), excludes the ID and size blocks.
    Artist | Title | Description | link text | link URL |
    DESC (4 bytes) <-- Block tag is repeated to make it possible to quickly 
    detect if a file has a desc.
    size of the description (4 bytes)


    Try adding a descriptor to a file then open it up in a hex editor and scroll 
    to the end of the file, you'll see it's pretty simple.

    All the descriptor fields are separated by "|" characters and each line in 
    the Description part is separated by a line-feed (0x0A) character.

    To find out if a file has a descriptor, seek to the end, go back 8 bytes, 
    read in 4 chars. If it's "DESC" then you read the size right after. Seek 
    back (SIZE + 8) and you're at the start of the actual content.

    */
}
