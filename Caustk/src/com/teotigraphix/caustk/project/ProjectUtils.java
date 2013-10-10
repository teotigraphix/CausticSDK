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

package com.teotigraphix.caustk.project;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.controller.ICaustkController;

/**
 * {@link ProjectManager} static utility methods.
 * 
 * @author Michael Schmalle
 */
public final class ProjectUtils {

    static boolean isProject(File projectDirectory, File file) {
        if (file.isAbsolute())
            return file.exists();
        return toProjectFile(projectDirectory, file).exists();
    }

    static File toProjectFile(File projectDirectory, File file) {
        if (file.isAbsolute())
            return file;
        return new File(projectDirectory, file.getPath());
    }

    static void saveProjectPreferences(ICaustkController controller,
            SessionPreferences sessionPreferences, File targetFile) throws IOException {
        String data = controller.getSerializeService().toPrettyString(sessionPreferences);
        FileUtils.writeStringToFile(targetFile, data);
    }

    static ProjectInfo createDefaultInfo() {
        ProjectInfo info = new ProjectInfo();
        info.setName("Untitled Project");
        info.setAuthor("Untitled Author");
        info.setCreated(new Date());
        info.setModified(new Date());
        info.setDescription("A new project");
        return info;
    }

    static Project createProject(ICaustkController controller, File file) throws IOException {
        Project project = new Project();
        project.setController(controller);
        project.setInitializing(true);
        // set the project sub directory in the /projects directory
        project.setDirectory(new File(file.getPath()));
        project.setInfo(ProjectUtils.createDefaultInfo());

        FileUtils.forceMkdir(project.getAbsolutDirectory());

        project.open();

        return project;
    }
}
