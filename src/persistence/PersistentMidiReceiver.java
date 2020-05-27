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

public abstract class PersistentMidiReceiver extends MidiReceiver {
    private static final int NUM_INLETS = 2;
    private static String[] INLET_NAMES = new String[]{
		"channel",
        "index"
	};

    private static final int LOAD_TIME = 11000;

    MaxClock loadingTimer;

    public PersistentMidiReceiver() {
        super(NUM_INLETS, INLET_NAMES);

        loadingTimer = new MaxClock(new Executable() {public void execute() { setup(); }});
        loadingTimer.delay(LOAD_TIME);
    }

    public PersistentMidiReceiver(int numInlets, String[] inletNames) {
        super(NUM_INLETS + numInlets, ArrayUtils.addAll(INLET_NAMES, inletNames));
    }

    protected void sendString(String message) {
        super.sendString(message);
        if (message.equals("persist")) {
            persist();
            return;
        }
    }

    protected int sendInt(int i) {
        switch (getInlet() - super.sendInt(i)) {
            case 0:
                System.out.println("Channel is " + i);
                PersistentObject.channel = i;
                break;
            case 1:
                System.out.println("Index is " + i);
                PersistentObject.ind = i;
                break;
        }
        return this.NUM_INLETS;
    }

    protected abstract void setup();

    protected abstract void persist();
}