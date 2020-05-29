package midi.sequencing;

import com.cycling74.max.*;
import com.cycling74.jitter.*;
import java.util.*;
import java.io.*;

import midi.controlled.KnobControlledMidiSender;
import viewers.*;
import viewers.custom.*;

public abstract class Sequencer extends KnobControlledMidiSender {
    private static final int NUM_OUTLETS = 1;
    private static final String[] OUTLET_NAMES = new String[]{
        "view matrix"
    };

    protected ViewerClock viewerClock;

    public Sequencer() {
        super(0, new String[0], NUM_OUTLETS, OUTLET_NAMES);

        viewerClock = new ViewerClock(new MidiViewer(notes),this,2,20.0);
    }

    protected void notifyDeleted() {
        viewerClock.notifyDeleted();
    }
}