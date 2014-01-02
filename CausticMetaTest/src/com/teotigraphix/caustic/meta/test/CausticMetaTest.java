
package com.teotigraphix.caustic.meta.test;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticFile;
import com.teotigraphix.caustk.core.ISoundGenerator;
import com.teotigraphix.caustk.core.internal.DesktopSoundGenerator;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class CausticMetaTest extends TestCase {

    private ISoundGenerator soundGenerator;

    protected File applicationStorageRoot;

    protected File causticStorageRoot;

    private boolean deleteResources = true;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        RuntimeUtils.STORAGE_ROOT = new File("C:/Users/Teoti/Documents").getAbsolutePath();
        RuntimeUtils.APP_ROOT = new File("C:/Users/Teoti/Documents/CausticMetaTests")
                .getAbsolutePath();

        soundGenerator = DesktopSoundGenerator.getInstance();
        soundGenerator.initialize();

        causticStorageRoot = new File("C:/Users/Teoti/Documents");
        applicationStorageRoot = new File(causticStorageRoot, "CaustkTests");

        if (deleteResources /*&& applicationStorageRoot.exists()*/)
            FileUtils.deleteQuietly(applicationStorageRoot);

        soundGenerator.onStart();
        soundGenerator.onResume();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private File createMockFile(String name) throws IOException {
        File srcFile = new File("src/com/teotigraphix/caustic/meta/test/DEMO SKA.caustic")
                .getAbsoluteFile();
        File destFile = new File(srcFile.getParentFile(), name);
        FileUtils.copyFile(srcFile, destFile, true);
        assertTrue(destFile.exists());
        return destFile;
    }

    public void test_add_metadata() throws IOException {
        File destFile = createMockFile("TEST1.caustic");

        CausticFile causticFile = new CausticFile(destFile);
        assertFalse(causticFile.hasMetadata());

        causticFile.setArtist("Artist");
        causticFile.setDescription("Foo bar");
        causticFile.setLinkText("My Link");
        causticFile.setLinkUrl("http://www.singlecellsoftware.com");
        causticFile.setTitle("The Song");

        causticFile.write();

        // reload the file
        causticFile = new CausticFile(destFile);
        assertTrue(causticFile.hasMetadata());

        assertEquals("Artist", causticFile.getArtist());
        assertEquals("Foo bar", causticFile.getDescription());
        assertEquals("My Link", causticFile.getLinkText());
        assertEquals("http://www.singlecellsoftware.com", causticFile.getLinkUrl());
        assertEquals("The Song", causticFile.getTitle());

        // remove the data
        causticFile.trim();
        assertFalse(causticFile.hasMetadata());

        // FileUtils.deleteQuietly(destFile);
    }

    public void test_addAndUpdate_metadata() throws IOException {
        File destFile = createMockFile("TEST2.caustic");

        CausticFile causticFile = new CausticFile(destFile);
        assertFalse(causticFile.hasMetadata());

        causticFile.setArtist("Artist");
        causticFile.setDescription("Foo bar");
        causticFile.setLinkText("");
        causticFile.setLinkUrl("");
        causticFile.setTitle("The Song");

        causticFile.write();

        // reload the file
        causticFile = new CausticFile(destFile);
        assertTrue(causticFile.hasMetadata());

        causticFile.setArtist("Foo Bar");
        causticFile.setLinkText("My Link");
        causticFile.setLinkUrl("http://www.singlecellsoftware.com");

        // update values, rewrite the file, trim() will be called before write
        // to slice of the old bytes
        causticFile.write();

        causticFile = new CausticFile(destFile);
        causticFile.read();

        assertEquals("Foo Bar", causticFile.getArtist());
        assertEquals("Foo bar", causticFile.getDescription());
        assertEquals("My Link", causticFile.getLinkText());
        assertEquals("http://www.singlecellsoftware.com", causticFile.getLinkUrl());
        assertEquals("The Song", causticFile.getTitle());
    }

    public void test_fileModel() throws IOException {
        //        File destFile = createMockFile("TEST.caustic");
        //
        //        FileModel fileModel = new FileModel(soundGenerator);
        //        fileModel.setFile(destFile);
        //        assertFalse(fileModel.getCausticFile().hasMetadata());
        //
        //        FileUtils.deleteQuietly(destFile);
    }

}
