
package com.teotigraphix.caustk.library;

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.core.CausticException;

public interface ILibraryManager extends IControllerComponent {

    Library getSelectedLibrary();

    void setSelectedLibrary(Library value);

    void load();

    Library loadLibrary(String name);

    void saveLibrary(Library library) throws IOException;

    void importSong(Library library, File causticFile) throws IOException, CausticException;

    void delete() throws IOException;

    Library createLibrary(String name) throws IOException;

    void clear();

}
