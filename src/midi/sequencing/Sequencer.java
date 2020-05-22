package midi.sequencing;

import com.cycling74.max.*;
import com.cycling74.jitter.*;
import java.util.*;
import java.io.*;

import viewers.MidiViewer;
import viewers.ViewerClock;



public abstract class Sequencer extends MidiSender {

    private static final String[] INLET_ASSIST = new String[]{
        "stuff in",
        "knob 1",
        "knob 2",
        "knob 3",
        "Y zoom",
        "knob 5",
        "knob 6",
        "knob 7",
        "Y offset"
    };

    private static final String[] OUTLET_ASSIST = new String[]{
		"midi 2.0 out (last 32 bits)",
        "midi 2.0 out (first 32 bits)",
        "view matrix"
    };

    protected ViewerClock viewerClock;

    public Sequencer() {
        super();

        declareOutlets(new int[]{DataTypes.ALL,DataTypes.ALL,DataTypes.ALL});

        setOutletAssist(OUTLET_ASSIST);

        declareInlets(new int[]{DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,
            DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,
            DataTypes.ALL,DataTypes.ALL,DataTypes.ALL});

        setInletAssist(INLET_ASSIST);

        viewerClock = new ViewerClock(new MidiViewer(notes),this,2,20.0);
    }

    public void inlet(int i) {
        if (getInlet() == 2) {
            // BASE_BEAT_LENGTH = i;
        } else if (getInlet() == 3) {
            // BASE_PITCH = i;
        }
    }

    protected void notifyDeleted() {
        viewerClock.notifyDeleted();
    }
}