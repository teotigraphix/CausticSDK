
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
import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.teotigraphix.gdx.groove.ui.components.FileExplorer;
import com.teotigraphix.gdx.groove.ui.components.ModePane.ModePaneStyle;
import com.teotigraphix.gdx.groove.ui.components.PatternPane.PatternPaneStyle;
import com.teotigraphix.gdx.groove.ui.components.TopBar;
import com.teotigraphix.gdx.groove.ui.components.TopBar.TopBarStyle;
import com.teotigraphix.gdx.groove.ui.components.TopBarListener.TopBarEvent;
import com.teotigraphix.gdx.groove.ui.components.TopBarListener.TopBarEventKind;
import com.teotigraphix.gdx.groove.ui.components.ViewStack;
import com.teotigraphix.gdx.groove.ui.components.ViewStack.ViewStackStyle;
import com.teotigraphix.gdx.groove.ui.components.ViewStackData;
import com.teotigraphix.gdx.groove.ui.model.IUIModel;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;
import com.teotigraphix.gdx.scene2d.ui.ButtonBarListener;
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

public abstract class UIFactory {

    //--------------------------------------------------------------------------
    // Inject
    //--------------------------------------------------------------------------

    @Inject
    private IUIModel uiModel;

    @SuppressWarnings("unchecked")
    protected <T extends IUIModel> T getModel() {
        return (T)uiModel;
    }

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

        initializeModePane(skin);
        initializePatternPane(skin);
    }

    protected void initializeFonts(Skin skin) {
        String name = "Ahaorni-14"; // Digital-18
        createFont(skin, StylesDefault.Font, name, "font/" + name + ".fnt");
        createFont(skin, StylesDefault.Font_TopBar_TextButton, name, "font/" + name + ".fnt");
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
        WindowStyle toastStyle = new WindowStyle(skin.getFont(StylesDefault.Font), Color.WHITE,
                skin.getDrawable(StylesDefault.Toast_background));
        skin.add(StylesDefault.Toast, toastStyle);

        WindowStyle windowStyle = new WindowStyle(skin.getFont(StylesDefault.Font), Color.WHITE,
                skin.getDrawable(StylesDefault.Window_background));
        skin.add(StylesDefault.Window, windowStyle);
    }

    private void initializeModePane(Skin skin) {
        TextButtonStyle buttonStyle = skin.get(StylesDefault.TextButton, TextButtonStyle.class);
        ModePaneStyle modePaneStyle = new ModePaneStyle(buttonStyle, 15);
        skin.add(StylesDefault.ModePane, modePaneStyle);
    }

    private void initializePatternPane(Skin skin) {

        TextButtonStyle bankButtonStyle = new TextButtonStyle(
                skin.getDrawable(StylesDefault.PatternPane_Pad_up),
                skin.getDrawable(StylesDefault.PatternPane_Pad_down),
                skin.getDrawable(StylesDefault.PatternPane_Pad_checked),
                skin.getFont(StylesDefault.Font));

        TextButtonStyle padButtonStyle = new TextButtonStyle(
                skin.getDrawable(StylesDefault.PatternPane_Pad_up),
                skin.getDrawable(StylesDefault.PatternPane_Pad_down),
                skin.getDrawable(StylesDefault.PatternPane_Pad_checked),
                skin.getFont(StylesDefault.Font));

        TextButtonStyle lengthButtonStyle = new TextButtonStyle(
                skin.getDrawable(StylesDefault.PatternPane_Pad_up),
                skin.getDrawable(StylesDefault.PatternPane_Pad_down),
                skin.getDrawable(StylesDefault.PatternPane_Pad_checked),
                skin.getFont(StylesDefault.Font));

        bankButtonStyle.fontColor = Color.BLACK;
        padButtonStyle.fontColor = Color.BLACK;
        lengthButtonStyle.fontColor = Color.BLACK;

        bankButtonStyle.disabledFontColor = Color.WHITE;
        padButtonStyle.disabledFontColor = Color.WHITE;
        lengthButtonStyle.disabledFontColor = Color.WHITE;
        bankButtonStyle.disabled = skin.getDrawable(StylesDefault.PatternPane_Pad_disabled);
        padButtonStyle.disabled = skin.getDrawable(StylesDefault.PatternPane_Pad_disabled);
        lengthButtonStyle.disabled = skin.getDrawable(StylesDefault.PatternPane_Pad_disabled);

        PatternPaneStyle style = new PatternPaneStyle(bankButtonStyle, padButtonStyle,
                lengthButtonStyle);
        skin.add(StylesDefault.PatternPane, style);
    }

    //----------------------------------
    // TopBar
    //----------------------------------

    public final TopBar createTopBar(Skin skin) {

        // TopBarStyle
        TopBarStyle topBarStyle = new TopBarStyle(skin.getDrawable(StylesDefault.TopBar_background));
        skin.add(StylesDefault.TopBar, topBarStyle);

        //TopBarStyle.TextButton
        TextButtonStyle textButtonStyle = new TextButtonStyle(
                skin.getDrawable(StylesDefault.TopBar_TextButton_up),
                skin.getDrawable(StylesDefault.TopBar_TextButton_checked),
                skin.getDrawable(StylesDefault.TopBar_TextButton_checked),
                skin.getFont(StylesDefault.Font_TopBar_TextButton));
        skin.add(StylesDefault.TopBar_TextButton, textButtonStyle);

        // Create TopBar
        TopBar instance = new TopBar(skin);
        instance.create(StylesDefault.TopBar);

        ButtonBar buttonBar = (ButtonBar)createTopBar_Center(skin);
        instance.getCenterChild().add(buttonBar).expand().fill();

        return instance;
    }

    public final Table createTopBar_Center(Skin skin) {
        IUIModel model = getModel();

        Array<ButtonBarItem> buttons = model.getButtons();

        TextButtonStyle buttonStyle = skin.get("TopBar.TextButton", TextButtonStyle.class);
        final ButtonBar instance = new ButtonBar(skin, buttons, false, buttonStyle);
        instance.addListener(new ButtonBarListener() {
            @Override
            public void selectedIndexChange(int selectedIndex) {
                TopBarEvent event = new TopBarEvent(TopBarEventKind.ViewIndexChange, selectedIndex);
                instance.fire(event);
            }
        });
        instance.setMaxButtonSize(80f);
        instance.create("default");
        return instance;
    }

    //----------------------------------
    // ViewStack
    //----------------------------------

    public final ViewStack createViewStack(Skin skin) {

        IUIModel model = getModel();

        ViewStack instance = new ViewStack(skin);
        for (ViewStackData data : model.getViews()) {
            data.getBehavior().setData(data);
            Table table = data.getBehavior().create();
            instance.addView(table);
        }

        instance.create(StylesDefault.ViewStack);

        return instance;
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
