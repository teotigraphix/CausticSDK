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

package com.teotigraphix.caustk.controller.core;

import com.teotigraphix.caustk.controller.ICausticLogger;

/**
 * Default implementation of the {@link ICausticLogger}.
 */
public class CaustkLogger implements ICausticLogger {

    @Override
    public void setLogLevel(int level) {
    }

    @Override
    public void model(String tag, String message) {
        debug(tag, message);
    }

    @Override
    public void view(String tag, String message) {
        debug(tag, message);
    }

    @Override
    public void osc(String message) {
        debug("OSC", message);
    }

    @Override
    public void log(String tag, String message) {
        System.out.println(tag + ", " + message);
    }

    @Override
    public void log(String tag, String message, Exception exception) {
        System.out.println(tag + ", " + message);
        exception.printStackTrace();
    }

    @Override
    public void debug(String tag, String message) {
        System.out.println(tag + ", " + message);
    }

    @Override
    public void debug(String tag, String message, Throwable throwable) {
        System.out.println(tag + ", " + message);
        throwable.printStackTrace();
    }

    @Override
    public void warn(String tag, String message) {
        System.err.println("WARNING:" + tag + ", " + message);
    }

    @Override
    public void warn(String tag, String message, Throwable throwable) {
        System.err.println("WARNING:" + tag + ", " + message);
        throwable.printStackTrace();
    }

    @Override
    public void err(String tag, String message) {
        System.err.println(tag + ", " + message);
    }

    @Override
    public void err(String tag, String message, Throwable throwable) {
        System.err.println(tag + ", " + message);
        throwable.printStackTrace();
    }

}
