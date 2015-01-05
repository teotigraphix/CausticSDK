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

package com.teotigraphix.caustk.groove.library;

public enum LibraryItemFormat {

    Product("gprd"),

    Project("gprj"),

    PatternBank("gptbk"),

    Group("ggrp"),

    Sound("gsnd"),

    Instrument("ginst"),

    Effect("gfx"),

    Sample("wav");

    private String extension;

    public String getExtension() {
        return extension;
    }

    LibraryItemFormat(String extension) {
        this.extension = extension;
    }

    public static LibraryItemFormat fromString(String extension) {
        for (LibraryItemFormat format : values()) {
            if (format.getExtension().equals(extension))
                return format;
        }
        return null;
    }
}
