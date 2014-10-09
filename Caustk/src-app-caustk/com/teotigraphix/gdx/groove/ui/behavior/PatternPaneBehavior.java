
package com.teotigraphix.gdx.groove.ui.behavior;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.teotigraphix.caustk.node.RackNode.RackNodeSelectionEvent;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode.PatternNodeNumMeasuresEvent;
import com.teotigraphix.caustk.node.machine.sequencer.PatternSequencerComponent;
import com.teotigraphix.caustk.node.machine.sequencer.PatternSequencerComponent.PatternSequencerNodeBankEvent;
import com.teotigraphix.caustk.node.machine.sequencer.PatternSequencerComponent.PatternSequencerNodePatternEvent;
import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.app.IApplicationState;
import com.teotigraphix.gdx.groove.ui.components.PatternPane;
import com.teotigraphix.gdx.groove.ui.components.PatternSelectionListener;
import com.teotigraphix.gdx.groove.ui.factory.StylesDefault;

public class PatternPaneBehavior extends CaustkBehavior {

    @Inject
    private IApplicationState applicationState;

    private PatternPane view;

    public PatternPaneBehavior() {
    }

    @Override
    public void onAwake() {
        super.onAwake();
        getRack().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getRack().unregister(this);
    }

    public PatternPane create() {
        view = new PatternPane(getSkin()); // XXX We don't call create in a Viewpane
        view.addListener(new PatternSelectionListener() {
            @Override
            public void bankChange(PatternSelectionEvent event, int index) {
                PatternSequencerComponent sequencer = getRackNode().getSelectedMachine()
                        .getSequencer();
                sequencer.setBankIndex(index, false);
            }

            @Override
            public void patternChange(PatternSelectionEvent event, int index) {
                PatternSequencerComponent sequencer = getRackNode().getSelectedMachine()
                        .getSequencer();
                sequencer.setPatternIndex(index, false);
            }

            @Override
            public void lengthChange(PatternSelectionEvent event, int length) {
                PatternSequencerComponent sequencer = getRackNode().getSelectedMachine()
                        .getSequencer();
                sequencer.getSelectedPattern().setNumMeasures(length);
            }
        });
        view.create(StylesDefault.PatternPane);
        view.setDisabled(true);
        return view;
    }

    @Override
    public void onShow() {
        super.onShow();
        refresh();
    }

    private void refresh() {
        MachineNode selectedMachine = getRackNode().getSelectedMachine();
        if (selectedMachine == null)
            return;

        PatternSequencerComponent sequencer = selectedMachine.getSequencer();

        view.selectBank(sequencer.getSelectedBankIndex());
        view.selectPattern(sequencer.getSelectedPatternIndex());
        view.selectLength(sequencer.getSelectedPattern().getNumMeasures());
    }

    @Subscribe
    public void onRackNodeSelectionEvent(RackNodeSelectionEvent event) {
        refresh();
    }

    @Subscribe
    public void onPatternSequencerNodeBankEvent(PatternSequencerNodeBankEvent event) {
        view.selectBank(event.getBank());
    }

    @Subscribe
    public void onPatternSequencerNodePatternEvent(PatternSequencerNodePatternEvent event) {
        PatternSequencerComponent sequencer = getRackNode().getSelectedMachine().getSequencer();
        view.selectPattern(event.getPattern());
        view.selectLength(sequencer.getSelectedPattern().getNumMeasures());
    }

    @Subscribe
    public void onPatternNodeNumMeasuresEvent(PatternNodeNumMeasuresEvent event) {
        view.selectLength(event.getValue());
    }

    public void enable() {
        view.setDisabled(false);
    }
}
