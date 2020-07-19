package midi;

import midi.Midi2;
import datatypes.Note;

import com.cycling74.max.*;
import java.util.*;
import java.lang.Math;
import java.io.*;

public abstract class MidiSender extends MaxObject {
    private static final int NUM_INLETS = 1;
    private static final String[] INLET_NAMES = new String[]{
        "messages in"
    };

    private static final int NUM_OUTLETS = 2;
	private static final String[] OUTLET_NAMES = new String[]{
		"midi 2.0 out (last 32 bits)",
        "midi 2.0 out (first 32 bits)"
    };

    protected PriorityQueue<Note> notes;

    public MidiSender() {
        super();
        this.setup(0, new String[0], 0, new String[0]);
    }

	public MidiSender(int numInlets, String[] inletNames, int numOutlets, String[] outletNames) {
        super();
        this.setup(numInlets, inletNames, numOutlets, outletNames);
    }

    public void inlet(float f) {
        sendDouble((double)f);
    }

    protected int sendDouble(double msg) {
        int parent = 0;
        return parent + this.NUM_INLETS;
    }

    public void inlet(int i) {
        sendInt(i);
    }

    protected int sendInt(int msg) {
        int parent = 0;
        return parent + this.NUM_INLETS;
    }

    public void anything(String message, Atom args[]) {
        sendString(message);
    }

    protected boolean sendString(String message) {
        if (message.equals("none")) {
            return true;
        }
        return false;
    }

    private void setup(int numInlets, String[] inletNames, int numOutlets, String[] outletNames) {
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


        int[] outletInfo = new int[numOutlets + NUM_OUTLETS];
        for(int i = 0; i < outletInfo.length; i++) {
            outletInfo[i] = DataTypes.ALL;
        }
        declareOutlets(outletInfo);

        ArrayList<String> outletNamesList = new ArrayList<String>();
        for(String s : this.OUTLET_NAMES) {
            outletNamesList.add(s);
        }
        for (String s : outletNames) {
            outletNamesList.add(s);
        }

        String[] outletNamesRay = new String[outletNamesList.size()]; 
        outletNamesRay = outletNamesList.toArray(outletNamesRay); 

		setOutletAssist(outletNamesRay);


        notes = new PriorityQueue<Note>();
    }

    protected void sendOut(long msg) {
        int id = Midi2.getNoteId(msg);
        System.out.println("SENDING : " + id + " | " + Midi2.isNoteOn(msg) + " | " + msg);
        outlet(0,(int)msg);
        outlet(1,(int)(msg >>> 32));
    }

    abstract protected void playNote(int pitch, int vel, int dur);
}