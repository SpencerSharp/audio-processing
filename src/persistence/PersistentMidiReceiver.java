package persistence;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.*;
import java.nio.file.*;
import java.io.*;
import java.util.stream.Stream; 
import java.util.Arrays;

import midi.MidiReceiver;
import utils.ArrayUtils;
import interfaces.custom.*;

public abstract class PersistentMidiReceiver extends MidiReceiver {
    private static final int NUM_INLETS = 2;
    private static String[] INLET_NAMES = new String[]{
		"channel",
        "index"
	};

    public PersistentMidiReceiver() {
        super(NUM_INLETS, INLET_NAMES, 0, new String[0]);
    }

    public PersistentMidiReceiver(int numInlets, String[] inletNames, int numOutlets, String[] outletNames) {
        super(NUM_INLETS + numInlets, ArrayUtils.addAll(INLET_NAMES, inletNames), numOutlets, outletNames);
    }

    protected void sendString(String message) {
        super.sendString(message);
        if (message.equals("persist")) {
            persist();
            return;
        }
        if (message.equals("setupfromdisc")) {
            setup();
            return;
        }
    }

    protected int sendDouble(double d) {
        super.sendDouble(d);
        return this.NUM_INLETS;
    }

    protected int sendInt(int i) {
        int parent = super.sendInt(i);
        switch (getInlet() - parent) {
            case 0:
                System.out.println("Channel is " + i);
                PersistentObject.channel = i;
                break;
            case 1:
                System.out.println("Index is " + i);
                PersistentObject.ind = i;
                break;
        }
        return parent + this.NUM_INLETS;
    }

    protected abstract void setup();

    protected abstract void persist();
}