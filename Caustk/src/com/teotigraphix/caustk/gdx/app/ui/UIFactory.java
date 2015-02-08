////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.gdx.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.google.inject.Inject;
import com.teotigraphix.caustk.gdx.app.IProjectModel;
import com.teotigraphix.caustk.gdx.scene2d.ui.Knob.KnobStyle;
import com.teotigraphix.caustk.gdx.scene2d.ui.ListRowRenderer.ListRowRendererStyle;
import com.teotigraphix.caustk.gdx.scene2d.ui.PaneStack.PaneStackStyle;
import com.teotigraphix.caustk.gdx.scene2d.ui.TextKnob.TextKnobStyle;
import com.teotigraphix.caustk.gdx.scene2d.ui.TextSlider.TextSliderStyle;
import com.teotigraphix.caustk.gdx.scene2d.ui.app.FileExplorer;
import com.teotigraphix.caustk.gdx.scene2d.ui.app.ModePane.ModePaneStyle;
import com.teotigraphix.caustk.gdx.scene2d.ui.app.PatternPane.PatternPaneStyle;
import com.teotigraphix.caustk.gdx.scene2d.ui.app.ViewStack.ViewStackStyle;
import com.teotigraphix.caustk.gdx.scene2d.ui.mixer.MixerPaneItem.MixerPaneItemStyle;

/*
 * - All required styles must be in the StyleClass constructor
 * - All base component styles have the 'default' styleName @see TopBarFactory
 * - The client Behavior will parent ONLY the top level component created
 *   within the factory, all other children are parented within the factories
 *   create method.
 * - All factories requiring fonts, declare them as constants
 */

public abstract class UIFactory {

    @Inject
    private IProjectModel projectModel;

    protected IProjectModel getProjectModel() {
        return projectModel;
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
        initializetButton(skin);
        initializeTextButton(skin);

        initializeScrollPaneStyle(skin);
        initializeListStyle(skin);
        initializePaneStack(skin);
        initializeSlider(skin);
        initializeProgressBar(skin);
        initializeTextSlider(skin);
        initializeViewStack(skin);
        initializeWindow(skin);

        initializeSelectBox(skin);

        initializeModePane(skin);
        initializePatternPane(skin);
        initializeMixerPane(skin);
        initializeKnob(skin);
    }

    private void initializeSelectBox(Skin skin) {
        SelectBoxStyle style = new SelectBoxStyle(skin.getFont(StylesDefault.Font), Color.WHITE,
                skin.getDrawable(StylesDefault.SelectBox_background),
                skin.get(ScrollPaneStyle.class), skin.get(ListStyle.class));
        skin.add(StylesDefault.SelectBox, style);
    }

    private void initializeKnob(Skin skin) {
        KnobStyle kstyle = new KnobStyle();
        kstyle.background = skin.getDrawable("defaults/Knob_background");
        kstyle.knob = skin.getDrawable("defaults/Knob_knob");
        skin.add("default", kstyle);

        TextKnobStyle style = new TextKnobStyle();
        style.background = skin.getDrawable("defaults/Knob_background");
        style.knob = skin.getDrawable("defaults/Knob_knob");
        style.font = skin.getFont("default-font");
        skin.add("default", style);
    }

    private void initializeMixerPane(Skin skin) {
        MixerPaneItemStyle itemStyle = new MixerPaneItemStyle(
                skin.getDrawable(StylesDefault.MixerPaneItem_background),
                skin.getDrawable(StylesDefault.MixerPaneItem_outline));
        skin.add(StylesDefault.MixerPaneItem, itemStyle);
    }

    protected void initializeTextSlider(Skin skin) {
        TextSliderStyle verticalStyle = new TextSliderStyle(
                skin.getDrawable(StylesDefault.TextSlider_background),
                skin.getDrawable(StylesDefault.TextSlider_knob), skin.getFont(StylesDefault.Font),
                Color.WHITE);
        skin.add(StylesDefault.TextSlider_Veritical, verticalStyle);
    }

    protected void initializeSlider(Skin skin) {
        SliderStyle verticalStyle = new SliderStyle(
                skin.getDrawable(StylesDefault.Slider_background),
                skin.getDrawable(StylesDefault.Slider_knob));
        skin.add(StylesDefault.Slider_Veritical, verticalStyle);
        SliderStyle horizontalStyle = new SliderStyle(
                skin.getDrawable(StylesDefault.Slider_background),
                skin.getDrawable(StylesDefault.Slider_knob));
        skin.add(StylesDefault.Slider_Horizontal, horizontalStyle);
    }

    protected void initializeProgressBar(Skin skin) {
        ProgressBarStyle verticalStyle = new ProgressBarStyle(
                skin.getDrawable(StylesDefault.Slider_background),
                skin.getDrawable(StylesDefault.Slider_knob));
        skin.add(StylesDefault.ProgressBar_Veritical, verticalStyle);
        ProgressBarStyle horizontalStyle = new ProgressBarStyle(
                skin.getDrawable(StylesDefault.Slider_background),
                skin.getDrawable(StylesDefault.Slider_knob));
        skin.add(StylesDefault.ProgressBar_Horizontal, horizontalStyle);
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

    protected void initializetButton(Skin skin) {
        ButtonStyle style = new ButtonStyle(skin.getDrawable(StylesDefault.TextButton_up),
                skin.getDrawable(StylesDefault.TextButton_down),
                skin.getDrawable(StylesDefault.TextButton_checked));
        style.disabled = skin.getDrawable(StylesDefault.TextButton_disabled);
        skin.add(StylesDefault.TextButton, style);
    }

    protected void initializeTextButton(Skin skin) {
        TextButtonStyle style = new TextButtonStyle(skin.getDrawable(StylesDefault.TextButton_up),
                skin.getDrawable(StylesDefault.TextButton_down),
                skin.getDrawable(StylesDefault.TextButton_checked),
                skin.getFont(StylesDefault.Font));
        style.disabled = skin.getDrawable(StylesDefault.TextButton_disabled);
        skin.add(StylesDefault.Button, style);
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
    // FileExplorer
    //----------------------------------

    // TODO Fix style or delete
    public FileExplorer createFileExplorer(Skin skin, int type) {
        FileExplorer fileExplorer = new FileExplorer("Foo", skin, "default", "default");
        return fileExplorer;
    }

}
