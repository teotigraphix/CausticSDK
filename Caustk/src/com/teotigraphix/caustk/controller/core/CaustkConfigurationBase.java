////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.controller.core;

import java.io.File;

import com.teotigraphix.caustk.controller.ICausticLogger;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.command.CommandManager;
import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.library.ILibraryManager;
import com.teotigraphix.caustk.library.core.LibraryManager;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.project.ProjectManager;
import com.teotigraphix.caustk.rack.sound.ISoundGenerator;
import com.teotigraphix.caustk.service.ISerializeService;
import com.teotigraphix.caustk.service.serialize.SerializeService;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * Used in unit tests of the toolkit framework. Need the
 * {@link DesktopSoundGenerator} desktop access for tool kit tests.
 */
public abstract class CaustkConfigurationBase implements ICaustkConfiguration {

    //--------------------------------------------------------------------------
    // ICaustkConfiguration API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // soundGenerator
    //----------------------------------

    private ISoundGenerator soundGenerator;

    @Override
    public ISoundGenerator getSoundGenerator() {
        return soundGenerator;
    }

    @Override
    public void setSoundGenerator(ISoundGenerator value) {
        soundGenerator = value;
    }

    //----------------------------------
    // applicationTitle
    //----------------------------------

    private String applicationId;

    @Override
    public final String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String value) {
        applicationId = value;
    }

    //----------------------------------
    // applicationTitle
    //----------------------------------

    private String applicationTitle;

    @Override
    public String getApplicationTitle() {
        return applicationTitle;
    }

    @Override
    public void setApplicationTitle(String value) {
        applicationTitle = value;
    }

    //----------------------------------
    // applicationRoot
    //----------------------------------

    private File applicationRoot;

    @Override
    public File getApplicationRoot() {
        return applicationRoot;
    }

    @Override
    public void setApplicationRoot(File value) {
        applicationRoot = value;
    }

    //----------------------------------
    // causticStorage
    //----------------------------------

    private File causticStorage;

    @Override
    public File getCausticStorage() {
        return causticStorage;
    }

    @Override
    public void setCausticStorage(File value) {
        causticStorage = value;
        RuntimeUtils.STORAGE_ROOT = value.getAbsolutePath();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public CaustkConfigurationBase() {
        //setCausticStorage(new File(System.getProperty("user.home")));
        initialize();
    }

    /**
     * Subclasses can initialize any properties specific to the platform.
     */
    abstract protected void initialize();

    //--------------------------------------------------------------------------
    // ICaustkConfiguration API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public ICausticLogger createLogger() {
        return new CaustkLogger();
    }

    @Override
    public ICaustkController createController(ICaustkApplication application) {
        return new CaustkController(application);
    }

    @Override
    public ISerializeService createSerializeService(ICaustkController controller) {
        return new SerializeService();
    }

    @Override
    public ICommandManager createCommandManager(ICaustkController controller) {
        return new CommandManager();
    }

    @Override
    public ILibraryManager createLibraryManager(ICaustkController controller) {
        return new LibraryManager();
    }

    @Override
    public IProjectManager createProjectManager(ICaustkController controller) {
        return new ProjectManager();
    }
}
