
package com.teotigraphix.gdx.groove.ui.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.groove.ui.components.FileExplorer;
import com.teotigraphix.gdx.groove.ui.components.TopBar;
import com.teotigraphix.gdx.groove.ui.components.ViewStack;
import com.teotigraphix.gdx.scene2d.ui.ListRowRenderer.ListRowRendererStyle;

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

    //--------------------------------------------------------------------------
    // Inject
    //--------------------------------------------------------------------------

    @Inject
    private TopBarFactory topBarFactory;

    @Inject
    private ViewStackFactory viewStackFactory;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public UIFactory() {
    }

    //--------------------------------------------------------------------------
    // Creation :: Methods
    //--------------------------------------------------------------------------

    public void createDefaults(Skin skin) {
        createFonts(skin);
        // XXX Turn this into an interface IStyleNames, then subclass for UIFactoryImpl style names
        initializeScrollPaneStyle(skin);
        initializeListStyle(skin);
    }

    protected void initializeScrollPaneStyle(Skin skin) {
        ScrollPaneStyle style = new ScrollPaneStyle();
        style.background = skin.getDrawable("defaults/ScrollPane_background");
        style.vScrollKnob = skin.getDrawable("defaults/ScrollPane_vScrollKnob");
        style.vScroll = skin.getDrawable("defaults/ScrollPane_vScroll");
        style.hScrollKnob = skin.getDrawable("defaults/ScrollPane_hScrollKnob");
        style.hScroll = skin.getDrawable("defaults/ScrollPane_hScroll");
        skin.add("default", style);
    }

    protected void initializeListStyle(Skin skin) {
        //------------------------------
        // ListStyle
        //------------------------------

        ListStyle listStyle = new ListStyle();
        listStyle.background = skin.getDrawable("defaults/List_background");
        listStyle.selection = skin.getDrawable("defaults/List_selection");
        listStyle.font = skin.getFont("default-font");
        listStyle.fontColorSelected = Color.CYAN; // #0099FF
        listStyle.fontColorUnselected = Color.WHITE;
        skin.add("default", listStyle);

        //------------------------------
        // ListRowRendererStyle
        //------------------------------

        ListRowRendererStyle rendererStyle = new ListRowRendererStyle();
        rendererStyle.background = skin.getDrawable("defaults/ListRowRenderer_background");
        rendererStyle.selection = skin.getDrawable("defaults/ListRowRenderer_selection");
        rendererStyle.down = skin.getDrawable("defaults/ListRowRenderer_down");
        rendererStyle.font = skin.getFont("default-font");
        rendererStyle.fontColor = Color.WHITE;
        rendererStyle.padding = 8f;
        skin.add("default", rendererStyle);
    }

    private void createFonts(Skin skin) {
        createFont(skin, "default-font", "Eras-12-B", "font/Eras-12-B.fnt");
        // TopBar
        createFont(skin, TopBarFactory.Font_TextButton, "Eras-12-B", "font/Eras-12-B.fnt");

        // create the default Label style
        LabelStyle labelStyle = new LabelStyle(skin.getFont("default-font"), Color.WHITE);
        skin.add("default", labelStyle);
    }

    private BitmapFont createFont(Skin skin, String id, String name, String location) {
        BitmapFont font = new BitmapFont(Gdx.files.internal(location), skin.getRegion(name), false);
        skin.add(id, font);
        return font;
    }

    //----------------------------------
    // TopBar
    //----------------------------------

    public TopBar createTopBar(Skin skin) {
        return topBarFactory.createTopBar(skin);
    }

    public Table createTopBar_Center(Skin skin) {
        return topBarFactory.createTopBar_Center(skin);
    }

    //----------------------------------
    // ViewStack
    //----------------------------------

    public ViewStack createViewStack(Skin skin) {
        return viewStackFactory.createViewStack(skin);
    }

    //----------------------------------
    // FileExplorer
    //----------------------------------

    public FileExplorer createFileExplorer(Skin skin, int type) {
        FileExplorer fileExplorer = new FileExplorer("Foo", skin, "default", "default");
        return fileExplorer;
    }

}
