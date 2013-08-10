
package com.teotigraphix.caustic.model;

import javafx.stage.Stage;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class StageModel extends ModelBase implements IStageModel {

    @Inject
    IApplicationModel applicationModel;

    //----------------------------------
    // stage
    //----------------------------------

    private Stage stage;

    public final Stage getStage() {
        return stage;
    }

    public final void setStage(Stage value) {
        stage = value;
    }

    //----------------------------------
    // title
    //----------------------------------

    private String title;

    /**
     * Refreshes the existing title based on the dirty flag.
     */
    public void refreshTitle() {
        setTitle(title);
    }

    /**
     * Returns the explicit title set using {@link #setTitle(String)}.
     * <p>
     * The Application title is always prefixed.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the explicit title for the main application Window.
     * 
     * @param value The new explicit title.
     */
    public void setTitle(String value) {
        String postfix = "";
        title = value;
        if (applicationModel.isDirty()) {
            postfix = "*";
        }
        getStage().setTitle(applicationModel.getName() + " - " + title + postfix);
    }

    public StageModel() {
        super();
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onRegister() {
    }
}
