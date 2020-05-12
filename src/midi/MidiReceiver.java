package midi;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.util.*;

import datatypes.Voice;
import players.AudioPlayer;

/*
My "instrument" archetype
*/
public abstract class MidiReceiver extends AudioPlayer {
    protected static final int MIDI_INLET = 0;

    private static final String[] INLET_ASSIST = new String[]{
        "none",
		"midi 2.0 in (last 32 bits)",
        "midi 2.0 in (first 32 bits)"
	};

    protected int pitch;
    protected int velocity;

    private ArrayList<Integer> first32bits;
    private ArrayList<Integer> last32bits;

    protected HashSet<Voice> voices;

	public MidiReceiver()
	{
        super();
		declareInlets(new int[]{DataTypes.ALL,DataTypes.ALL,DataTypes.ALL});

		setInletAssist(INLET_ASSIST);

        first32bits = new ArrayList<Integer>();
        last32bits = new ArrayList<Integer>();

        voices = new HashSet<Voice>();

        this.curTime = 0;
    }

    public void setPitch(int i) {
        this.pitch = i;
    }

    public void setVelocity(int i) {
        this.velocity = i;
    }

    public void handleMidiMsg(long msg) {
        // System.out.println("msg is " + msg);
        int id = Midi2.getNoteId(msg);
        if (Midi2.isNoteOn(msg)) {
            voices.add(new Voice(id, Midi2.getPitch(msg)));
        } else if (Midi2.isNoteOff(msg)) {
            // HOW TO HAVE MULTIPLE VOICES WITH SAME PITCH???
            // use note #
            voices.remove(id);
        }
    }

    private void handleMidiBits() {
        long midiMsg = (long) last32bits.remove(0);
        midiMsg |= ((long)first32bits.remove(0)) << 32;
        handleMidiMsg(midiMsg);
    }

    public void inlet(int msg) {
        // System.out.println("received int " + msg + " inlet " + getInlet());
        if (getInlet() == 0) {
            last32bits.add(msg);
            if (first32bits.size() > 0) {
                handleMidiBits();
            }
        } else if (getInlet() == 1) {
            first32bits.add(msg);
            if (last32bits.size() > 0) {
                handleMidiBits();
            }
        }
    }

	public void inlet(long msg) {
        // System.out.println("received long " + msg);
		if (getInlet() == MIDI_INLET) {
			handleMidiMsg(msg);
		}
	}

    protected void step() {
        super.step();
    }
}
