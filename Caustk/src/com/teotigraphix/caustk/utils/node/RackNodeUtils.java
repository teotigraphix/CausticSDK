
package com.teotigraphix.caustk.utils.node;

import java.io.File;

import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.node.RackInstance;

public class RackNodeUtils {

    /**
     * Creates a new {@link RackInstance} and returns it.
     * <p>
     * This is the only {@link RackInstance} creation method that does NOT assign
     * the node the this rack(which will call
     * {@link com.teotigraphix.caustk.core.osc.RackMessage#BLANKRACK}).
     * <p>
     * This method allows for {@link RackInstance}s to be created and initialized
     * before assigning that state to the native rack(this rack).
     */
    public static RackInstance create() {
        RackInstance rackNode = new RackInstance();
        return rackNode;
    }

    /**
     * Creates a new {@link RackInstance} that wraps an existing
     * <code>.caustic</code> file and returns it.
     * <p>
     * After the {@link RackInstance} is created, the native rack is cleared with
     * {@link com.teotigraphix.caustk.core.osc.RackMessage#BLANKRACK} and the
     * {@link RackInstance#restore()} method is called which restores the internal
     * state of the {@link RackInstance} with the state that was loaded into the
     * native rack by the rack node's <code>.caustic</code> file.
     * <p>
     * The rack nod is the on the
     * {@link com.teotigraphix.caustk.core.ICaustkRack}.
     * 
     * @param file The <code>.caustic</code> file that will be used to restore
     *            the {@link RackInstance}'s state.
     */
    public static RackInstance create(File file) {
        RackInstance rackNode = new RackInstance(file);
        CaustkRuntime.getInstance().getRack().restore(rackNode);
        return rackNode;
    }

    /**
     * Creates a new {@link RackInstance} and returns it.
     * 
     * @param relativeOrAbsolutePath The relative or absolute location of the
     *            <code>.caustic</code> file. The file is for saving, not
     *            loading with this method. See {@link #create(java.io.File)} to
     *            restore a {@link RackInstance} from an existing
     *            <code>.caustic</code> file.
     */
    public static RackInstance create(String relativeOrAbsolutePath) {
        RackInstance rackNode = new RackInstance(relativeOrAbsolutePath);
        CaustkRuntime.getInstance().getRack().restore(rackNode);
        return rackNode;
    }

    //    /**
    //     * Takes the state of the {@link RackNode} and applies it to the
    //     * {@link CaustkRack} by creating machines and updating all native rack
    //     * state based on the node graph.
    //     * <p>
    //     * The {@link CaustkRack} is reset, native rack cleared and all machines are
    //     * created through OSC and updated through OSC in the node graph.
    //     * 
    //     * @param rackNode The new rack node state.
    //     */
    //    public static void create(RackNode rackNode) {
    //        // the native rack is created based on the state of the node graph
    //        // machines/patterns/effects in the node graph get created through OSC
    //        CaustkRuntime.getInstance().getRack().setRackNode(rackNode);
    //        rackNode.create();
    //    }
}
