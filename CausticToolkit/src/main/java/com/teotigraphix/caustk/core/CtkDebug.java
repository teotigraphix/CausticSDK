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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public final class CtkDebug {

    /**
     * @param level
     * @see Application#LOG_DEBUG
     * @see Application#LOG_ERROR
     * @see Application#LOG_INFO
     * @see Application#LOG_NONE
     */
    public static void setLogLevel(int level) {
        Gdx.app.setLogLevel(level);
    }

    public static void model(String tag, String message) {
        debug(tag, message);
    }

    public static void view(String tag, String message) {
        debug(tag, message);
    }

    public static void osc(String message) {
        debug("OSC", message);
    }

    public static void log(String tag, String message) {
        Gdx.app.log(tag, message);
    }

    public static void log(String tag, String message, Exception exception) {
        Gdx.app.log(tag, message, exception);
    }

    public static void debug(String tag, String message) {
        Gdx.app.debug(tag, message);
    }

    public static void debug(String tag, String message, Throwable throwable) {
        Gdx.app.debug(tag, message, throwable);
    }

    public static void warn(String tag, String message) {
        Gdx.app.error(tag, "WARNING: " + message);
    }

    public static void warn(String tag, String message, Throwable throwable) {
        Gdx.app.error(tag, "WARNING: " + message, throwable);
    }

    public static void err(String tag, String message) {
        Gdx.app.error(tag, message);
    }

    public static void err(String tag, String message, Throwable throwable) {
        Gdx.app.error(tag, message, throwable);
    }
}
