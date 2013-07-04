
package com.teotigraphix.caustk.utls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.teotigraphix.caustk.controller.ISerialize;

public class JsonUtils {

    public static String toGson(Object serialized, boolean prettyPrint) {
        GsonBuilder builder = new GsonBuilder();
        if (prettyPrint) {
            builder.setPrettyPrinting();
        }
        Gson gson = builder.create();
        if (serialized instanceof ISerialize)
            ((ISerialize)serialized).sleep();
        return gson.toJson(serialized);
    }

    public static <T> T fromGson(File file, Class<T> classOfT) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        T result = null;
        try {
            result = gson.fromJson(new FileReader(file), classOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (result instanceof ISerialize)
            ((ISerialize)result).wakeup();
        return result;
    }

    public static <T> T fromGson(String data, Class<T> classOfT) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        T result = null;
        try {
            result = gson.fromJson(new StringReader(data), classOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
        if (result instanceof ISerialize)
            ((ISerialize)result).wakeup();
        return result;
    }

}
