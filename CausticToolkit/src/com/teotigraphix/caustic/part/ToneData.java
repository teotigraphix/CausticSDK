////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.part;

import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.IPersist;

/**
 * A data object for the ITone.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class ToneData implements IPersist {

    private static final String ATT_NAME = "name";

    private static final String TAG_DESCRIPTION = "description";

    private static final String ATT_AUTHOR = "author";

    private static final String ATT_TAGS = "tags";

    //--------------------------------------------------------------------------
    // 
    //  Public :: Properties
    // 
    //--------------------------------------------------------------------------

    //----------------------------------
    //  name
    //----------------------------------

    private String mName;

    /**
     * The name of the tone.
     */
    public final String getName() {
        return mName;
    }

    public final void setName(String name) {
        this.mName = name;
    }

    //----------------------------------
    //  description
    //----------------------------------

    private String mDescription;

    /**
     * A description of the tone.
     */
    public final String getDescription() {
        return mDescription;
    }

    public final void setDescription(String description) {
        this.mDescription = description;
    }

    //----------------------------------
    //  author
    //----------------------------------

    private String mAuthor;

    /**
     * The author of the tone.
     */
    public final String getAuthor() {
        return mAuthor;
    }

    public final void setAuthor(String author) {
        this.mAuthor = author;
    }

    //----------------------------------
    //  tags
    //----------------------------------

    private String mTags;

    /**
     * Tags that describe the tone, used for searching and sorting.
     */
    public final String getTags() {
        return mTags;
    }

    public final void setTags(String tags) {
        this.mTags = tags;
    }

    //--------------------------------------------------------------------------
    // 
    //  IPersistable API :: Methods
    // 
    //--------------------------------------------------------------------------

    @Override
    public void copy(IMemento memento) {
        memento.putString(ATT_NAME, mName);
        if (mDescription != null)
            memento.createChild(TAG_DESCRIPTION).putTextData(mDescription);
        if (mAuthor != null)
            memento.putString(ATT_AUTHOR, mAuthor);
        if (mTags != null)
            memento.putString(ATT_TAGS, mTags);
    }

    @Override
    public void paste(IMemento memento) {
        //setName(memento.getString(ATT_NAME));
        //setDescription(memento.getChild(TAG_DESCRIPTION).getTextData());
        //setAuthor(memento.getString(ATT_AUTHOR));
        //setTags(memento.getString(ATT_TAGS));
    }
}
