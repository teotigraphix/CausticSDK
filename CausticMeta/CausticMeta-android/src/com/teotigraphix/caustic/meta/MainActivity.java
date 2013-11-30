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

package com.teotigraphix.caustic.meta;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.teotigraphix.caustic.meta.model.FileModel;
import com.teotigraphix.caustic.meta.view.MainActivityMediator;
import com.teotigraphix.caustk.core.CaustkActivity;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * @author Michael Schmalle
 */
public class MainActivity extends CaustkActivity {

    static {
        // by setting this constant, the RuntimeUtils class can be used
        // to get File locations and easy file access to the /caustic/* directories
        RuntimeUtils.STORAGE_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    FileModel fileModel;

    MainActivityMediator mainActivityMediator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileModel = new FileModel(this);

        setContentView(R.layout.main);

        mainActivityMediator = new MainActivityMediator(this, fileModel);
        mainActivityMediator.onAttach();
        mainActivityMediator.onCreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                File file = (File)data.getSerializableExtra(FileExplorer.EXTRA_FILE);
                try {
                    fileModel.setFile(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    protected int getActivationKey() {
        return 0xA8DF832A;
    }
}
