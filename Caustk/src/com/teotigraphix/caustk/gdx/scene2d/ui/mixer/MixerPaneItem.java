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

package com.teotigraphix.caustk.gdx.scene2d.ui.mixer;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.core.osc.MixerControls;
import com.teotigraphix.caustk.gdx.app.ui.StylesDefault;
import com.teotigraphix.caustk.gdx.scene2d.ui.TextKnob;
import com.teotigraphix.caustk.gdx.scene2d.ui.UITable;
import com.teotigraphix.caustk.gdx.scene2d.ui.auto.AutoTextSlider;
import com.teotigraphix.caustk.gdx.scene2d.ui.auto.AutomationItem;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.patch.MixerChannel;
import com.teotigraphix.caustk.node.master.MasterNode;

public class MixerPaneItem extends UITable {

    private Map<MixerControls, AutomationItem> automationItems = new HashMap<MixerControls, AutomationItem>();

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private int index;

    private MixerItemState currentState;

    private MixerPaneItemListener listener;

    private boolean updating;

    private Array<TextKnob> knobs = new Array<TextKnob>();

    private boolean selected;

    private boolean triggered;

    private Color machineColor;

    //--------------------------------------------------------------------------
    // Private Component :: Variables
    //--------------------------------------------------------------------------

    private Image outlineGlow;

    private TextButton stateButton;

    private TextButton soloButton;

    private TextButton muteButton;

    private AutoTextSlider volumeSlider;

    private TextKnob panKnob;

    private Cell<TextKnob> panCell;

    private Cell<Table> fxCell;

    private Cell<Table> eqCell;

    private TextKnob widthKnob;

    private TextKnob delayKnob;

    private TextKnob reverbKnob;

    private TextKnob highKnob;

    private TextKnob midKnob;

    private TextKnob bassKnob;

    private Table eqKnobRow;

    private Table fxKnobRow;

    private Table nameRow;

    @SuppressWarnings("unused")
    private Cell<Table> nameCell;

    private Label nameLabel;

    private Table paramTable;

    //--------------------------------------------------------------------------
    // Public :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Returns the channel's machine index.
     */
    public int getIndex() {
        return index;
    }

    //----------------------------------
    // machineColor
    //----------------------------------

    public void setMachineColor(Color machineColor) {
        this.machineColor = machineColor;
        setColors();
    }

