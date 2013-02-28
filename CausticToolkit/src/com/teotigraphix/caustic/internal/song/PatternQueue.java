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

package com.teotigraphix.caustic.internal.song;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.sequencer.IPatternSequencer;
import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.song.ISong.ISongListener;
import com.teotigraphix.caustic.song.ITrack;

public class PatternQueue implements ISongListener {

    private static final int EMPTY_BANK = 3;

    private static final int EMPTY_PATTERN = 15;

    private static final String TAG = "PatternQueue";

    private List<PatternQueueData> mQueue = new ArrayList<PatternQueueData>();

    private List<PatternQueueData> mQueuePlaying = new ArrayList<PatternQueueData>();

    private boolean mRecordMode;

    public final boolean isRecordMode() {
        return mRecordMode;
    }

    public final void setRecordMode(boolean value) {
        mRecordMode = value;
    }

    final List<PatternQueueData> getQueue() {
        return mQueue;
    }

    final List<PatternQueueData> getQueuePlaying() {
        return mQueuePlaying;
    }

    private List<QueueListener> mListeners = new ArrayList<QueueListener>();

    public PatternQueue() {
    }

    public void touch(PatternQueueData data) {
        if (data.isQueueMode()) {
            // if item is scheduled or playing
            if (mQueue.contains(data) || mQueuePlaying.contains(data)) {
                if (data.getState() == PatternState.PLAYING) {
                    // schedule removal next measure
                    data.setState(PatternState.REMOVING);
                } else {
                    // remove a queued item before it has entered the play queue
                    mQueue.remove(data);
                    data.setState(PatternState.IDLE);
                    fireOnRemoved(data);
                    System.out.println(TAG + " remove scheduled item "
                            + data.getPhrase().toString());
                }
            } else {
                // schedule the item to be played next measure
                checkForExistingPatternInTrack(data);
                mQueue.add(data);
                data.setState(PatternState.INDETERMINATE);
                System.out.println(TAG + " schedule item " + data.getPhrase().toString());
            }
        } else {
            //model.playPattern(data);
        }
    }

    private void checkForExistingPatternInTrack(PatternQueueData data) {
        // get all of phrase
        List<PatternQueueData> result = new ArrayList<PatternQueueData>();
        for (PatternQueueData item : mQueue) {
            if (item.getTrack() == data.getTrack()) {
                result.add(item);
            }
        }

        if (result.size() == 1) {
            PatternQueueData remove = result.get(0);
            mQueue.remove(remove);
            remove.setState(PatternState.IDLE);
            fireOnRemoved(remove);
            System.out.println(TAG + " remove multi-track item " + remove.getPhrase().toString());
        }

    }

    private void remove(PatternQueueData data) {
        if (data.isQueueMode()) {
            if (data.getState() == PatternState.PLAYING) {
                data.setState(PatternState.REMOVING);
            } else if (data.getState() == PatternState.REMOVING) {
                data.setState(PatternState.IDLE);
                if (mQueue.contains(data)) {
                    mQueue.remove(data);
                }

                if (mQueuePlaying.contains(data)) {
                    mQueuePlaying.remove(data);
                    playEmptyPattern(data);
                }

                fireOnRemoved(data);
            }
        }
    }

    private void fireOnRemoved(PatternQueueData data) {
        for (QueueListener listener : mListeners) {
            listener.onRemoved(data);
        }
    }

    private void playEmptyPattern(PatternQueueData data) {
        IPatternSequencer sequencer = data.getSequencer();
        sequencer.setBankPattern(EMPTY_BANK, EMPTY_PATTERN);
    }

    private void addTrackPhrase(PatternQueueData data) {
        ITrack track = data.getTrack();

        // XXX ????
        data.getSequencer().setBankPattern(data.getPhrase().getBank(), data.getPhrase().getIndex());

        if (mRecordMode) {

            try {
                track.addPattern(data.getPhrase());
            } catch (CausticException e) {
                e.printStackTrace();
            }
        }
    }

    public static class PatternQueueData {

        private ITrack mTrack;

        private View mView;

        public final ITrack getTrack() {
            return mTrack;
        }

        public final IPatternSequencer getSequencer() {
            //return mPhrase.getSequencer();
            return mTrack.getPart().getTone().getSequencer();
        }

        public PatternQueueData(ITrack track, IPhrase phrase, View button) {
            mTrack = track;
            setPhrase(phrase);
            mView = button;
        }

        //----------------------------------
        //  pattern
        //----------------------------------

        private IPhrase mPhrase;

        public IPhrase getPhrase() {
            return mPhrase;
        }

        void setPhrase(IPhrase value) {
            mPhrase = value;
        }

        //----------------------------------
        //  queueMode
        //----------------------------------

        private boolean mQueueMode = true;

        public boolean isQueueMode() {
            return mQueueMode;
        }

        public void setQueueMode(boolean value) {
            mQueueMode = value;
        }

        //----------------------------------
        //  loop
        //----------------------------------

        private boolean mLoop = false;

        public boolean isLoop() {
            return mLoop;
        }

        public void setLoop(boolean value) {
            mLoop = value;
        }

        //----------------------------------
        //  state
        //----------------------------------

        private PatternState mState = PatternState.IDLE;

        public PatternState getState() {
            return mState;
        }

        public void setState(PatternState value) {
            mState = value;
            if (mView != null) {
                mView.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.invalidate();
                    }
                });
            }
        }

        @Override
        public String toString() {
            return "PatternData (" + mPhrase.getIndex() + ")[mPhrase=" + mPhrase + ", mState="
                    + mState + "]";
        }
    }

    public enum PatternState {
        IDLE, INDETERMINATE, PLAYING, REMOVING;
    }

    public void removeOthers(PatternQueueData data) {
        List<PatternQueueData> copy = new ArrayList<PatternQueueData>(mQueuePlaying);

        for (PatternQueueData target : copy) {
            if (target.getTrack().getIndex() == data.getTrack().getIndex()) {
                target.setState(PatternState.REMOVING);
            }
        }
    }

    @Override
    public void onBeatChanged(int beat, int oldBeat) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMeasureChanged(int measure, int oldMeasure) {
        List<PatternQueueData> playing = new ArrayList<PatternQueueData>(mQueuePlaying);
        Log.d(TAG, "onMeasureChanged() " + measure);

        // - remove one shot patterns
        for (PatternQueueData data : playing) {
            if (data.isLoop()) {
                if (data.getState() == PatternState.REMOVING) {
                    System.out.println(TAG + " remove loop " + data.getPhrase().toString());
                    remove(data);
                }
            } else {
                data.setState(PatternState.REMOVING);
                //mQueuePlaying.remove(data);
                System.out.println(TAG + " remove oneShot " + data.getPhrase().toString());
                remove(data);
            }
        }

        // - start queued patterns
        for (PatternQueueData data : mQueue) {
            mQueuePlaying.add(data);
        }

        mQueue.clear();

        // send all playing pattern events
        for (PatternQueueData data : mQueuePlaying) {
            data.setState(PatternState.PLAYING);
            addTrackPhrase(data);
        }
    }

    @Override
    public void onPatternAdded(SongPatternInfo info) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPatternRemoved(SongPatternInfo info) {
        // TODO Auto-generated method stub

    }

    public void addListener(QueueListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(QueueListener listener) {
        mListeners.remove(listener);
    }

    public interface QueueListener {
        void onAdded(PatternQueueData data);

        void onRemoved(PatternQueueData data);
    }
}
