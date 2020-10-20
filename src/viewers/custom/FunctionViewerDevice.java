package viewers.custom;

import viewers.ViewerClock;
import midi.MidiForwarder;
import interfaces.custom.MidiViewerKnobControl;
import interfaces.CustomKnobControl;
import utils.*;
import audio.*;
import datatypes.Note;

public class FunctionViewerDevice extends MidiForwarder {


    private static final int NUM_OUTLETS = 1;
    private static final String[] OUTLET_NAMES = new String[]{
        "view matrix"
    };

    FunctionViewer viewer;
    protected ViewerClock viewerClock;

    public FunctionViewerDevice() {
        super(0, new String[0], NUM_OUTLETS, OUTLET_NAMES);

        // notes.add(new Note(60, 127));

        viewer = new FunctionViewer();

        viewerClock = new ViewerClock(viewer,this,12,20.0);
    }

    public FunctionViewerDevice(int numInlets, String[] inletNames, int numOutlets, String[] outletNames) {
        super(numInlets, inletNames, NUM_OUTLETS + numOutlets, ArrayUtils.addAll(OUTLET_NAMES, outletNames));

        viewer = new FunctionViewer();

        viewerClock = new ViewerClock(viewer,this,12,20.0);
    }

    private MidiViewerKnobControl knobs;

    public CustomKnobControl getKnobs() {
        return knobs;
    }

    protected void initFunctions() {
        // zoom = getKnobs().get(3);
        // offset = getKnobs().get(7);

        GlobalFunction

        viewer.setup();

        // viewer.yZoom = zoom;
        // viewer.yOffset = offset;
    }

    protected void setup() {
        // int knobBaseIn = 4;
        // int knobBaseOut = 2;
        // knobs = new MidiViewerKnobControl(this, knobBaseIn);
        // System.out.println("SETUP INIT KNOBS TO " + knobs);
        // for (int i = 0; i < 8; i++) {
        //     outlet(knobBaseOut+i, this.getKnobs().getValue(i));
        // }

        // declareInlets()
        

        // declareOutlets(outletInfo);
        // setOutletAssist(outletNamesRay);

        // GlobalFunction.refresh();

        this.initFunctions();
    }

    protected void notifyDeleted() {
        viewerClock.notifyDeleted();
    }
}