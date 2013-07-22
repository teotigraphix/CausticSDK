
package com.teotigraphix.libraryeditor.view;

import java.io.File;
import java.io.IOException;

import javafx.scene.control.Dialogs;
import javafx.scene.control.Dialogs.DialogResponse;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import com.google.inject.Inject;
import com.teotigraphix.caustic.utils.FileUtil;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.core.MediatorBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class MainToolBarMediator extends MediatorBase {

    public MainToolBarMediator() {
    }

    @Inject
    public MainToolBarMediator(ICaustkApplicationProvider provider) {
        super(provider);
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();
    }

    public void createLibrary() {
        // applicationModel.getStage(
        String libraryName = Dialogs.showInputDialog(null, "Name", "Create a new Library",
                "Create library", "UntitledLibrary");
        if (libraryName == null)
            return; // canceled

        File libraryFile = new File(libraryName);
        if (getController().getLibraryManager().isLibrary(libraryFile)) {
            DialogResponse response = Dialogs.showConfirmDialog(null, "Library " + libraryName
                    + " exists, overwrite?");
            if (response == DialogResponse.NO || response == DialogResponse.CANCEL)
                return;

            try {
                getController().getLibraryManager().deleteLibrary(libraryFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Library library = null;
        try {
            library = getController().getLibraryManager().createLibrary(libraryName);
            getController().getLibraryManager().setSelectedLibrary(library);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void importFile() {
        System.out.println("onSaveButtonAction");
        FileChooser chooser = FileUtil.createDefaultFileChooser(RuntimeUtils
                .getCausticSongsDirectory().getAbsolutePath(), "Caustic song file", "*.caustic");
        File causticFile = chooser.showOpenDialog(null);

        // if no current library open error dialog
        Library library = getController().getLibraryManager().getSelectedLibrary();
        try {
            getController().getLibraryManager().importSong(library, causticFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CausticException e) {
            e.printStackTrace();
        }

    }

    public void saveLibrary() {
        Library library = getController().getLibraryManager().getSelectedLibrary();
        try {
            getController().getLibraryManager().saveLibrary(library);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLibrary() {
        DirectoryChooser chooser = FileUtil.createDefaultDirectoryChooser(null);
        File libDirectory = chooser.showDialog(null);
        Library library = getController().getLibraryManager().loadLibrary(libDirectory.getName());
        getController().getLibraryManager().setSelectedLibrary(library);        
    }

}
