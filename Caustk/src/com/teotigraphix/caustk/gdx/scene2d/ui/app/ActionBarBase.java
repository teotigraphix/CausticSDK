
package com.teotigraphix.caustk.gdx.scene2d.ui.app;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Pools;
import com.teotigraphix.caustk.gdx.app.ui.StylesDefault;
import com.teotigraphix.caustk.gdx.scene2d.ui.UITable;
import com.teotigraphix.caustk.gdx.scene2d.ui.app.ActionBarListener.ActionBarEvent;
import com.teotigraphix.caustk.gdx.scene2d.ui.app.ActionBarListener.ActionBarEventKind;

public abstract class ActionBarBase extends UITable {

    private Actor backButton;

    private Table toolBar;

    public ActionBarBase(Skin skin) {
        super(skin);
    }

    @Override
    protected void createChildren() {
        toolBar = createToolBar();
        add(toolBar).expandX().fillX().height(80f);

        backButton = createBackButton();
        add(backButton).size(80f, 80f);
    }

    protected abstract Table createToolBar();

    protected Actor createBackButton() {
        Image button = new Image(getSkin().getDrawable(StylesDefault.BackButton_image));
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                ActionBarEvent e = Pools.obtain(ActionBarEvent.class);
                e.setKind(ActionBarEventKind.BackTap);
                fire(e);
                Pools.free(e);
            }
        });
        return button;
    }

    //    public static interface IActionBarActionn {
    //        void exectute(ProjectModelImpl projectModel) throws Exception;
    //    }
}
