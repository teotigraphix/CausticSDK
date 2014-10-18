
package com.teotigraphix.gdx.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.gdx.controller.IFileManager;
import com.teotigraphix.gdx.controller.IViewManager;
import com.teotigraphix.gdx.controller.ViewBase;
import com.teotigraphix.gdx.groove.ui.components.SceneViewChildData;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;

@Singleton
public class ProjectModel extends ApplicationComponent implements IProjectModel, IProjectModelWrite {

    private static final String TAG = "ProjectModel";

    @Inject
    private IApplicationStateHandlers applicationStates;

    @Inject
    private IFileManager fileManager;

    @Inject
    private IViewManager viewManager;

    protected IFileManager getFileManager() {
        return fileManager;
    }

    @Override
    public boolean hasMachine(int index) {
        return project.getRackNode().containsMachine(index);
    }

    @Override
    public final int getSelectedMachineIndex() {
        return getSelectedMachine().getIndex();
    }

    @Override
    public void setSelectedMachineIndex(int machineIndex) {
        if (project.getRackNode().getSelectedIndex() == machineIndex)
            return;
        project.getRackNode().setSelectedIndex(machineIndex);
        getEventBus().post(
                new ProjectModelEvent(ProjectModelEventKind.MachineSelectionChange, this));
    }

    @Override
    public MachineNode getMachine(int channelIndex) {
        return project.getRackNode().getMachine(channelIndex);
    }

    @Override
    public final MachineNode getSelectedMachine() {
        return project.getRackNode().getSelectedMachine();
    }

    @Override
    public final PatternNode getSelectedMachinePattern() {
        MachineNode selectedMachine = getSelectedMachine();
        if (selectedMachine == null)
            return null;
        return selectedMachine.getSequencer().getSelectedPattern();
    }

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Project project;

    private ProjectState state;

    private Array<SceneViewChildData> views;

    private Array<ButtonBarItem> buttons;

    private Array<ButtonBarItem> viewButtons;

    public ProjectState getState() {
        return state;
    }

    //----------------------------------
    // project
    //----------------------------------

    @Override
    public ProjectProperties getProperties() {
        return project.getProperties();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Project> T getProject() {
        return (T)project;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Project> void setProject(T project) throws IOException {
        T oldProject = (T)this.project;
        if (oldProject != null) {
            closeProject(oldProject);
        }

        log(TAG, "setProject() - " + project.getNativePath());
        this.project = project;

        if (!project.isCreated()) {
            createProject(project);
        } else {
            loadProject(project);
        }
    }

    @Override
    public void save() {
        saveProject(getProject());
    }

    private void createProject(Project project) throws IOException {
        log(TAG, "createProject()");
        applicationStates.onProjectCreate(project);
        fileManager.setStartupProject(project);
        project.save();
    }

    private void loadProject(Project project) {
        log(TAG, "loadProject()");
        fileManager.setStartupProject(project);
        applicationStates.onProjectLoad(project);
    }

    private void saveProject(Project project) {
        log(TAG, "saveProject()");
        applicationStates.onProjectSave(project);
        try {
            project.save();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void closeProject(Project project) {
        log(TAG, "closeProject()");
        applicationStates.onProjectClose(project);
    }

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    @Override
    public Collection<ViewBase> getViews() {
        ArrayList<ViewBase> values = new ArrayList<ViewBase>(state.getViews().values());
        Collections.sort(values, new Comparator<ViewBase>() {
            @Override
            public int compare(ViewBase lhs, ViewBase rhs) {
                return lhs.getIndex() > rhs.getIndex() ? 1 : -1;
            }
        });
        return values;
    }

    //----------------------------------
    // selectedView
    //----------------------------------

    @Override
    public ViewBase getSelectedView() {
        return getState().getSelectedView();
    }

    @Override
    public void setSelectedViewId(int viewId) {
        if (getState().getSelectedViewId() == viewId)
            return;
        getState().setSelectedViewId(viewId);
        getEventBus().post(new ProjectModelEvent(ProjectModelEventKind.ViewChange, this));
    }

    //----------------------------------
    // viewIndex
    //----------------------------------

    @Override
    public int getViewIndex() {
        return getSelectedView().getIndex();
    }

    /**
     * @param viewIndex
     * @see ProjectModelEventKind#ViewChange
     */
    @Override
    public void setViewIndex(int viewIndex) {
        if (getViewIndex() == viewIndex)
            return;
        state.setSelectedViewId(getViewIdByIndex(viewIndex));
        getEventBus().post(new ProjectModelEvent(ProjectModelEventKind.ViewChange, this));
    }

    @Override
    public ViewBase getViewByIndex(int viewIndex) {
        for (ViewBase view : state.getViews().values()) {
            if (view.getIndex() == viewIndex)
                return view;
        }
        return null;
    }

    private int getViewIdByIndex(int viewIndex) {
        for (ViewBase view : state.getViews().values()) {
            if (view.getIndex() == viewIndex)
                return view.getId();
        }
        return -1;
    }

    //----------------------------------
    // views
    //----------------------------------

    @Override
    public void setSceneViewIndex(int viewIndex) {
        if (state.getSceneViewIndex() == viewIndex)
            return;
        state.setSceneViewIndex(viewIndex);
        getEventBus().post(new ProjectModelEvent(ProjectModelEventKind.SceneViewChange, this));
    }

    @Override
    public int getSceneViewIndex() {
        return state.getSceneViewIndex();
    }

    @Override
    public Array<SceneViewChildData> getSceneViews() {
        return views;
    }

    @Override
    public void setSceneViews(Array<SceneViewChildData> views) {
        this.views = views;
    }

    //----------------------------------
    // buttons
    //----------------------------------

    @Override
    public Array<ButtonBarItem> getSceneButtons() {
        if (buttons != null)
            return buttons;

        buttons = new Array<ButtonBarItem>();
        for (SceneViewChildData data : views) {
            buttons.add(data.toButtonBarItem());
        }

        return buttons;
    }

    @Override
    public void setSceneButtons(Array<ButtonBarItem> buttons) {
        this.buttons = buttons;
    }

    //----------------------------------
    // viewButtons
    //----------------------------------

    @Override
    public Array<ButtonBarItem> getViewButtons() {
        return viewButtons;
    }

    @Override
    public void setViewButtons(Array<ButtonBarItem> viewButtons) {
        this.viewButtons = viewButtons;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ProjectModel() {
    }

    @Override
    public void restore(ProjectState state) {
        this.state = state;

        getEventBus().post(new ProjectModelEvent(ProjectModelEventKind.SceneViewChange, this));
        getEventBus().post(new ProjectModelEvent(ProjectModelEventKind.ViewChange, this));

        viewManager.restore(state);
    }

}
