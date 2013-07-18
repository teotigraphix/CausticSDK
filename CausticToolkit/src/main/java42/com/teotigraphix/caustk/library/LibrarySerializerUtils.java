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

package com.teotigraphix.caustk.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.teotigraphix.caustic.core.XMLMemento;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.vo.EffectRackInfo;
import com.teotigraphix.caustk.library.vo.MixerPanelInfo;
import com.teotigraphix.caustk.library.vo.RackInfo;
import com.teotigraphix.caustk.tone.Tone;

public class LibrarySerializerUtils {

    //    public static RackInfo createRackInfo(IRack rack) {
    //        RackInfo info = new RackInfo();
    //        XMLMemento memento = XMLMemento.createWriteRoot("rack");
    //        for (IMachine machine : rack.getMachineMap().values()) {
    //            if (machine != null) {
    //                IMemento child = memento.createChild("machine");
    //                child.putInteger("index", machine.getIndex());
    //                child.putInteger("active", machine != null ? 1 : 0);
    //                child.putString("id", machine.getId());
    //                child.putString("type", machine.getType().getValue());
    //            }
    //        }
    //        info.setData(memento.toString());
    //        return info;
    //    }

    // called from TrackUtils
    public static RackInfo createRackInfo(ICaustkController controller) {
        RackInfo info = new RackInfo();
        XMLMemento memento = XMLMemento.createWriteRoot("rack");
        if (controller.getSoundSource().getTones().size() > 0) {
            for (int i = 0; i < 6; i++) {
                Tone tone = controller.getSoundSource().getTone(i);
                //                IMachine machine = tone.getMachine();
                //                if (machine != null) {
                //                    IMemento child = memento.createChild("machine");
                //                    child.putInteger("index", i);
                //                    child.putInteger("active", machine != null ? 1 : 0);
                //                    child.putString("id", machine.getId());
                //                    child.putString("type", machine.getType().getValue());
                //                }
            }
        }
        info.setData(memento.toString());
        return info;
    }

    //    public static RackInfo createRackInfo(Rack rack, HashMap<Integer, LibraryPatch> patches) {
    //        RackInfo info = new RackInfo();
    //        XMLMemento memento = XMLMemento.createWriteRoot("rack");
    //        for (int i = 0; i < 6; i++) {
    //            IMachine machine = rack.getMachine(i);
    //            IMemento child = memento.createChild("machine");
    //            child.putInteger("index", i);
    //            child.putInteger("active", machine != null ? 1 : 0);
    //            LibraryPatch patch = patches.get(i);
    //            if (patch != null)
    //                child.putString("patchId", patch.getId().toString());
    //            if (machine != null) {
    //                child.putString("id", machine.getId());
    //                child.putString("type", machine.getType().getValue());
    //            }
    //        }
    //        info.setData(memento.toString());
    //        return info;
    //    }

    public static MixerPanelInfo createMixerPanelInfo(IMixerPanel mixerPanel) {
        MixerPanelInfo info = new MixerPanelInfo();
        XMLMemento memento = XMLMemento.createWriteRoot("mixer");
        mixerPanel.copy(memento);
        info.setData(memento.toString());
        return info;
    }

    //    public static MixerPanelInfo createMixerPanelInfo(Rack rack) {
    //        return createMixerPanelInfo(rack.getMixerPanel());
    //    }

    public static EffectRackInfo createEffectRackInfo(IEffectsRack effectsRack) {
        EffectRackInfo info = new EffectRackInfo();
        XMLMemento memento = XMLMemento.createWriteRoot("effects");
        effectsRack.copy(memento);
        info.setData(memento.toString());
        return info;
    }

    //    public static EffectRackInfo createEffectRackInfo(Rack rack) {
    //        return createEffectRackInfo(rack.getEffectsRack());
    //    }

    public static String md5(File file) throws NoSuchAlgorithmException, IOException {
        //        try {
        //            String s = PULSAR_CAUSTIC.getAbsolutePath();
        //            MessageDigest md5 = MessageDigest.getInstance("MD5");
        //            md5.update(s.getBytes(), 0, s.length());
        //            String signature = new BigInteger(1, md5.digest()).toString(16);
        //            System.out.println("Signature: " + signature);
        //
        //        } catch (final NoSuchAlgorithmException e) {
        //            e.printStackTrace();
        //        }
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(file);

        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }

        fis.close();

        byte[] mdbytes = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //System.out.println("Digest(in hex format):: " + sb.toString());
        //assertEquals("8ff156ff36376d2517cef39cdd43876a", sb.toString());
        return sb.toString();
    }

    public static String toMD5String(File file, String name) {
        String id = null;
        try {
            id = LibrarySerializerUtils.md5(file);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id + "-" + name;
    }
}
