
package com.teotigraphix.gdx.groove.ui.components.mixer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.core.osc.MixerChannelMessage.MixerChannelControl;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.patch.MixerChannel;
import com.teotigraphix.gdx.groove.ui.components.UITable;
import com.teotigraphix.gdx.groove.ui.factory.StylesDefault;
import com.teotigraphix.gdx.scene2d.ui.Knob.KnobStyle;
import com.teotigraphix.gdx.scene2d.ui.TextKnob;
import com.teotigraphix.gdx.scene2d.ui.TextKnob.TextKnobStyle;

public class MixerPaneItem extends UITable {

    private int index;

    private MixerItemState currentState;

    private MixerPaneItemListener listener;

    private TextButton stateButton;

    private TextButton soloButton;

    private TextButton muteButton;

    private Slider volumeSlider;

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

    private boolean updating;

    private Table paramTable;

    private Array<TextKnob> knobs = new Array<TextKnob>();

    private boolean selected;

    private Color machineColor;

    public int getIndex() {
        return index;
    }

    public MixerPaneItem(Skin skin, int index) {
        super(skin);
        this.index = index;
    }

    public void setMachineColor(Color machineColor) {
        this.machineColor = machineColor;
        setColors();
    }

    protected void setColors() {
        if (selected) {
            nameLabel.setColor(Color.ORANGE);
            paramTable.setColor(Color.ORANGE);
            for (TextKnob knob : knobs) {
                knob.setColor(Color.BLACK);
            }
        } else {
            if (nameLabel != null) {
                nameLabel.setColor(Color.WHITE);
                paramTable.setColor(machineColor != null ? machineColor : Color.BLACK);
            }
            for (TextKnob knob : knobs) {
                knob.setColor(machineColor != null ? Color.BLACK : Color.WHITE);
            }
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        setColors();
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    protected void createChildren() {
        //debug();

        paramTable = new Table();
        paramTable.setBackground(getSkin().getDrawable("MixerPaneItem_background"));
        paramTable.setColor(Color.BLACK);

        KnobStyle kstyle = new KnobStyle();
        kstyle.background = getSkin().getDrawable("defaults/Knob_background");
        kstyle.knob = getSkin().getDrawable("defaults/Knob_knob");
        getSkin().add("default", kstyle);

        TextKnobStyle style = new TextKnobStyle();
        style.background = getSkin().getDrawable("defaults/Knob_background");
        style.knob = getSkin().getDrawable("defaults/Knob_knob");
        style.font = getSkin().getFont("default-font");
        getSkin().add("default", style);

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

        setCurrentState(MixerItemState.Volume);

        setColors();
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
                send(MixerChannelControl.StereoWidth, widthKnob.getValue());
            }
        });
        parent.add(widthKnob).padTop(2f);
        parent.row();
        delayKnob = new TextKnob(0f, 1f, 0.01f, "DELAY", getSkin(), "default");
        delayKnob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerChannelControl.DelaySend, delayKnob.getValue());
            }
        });
        parent.add(delayKnob).padTop(2f);
        parent.row();
        reverbKnob = new TextKnob(0f, 0.5f, 0.01f, "REVERB", getSkin(), "default");
        reverbKnob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerChannelControl.ReverbSend, reverbKnob.getValue());
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
                send(MixerChannelControl.High, highKnob.getValue());
            }
        });
        parent.add(highKnob).padTop(2f);
        parent.row();
        midKnob = new TextKnob(-1f, 1f, 0.01f, "MID", getSkin(), "default");
        midKnob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerChannelControl.Mid, midKnob.getValue());
            }
        });
        parent.add(midKnob).padTop(2f);
        parent.row();
        bassKnob = new TextKnob(-1f, 1f, 0.01f, "BASS", getSkin(), "default");
        bassKnob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerChannelControl.Bass, bassKnob.getValue());
            }
        });
        parent.add(bassKnob).padTop(2f);

        knobs.add(highKnob);
        knobs.add(midKnob);
        knobs.add(bassKnob);
    }

    private void createVolume(Table parent) {
        volumeSlider = new Slider(0f, 2f, 0.01f, true, getSkin());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerChannelControl.Volume, volumeSlider.getValue());
            }
        });
        parent.add(volumeSlider).width(45f).expandY().fillY().padTop(5f).padBottom(5f);

        parent.row();

        panKnob = new TextKnob(-1f, 1f, 0.01f, "PAN", getSkin(), "default");
        panKnob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                send(MixerChannelControl.Pan, panKnob.getValue());
            }
        });
        panKnob.setValue(0f);
        panCell = parent.add(panKnob).padTop(2f);

        knobs.add(panKnob);
    }

    protected void send(MixerChannelControl control, float value) {
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
        soloButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (updating)
                    return;
                send(MixerChannelControl.Solo, soloButton.isChecked() ? 1f : 0f);
            }
        });

        muteButton = new TextButton("M", getSkin());
        muteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (updating)
                    return;
                send(MixerChannelControl.Mute, muteButton.isChecked() ? 1f : 0f);
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

    public void refresh(MachineNode machineNode) {
        nameLabel.setText(machineNode.getName());

        MixerChannel channel = machineNode.getMixer();
        panKnob.setValue(channel.getPan());
        volumeSlider.setValue(channel.getVolume());

        highKnob.setValue(channel.getHigh());
        midKnob.setValue(channel.getMid());
        bassKnob.setValue(channel.getBass());

        widthKnob.setValue(channel.getStereoWidth());
        delayKnob.setValue(channel.getDelaySend());
        reverbKnob.setValue(channel.getReverbSend());

        refreshSolo(machineNode);
    }

    public static interface MixerPaneItemListener {
        void onSend(int index, MixerChannelControl control, float value);
    }

    public void setMixerPaneItemListener(MixerPaneItemListener l) {
        this.listener = l;
    }

    public void refreshSolo(MachineNode machineNode) {
        MixerChannel channel = machineNode.getMixer();
        updating = true;
        soloButton.setChecked(channel.isSolo());
        muteButton.setChecked(channel.isMute());
        updating = false;
    }

}
