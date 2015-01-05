
package com.teotigraphix.caustk.node;

import com.teotigraphix.caustk.core.osc.IOSCControl;

public class NodeBaseEvents {

    /**
     * Base class for all {@link NodeBase} events posted through the
     * {@link com.teotigraphix.caustk.core.CaustkRack#getEventBus()}.
     * 
     * @author Michael Schmalle
     * @since 1.0
     * @see com.teotigraphix.caustk.core.CaustkRack#getEventBus()
     */
    public static abstract class NodeEvent {

        private NodeBase target;

        private IOSCControl control;

        /**
         * Returns the {@link NodeBase} target that posted the event.
         */
        public final NodeBase getTarget() {
            return target;
        }

        /**
         * The {@link com.teotigraphix.caustk.core.osc.IOSCControl} OSC control
         * that was sent to the native audio core.
         */
        public IOSCControl getControl() {
            return control;
        }

        /**
         * Creates a
         * {@link com.teotigraphix.caustk.node.NodeBaseEvents.NodeEvent}.
         * 
         * @param target The {@link NodeBase} target that posted the event.
         */
        public NodeEvent(NodeBase target) {
            this.target = target;
        }

        public NodeEvent(NodeBase target, IOSCControl control) {
            this.target = target;
            this.control = control;
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see NodeBase#setLabel(String)
     */
    public static class NodeLabelEvent extends NodeEvent {

        private String label;

        public String getLabel() {
            return label;
        }

        public NodeLabelEvent(NodeBase target, String label) {
            super(target);
            this.label = label;
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see NodeBase#setIcon(Object)
     */
    public static class NodeIconEvent extends NodeEvent {

        private Object icon;

        public Object getIcon() {
            return icon;
        }

        public NodeIconEvent(NodeBase target, Object icon) {
            super(target);
            this.icon = icon;
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see NodeBase#setColor(Object)
     */
    public static class NodeColorEvent extends NodeEvent {

        private Object color;

        public Object getColor() {
            return color;
        }

        public NodeColorEvent(NodeBase target, Object color) {
            super(target);
            this.color = color;
        }
    }

    /**
     * @author Michael Schmalle
     * @since 1.0
     * @see NodeBase#setSelected(boolean)
     */
    public static class NodeSelectedEvent extends NodeEvent {

        private boolean selected;

        public boolean isSelected() {
            return selected;
        }

        public NodeSelectedEvent(NodeBase target, boolean selected) {
            super(target);
            this.selected = selected;
        }
    }
}
