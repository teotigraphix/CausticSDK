
package com.teotigraphix.caustk.sequencer.queue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.IDispatcher;
import com.teotigraphix.caustk.service.ISerialize;

public class QueueSong implements ISerialize {

    private Map<Integer, Map<Integer, QueueData>> map = new HashMap<Integer, Map<Integer, QueueData>>();

    private transient ICaustkController controller;

    //----------------------------------
    // file
    //----------------------------------

    private File file;

    public final File getFile() {
        return file;
    }

    /**
     * Returns the absolute location of the song file on disk, within the
     * project's resource directory.
     */
    public File getAbsoluteFile() {
        final File absoluteFile = controller.getProjectManager().getProject()
                .getResource(file.getPath()).getAbsoluteFile();
        return absoluteFile;
    }

    IDispatcher getDispatcher() {
        return controller.getQueueSequencer();
    }

    public QueueSong() {
    }

    public QueueSong(File file) {
        this.file = file;
    }

    public Collection<QueueData> getQueueData(int bankIndex) {
        Collection<QueueData> result = new ArrayList<QueueData>();
        if (map.containsKey(bankIndex)) {
            for (QueueData queueData : getBankMap(bankIndex).values()) {
                result.add(queueData);
            }
        }
        return result;
    }

    private Map<Integer, QueueData> getBankMap(int bankIndex) {
        Map<Integer, QueueData> bankMap = map.get(bankIndex);
        if (bankMap == null) {
            bankMap = new HashMap<Integer, QueueData>();
            map.put(bankIndex, bankMap);
        }
        return bankMap;
    }

    public QueueData getQueueData(int bankIndex, int patternIndex) {
        Map<Integer, QueueData> bankMap = getBankMap(bankIndex);
        QueueData queueData = bankMap.get(patternIndex);
        if (queueData == null) {
            queueData = new QueueData(bankIndex, patternIndex);
            queueData.wakeup(controller);
            bankMap.put(patternIndex, queueData);
        }
        return queueData;
    }

    public Map<Integer, QueueData> getView(int bankIndex) {
        Map<Integer, QueueData> map = getBankMap(bankIndex);
        Map<Integer, QueueData> result = new HashMap<Integer, QueueData>(16);
        for (int i = 0; i < 16; i++) {
            result.put(i, map.get(i));
        }
        return result;
    }

    public QueueDataChannel getChannel(int bankIndex, int patternIndex, int toneIndex) {
        Map<Integer, QueueData> bankMap = getBankMap(bankIndex);
        QueueData queueData = bankMap.get(patternIndex);
        QueueDataChannel channel = queueData.getChannel(toneIndex);
        return channel;
    }

    @Override
    public void sleep() {
        for (Map<Integer, QueueData> bankMap : map.values()) {
            for (QueueData queueData : bankMap.values()) {
                queueData.sleep();
            }
        }
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        for (Map<Integer, QueueData> bankMap : map.values()) {
            for (QueueData queueData : bankMap.values()) {
                queueData.wakeup(controller);
            }
        }
    }

}
