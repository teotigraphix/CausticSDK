
package com.teotigraphix.caustk.service;

import java.io.File;

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
    
    String toString(Object serialized);
}
