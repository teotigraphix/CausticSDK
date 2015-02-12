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

/**
 * Wraps a .caustic {@link java.io.File} and can read or write metadata to the
 * .caustic file.
 * 
 * @author Michael Schmalle
 */
public class CausticFile {

    private static final String DESC_TAG = "DESC";

    private File file;

    private String artist;

    private String title;

    private String description;

    private String linkText;

    private String linkUrl;

    private boolean hasDescription = false;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // file
    //----------------------------------

    public File getFile() {
        return file;
    }

    //----------------------------------
    // artist
    //----------------------------------

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    //----------------------------------
    // title
    //----------------------------------

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //----------------------------------
    // description
    //----------------------------------

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //----------------------------------
    // linkText
    //----------------------------------

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    //----------------------------------
    // linkUrl
    //----------------------------------

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    /**
     * Returns whether the native <code>.caustic</code> file exists and is not
     * <code>null</code>.
     */
    public boolean isLoaded() {
        return file != null && file.exists();
    }

    /**
     * Returns whether the native file exists and that the metadata is loaded.
     * <p>
     * The {@link #read()} method must be called before this returns true.
     */
    public boolean hasMetadata() {
        return isLoaded() && hasDescription;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Must call {@link #read()}.
     * 
     * @param file
     */
    public CausticFile(File file) {
        this.file = file;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Reads a .caustic file and extracts the metadata descriptor information.
     * 
     * @throws java.io.FileNotFoundException
     */
    public void read() throws FileNotFoundException {
        readFile(file);
    }

    /**
     * Writes a .caustic file and attaches the metadata descriptor information
     * to it.
     * 
     * @throws java.io.IOException
     */
    public void write() throws IOException {
        if (hasDescription) {
            // if this contains a description, trim() before saving
            internalTrim();
        }
        writeFile();
    }

    public void trim() throws IOException {
        internalTrim();
        hasDescription = false;
    }

    private void internalTrim() throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        long size = getCausticDataLength(accessFile);
        long length = accessFile.length();
        long newSize = length - size;
        accessFile.getChannel().truncate(newSize);
        accessFile.close();
    }

    private static long getCausticDataLength(RandomAccessFile file) throws IOException {
        long fileLength = file.length();
        file.seek(fileLength - 4);
        int contentLength = reverse(file.readInt());
        return 8 + contentLength + 8;
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    public static int reverse(int x) {
        return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(x)
                .order(ByteOrder.LITTLE_ENDIAN).getInt(0);
    }

    private void readFile(File file) throws FileNotFoundException {
        RandomAccessFile accessFile = new RandomAccessFile(file, "r");

        try {
            // 4 bytes for DESC + 4 bytes for meta length
            final int footerLength = 8;
            // File size
            long fileLength = accessFile.length();

            accessFile.seek(fileLength - footerLength);

            // Check fo the DESC tag
            byte[] desc = new byte[4];
            accessFile.read(desc, 0, 4);

            hasDescription = new String(desc).equals(DESC_TAG);
            if (!hasDescription)
                return;

            int metadataLength = reverse(accessFile.readInt());

            accessFile.seek(0);
            accessFile.seek(fileLength - metadataLength - footerLength);

            byte[] bytes = new byte[metadataLength];
            accessFile.read(bytes, 0, metadataLength);

            String s = new String(bytes, Charset.forName("UTF-8"));

            String[] split = s.split("\\|");
            final int slen = split.length;
            artist = split[0];
            title = split[1];
            description = split[2];
            if (slen > 3)
                linkText = split[3];
            if (slen > 4)
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

    private void writeFile() throws IOException {
        final StringBuilder metadata = new StringBuilder();
        metadata.append(artist);
        metadata.append("|");
        metadata.append(title);
        metadata.append("|");
        metadata.append(description);
        metadata.append("|");
        metadata.append(linkText);
        metadata.append("|");
        metadata.append(linkUrl);
        metadata.append("|");

        final RandomAccessFile raf = new RandomAccessFile(file, "rw");
        long len = raf.length();
        raf.seek(len);
        byte[] DESC = new byte[] {
                68, 69, 83, 67
        };

        byte[] bytes = metadata.toString().getBytes();
        int contentLength = bytes.length;
        int length = 8 + contentLength + 8;

        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(DESC);// 4
        buffer.putInt(contentLength);
        buffer.put(bytes);
        buffer.put(DESC);
        buffer.putInt(contentLength);

        byte[] array = buffer.array();

        raf.write(array);
        raf.close();

        hasDescription = true;
    }

    static final byte[] intToByteArray(int value) {
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
