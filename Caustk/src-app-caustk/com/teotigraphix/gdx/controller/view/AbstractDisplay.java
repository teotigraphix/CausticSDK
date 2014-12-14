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

package com.teotigraphix.gdx.controller.view;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public abstract class AbstractDisplay {

    public static final int FORMAT_RAW = 0;

    public static final int FORMAT_VALUE = 1;

    public static final int FORMAT_PAN = 2;

    protected IDisplayOutput output;

    private int numLines;

    private int numBlocks;

    protected int numCells;

    protected int numChars;

    private String emptyLine;

    private String[] currentMessage;

    private String[] message;

    protected String[] cells;

    private String notificationMessage;

    private boolean isNotificationActive;

    private Task task;

    public void setOuput(IDisplayOutput output) {
        this.output = output;
    }

    public AbstractDisplay(IDisplayOutput output, int numLines, int numBlocks, int numCells,
            int numChars) {
        this.output = output;
        this.numLines = numLines;
        this.numBlocks = numBlocks;
        this.numCells = numCells;
        this.numChars = numChars;

        this.emptyLine = "";
        for (int i = 0; i < numChars; i++)
            this.emptyLine += " ";
        this.notificationMessage = this.emptyLine;
        this.isNotificationActive = false;

        this.currentMessage = initArray(null, this.numLines);
        this.message = initArray(null, this.numLines);
        this.cells = initArray(null, this.numLines * this.numCells);
    }

    public abstract AbstractDisplay clearCell(int row, int cell);

    public abstract AbstractDisplay setBlock(int row, int block, String value);

    public abstract AbstractDisplay setCell(int row, int cell, Object value, int format);

    public abstract AbstractDisplay setCell(int row, int cell, Object value);

    public abstract AbstractDisplay writeLine(int row, String text);

    //

    // Displays a notification message on the display for 3 seconds
    public void showNotification(String message, boolean centered) {
        showNotification(message, 3f, centered);
    }

    public void showNotification(String message, float delay, boolean centered) {
        String padding = emptyLine.substring(0, Math.round((numChars - message.length()) / 2));
        String temp = message;
        if (centered)
            temp = padding + message + padding;
        if (temp.length() > numChars) {
            temp = temp.substring(0, numChars);
        }
        notificationMessage = temp; //(padding + message + padding).substring(0, numChars);

        if (task != null) {
            task.cancel();
            isNotificationActive = false;
        } else {
            task = new Task() {
                @Override
                public void run() {
                    isNotificationActive = false;
                    forceFlush();
                }
            };
        }

        if (!isNotificationActive) {
            isNotificationActive = true;
            Timer.schedule(task, delay);
        }
    }

    public AbstractDisplay clear() {
        for (int i = 0; i < numLines; i++)
            this.clearRow(i);
        return this;
    }

    public AbstractDisplay clearRow(int row) {
        for (int i = 0; i < numBlocks; i++)
            this.clearBlock(row, i);
        return this;
    }

    public AbstractDisplay clearBlock(int row, int block) {
        int cell = 2 * block;
        this.clearCell(row, cell);
        this.clearCell(row, cell + 1);
        return this;
    }

    public AbstractDisplay clearColumn(int column) {
        for (int i = 0; i < numLines; i++)
            clearBlock(i, column);
        return this;
    }

    public AbstractDisplay setRow(int row, String str) {
        message[row] = str;
        return this;
    }

    public AbstractDisplay done(int row) {
        int index = row * numCells;
        message[row] = "";
        for (int i = 0; i < numCells; i++)
            message[row] += cells[index + i] == null ? "" : cells[index + i];
        return this;
    }

    public AbstractDisplay allDone() {
        for (int row = 0; row < numLines; row++)
            this.done(row);
        return this;
    }

    public void forceFlush() {
        for (int row = 0; row < numLines; row++)
            this.currentMessage[row] = "";
    }

    public void flush() {
        if (this.isNotificationActive) {
            this.writeLine(0, this.notificationMessage);
            for (int row = 1; row < this.numLines; row++)
                this.writeLine(row, this.emptyLine);
            return;
        }

        for (int row = 0; row < this.numLines; row++) {
            // Has anything changed?
            if (this.currentMessage[row] == this.message[row])
                continue;
            this.currentMessage[row] = this.message[row];
            if (this.currentMessage[row] != null)
                this.writeLine(row, this.currentMessage[row]);
        }
    }

    //    public void reverseStr(String str) {
    //        String r = "";
    //        for (int i = 0; i < str.length; i++)
    //            r = str[i] + r;
    //        return r;
    //    }

    private static String[] initArray(String object, int length) {
        String[] result = new String[length];
        for (int i = 0; i < length; i++) {
            result[i] = object;
        }
        return result;
    }

}
