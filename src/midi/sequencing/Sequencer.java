package midi.sequencing;

import com.cycling74.max.*;
import com.cycling74.jitter.*;
import java.util.*;
import java.io.*;

import viewers.MidiViewer;
import viewers.ViewerClock;



public abstract class Sequencer extends MidiSender {
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

        viewerClock = new ViewerClock(new MidiViewer(notes),this,2,20.0);
    }

    protected void notifyDeleted() {
        viewerClock.notifyDeleted();
    }
}