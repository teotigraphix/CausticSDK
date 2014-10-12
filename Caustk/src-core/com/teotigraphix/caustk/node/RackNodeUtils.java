
package com.teotigraphix.caustk.node;

import java.io.File;

import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.osc.RackMessage;

public class RackNodeUtils {

    /**
     * Creates a new {@link RackNode} and returns it.
     * <p>
     * This is the only {@link RackNode} creation method that does NOT assign
     * the node the this rack(which will call {@link RackMessage#BLANKRACK}).
     * <p>
     * This method allows for {@link RackNode}s to be created and initialized
     * before assigning that state to the native rack(this rack).
     */
    public static RackNode create() {
        RackNode rackNode = new RackNode();
        return rackNode;
    }

    /**
     * Creates a new {@link RackNode} that wraps an existing
     * <code>.caustic</code> file and returns it.
     * <p>
     * After the {@link RackNode} is created, the native rack is cleared with
     * {@link RackMessage#BLANKRACK} and the {@link RackNode#restore()} method
     * is called which restores the internal state of the {@link RackNode} with
     * the state that was loaded into the native rack by the rack node's
     * <code>.caustic</code> file.
     * <p>
     * The rack nod is the on the {@link ICaustkRack}.
     * 
     * @param file The <code>.caustic</code> file that will be used to restore
     *            the {@link RackNode}'s state.
     */
    public static RackNode create(File file) {
        RackNode rackNode = new RackNode(file);
        CaustkRuntime.getInstance().getRack().restore(rackNode);
        return rackNode;
    }

    /**
     * Creates a new {@link RackNode} and returns it.
     * 
     * @param relativeOrAbsolutePath The relative or absolute location of the
     *            <code>.caustic</code> file. The file is for saving, not
     *            loading with this method. See {@link #create(File)} to restore
     *            a {@link RackNode} from an existing <code>.caustic</code>
     *            file.
     */
    public static RackNode create(String relativeOrAbsolutePath) {
        RackNode rackNode = new RackNode(relativeOrAbsolutePath);
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
