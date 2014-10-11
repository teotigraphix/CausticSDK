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

package com.teotigraphix.gdx.controller;

import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternUtils;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.gdx.app.ApplicationComponent;
import com.teotigraphix.gdx.app.IApplicationModel;
import com.teotigraphix.gdx.app.IProjectFactory;
import com.teotigraphix.gdx.app.IProjectModel;
import com.teotigraphix.gdx.app.Project;

@Singleton
public class FileManager extends ApplicationComponent implements IFileManager {

    public static final String LAST_PROJECT_PATH = "last-project-path";

    private static final String TAG = "FileManager";

    //--------------------------------------------------------------------------
    // Inject :: Variables
    //--------------------------------------------------------------------------

    @Inject
    private IApplicationModel applicationModel;

    @Inject
    private IProjectFactory projectFactory;

    @Inject
    private IProjectModel projectModel;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private File applicationDirectory;

    private File projectsDirectory;

    private File tempDirectory;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    public final File getApplicationDirectory() {
        return applicationDirectory;
    }

    @Override
    public final File getProjectsDirectory() {
        return projectsDirectory;
    }

    public final File getApplicationTempDirectory() {
        return tempDirectory;
    }

    private File getStartupProjectFile() {
        String path = applicationModel.getPreferences().getString(LAST_PROJECT_PATH, null);
        if (path == null)
            return new File("");
        return new File(path);
    }

