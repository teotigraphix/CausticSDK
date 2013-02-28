
package com.teotigraphix.caustic.air.functions;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.singlecellsoftware.causticcore.CausticCore;
import com.teotigraphix.caustic.air.CausticExtensionContext;
import com.teotigraphix.caustic.air.utils.MessageUtils;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class QueryMessage implements FREFunction {

    @Override
    public FREObject call(FREContext context, FREObject[] args) {
        CausticExtensionContext c = (CausticExtensionContext)context;
        CausticCore engine = c.getCausticCore();

        String message = MessageUtils.getString(args[0]);
        Log.d("QueryMessage", message);

        String result = engine.QueryOSC(message);
        if (result != null) {
            try {
                return FREObject.newObject(result);
            } catch (FREWrongThreadException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
