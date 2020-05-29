package midi;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;
import java.lang.*;

import audio.AudioPlayer;

/*
My "instrument" archetype
*/
public abstract class MidiReceiver extends AudioPlayer {
    private static final int NUM_INLETS = 2;
    private static final String[] INLET_NAMES = new String[]{
		"midi 2.0 in (last 32 bits)",
        "midi 2.0 in (first 32 bits)"
	};

    protected int pitch;
    protected int velocity;

    private ArrayList<Integer> first32bits;
    private ArrayList<Integer> last32bits;

    // protected HashSet<Voice> voices;

    public MidiReceiver() {
        super();
        this.setup(0, new String[0]);
    }

	public MidiReceiver(int numInlets, String[] inletNames, int numOutlets, String[] outletNames) {
        super(numOutlets, outletNames);
        this.setup(numInlets, inletNames);
    }

    private void setup(int numInlets, String[] inletNames) {
        int[] inletInfo = new int[numInlets + NUM_INLETS];
        for(int i = 0; i < inletInfo.length; i++) {
            inletInfo[i] = DataTypes.ALL;
        }
        declareInlets(inletInfo);

        ArrayList<String> inletNamesList = new ArrayList<String>();
        for(String s : this.INLET_NAMES) {
            inletNamesList.add(s);
        }
        for (String s : inletNames) {
            inletNamesList.add(s);
        }

        String[] inletNamesRay = new String[inletNamesList.size()]; 
        inletNamesRay = inletNamesList.toArray(inletNamesRay); 

		setInletAssist(inletNamesRay);

        first32bits = new ArrayList<Integer>();
        last32bits = new ArrayList<Integer>();

        // voices = new HashSet<Voice>();

        this.curTime = 0;
    }

    @Override
    public void declareInlets(int[] ray) {
        super.declareInlets(ray);
    }

    protected String[] getInletNames() {
        ArrayList<String> inletNames = new ArrayList<String>();
        for (String s : super.getInletNames()) {
            inletNames.add(s);
        }
        for(String s : this.INLET_NAMES) {
            inletNames.add(s);
        }
        String[] inletNamesRay = new String[inletNames.size()]; 
        inletNamesRay = inletNames.toArray(inletNamesRay); 
        return inletNamesRay;
    }

    public void handleMidiMsg(long msg) {
        // int id = Midi2.getNoteId(msg);
        // if (Midi2.isNoteOn(msg)) {
        //     voices.add(new Voice(id, Midi2.getPitch(msg)));
        // } else if (Midi2.isNoteOff(msg)) {
        //     voices.remove(id);
        // }
    }

    private void handleMidiBits() {
        long midiMsg = (long) last32bits.remove(0);
        midiMsg |= ((long)first32bits.remove(0)) << 32;
        handleMidiMsg(midiMsg);
    }

    public void inlet(float f) {
        sendDouble((double)f);
    }

    public void inlet(int i) {
        sendInt(i);
    }

    public void anything(String message, Atom args[]) {
        sendString(message);
    }

    protected void sendString(String message) {
        if (message.equals("none")) {
            return;
        }
    }

    protected int sendDouble(double msg) {
        int parent = 0;
        return parent + this.NUM_INLETS;
    }

    protected int sendInt(int msg) {
        int parent = 0;
        switch (getInlet()) {
            case 0:
                last32bits.add(msg);
                if (first32bits.size() > 0) {
                    handleMidiBits();
                }
                break;
            case 1:
                first32bits.add(msg);
                if (last32bits.size() > 0) {
                    handleMidiBits();
                }
                break;
        }
        return parent + this.NUM_INLETS;
    }

    protected void step() {
        super.step();
    }
}
