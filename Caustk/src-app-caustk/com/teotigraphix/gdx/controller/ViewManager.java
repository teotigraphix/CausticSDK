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

package com.teotigraphix.gdx.controller;

import java.util.Collection;

import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.controller.core.AbstractDisplay;
import com.teotigraphix.caustk.controller.core.AbstractSequencerView;
import com.teotigraphix.caustk.controller.helper.AbstractGrid;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.gdx.app.ApplicationComponent;
import com.teotigraphix.gdx.app.IProjectModel;

@Singleton
public abstract class ViewManager extends ApplicationComponent implements IViewManager {

    @Inject
    private IProjectModel projectModel;

    private AbstractGrid pads;

    private AbstractDisplay display;

    private AbstractDisplay subDisplay;

    public IProjectModel getProjectModel() {
        return projectModel;
    }

    @Override
    public int getSceneViewIndex() {
        return getProjectModel().getSceneViewIndex();
    }

    @Override
    public void setSceneViewIndex(int index) {
        getProjectModel().setSceneViewIndex(index);
    }

    @Override
    public PatternNode getSelectedMachinePattern() {
        return projectModel.getSelectedMachinePattern();
    }

    @Override
    public AbstractSequencerView getSequencerView() {
        if (getSelectedView() instanceof AbstractSequencerView)
            return (AbstractSequencerView)getSelectedView();
        return null;
    }

    @Override
    public AbstractGrid getPads() {
        return pads;
    }

    public void setPads(AbstractGrid pads) {
        this.pads = pads;
    }

    //----------------------------------
    // display
    //----------------------------------

    @Override
    public AbstractDisplay getDisplay() {
        return display;
    }

    protected void setDisplay(AbstractDisplay display) {
        this.display = display;
    }

    //----------------------------------
    // subDisplay
    //----------------------------------

    @Override
    public AbstractDisplay getSubDisplay() {
        return subDisplay;
    }

    protected void setSubDisplay(AbstractDisplay subDisplay) {
        this.subDisplay = subDisplay;
    }

    //----------------------------------
    // scene views
    //----------------------------------

    @Override
    public boolean isCurrent(int viewId) {
        ViewBase selectedView = getSelectedView();
        if (selectedView == null)
            return false;
        return selectedView.getId() == viewId;
    }

    @Override
    public ViewBase getSelectedView() {
        return projectModel.getSelectedView();
    }

    @Override
    public void setSelectedView(int index) {
        projectModel.setViewIndex(index);
        getSelectedView().onActivate();
        flush();
    }

    @Override
    public ViewBase getView(int index) {
        return projectModel.getViewByIndex(index);
    }

    @Override
    public ViewBase getViewById(int viewId) {
        for (ViewBase view : projectModel.getViews()) {
            if (view.getId() == viewId)
                return view;
        }
        return null;
    }

    public Collection<ViewBase> getViews() {
        return projectModel.getViews();
    }

    public ViewManager() {
    }

    @Override
    public void onArrowUp(boolean down) {
        getSelectedView().onArrowUp(down);
    }

    @Override
    public void onArrowRight(boolean down) {
        getSelectedView().onArrowRight(down);
    }

    @Override
    public void onArrowLeft(boolean down) {
        getSelectedView().onArrowLeft(down);
    }

    @Override
    public void onArrowDown(boolean down) {
        getSelectedView().onArrowDown(down);
    }

    @Override
    public void flush(boolean clear) {

        if (clear) {
            getPads().clear();
        }

        ViewBase selectedView = getSelectedView();
        if (selectedView != null)
            selectedView.updateArrows();

        if (getDisplay() != null) {
            getDisplay().flush();
            getSubDisplay().flush();
        }

        if (getPads() != null) {
            getPads().flush();
        }

        for (IViewManagerFlushListener listener : flushListeners) {
            listener.flush();
        }
    }

    @Override
    public void flush() {
        flush(false);
    }

    private Array<IViewManagerFlushListener> flushListeners = new Array<IViewManagerFlushListener>();

    @Override
    public void addFlushListener(IViewManagerFlushListener listener) {
        flushListeners.add(listener);
    }

    @Override
    public void removeFlushListener(IViewManagerFlushListener listener) {
        flushListeners.removeValue(listener, false);
    }

    @Override
    public void onStartUI() {
        getEventBus().post(new ViewManagerStartUIEvent());
    }

    @Override
    public void onRestartUI() {
        getEventBus().post(new ViewManagerReStartUIEvent());
    }

    @Override
    public void onRefresh() {
        getEventBus().post(new ViewManagerRefreshUIEvent());
    }
}
