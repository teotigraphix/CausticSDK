
package com.teotigraphix.ambientmallets;

import java.io.File;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class AmbientMalletsSkinCompiler {

    public static void main(String[] args) {
        Settings settings = new Settings();
        settings.maxWidth = 1024;
        settings.maxHeight = 1024;
        settings.filterMin = TextureFilter.Linear;
        settings.filterMag = TextureFilter.Linear;
        File directory = new File(".").getAbsoluteFile().getParentFile().getParentFile();
        TexturePacker2.process(settings, new File(directory,
                "ambient-mallets-android/assets/images").getPath(),
                "../ambient-mallets-android/assets", "skin");
    }
}
