package midi.sequencing;

import midi.Midi2;
import datatypes.Note;

import com.cycling74.max.*;
import java.util.*;
import java.lang.Math;
import java.io.*;

public abstract class MidiSender extends MaxObject {
    protected static final int MIDI_OUTLET = 0;

    private int tickIndex;
    private boolean isRunning;

    protected PriorityQueue<Note> notes;

    private static final String[] INLET_ASSIST = new String[]{
        "stuff in"
    };

	private static final String[] OUTLET_ASSIST = new String[]{
		"midi 2.0 out (last 32 bits)",
        "midi 2.0 out (first 32 bits)"
    };
    
    public MidiSender() {
        declareInlets(new int[]{DataTypes.ALL});
		declareOutlets(new int[]{DataTypes.ALL,DataTypes.ALL});

        setInletAssist(INLET_ASSIST);
		setOutletAssist(OUTLET_ASSIST);

        notes = new PriorityQueue<Note>();
    }

    public void inlet(int i) {
    }

    protected void sendOut(long msg) {
        outlet(0,(int)msg);
        outlet(1,(int)(msg >>> 32));
    }

    abstract protected void playNote(int pitch, int vel, int dur);

    protected void notifyDeleted() {
		// objectStillExists = false;
	}
}