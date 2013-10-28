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

package com.teotigraphix.caustk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author Michael Schmalle
 */
public class KryoUtils {

    private static Kryo kryo;

    static {
        kryo = new Kryo();
    }

    public static <T> T readFileObject(File file, Class<T> clazz) throws FileNotFoundException {
        T state = null;
        try {
            Input input = new Input(new FileInputStream(file));
            state = kryo.readObject(input, clazz);
            input.close();
        } catch (FileNotFoundException e) {
            throw e;
        }
        return state;
    }

    public static void writeFileObject(File file, Object state) throws FileNotFoundException {
        Output output = new Output(new FileOutputStream(file));
        kryo.writeObject(output, state);
        output.close();
    }

}
