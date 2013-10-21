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

package com.teotigraphix.caustk.tone;

import java.io.IOException;
import java.io.StringReader;

import com.google.gson.stream.JsonReader;

/**
 * Various static methods used with the various Tone framework.
 * 
 * @author Michael Schmalle
 */
public final class ToneUtils {

    public static int getComponentCount(Tone tone) {
        return tone.getComponentCount();
    }

    /**
     * Returns the specific tone class using the <code>toneType</code> property
     * in the JSON string.
     * 
     * @param data Valid serialized Tone data.
     * @throws IOException
     */
    public static Class<? extends Tone> getToneClass(String data) throws IOException {
        ToneType type = readToneType(data);
        switch (type) {
            case Bassline:
                return BasslineTone.class;
            case Beatbox:
                return BeatboxTone.class;
            case EightBitSynth:
                return EightBitSynth.class;
            case FMSynth:
                return FMSynthTone.class;
            case Modular:
                return ModularTone.class;
            case Organ:
                return OrganTone.class;
            case PadSynth:
                return PadSynthTone.class;
            case PCMSynth:
                return PCMSynthTone.class;
            case SubSynth:
                return SubSynthTone.class;
            case Vocoder:
                return VocoderTone.class;
        }
        return null;
    }

    /**
     * Reads and returns the {@link ToneType} from the JSON data.
     * 
     * @param data Valid serialized Tone data.
     * @throws IOException
     */
    public static ToneType readToneType(String data) throws IOException {
        JsonReader reader = new JsonReader(new StringReader(data));
        String type = null;
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("toneType")) {
                    type = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } finally {
            reader.close();
        }
        if (type != null)
            return ToneType.valueOf(type);

        return null;
    }

    public static void setName(Tone tone, String name) {
        tone.setNameInternal(name);
    }
}
