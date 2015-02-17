
package com.teotigraphix.caustk.gdx.app.model.song;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.google.common.eventbus.EventBus;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.gdx.app.model.song.ISongFileCollection.SongFileCollectionEvent;
import com.teotigraphix.caustk.gdx.app.model.song.ISongFileCollection.SongFileCollectionEventKind;
import com.teotigraphix.caustk.utils.core.RuntimeUtils;

public class SongFileLoader {

    @Tag(0)
    private ISongFileCollection _collection;

    private Queue<SongFileQueueItem> exportQueue = new LinkedList<SongFileQueueItem>();

    private SongFileQueueItem currentExport;

    private SongFileSource currentSource;

    private EventBus getEventBus() {
        return CaustkRuntime.getInstance().getApplication().getEventBus();
    }

    public synchronized SongFileQueueItem getCurrentExport() {
        return currentExport;
    }

    SongFileLoader() {
    }

    void load(SongFileSource source, Collection<File> rawFiles) {
        load(source, rawFiles, false);
    }

    void load(SongFileSource source, Collection<File> rawFiles, boolean clear) {
        this.currentSource = source;

        if (clear)
            currentSource.getFiles().clear();

        for (File file : rawFiles) {
            exportQueue.add(new SongFileQueueItem(currentSource, file));
        }

        getEventBus().post(
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
            System.out.println("Action_FileLoadComplete");
            RuntimeUtils.postRunnable(new Runnable() {
                @Override
                public void run() {
                    getEventBus()
                            .post(new SongFileCollectionEvent(
                                    SongFileCollectionEventKind.UI_FilesChange));
                    getEventBus().post(
                            new SongFileCollectionEvent(
                                    SongFileCollectionEventKind.Action_SourceLoadComplete,
                                    currentSource));
                    currentSource = null;
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
        getEventBus().post(new SongFileCollectionEvent(kind, file));
    }

    private void setCurrentExport(SongFileQueueItem currentExport) {
        this.currentExport = currentExport;
        final File file = currentExport.file;
        RuntimeUtils.postRunnable(new Runnable() {
            @Override
            public void run() {
                fire(SongFileCollectionEventKind.Action_FileLoadUpdate, file);
            }
        });
    }

    class SongFileQueueItem {

        private File file;

        private SongFileSource root;

        SongFileQueueItem(SongFileSource root, File file) {
            this.root = root;
            this.file = file;
        }

        void load() {
            SongFile songFile = SongFile.create(file);
            songFile.load(getRack());
            root.getFiles().add(songFile);
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
