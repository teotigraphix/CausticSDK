
package com.teotigraphix.caustk.gdx.app.model.song;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.teotigraphix.caustk.gdx.app.model.song.SongFileCollection.SongFileCollectionEvent;
import com.teotigraphix.caustk.gdx.app.model.song.SongFileCollection.SongFileCollectionEventKind;

public class SongFileLoader {

    private SongFileCollection collection;

    private Queue<SongFileQueueItem> exportQueue = new LinkedList<SongFileQueueItem>();

    private SongFileQueueItem currentExport;

    public synchronized SongFileQueueItem getCurrentExport() {
        return currentExport;
    }

    public SongFileLoader(SongFileCollection collection) {
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

        collection.fire(SongFileCollectionEventKind.Action_FileLoadStart);

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
                    collection.fire(SongFileCollectionEventKind.FilesChange);
                    collection.fire(SongFileCollectionEventKind.Action_FileLoadComplete);
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
            songFile.load(collection.getRack());
            collection.getFiles().add(songFile);
        }
    }

}
