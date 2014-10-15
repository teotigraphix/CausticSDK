
package com.teotigraphix.gdx.app;

import java.util.Collection;

import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.gdx.controller.ViewBase;
import com.teotigraphix.gdx.groove.ui.components.SceneViewChildData;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;

/**
 * The {@link IApplicationModel} loads and sets the {@link #getProject()}.
 */
public interface IProjectModel {

    MachineNode getSelectedMachine();

    PatternNode getSelectedPattern();

    ProjectProperties getProperties();

    //--------------------------------------------------------------------------
    // Properties
    //--------------------------------------------------------------------------

    <T extends Project> T getProject();

    //----------------------------------
    // SceneViews
    //----------------------------------

    Array<ButtonBarItem> getSceneButtons();

    void setSceneButtons(Array<ButtonBarItem> buttons);

    Array<SceneViewChildData> getSceneViews();

    void setSceneViews(Array<SceneViewChildData> views);

    int getSceneViewIndex();

    void setSceneViewIndex(int viewIndex);

    //----------------------------------
    // Views
    //----------------------------------

    Collection<ViewBase> getViews();

    ViewBase getViewByIndex(int viewIndex);

    ViewBase getSelectedView();

    void setSelectedViewId(int viewid);

    int getViewIndex();

    void setViewIndex(int viewIndex);

    Array<ButtonBarItem> getViewButtons();

    void setViewButtons(Array<ButtonBarItem> buttons);

    //--------------------------------------------------------------------------
    // Methods
    //--------------------------------------------------------------------------

    void restore(ProjectState state);

    public static enum ProjectModelEventKind {
        SceneViewChange, ViewChange;
    }

    public static class ProjectModelEvent {
        private ProjectModelEventKind kind;

        private IProjectModel model;

        public ProjectModelEventKind getKind() {
            return kind;
        }

        public IProjectModel getModel() {
            return model;
        }

        public ProjectModelEvent(ProjectModelEventKind kind, IProjectModel model) {
            this.kind = kind;
            this.model = model;
        }
    }

}
