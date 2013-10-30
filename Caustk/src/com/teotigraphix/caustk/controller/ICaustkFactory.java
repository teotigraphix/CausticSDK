
package com.teotigraphix.caustk.controller;

import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.controller.core.CaustkController;
import com.teotigraphix.caustk.library.ILibraryManager;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.service.ISerializeService;

/**
 * @author Michael Schmalle
 */
public interface ICaustkFactory {

    /**
     * The main {@link CaustkController} instance that instrumentates the whole
     * application sequencing from patterns, parts, presets, memory and all
     * other things needing controlling.
     */
    ICaustkController createController();

    /**
     * Creates the single {@link ISerializeService} for the application's
     * controller.
     * 
     * @return An instance of the {@link ISerializeService}
     */
    ISerializeService createSerializeService();

    /**
     * Creates the single {@link ICommandManager} for the application's
     * controller.
     * 
     * @return An instance of the {@link ICommandManager}
     */
    ICommandManager createCommandManager();

    /**
     * Creates the single {@link ILibraryManager} for the application's
     * controller.
     * 
     * @return An instance of the {@link ILibraryManager}
     */
    ILibraryManager createLibraryManager();

    /**
     * Creates the single {@link IProjectManager} for the application's
     * controller.
     * 
     * @return An instance of the {@link IProjectManager}
     */
    IProjectManager createProjectManager();

    IRack createRack();
}
