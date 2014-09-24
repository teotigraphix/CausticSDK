
package com.teotigraphix.gdx.groove.ui.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.groove.ui.components.FileExplorer;
import com.teotigraphix.gdx.groove.ui.components.TopBar;
import com.teotigraphix.gdx.groove.ui.components.ViewStack;
import com.teotigraphix.gdx.groove.ui.components.ViewStack.ViewStackStyle;
import com.teotigraphix.gdx.scene2d.ui.ListRowRenderer.ListRowRendererStyle;
import com.teotigraphix.gdx.scene2d.ui.PaneStack.PaneStackStyle;

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
        initializeFonts(skin);

        initializeLabelStyle(skin);
        initializeTextButton(skin);

        initializeScrollPaneStyle(skin);
        initializeListStyle(skin);
        initializePaneStack(skin);
        initializeViewStack(skin);
        initializeWindow(skin);
    }

    protected void initializeFonts(Skin skin) {
        String name = "Ahaorni-14"; // Digital-18
        createFont(skin, StylesDefault.Font, name, "font/" + name + ".fnt");

        // TopBar XXX Should this be here?
        createFont(skin, TopBarFactory.Font_TextButton, name, "font/" + name + ".fnt");
    }

    protected BitmapFont createFont(Skin skin, String id, String name, String location) {
        BitmapFont font = new BitmapFont(Gdx.files.internal(location), skin.getRegion("fonts/"
                + name), false);
        skin.add(id, font);
        return font;
    }

    protected void initializeLabelStyle(Skin skin) {
        LabelStyle labelStyle = new LabelStyle(skin.getFont(StylesDefault.Font), Color.WHITE);
        skin.add(StylesDefault.Label, labelStyle);
    }

    protected void initializeTextButton(Skin skin) {
        TextButtonStyle style = new TextButtonStyle(skin.getDrawable(StylesDefault.TextButton_up),
                skin.getDrawable(StylesDefault.TextButton_down),
                skin.getDrawable(StylesDefault.TextButton_checked),
                skin.getFont(StylesDefault.Font));
        style.disabled = skin.getDrawable(StylesDefault.TextButton_disabled);
        skin.add(StylesDefault.TextButton, style);
    }

    protected void initializeScrollPaneStyle(Skin skin) {
        ScrollPaneStyle style = new ScrollPaneStyle();
        style.background = skin.getDrawable(StylesDefault.ScrollPane_background);
        style.vScrollKnob = skin.getDrawable(StylesDefault.ScrollPane_vScrollKnob);
        style.vScroll = skin.getDrawable(StylesDefault.ScrollPane_vScroll);
        style.hScrollKnob = skin.getDrawable(StylesDefault.ScrollPane_hScrollKnob);
        style.hScroll = skin.getDrawable(StylesDefault.ScrollPane_hScroll);
        skin.add(StylesDefault.ScrollPane, style);
    }

    protected void initializeListStyle(Skin skin) {
        //------------------------------
        // ListStyle
        //------------------------------

        ListStyle listStyle = new ListStyle();
        listStyle.background = skin.getDrawable(StylesDefault.List_background);
        listStyle.selection = skin.getDrawable(StylesDefault.List_selection);
        listStyle.font = skin.getFont(StylesDefault.Font);
        listStyle.fontColorSelected = Color.CYAN; // #0099FF
        listStyle.fontColorUnselected = Color.WHITE;
        skin.add(StylesDefault.List, listStyle);

        //------------------------------
        // ListRowRendererStyle
        //------------------------------

        ListRowRendererStyle rendererStyle = new ListRowRendererStyle();
        rendererStyle.background = skin.getDrawable(StylesDefault.ListRowRenderer_background);
        rendererStyle.selection = skin.getDrawable(StylesDefault.ListRowRenderer_selection);
        rendererStyle.down = skin.getDrawable(StylesDefault.ListRowRenderer_selection);
        rendererStyle.font = skin.getFont(StylesDefault.Font);
        rendererStyle.fontColor = Color.WHITE;
        rendererStyle.padding = 8f;
        skin.add(StylesDefault.ListRowRenderer, rendererStyle);
    }

    protected void initializePaneStack(Skin skin) {
        TextButtonStyle tabButtonStyle = new TextButtonStyle(
                skin.getDrawable(StylesDefault.PaneStack_TabButton_up),
                skin.getDrawable(StylesDefault.PaneStack_TabButton_down),
                skin.getDrawable(StylesDefault.PaneStack_TabButton_down),
                skin.getFont(StylesDefault.Font));
        skin.add(StylesDefault.PaneStack_TabButton, tabButtonStyle);

        PaneStackStyle paneStackStyle = new PaneStackStyle();
        paneStackStyle.background = skin.getDrawable(StylesDefault.PaneStack_background);
        paneStackStyle.padding = 2f;
        paneStackStyle.tabBarGap = 1f;
        paneStackStyle.tabBarThickness = 50;
        paneStackStyle.tabStyle = skin
                .get(StylesDefault.PaneStack_TabButton, TextButtonStyle.class);
        skin.add(StylesDefault.PaneStack, paneStackStyle);
    }

    protected void initializeViewStack(Skin skin) {
        ViewStackStyle style = new ViewStackStyle(
                skin.getDrawable(StylesDefault.ViewStack_background));
        skin.add(StylesDefault.ViewStack, style);
    }

    protected void initializeWindow(Skin skin) {
        WindowStyle windowStyle = new WindowStyle(skin.getFont(StylesDefault.Font), Color.WHITE,
                skin.getDrawable(StylesDefault.Window_background));
        skin.add(StylesDefault.Window, windowStyle);
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

    // TODO Fix style or delete
    public FileExplorer createFileExplorer(Skin skin, int type) {
        FileExplorer fileExplorer = new FileExplorer("Foo", skin, "default", "default");
        return fileExplorer;
    }

}
