
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Array;

public class PadGrid extends WidgetGroup {

    int numRows = 12;

    int numColums = 16;

    private boolean sizeInvalid;

    private Skin skin;

    public PadGrid(Skin skin) {
        this.skin = skin;
        createChildren();
    }

    private void createChildren() {
        int index = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColums; j++) {
                TextButton button = new TextButton(index + "", skin);
                addActor(button);
                index++;
            }
        }
    }

    @Override
    public void layout() {
        // called at the end of validate()

        if (sizeInvalid)
            computeSize();

        float gap = 0f;

        float calcX = 0f;
        float calcY = 0f;

        float calcWidth = ((getWidth() / numColums) - ((numColums - 1) * gap));
        float calcHeight = ((getHeight() / numRows) - ((numRows - 1) * gap));

        int index = 0;
        Array<Actor> children = getChildren();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColums; j++) {
                Actor child = children.get(index);
                child.setX(calcX);
                child.setY(calcY);
                child.setSize(calcWidth, calcHeight);
                calcX += gap + calcWidth;
                index++;
            }
            calcX = 0;
            calcY += gap + calcHeight;
        }

        //        Array<Actor> children = getChildren();
        //        for (int i = 0, n = children.size; i < n; i++) {
        //            Actor child = children.get(i);
        //
        //        }
    }

    private void computeSize() {
        // TODO Auto-generated method stub

    }
}