    @Override
    public void setStartupProject(Project project) {
        applicationModel.getPreferences().putString(LAST_PROJECT_PATH,
                project.getFile().getAbsolutePath());
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public FileManager() {
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    // Called from ApplicationStates.construct()
    @Override
    public void setupApplicationDirectory() {
        applicationModel.getApplication().getLogger().log(TAG, "setupApplicationDirectory()");

        // create the application projects folder if not exists
        // ExternalStorage/AppName
        applicationDirectory = RuntimeUtils.getApplicationDirectory();
        if (!applicationDirectory.exists())
            applicationDirectory.mkdirs();

        // ExternalStorage/AppName/projects
        projectsDirectory = RuntimeUtils.getApplicationProjectsDirectory();
        if (!projectsDirectory.exists())
            projectsDirectory.mkdirs();

        // ExternalStorage/AppName/.temp
        tempDirectory = RuntimeUtils.getApplicationTempDirectory();
        if (!tempDirectory.exists())
            tempDirectory.mkdirs();
    }

    @Override
    public Project createOrLoadStartupProject() throws IOException {
        applicationModel.getApplication().getLogger().log(TAG, "createOrLoadStartupProject()");

        Project project = null;

        File projectFile = getStartupProjectFile();

        if (!projectFile.exists()) {
            err(TAG, "Could not load project file " + projectFile.getAbsolutePath());
            // create new Untitled Project
            project = createProject(new File(getNextProjectName()));

        } else {
            // load existing
            project = projectFactory.readProject(projectFile, true);
        }

        return project;
    }

    @Override
    public Project loadProject(File projectFile) throws IOException {
        Project project = projectFactory.readProject(projectFile, true);
        return project;
    }

    @Override
    public Project readProject(File projectFile) throws IOException {
        Project project = projectFactory.readProject(projectFile, false);
        return project;
    }

    @Override
    public Project createProject(File projectLocation) throws IOException {
        Project project = null;
        String projectName = projectLocation.getName();
        File projectBaseDirectory = toProjectDirectory(projectName);
        File projectFile = toProjectFile(projectBaseDirectory, projectName);
        log(TAG, "  Creating new project " + projectFile.getAbsolutePath());

        project = projectFactory.createDefaultProject(projectName, projectFile);

        return project;
    }

    // TODO Rej Ask to fix space bug in export WAV
    @Override
    public String getNextProjectName() {
        String appName = getApplication().getApplicationName();
        String name = "Untitled" + appName + "Project";

        if (!new File(projectsDirectory, name).exists())
            return name;

        File projectsDirectory = getProjectsDirectory();
        File testDirectory = null;

        for (int i = 1; i < 100; i++) {
            testDirectory = new File(projectsDirectory, name + "" + i);
            if (!testDirectory.exists())
                break;
        }

        return testDirectory.getName();
    }

    private int count = 0;

    @Override
    public Array<File> exportPatternStems(final String exportName, final Array<PatternNode> patterns)
            throws IOException {
        final Array<File> files = new Array<File>();

        Project project = projectModel.getProject();

        final ICaustkRack rack = applicationModel.getApplication().getRack();
        // Root/CausticLive/Projects/MyProj1/export/exportName
        final File tempDirectory = new File(project.getResource("export"), exportName);

        String nativepath = tempDirectory.getAbsolutePath();//.replaceAll(" ", "-");
        final File exportDirectory = new File(nativepath);
        exportDirectory.mkdirs();
        // Root/CausticLive/Projects/MyProj1/export/exportName/

        count = 0;

        Timer t = new Timer();
        t.scheduleTask(new Task() {
            @Override
            public void run() {
                PatternNode patternNode = patterns.get(count);
                int bankIndex = patternNode.getBankIndex();
                int patternIndex = patternNode.getPatternIndex();
                int patternLength = patternNode.getNumMeasures();
                exportStem(files, rack, exportDirectory, exportName, patternNode.getMachineIndex(),
                        bankIndex, patternIndex, patternLength);
                System.out.println("Exported " + count);
                count++;
                if (count == patterns.size) {
                    stemExportComplete();
                }
            }
        }, 0f, 1f, patterns.size - 1);

        return files;
    }

    public static void exportStem(Array<File> files, ICaustkRack rack, File exportDirectory,
            String exportName, int machineIndex, int bankIndex, int patternIndex, int patternLength) {

        SequencerMessage.CLEAR_PATTERNS.send(rack);

        SequencerMessage.LOOP_POINTS.send(rack, 0, patternLength);
        SequencerMessage.PLAY_POSITION.send(rack, 0);

        SequencerMessage.PATTERN_EVENT.send(rack, machineIndex, 0, bankIndex, patternIndex,
                patternLength);

        // caustic/studio/export/[ALLEY 01]-[120bpm]//[ALLEY 01]-[INST1]-[A2]-[120bpm].wav
        String sampleName = getExportStemSampleName(rack, exportName, machineIndex, bankIndex,
                patternIndex);
        File location = new File(exportDirectory, sampleName);
        SequencerMessage.EXPORT.send(rack, "loop", "WAV", 100, location.getAbsolutePath());

        files.add(location);
    }

    // ALLEY 01-INST1-A2-120bpm
    public static String getExportStemSampleName(ICaustkRack rack, String exportName,
            int machineIndex, int bankIndex, int patternIndex) {
        String machineName = rack.getMachine(machineIndex).getName();
        String patternName = PatternUtils.toString(bankIndex, patternIndex);
        String bpm = (int)rack.getSequencer().getBPM() + "";
        return exportName + "-" + machineName + "-" + patternName + "-" + bpm + "bpm";
    }

    //    public void exportPatternStems(String currentName) {
    //        // caustic/studio/export/ALLEY 01-120bpm
    //        String stemDirectoryName = getExportStemDirectoryName(getApplication(), this);
    //        final File stemDirectory = getStudioExportFile(stemDirectoryName);
    //        if (!stemDirectory.exists())
    //            stemDirectory.mkdirs();
    //
    //        final int bankIndex = getSelectedBank();
    //        final int patternIndex = getSelectedPattern();
    //        final int patternLength = getSelectedPatternLength();
    //
    //        count = 0;
    //        Timer t = new Timer();
    //        t.scheduleTask(new Task() {
    //            @Override
    //            public void run() {
    //                exportStem(count, stemDirectory, bankIndex, patternIndex, patternLength);
    //                System.out.println("Exported " + count);
    //                count++;
    //                if (count == 14) {
    //                    stemExportComplete();
    //                }
    //            }
    //        }, 0f, 1f, 13);
    //    }
    //
    //    // ALLEY 01-120bpm
    //    public static String getExportStemDirectoryName(ICaustkApplication application,
    //            IApplicationModel applicationModel) {
    //        ICaustkRack rack = application.getRack();
    //        String bpm = (int)rack.getSequencer().getBPM() + "";
    //        String currentName = applicationModel.getSoundKit().getName().replace(".caustic", "");
    //        return currentName + "-" + bpm + "bpm";
    //    }
    //
    //    public static File getStudioExportFile(String nameNoExtension) {
    //        File file = new File(getStudioExportDirectory(), nameNoExtension);
    //        return file;
    //    }
    //
    //    public static File getStudioExportDirectory() {
    //        File root = RuntimeUtils.getCausticDirectory();
    //        root = new File(root, "studio/export");
    //        if (!root.exists())
    //            root.mkdirs();
    //        return root;
    //    }

    private void stemExportComplete() {
        //XXX post(new OnApplicationMangerShowOptions(false));
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    private File toProjectFile(File projectDirectory, String projectName) {
        return new File(projectDirectory, projectName + ".prj");
    }

    private File toProjectDirectory(String projectName) {
        File directory = new File(getProjectsDirectory(), projectName);
        // create the container directory for the project
        if (!directory.exists())
            directory.mkdirs();
        return directory;
    }

}
