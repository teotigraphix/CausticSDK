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

package com.teotigraphix.caustic.internal.rack;

import java.io.File;

import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.rack.IRackSong;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class RackSong implements IRackSong
{

    //--------------------------------------------------------------------------
    //
    // IRackSong API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // name
    //----------------------------------

    private String mName;

    @Override
    public String getName()
    {
        return mName;
    }

    @Override
    public void setName(String value)
    {
        mName = value;
    }

    //----------------------------------
    // path
    //----------------------------------

    private String mPath;

    @Override
    public String getPath()
    {
        return mPath;
    }

    @Override
    public void setPath(String value)
    {
        mPath = value;
    }

    @Override
    public File getFile()
    {
        if (mPath == null)
            return null;
        return new File(mPath);
    }

    RackSong(String name)
    {
        setName(name);
    }

    @Override
    public void create(IRack rack)
    {
    }

    @Override
    public void load()
    {
    }

    @Override
    public void save(String name)
    {
    }

    @Override
    public void dispose()
    {
        setName(null);
    }
}
