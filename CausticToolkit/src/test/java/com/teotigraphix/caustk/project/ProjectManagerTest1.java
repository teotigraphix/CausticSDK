
package com.teotigraphix.caustk.project;

import static junit.framework.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.tone.ToneType;

public class ProjectManagerTest1 extends CaustkTestBase {

    private final static File PROJECT_DIR = new File("ProjectManagerTestProject");

    private ProjectManager projectManager;

    private ISoundSource soundSource;

    @Override
    protected void start() throws CausticException, IOException {
        projectManager = (ProjectManager)controller.getProjectManager();
        soundSource = controller.getSoundSource();
    }

    @Override
    protected void end() {
        controller = null;
        soundSource = null;
    }

    @Test
    public void test_create_empty_project() throws CausticException, IOException {
        Project project = projectManager.createProject(PROJECT_DIR);
        projectManager.save();
        Assert.assertTrue(project.getStateFile().exists());
    }

    @Test
    public void test_create_tone_project() throws CausticException, IOException {
        Project project = projectManager.createProject(PROJECT_DIR);

        soundSource.createTone("part1", ToneType.SubSynth);
        soundSource.createTone("part2", ToneType.Bassline);
        soundSource.createTone("part3", ToneType.Beatbox);
        soundSource.createTone("part4", ToneType.PCMSynth);

        projectManager.save();
        Assert.assertTrue(project.getStateFile().exists());
    }

    @Test
    public void test_create_project() throws CausticException, IOException {
        Project project1 = projectManager.createProject(PROJECT_DIR);
        projectManager.save();
        assertNotNull(project1);
        Assert.assertTrue(project1.getStateFile().exists());
        Project project2 = projectManager.load(PROJECT_DIR);
        assertNotNull(project2);

        String serialized1 = controller.getSerializeService().toPrettyString(project1);
        String serialized2 = controller.getSerializeService().toPrettyString(project2);
        Assert.assertEquals(serialized1, serialized2);
    }
}
