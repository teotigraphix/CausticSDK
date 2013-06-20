
package com.teotigraphix.caustk.utls;

import java.util.Iterator;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.thoughtworks.xstream.XStream;

public class JsonFormatter {

    public static String format(final JSONObject object) throws JSONException {
        final JsonVisitor visitor = new JsonVisitor(4, ' ');
        visitor.visit(object, 0);
        return visitor.toString();
    }

    private static class JsonVisitor {

        private final StringBuilder builder = new StringBuilder();

        private final int indentationSize;

        private final char indentationChar;

        public JsonVisitor(final int indentationSize, final char indentationChar) {
            this.indentationSize = indentationSize;
            this.indentationChar = indentationChar;
        }

        private void visit(final JSONArray array, final int indent) throws JSONException {
            final int length = array.length();
            if (length == 0) {
                write("[]", indent);
            } else {
                write("[", indent);
                for (int i = 0; i < length; i++) {
                    visit(array.get(i), indent + 1);
                }
                write("]", indent);
            }

        }

        private void visit(final JSONObject obj, final int indent) throws JSONException {
            final int length = obj.length();
            if (length == 0) {
                write("{}", indent);
            } else {
                write("{", indent);
                @SuppressWarnings("unchecked")
                final Iterator<String> keys = obj.keys();
                while (keys.hasNext()) {
                    final String key = keys.next();
                    write(key + " :", indent + 1);
                    visit(obj.get(key), indent + 1);
                    if (keys.hasNext()) {
                        write(",", indent + 1);
                    }
                }
                write("}", indent);
            }
        }

        private void visit(final Object object, final int indent) throws JSONException {
            if (object instanceof JSONArray) {
                visit((JSONArray)object, indent);
            } else if (object instanceof JSONObject) {
                visit((JSONObject)object, indent);
            } else {
                if (object instanceof String) {
                    write("\"" + (String)object + "\"", indent);
                } else {
                    write(String.valueOf(object), indent);
                }
            }

        }

        private void write(final String data, final int indent) {
            for (int i = 0; i < (indent * indentationSize); i++) {
                builder.append(indentationChar);
            }
            builder.append(data).append('\n');
        }

        @Override
        public String toString() {
            return builder.toString();
        }

    }

    public static String toJson(XStream stream, Object serialized, boolean format) {
        String data = stream.toXML(serialized);
        if (format) {
            JSONObject obj = null;
            try {
                obj = new JSONObject(data);
                data = JsonFormatter.format(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static String toJson(XStream stream, Object serialized) {
        return toJson(stream, serialized, false);
    }

}
