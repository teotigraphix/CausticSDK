
package com.teotigraphix.caustk.core;

import com.teotigraphix.caustk.rack.IRack;

public interface IRackSerializer {

    void restore(IRack rack);

    void update(IRack rack);
}
