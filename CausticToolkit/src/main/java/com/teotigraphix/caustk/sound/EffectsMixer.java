
package com.teotigraphix.caustk.sound;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.utils.ExceptionUtils;

public class EffectsMixer implements ISerialize, IRestore {

    public EffectsMixer() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void restore() {
        // TODO Auto-generated method stub

    }

    @Override
    public void sleep() {
        // TODO Auto-generated method stub

    }

    @Override
    public void wakeup(ICaustkController controller) {
        // TODO Auto-generated method stub

    }

    protected final RuntimeException newRangeException(String control, String range, Object value) {
        return ExceptionUtils.newRangeException(control, range, value);
    }
}
