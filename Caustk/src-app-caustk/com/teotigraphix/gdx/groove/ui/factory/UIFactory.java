
package com.teotigraphix.gdx.groove.ui.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.groove.ui.components.TopBar;
import com.teotigraphix.gdx.groove.ui.components.ViewStack;

/*
 * - All required styles must be in the StyleClass constructor
 * - All base component styles have the 'default' styleName @see TopBarFactory
 * - The client Behavior will parent ONLY the top level component created
 *   within the factory, all other children are parented within the factories
 *   create method.
 * - All factories requiring fonts, declare them as constants
 */

@Singleton
public class UIFactory {

    private UIFactoryModel factoryModel;

    private TopBarFactory topBarFactory;

    private ViewStackFactory viewStackFactory;

    public UIFactoryModel getFactoryModel() {
        return factoryModel;
    }

    @Inject
    public void setFactoryModel(UIFactoryModel factoryModel) {
        this.factoryModel = factoryModel;
    }

    public UIFactory() {
        topBarFactory = new TopBarFactory(this);
        viewStackFactory = new ViewStackFactory(this);
    }

    public void createFonts(Skin skin) {
        createFont(skin, "default-font", "Eras-12-B", "font/Eras-12-B.fnt");
        // TopBar
        createFont(skin, TopBarFactory.Font_TextButton, "Eras-12-B", "font/Eras-12-B.fnt");
    }

    private BitmapFont createFont(Skin skin, String id, String name, String location) {
        BitmapFont font = new BitmapFont(Gdx.files.internal(location), skin.getRegion(name), false);
        skin.add(id, font);
        return font;
    }

    public TopBar createTopBar(Skin skin) {
        return topBarFactory.createTopBar(skin);
    }

    public Table createTopBar_Center(Skin skin) {
        return topBarFactory.createTopBar_Center(skin);
    }

    public ViewStack createViewStack(Skin skin) {
        return viewStackFactory.createViewStack(skin);
    }
}
