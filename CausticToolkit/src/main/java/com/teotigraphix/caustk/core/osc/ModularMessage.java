
package com.teotigraphix.caustk.core.osc;

public class ModularMessage extends CausticMessage {
    /**
     * Message: <code>/caustic/[machine_index]/create [bay] [type]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack.
     * <li><strong>bay</strong>:
     * <li><strong>type</strong>:
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     */
    public static final ModularMessage CREATE = new ModularMessage("/caustic/${0}/create ${1} ${2}");

    // /caustic/[machine]/connect [src_bay] [src_jack] [dest_bay] [dest_jack]

    /**
     * Message:
     * <code>/caustic/[machine_index]/connect [src_bay] [src_jack] [dest_bay] [dest_jack]</code>
     * <p>
     * <strong>Default</strong>: <code>120.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack.
     * <li><strong>src_bay</strong>:
     * <li><strong>src_jack</strong>:
     * <li><strong>dest_bay</strong>:
     * <li><strong>dest_jack</strong>:</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     */
    public static final ModularMessage CONNECT = new ModularMessage(
            "/caustic/${0}/connect ${1} ${2} ${3} ${4}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/[bay]/[control_name] [value]</code>
     * <p>
     * <strong>Default</strong>:
     * <code>Will return the value if the value is empty.</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index in the rack.
     * <li><strong>bay</strong>:
     * <li><strong>control_name</strong>:
     * <li><strong>value</strong>:
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final ModularMessage SET = new ModularMessage("/caustic/${0}/${1}/${2} ${3}");

    public ModularMessage(String message) {
        super(message);
    }

}
