
package com.teotigraphix.gdx.controller;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class ViewBase {

    @Tag(0)
    private int id;

    private int index;

    private String label;

    private boolean canScrollUpFlag;

    private boolean canScrollRightFlag;

    private boolean canScrollLeftFlag;

    private boolean canScrollDownFlag;

    public boolean canScrollRight() {
        return canScrollRightFlag;
    }

    public boolean canScrollUp() {
        return canScrollUpFlag;
    }

    public boolean canScrollLeft() {
        return canScrollLeftFlag;
    }

    public boolean canScrollDown() {
        return canScrollDownFlag;
    }

    protected void setCanScrollDown(boolean canScrollDown) {
        this.canScrollDownFlag = canScrollDown;
    }

    protected void setCanScrollLeft(boolean canScrollLeft) {
        this.canScrollLeftFlag = canScrollLeft;
    }

    protected void setCanScrollRight(boolean canScrollRight) {
        this.canScrollRightFlag = canScrollRight;
    }

    protected void setCanScrollUp(boolean canScrollUp) {
        this.canScrollUpFlag = canScrollUp;
    }

    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public String getLabel() {
        return label;
    }

    ViewBase() {
    }

    public ViewBase(int id, int index, String label) {
        this.id = id;
        this.index = index;
        this.label = label;
    }

    public void onArrowUp(boolean down) {

    }

    public void onArrowRight(boolean down) {

    }

    public void onArrowLeft(boolean down) {

    }

    public void onArrowDown(boolean down) {

    }

    public void updateArrows() {

    }
}
