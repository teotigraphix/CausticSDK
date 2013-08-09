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

package com.teotigraphix.caustk.service;

import java.io.File;
import java.io.IOException;

public interface ISerializeService {

    /**
     * Returns a new instance of the {@link File} content based on the Type
     * passed.
     * 
     * @param file
     * @param classOfT
     * @return
     */
    <T> T fromFile(File file, Class<T> classOfT);

    <T> T fromString(String data, Class<T> classOfT);
    
    <T> T copy(Object data, Class<T> classOfT);

    //String toString(Object serialized);

    String toPrettyString(Object serialized);

    String toString(Object serialized);

    void save(File target, Object serialized) throws IOException;
}
