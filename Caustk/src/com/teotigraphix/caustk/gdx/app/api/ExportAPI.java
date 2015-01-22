
package com.teotigraphix.caustk.gdx.app.api;

import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.gdx.app.AbstractProjectModelAPI;
import com.teotigraphix.caustk.gdx.app.ProjectModel;
import com.teotigraphix.caustk.gdx.app.ProjectState;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.sequencer.PatternNode;
import com.teotigraphix.caustk.node.machine.sequencer.TrackComponent;
import com.teotigraphix.caustk.node.sequencer.SequencerNode.ExportLoopMode;
import com.teotigraphix.caustk.node.sequencer.SequencerNode.ExportType;

public class ExportAPI extends AbstractProjectModelAPI {

    private ProjectModel projectModel;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public ExportAPI(ProjectModel projectModel) {
        super(projectModel);
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    /**
     * @throws IOException
     * @throws CausticException
     */
    public File exportWav(String name, PatternNode pattern) throws IOException, CausticException {
        String uniqueName = createName(name);
        File location = new File(getProjectModel().getProjectAPI().getProject()
                .getResource("export"), uniqueName);
        final File absoluteLocation = new File(getProjectModel().getProjectAPI().getProject()
                .getResource("export"), uniqueName + ".wav");

        int numBars = pattern.getNumMeasures();
        int bankIndex = pattern.getBankIndex();
        int patternIndex = pattern.getPatternIndex();

        RackNode rackNode = getRackNode();
        MachineNode machine = rackNode.getMachine(0);

        rackNode.getSequencer().clearPatterns();

        // add current pattern to song sequencer
        TrackComponent track = machine.getTrack();
        track.clearEntries();

        track.addEntry(machine.getSequencer().getPattern(bankIndex, patternIndex), 0, numBars);
        // set loop points
        rackNode.getSequencer().setLoopPoints(0, numBars);

        // remove delay, reverb
        float delaySend = machine.getMixer().getDelaySend();
        float reverbSend = machine.getMixer().getReverbSend();
        machine.getMixer().setDelaySend(0f);
        machine.getMixer().setReverbSend(0f);

        // XXX Figure out how to do this on a thread
        // export wav
        rackNode.getSequencer().exportSong(ExportLoopMode.Song, ExportType.Wav, 100,
                location.getAbsolutePath());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }

        machine.getMixer().setDelaySend(delaySend);
        machine.getMixer().setReverbSend(reverbSend);

        // clear patterns
        rackNode.getSequencer().clearPatterns();
        track.clearEntries();

        // check file exists
        if (!absoluteLocation.exists())
            throw new IOException("Wav was not exported: " + location);

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                projectModel.getEventBus().post(
                        new ExportAPIEvent(ExportAPIEventKind.ExportWavComplete, absoluteLocation));
            }
        });

        return absoluteLocation;
    }

    public static enum ExportAPIEventKind {
        ExportWavComplete
    }

    public static class ExportAPIEvent {

        private ExportAPIEventKind kind;

        private File file;

        public ExportAPIEventKind getKind() {
            return kind;
        }

        public File getFile() {
            return file;
        }

        public ExportAPIEvent(ExportAPIEventKind kind, File file) {
            this.kind = kind;
            this.file = file;
        }
    }

    public String createName(String name) {
        // [name]-[projectName]-[bmp]bpm
        RackNode rackNode = getRackNode();
        String projectName = getProjectModel().getProjectAPI().getProject().getName();
        String bpm = Integer.toString((int)rackNode.getSequencer().getBPM());
        return projectName + "-" + name + "-" + bpm + "bpm";
    }

    @Override
    public void restore(ProjectState state) {
        // TODO Auto-generated method stub

    }
}
