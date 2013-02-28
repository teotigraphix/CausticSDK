////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.internal.machine;

import com.teotigraphix.caustic.filter.IVolumeComponent;
import com.teotigraphix.caustic.internal.filter.VolumeComponent;
import com.teotigraphix.caustic.internal.sampler.BeatboxSampler;
import com.teotigraphix.caustic.machine.IBeatbox;
import com.teotigraphix.caustic.sampler.IBeatboxSampler;

/**
 * The default implementation of the {@link IBeatbox} interface.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Beatbox extends Synth implements IBeatbox {

    //--------------------------------------------------------------------------
    //
    // IBeatbox API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // volume
    //----------------------------------

    private IVolumeComponent mVolume;

    @Override
    public IVolumeComponent getVolume() {
        return mVolume;
    }

    public void setVolume(IVolumeComponent value) {
        mVolume = value;
    }

    //----------------------------------
    // sampler
    //----------------------------------

    private IBeatboxSampler mSampler;

    @Override
    public IBeatboxSampler getSampler() {
        return mSampler;
    }

    public void setSampler(IBeatboxSampler value) {
        mSampler = value;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    /**
     * Constructor.
     */
    public Beatbox(String id) {
        super();
        setId(id);
    }

    //--------------------------------------------------------------------------
    //
    // Overridden Public :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
        super.createComponents();
        setVolume(new VolumeComponent(this));
        setSampler(new BeatboxSampler(this));
    }

    @Override
    public void restore() {
        getSequencer().restore();
        getSampler().restore();
    }

    @Override
    public void dispose() {
        super.dispose();
        setVolume(null);
        setSampler(null);
        setSequencer(null);
    }
}
