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

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.part.Part;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.part.ITone;
import com.teotigraphix.caustic.song.IPartPattern;
import com.teotigraphix.caustic.song.IPartSong;
import com.teotigraphix.caustic.song.IPattern;
import com.teotigraphix.common.IMemento;

public class PartSong extends Song implements IPartSong {

    //----------------------------------
    //  patterns
    //----------------------------------

    private List<IPattern> mPatterns;

    private List<SongPatternInfo> mInfos;

    @Override
    public List<IPattern> getPatterns() {
        List<IPattern> result = new ArrayList<IPattern>();
        for (SongPatternInfo info : mInfos) {
            result.add(info.getPattern());
        }
        return result;
    }

    //@Override
    public void setPatterns(List<IPattern> value) {
        //mPatterns = value;
    }

    //----------------------------------
    //  selectedPattern
    //----------------------------------

    @Override
    public int getNumPatterns() {
        return getPatterns().size();
    }

    //----------------------------------
    //  selectedPattern
    //----------------------------------

    @Override
    public IPattern getSelectedPattern() {
        return getPatternAt(getCurrentBeat());
    }

    public PartSong(String name) {
        super(name);
        mInfos = new ArrayList<SongPatternInfo>();
    }

    @Override
    public void addPattern(IPartPattern pattern) {
        int bank = pattern.getBank();
        int index = pattern.getIndex();
        int endBar = mNextInsertBar + pattern.getLength();

        if (pattern instanceof IPartPattern) {
            IPartPattern pp = pattern;
            for (IPart part : pp.getParts()) {
                List<ITone> tones = ((Part)part).getTones();
                for (ITone tone : tones) {
                    try {
                        getProject().getWorkspace().getRack().getSequencer()
                                .addPattern(tone, bank, index, mNextInsertBar, endBar);
                    } catch (CausticException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        mNextInsertBar = endBar;
    }

    protected void add(IPattern pattern) {

        SongPatternInfo last = mInfos.get(mInfos.size() - 1);

        //mPatterns.add(pattern);

        int startBar = last.getEndBar();
        int endBar = startBar + pattern.getLength();

        SongPatternInfo info = new SongPatternInfo(pattern, startBar, endBar);
        mInfos.add(info);

        insert(info);
    }

    private void insert(SongPatternInfo info) {
        // to get the start wee need the pattern before this index
        // to get it's info
        if (info.getPattern() instanceof IPartPattern) {
            for (IPart part : ((IPartPattern)info.getPattern()).getParts()) {
                List<ITone> tones = ((Part)part).getTones();
                for (ITone tone : tones) {
                    try {
                        getProject()
                                .getWorkspace()
                                .getRack()
                                .getSequencer()
                                .addPattern(tone, info.getPattern().getBank(),
                                        info.getPattern().getIndex(), info.getStartBar(),
                                        info.getEndBar());
                    } catch (CausticException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected void add(int index, IPattern pattern) {
        mPatterns.add(index, pattern);
    }

    @Override
    public void addPatternAt(IPartPattern pattern, int startBar, int endBar) {

        if (hasPatternAt(startBar)) {
            removePatternAt(startBar, endBar);
        }

        // TODO for now the ISong API supports 1 bar pattern insertions
        SongPatternInfo info = new SongPatternInfo(pattern, startBar, endBar);
        // NOTE: there will need to be custom logic when multiple spanning
        // patterns are allowed, suchas a pattern spanning 4 bars would either
        // take up 4 info slots OR the info would be smart enough to  duplicate
        // itself 4 times as it's being exported or played
        mInfos.add(info);

        // XXX This is going to have to loop through all parts and add them
        // for any step sequencing this is how it will be done, all parts are
        // added to the song sequencer, they will just be muted or have no data
        //		for (IPart part : pattern.getParts()) {
        //			IMachine machine = part.getMachine();

        //			getProject()
        //					.getRack()
        //					.getSequencer()
        //					.sendPatternEvent(machine, startBar, pattern.getBank(),
        //							pattern.getIndex(), endBar);
        //		}

        firePatternAdd(info);
    }

    @Override
    public IPartPattern removePatternAt(int startBar, int endBar) {
        SongPatternInfo info = getSongInfoAt(startBar);
        if (info == null)
            return null;
        IPattern pattern = info.getPattern();
        mInfos.remove(info);

        //		for (IPart part : pattern.getParts()) {

        //			IMachine machine = part.getMachine();
        //			getProject().getRack().getSequencer()
        //					.sendPatternEvent(machine, startBar, -1, -1, endBar);
        //getProject()
        //		.getRack()
        //		.getEngine()
        //		.sendMessage(
        //				"/caustic/sequencer/pattern_event subsynth1 0 -1");
        //		}

        firePatternRemove(info);
        return (IPartPattern)pattern;
    }

    @Override
    public List<IPartPattern> removeAllPatterns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPartPattern getPatternAt(int bar) {
        for (SongPatternInfo info : mInfos) {
            if (info.getStartBar() == bar)
                return (IPartPattern)info.getPattern();
        }
        return null;
    }

    @Override
    public boolean hasPatternAt(int bar) {
        for (SongPatternInfo info : mInfos) {
            if (info.getStartBar() == bar)
                return true;
        }
        return false;
    }

    protected SongPatternInfo getSongInfoAt(int bar) {
        for (SongPatternInfo info : mInfos) {
            if (info.getStartBar() == bar)
                return info;
        }
        return null;
    }

    @Override
    public void copy(IMemento memento) {
        super.copy(memento);
        memento.putString("name", "Foo");
        for (IPattern pattern : getPatterns()) {
            pattern.copy(memento.createChild("pattern"));
        }
    }

    @Override
    public void paste(IMemento memento) {
        super.paste(memento);
        for (IPattern pattern : getPatterns()) {
            pattern.paste(memento.getChild("pattern"));
        }
    }
}
