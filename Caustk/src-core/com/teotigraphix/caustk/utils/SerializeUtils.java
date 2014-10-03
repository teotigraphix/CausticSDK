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

package com.teotigraphix.caustk.utils;

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustk.core.CaustkRuntime;

public class SerializeUtils {

    public static <T> void pack(File file, T instance) throws IOException {
        CaustkRuntime.getInstance().getFactory().getRuntime().getRack().getSerializer()
                .serialize(file, instance);
    }

    public static <T> T unpack(File file, Class<T> clazz) throws IOException {
        return clazz.cast(CaustkRuntime.getInstance().getFactory().getRuntime().getRack()
                .getSerializer().deserialize(file, clazz));
    }

    //    public static <T> T unpack(String json, Class<?> clazz) throws CausticException {
    //        return CaustkRuntime.getInstance().getFactory().deserialize(json, clazz);
    //    }

}