    //----------------------------------
    // selected
    //----------------------------------

    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets selected state.
     * 
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        setColors();
    }

    //----------------------------------
    // triggered
    //----------------------------------

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MixerPaneItem(Skin skin, int index) {
        super(skin);
        this.index = index;
        setStyleClass(MixerPaneItemStyle.class);
    }

    //--------------------------------------------------------------------------
    // Public :: Methods
    //--------------------------------------------------------------------------

    public void redraw(MasterNode masterNode) {
        nameLabel.setText("Master");
        panKnob.setVisible(false);
        muteButton.setVisible(false);
        soloButton.setVisible(false);
        stateButton.setVisible(false);

        highKnob.setValue(masterNode.getEqualizer().getHigh());
        midKnob.setValue(masterNode.getEqualizer().getMid());
        bassKnob.setValue(masterNode.getEqualizer().getBass());

        volumeSlider.setValue(masterNode.getVolume().getOut());
    }

    public void redraw(MachineNode machineNode) {
        nameLabel.setText(String.valueOf(machineNode.getIndex() + 1));

        MixerChannel channel = machineNode.getMixer();
        panKnob.setValue(channel.getPan());
        volumeSlider.setValue(channel.getVolume());

        highKnob.setValue(channel.getEqHigh());
        midKnob.setValue(channel.getEqMid());
        bassKnob.setValue(channel.getEqBass());

        widthKnob.setValue(channel.getStereoWidth());
        delayKnob.setValue(channel.getDelaySend());
        reverbKnob.setValue(channel.getReverbSend());

        redrawSolo(machineNode);
    }

    public void redrawSolo(MachineNode machineNode) {
        MixerChannel channel = machineNode.getMixer();
        updating = true;
        soloButton.setChecked(channel.isSolo());
        muteButton.setChecked(channel.isMute());
        updating = false;
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createChildren() {

        automationItems
                .put(MixerControls.DelaySend, AutomationItem.create(MixerControls.DelaySend));
        automationItems.put(MixerControls.EqBass, AutomationItem.create(MixerControls.EqBass));
        automationItems.put(MixerControls.EqHigh, AutomationItem.create(MixerControls.EqHigh));
        automationItems.put(MixerControls.EqMid, AutomationItem.create(MixerControls.EqMid));
        automationItems.put(MixerControls.Mute, AutomationItem.create(MixerControls.Mute));
        automationItems.put(MixerControls.Pan, AutomationItem.create(MixerControls.Pan));
        automationItems.put(MixerControls.ReverbSend,
                AutomationItem.create(MixerControls.ReverbSend));
        automationItems.put(MixerControls.Solo, AutomationItem.create(MixerControls.Solo));
        automationItems.put(MixerControls.StereoWidth,
                AutomationItem.create(MixerControls.StereoWidth));
        automationItems.put(MixerControls.Volume, AutomationItem.create(MixerControls.Volume));

        MixerPaneItemStyle style = getStyle();

        paramTable = new Table();
        paramTable.setBackground(style.param_background);
        paramTable.setColor(Color.BLACK);

        nameRow = new Table();

        eqKnobRow = new Table();
        fxKnobRow = new Table();
        Table volumeRow = new Table();
        Table controlRow = new Table();

        createName(nameRow);

        createEQKnobs(eqKnobRow);
        createFXKnobs(fxKnobRow);

        createVolume(volumeRow);
        createControls(controlRow);

        nameCell = add(nameRow).expandX().fillX();
        row();
        eqCell = paramTable.add(eqKnobRow).expandX().fillX();
        paramTable.row();
        fxCell = paramTable.add(fxKnobRow).expandX().fillX();

        paramTable.row();
        paramTable.add(volumeRow).expand().fill().pad(2f);

        add(paramTable).expand().fill().pad(2f);
        row();
        add(controlRow).expandX().fillX();

        outlineGlow = new Image(style.outline);
        outlineGlow.setTouchable(Touchable.disabled);

        addActor(outlineGlow);

        setCurrentState(MixerItemState.Volume);

        setColors();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        outlineGlow.setVisible(triggered);
        outlineGlow.setBounds(paramTable.getX() - 5f, paramTable.getY() - 5f,
                paramTable.getWidth() + 10f, paramTable.getHeight() + 10f);
        super.draw(batch, parentAlpha);
    }

    protected void setColors() {
        if (selected) {
            nameLabel.setColor(Color.ORANGE);
            paramTable.setColor(Color.DARK_GRAY);
            for (TextKnob knob : knobs) {
                knob.setColor(Color.WHITE);
            }
        } else {
            if (nameLabel != null) {
                nameLabel.setColor(Color.WHITE);
                paramTable.setColor(Color.BLACK);
                // machineColor != null ? machineColor : Color.BLACK
                volumeSlider.setColor(machineColor != null ? machineColor : Color.WHITE);
            }
            for (TextKnob knob : knobs) {
                knob.setColor(Color.WHITE);
            }
        }
        outlineGlow.setColor(volumeSlider.getColor());
    }

    private void createName(Table parent) {
        nameLabel = new Label((index + 1) + "", getSkin());
        parent.add(nameLabel);
    }

    private void createFXKnobs(Table parent) {
        widthKnob = new TextKnob(-1f, 1f, 0.01f, "WIDTH", getSkin(), "default");
        widthKnob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerControls.StereoWidth, widthKnob.getValue());
            }
        });
        parent.add(widthKnob).padTop(2f);
        parent.row();
        delayKnob = new TextKnob(0f, 1f, 0.01f, "DELAY", getSkin(), "default");
        delayKnob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerControls.DelaySend, delayKnob.getValue());
            }
        });
        parent.add(delayKnob).padTop(2f);
        parent.row();
        reverbKnob = new TextKnob(0f, 0.5f, 0.01f, "REVERB", getSkin(), "default");
        reverbKnob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerControls.ReverbSend, reverbKnob.getValue());
            }
        });
        parent.add(reverbKnob).padTop(2f);

        knobs.add(widthKnob);
        knobs.add(delayKnob);
        knobs.add(reverbKnob);
    }

    private void createEQKnobs(Table parent) {
        highKnob = new TextKnob(-1f, 1f, 0.01f, "HIGH", getSkin(), "default");
        highKnob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerControls.EqHigh, highKnob.getValue());
            }
        });
        parent.add(highKnob).padTop(2f);
        parent.row();
        midKnob = new TextKnob(-1f, 1f, 0.01f, "MID", getSkin(), "default");
        midKnob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerControls.EqMid, midKnob.getValue());
            }
        });
        parent.add(midKnob).padTop(2f);
        parent.row();
        bassKnob = new TextKnob(-1f, 1f, 0.01f, "BASS", getSkin(), "default");
        bassKnob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerControls.EqBass, bassKnob.getValue());
            }
        });
        parent.add(bassKnob).padTop(2f);

        knobs.add(highKnob);
        knobs.add(midKnob);
        knobs.add(bassKnob);
    }

    private void createVolume(Table parent) {
        volumeSlider = new AutoTextSlider(getSkin(), automationItems.get(MixerControls.Volume));
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerControls.Volume, volumeSlider.getValue());
            }
        });
        volumeSlider.create();
        parent.add(volumeSlider).width(45f).expandY().fillY().padTop(5f).padBottom(5f);

        parent.row();

        panKnob = new TextKnob(-1f, 1f, 0.01f, "PAN", getSkin(), "default");
        panKnob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerControls.Pan, panKnob.getValue());
            }
        });
        panKnob.setValue(0f);
        panCell = parent.add(panKnob).padTop(2f);

        knobs.add(panKnob);
    }

    protected void send(MixerControls control, float value) {
        listener.onSend(index, control, value);
    }

    private void createControls(Table parent) {

        TextButtonStyle style = new TextButtonStyle(getSkin().getDrawable(
                StylesDefault.TextButton_up), getSkin().getDrawable(StylesDefault.TextButton_down),
                getSkin().getDrawable(StylesDefault.TextButton_up), getSkin().getFont(
                        StylesDefault.Font));

        stateButton = new TextButton("VOL", style);
        stateButton.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (currentState == MixerItemState.Volume) {
                    setCurrentState(MixerItemState.Eq);
                } else if (currentState == MixerItemState.Eq) {
                    setCurrentState(MixerItemState.Fx);
                } else if (currentState == MixerItemState.Fx) {
                    setCurrentState(MixerItemState.Volume);
                }
            }
        });

        soloButton = new TextButton("S", getSkin());
        // XXX Add style        soloButton.setStyle(getSkin().get("MachineSlot_SoloButton", TextButtonStyle.class));
        soloButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (updating)
                    return;
                send(MixerControls.Solo, soloButton.isChecked() ? 1f : 0f);
            }
        });

        muteButton = new TextButton("M", getSkin());
        // XXX Add style        muteButton.setStyle(getSkin().get("MachineSlot_MuteButton", TextButtonStyle.class));
        muteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (updating)
                    return;
                send(MixerControls.Mute, muteButton.isChecked() ? 1f : 0f);
            }
        });

        parent.add(stateButton).size(45f, 30f).padBottom(5f);
        parent.row();
        parent.add(soloButton).size(45f, 30f).padBottom(5f);
        parent.row();
        parent.add(muteButton).size(45f, 30f).padBottom(5f);
    }

    protected void setCurrentState(MixerItemState state) {
        if (state == currentState)
            return;

        currentState = state;
        switch (currentState) {
            case Eq:
                showEq(true);
                showFx(false);
                showPan(false);
                stateButton.setText("EQ");
                break;

            case Fx:
                showEq(false);
                showFx(true);
                showPan(false);
                stateButton.setText("FX");
                break;

            case Volume:
                showEq(false);
                showFx(false);
                showPan(true);
                stateButton.setText("VOL");
                break;
        }
    }

    public enum MixerItemState {
        Volume, Eq, Fx
    }

    private void showEq(boolean show) {
        eqKnobRow.setLayoutEnabled(show);
        eqKnobRow.setVisible(show);
        if (show)
            eqCell.setActor(eqKnobRow);
        else
            eqCell.setActor(null);
    }

    private void showFx(boolean show) {
        fxKnobRow.setLayoutEnabled(show);
        fxKnobRow.setVisible(show);
        if (show)
            fxCell.setActor(fxKnobRow);
        else
            fxCell.setActor(null);
    }

    private void showPan(boolean show) {
        panKnob.setLayoutEnabled(show);
        panKnob.setVisible(show);
        if (show)
            panCell.setActor(panKnob);
        else
            panCell.setActor(null);
    }

    public static interface MixerPaneItemListener {
        void onSend(int index, MixerControls control, float value);
    }

    public void setMixerPaneItemListener(MixerPaneItemListener l) {
        this.listener = l;
    }

    public static class MixerPaneItemStyle {

        public Drawable param_background;

        public Drawable outline;

        public MixerPaneItemStyle(Drawable param_background, Drawable outline) {
            this.param_background = param_background;
            this.outline = outline;
        }

    }

}
