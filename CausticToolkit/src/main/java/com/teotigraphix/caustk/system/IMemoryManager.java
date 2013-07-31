
package com.teotigraphix.caustk.system;

import com.teotigraphix.caustk.system.Memory.Category;
import com.teotigraphix.caustk.system.Memory.Type;

public interface IMemoryManager {

    Memory getSelectedMemoryBank();

    TemporaryMemory getTemporaryMemory();

    MemoryBank getMemoryBank(Type value);

    Type getSelectedMemoryType();

    void setSelectedMemoryType(Type value);

    Category getSelectedMemoryCategory();

    void setSelectedMemoryCategory(Category value);

}
