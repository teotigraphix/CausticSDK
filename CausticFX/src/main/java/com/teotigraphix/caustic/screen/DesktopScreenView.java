
package com.teotigraphix.caustic.screen;

import javafx.scene.layout.Pane;

import com.teotigraphix.caustic.mediator.DesktopMediatorBase;

public abstract class DesktopScreenView extends DesktopMediatorBase implements IScreenView {

    private Pane rootPane;

    private void setRoot(Pane value) {
        rootPane = value;
        screenRoot = new ScreenRootFX(rootPane);
    }

    private ScreenRootFX screenRoot;

    @Override
    public IScreenRoot getScreenRoot() {
        return screenRoot;
    }

    public Pane getRoot() {
        return rootPane;
    }

    protected abstract String getResource();

    @Override
    public <T> void create(T root) {
        Pane base = Pane.class.cast(root);
        String resource = getResource();
        if (resource == null) {
            // placeholder for injections
            setRoot((Pane)root);
            create((Pane)root);
        } else {
            Pane component = load(resource);
            setRoot(component);
            base.getChildren().add(component);
            create(component);
        }
    }

    public static class ScreenRootFX implements IScreenRoot {

        private Pane root;

        public ScreenRootFX(Pane root) {
            this.root = root;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T getRoot() {
            return (T)root;
        }

        @Override
        public void show() {
            root.setVisible(true);
            root.toFront();
        }

        @Override
        public void hide() {
            root.setVisible(false);
            root.toBack();
        }

    }

    @Override
    public void onRegister() {
    }
}
