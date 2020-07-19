package viewers.custom;

import viewers.ViewerClock;
import midi.MidiForwarder;
import interfaces.custom.MidiViewerKnobControl;
import interfaces.CustomKnobControl;
import utils.*;
import datatypes.Note;

public class MidiViewerDevice extends MidiForwarder {


    private static final int NUM_OUTLETS = 1;
    private static final String[] OUTLET_NAMES = new String[]{
        "view matrix"
    };

    MidiViewer viewer;
    protected ViewerClock viewerClock;

    private Evaluatable zoom;
    private Evaluatable offset;

    public MidiViewerDevice() {
        super(0, new String[0], NUM_OUTLETS, OUTLET_NAMES);

        notes.add(new Note(60, 127));

        viewer = new MidiViewer(notes);

        viewerClock = new ViewerClock(viewer,this,12,20.0);
    }

    public MidiViewerDevice(int numInlets, String[] inletNames, int numOutlets, String[] outletNames) {
        super(numInlets, inletNames, NUM_OUTLETS + numOutlets, ArrayUtils.addAll(OUTLET_NAMES, outletNames));
    }

    private MidiViewerKnobControl knobs;

    public CustomKnobControl getKnobs() {
        return knobs;
    }

    protected void initFunctions() {
        zoom = getKnobs().get(3);
        offset = getKnobs().get(7);

        viewer.yZoom = zoom;
        viewer.yOffset = offset;
    }

    protected void setup() {
        int knobBaseIn = 4;
        int knobBaseOut = 2;
        knobs = new MidiViewerKnobControl(this, knobBaseIn);
        for (int i = 0; i < 8; i++) {
            outlet(knobBaseOut+i, this.getKnobs().getValue(i));
        }
        this.initFunctions();
    }

    protected void notifyDeleted() {
        viewerClock.notifyDeleted();
    }
}