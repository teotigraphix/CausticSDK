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

import com.teotigraphix.caustk.controller.ICaustkLogger;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.command.CommandManager;
import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.project.ProjectManager;
import com.teotigraphix.caustk.rack.ISoundGenerator;
import com.teotigraphix.caustk.service.ISerializeService;
import com.teotigraphix.caustk.service.serialize.SerializeService;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.caustk.workstation.CaustkFactory;
import com.teotigraphix.caustk.workstation.ICaustkFactory;

/**
 * Used in unit tests of the toolkit framework. Need the
 * {@link DesktopSoundGenerator} desktop access for tool kit tests.
 * 
 * @author Michael Schmalle
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
    // application
    //----------------------------------

    private ICaustkApplication application;

    @Override
    public ICaustkApplication getApplication() {
        return application;
    }

    @Override
    public void setApplication(ICaustkApplication value) {
        application = value;
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
        RuntimeUtils.APP_ROOT = value.getAbsolutePath();
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
    public ICaustkLogger createLogger() {
        return new CaustkLogger();
    }

    //--------------------------------------------------------------------------
    // Public Application Component Creation API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public ICaustkController createController() {
        return new CaustkController(application);
    }

    @Override
    public ISerializeService createSerializeService() {
        return new SerializeService();
    }

    @Override
    public ICommandManager createCommandManager() {
        return new CommandManager();
    }

    @Override
    public IProjectManager createProjectManager() {
        return new ProjectManager();
    }

    @Override
    public ICaustkFactory createFactory(ICaustkApplication application) {
        return new CaustkFactory(application);
    }
}
