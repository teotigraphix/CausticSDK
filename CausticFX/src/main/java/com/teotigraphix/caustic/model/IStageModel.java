package com.teotigraphix.caustic.model;

import javafx.stage.Stage;

public interface IStageModel extends ICaustkModel {

    Stage getStage();

    void setStage(Stage value);

    void refreshTitle();

    String getTitle();

    void setTitle(String value);

}
