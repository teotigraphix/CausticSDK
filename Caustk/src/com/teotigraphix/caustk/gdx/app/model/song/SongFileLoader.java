
package com.teotigraphix.caustk.gdx.app.model.song;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.gdx.app.model.song.ISongFileCollection.SongFileCollectionEvent;
import com.teotigraphix.caustk.gdx.app.model.song.ISongFileCollection.SongFileCollectionEventKind;

public class SongFileLoader {

    @Tag(0)
    private ISongFileCollection collection;

    private Queue<SongFileQueueItem> exportQueue = new LinkedList<SongFileQueueItem>();

    private SongFileQueueItem currentExport;

    public synchronized SongFileQueueItem getCurrentExport() {
        return currentExport;
    }

    public SongFileLoader(ISongFileCollection collection) {
        this.collection = collection;
    }

    void load(Collection<File> rawFiles) {
        load(rawFiles, false);
    }

    void load(Collection<File> rawFiles, boolean clear) {
        if (clear)
            collection.getFiles().clear();

        for (File file : rawFiles) {
            exportQueue.add(new SongFileQueueItem(file));
        }

        collection.getEventBus().post(
                new SongFileCollectionEvent(SongFileCollectionEventKind.Action_FileLoadStart));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                nextItem();
            }
        });
        thread.start();

    }

    private void nextItem() {
        if (exportQueue.isEmpty()) {
            currentExport = null;
            exportQueue.clear();
            System.out.println("FilesChange");
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    collection.getEventBus().post(
                            new SongFileCollectionEvent(SongFileCollectionEventKind.FilesChange));
                    collection.getEventBus().post(
                            new SongFileCollectionEvent(
                                    SongFileCollectionEventKind.Action_FileLoadComplete));
                }
            });
        } else {
            currentExport = exportQueue.remove();
            setCurrentExport(currentExport);
            currentExport.load();

            nextItem();
        }
    }

    private void fire(SongFileCollectionEventKind kind, File file) {
        collection.getEventBus().post(new SongFileCollectionEvent(kind, file));
    }

    private void setCurrentExport(SongFileQueueItem currentExport) {
        this.currentExport = currentExport;
        final File file = currentExport.file;
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                fire(SongFileCollectionEventKind.Action_FileLoadUpdate, file);
            }
        });
    }

    class SongFileQueueItem {

        private File file;

        SongFileQueueItem(File file) {
            this.file = file;
        }

        void load() {
            SongFile songFile = SongFile.create(file);
            songFile.load(getRack());
            collection.getFiles().add(songFile);
            try {
                songFile.read();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    ICaustkRack getRack() {
        return CaustkRuntime.getInstance().getRack();
    }

}
