
package com.teotigraphix.caustic.air.utils;

import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREWrongThreadException;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class MessageUtils {

    /**
     * Returns a String from the {@link FREObject}.
     * 
     * @param object The wrapper object.
     */
    public static final String getString(FREObject object) {
        String message = null;
        try {
            message = object.getAsString();
        } catch (IllegalStateException e) {
            // TODO (mschmalle) throw CausticException
            e.printStackTrace();
        } catch (FRETypeMismatchException e) {
            e.printStackTrace();
        } catch (FREInvalidObjectException e) {
            e.printStackTrace();
        } catch (FREWrongThreadException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * Returns an int from the {@link FREObject}.
     * 
     * @param object The wrapper object.
     */
    public static final int getInt(FREObject object) {
        int value = Integer.MAX_VALUE;
        try {
            value = object.getAsInt();
        } catch (IllegalStateException e) {
            // TODO (mschmalle) throw CausticException
            e.printStackTrace();
        } catch (FRETypeMismatchException e) {
            e.printStackTrace();
        } catch (FREInvalidObjectException e) {
            e.printStackTrace();
        } catch (FREWrongThreadException e) {
            e.printStackTrace();
        }
        return value;
    }
}
