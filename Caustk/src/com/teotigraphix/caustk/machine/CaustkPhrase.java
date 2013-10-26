
package com.teotigraphix.caustk.machine;

import java.io.File;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.OutputPanelMessage;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.utils.PatternUtils;

public class CaustkPhrase implements ICaustkComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private UUID id;

    @Tag(1)
    private String name;

    @Tag(2)
    private File file;

    @Tag(3)
    private int index;

    @Tag(4)
    private CaustkMachine machine;

    @Tag(5)
    private MachineType machineType;

    private String noteData;

    private float tempo;

    private int length;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // id
    //----------------------------------

    @Override
    public final UUID getId() {
        return id;
    }

    //----------------------------------
    // name
    //----------------------------------

    @Override
    public final String getName() {
        return name;
    }

    //----------------------------------
    // file
    //----------------------------------

    @Override
    public final File getFile() {
        return file;
    }

    //----------------------------------
    // index
    //----------------------------------

    public final int getIndex() {
        return index;
    }

    public final int getBankIndex() {
        return PatternUtils.getBank(index);
    }

    public final int getPatternIndex() {
        return PatternUtils.getPattern(index);
    }

    //----------------------------------
    // machine
    //----------------------------------

    public final CaustkMachine getMachine() {
        return machine;
    }

    //----------------------------------
    // machineType
    //----------------------------------

    public final MachineType getMachineType() {
        return machineType;
    }

    //----------------------------------
    // noteData
    //----------------------------------

    public final String getNoteData() {
        return noteData;
    }

    //----------------------------------
    // tempo
    //----------------------------------

    public final float getTempo() {
        return tempo;
    }

    //----------------------------------
    // length
    //----------------------------------

    public final int getLength() {
        return length;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    CaustkPhrase() {
    }

    CaustkPhrase(UUID id, int index, MachineType machineType) {
        this.id = id;
        this.index = index;
        this.machineType = machineType;
    }

    CaustkPhrase(UUID id, int index, CaustkMachine machine) {
        this.id = id;
        this.index = index;
        this.machine = machine;
        this.machineType = machine.getMachineType();
    }

    public void load(CaustkLibraryFactory factory) throws CausticException {
        final IRack rack = factory.getRack();

        // set the current bank and pattern of the machine to query
        // the string pattern data
        // XXX when pattern_sequencer takes bank, pattern FIX THIS
        PatternSequencerMessage.BANK.send(rack, index, getBankIndex());
        PatternSequencerMessage.PATTERN.send(rack, index, getPatternIndex());

        // load one phrase per pattern; load ALL patterns
        // as caustic machine patterns
        length = (int)PatternSequencerMessage.NUM_MEASURES.query(rack, index);
        tempo = OutputPanelMessage.BPM.query(rack);
        noteData = PatternSequencerMessage.QUERY_NOTE_DATA.queryString(rack, index);
    }

}

/*

    phrase.setMetadataInfo(new MetadataInfo());
    phrase.setId(UUID.randomUUID());

    phrase.setResolution(calculateResolution(noteData));
    TagUtils.addDefaultTags(phrase);
    library.addPhrase(phrase);

*/
