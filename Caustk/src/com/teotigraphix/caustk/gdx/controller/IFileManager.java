////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.gdx.controller;

import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.caustk.gdx.app.Project;

import java.io.File;
import java.io.IOException;

public interface IFileManager {

    void setupApplicationDirectory();

    void setStartupProject(Project project);

    File getProjectsDirectory();

    String getNextProjectName();

    Project createOrLoadStartupProject() throws IOException;

    /**
     * Creates a new Project.
     * 
     * @param projectLocation The directory location where the getName() is used
     *            to create the base project directory.
     * @throws java.io.IOException
     */
    Project createProject(File projectLocation) throws IOException;

    /**
     * Loads an existing Project.
     * 
     * @param projectFile The .prj file held within the same named directory.
     * @throws java.io.IOException
     */
    Project loadProject(File projectFile) throws IOException;

    /**
     * Reads a project file from a serialized state, no rack loading occurs.
     * 
     * @param projectFile
     * @throws java.io.IOException
     */
    Project readProject(File projectFile) throws IOException;

    Array<File> exportPatternStems(String exportName, Array<PatternNode> patterns)
            throws IOException;

}
