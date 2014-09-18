
package com.teotigraphix.gdx.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.groove.ui.components.UITable;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;
import com.teotigraphix.gdx.scene2d.ui.PaneStackListener.PaneStackEvent;
import com.teotigraphix.gdx.scene2d.ui.PaneStackListener.PaneStackEventKind;

/**
 * The {@link PaneStack} holds a stack of panes and uses a selectedIndex to show
 * the top pane while hiding all other panes.
 * <p>
 * The PaneStack also carries a {@link ButtonBar} option for quick tab bar like
 * functionality.
 * 
 * @author Michael Schmalle
 */
public class PaneStack extends UITable {

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private Table barContainer;

    private Table contentContainer;

    private ButtonBar buttonBar;

    private Stack stack;

    private Array<UITable> panes = new Array<UITable>();

    private int buttonBarAlign;

    private Float maxButtonSize;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // selectedIndex
    //----------------------------------

    private int selectedIndex;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;
        int old = selectedIndex;
        selectedIndex = value;
        PaneStackEvent event = new PaneStackEvent(PaneStackEventKind.SelectedIndexChange,
                selectedIndex);
        if (fire(event)) {
            selectedIndex = old;
        }
        invalidate();
    }

    //----------------------------------
    // maxButtonSize
    //----------------------------------

    public void setMaxButtonSize(Float maxButtonSize) {
        this.maxButtonSize = maxButtonSize;
        invalidateHierarchy();
    }

    public Float getMaxButtonSize() {
        return maxButtonSize;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * @param skin
     * @param buttonBarAlign An {@link Align} value, top or bottom.
     */
    public PaneStack(Skin skin, String styleName, int buttonBarAlign, float maxButtonSize) {
        super(skin);
        setStyleName(styleName);
        setStyleClass(PaneStackStyle.class);

        this.buttonBarAlign = buttonBarAlign;
        this.maxButtonSize = maxButtonSize;
    }

    public void addPane(UITable actor) {
        panes.add(actor);
    }

    @Override
    public void layout() {
        updateSelectedIndex();
        super.layout();
    }

    @Override
    protected void createChildren() {
        PaneStackStyle style = getStyle();

        pad(style.padding);

        //debug();

        barContainer = new Table();
        barContainer.left();
        barContainer.setBackground(style.tabBarBackground);

        Array<ButtonBarItem> items = new Array<ButtonBar.ButtonBarItem>();

        buttonBar = new ButtonBar(getSkin(), items, false, style.tabStyle);
        buttonBar.setMaxButtonSize(maxButtonSize);
        buttonBar.setGap(style.tabBarGap);
        buttonBar.addListener(new ButtonBarListener() {
            @Override
            public void selectedIndexChange(int selectedIndex) {
                setSelectedIndex(selectedIndex);
            }
        });

        barContainer.add(buttonBar).height(style.tabBarThickness);

        contentContainer = new Table();
        contentContainer.setBackground(style.background);
        // XXX contentContainer.pad(5f);

        stack = new Stack();

        if (buttonBarAlign == Align.top) {
            add(barContainer).fillX();
            row();
            add(contentContainer).expand().fill();
            contentContainer.add(stack).expand().fill();
        } else if (buttonBarAlign == Align.bottom) {
            add(contentContainer).expand().fill();
            contentContainer.add(stack).expand().fill();
            row();
            add(barContainer).fillX();
        }

        //------------------------------
        // Create content
        Array<ButtonBarItem> labels = new Array<ButtonBarItem>();
        for (Actor pane : panes) {
            stack.addActor(pane);
            PaneInfo info = (PaneInfo)pane.getUserObject();
            labels.add(new ButtonBarItem(info.getId(), info.getName(), info.getIcon(), info
                    .getHelpText()));
        }

        panes.clear();

        buttonBar.setItems(labels);
        buttonBar.validate();
    }

    protected void updateSelectedIndex() {
        buttonBar.select(selectedIndex, true);
        for (Actor actor : stack.getChildren()) {
            actor.setVisible(false);
        }
        stack.getChildren().get(selectedIndex).setVisible(true);
    }

    public static class PaneInfo {

        private String id;

        private String name;

        private String icon;

        private String helpText;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getHelpText() {
            return helpText;
        }

        public void setHelpText(String helpText) {
            this.helpText = helpText;
        }

        public PaneInfo(String id, String name, String icon, String helpText) {
            this.id = id;
            this.name = name;
            this.icon = icon;
            this.helpText = helpText;
        }

    }

    public static class PaneStackStyle {

        public Drawable tabBarBackground;

        public Drawable background;

        public TextButtonStyle tabStyle;

        public float tabBarGap = 2f;

        public float tabBarThickness = 50f;

        public float maxTabWidth = Float.NaN;

        public float padding = 0f;

        public PaneStackStyle() {
        }
    }

}
