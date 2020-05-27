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
import interfaces.CustomKnobControl;

public abstract class KnobControlledMidiReceiver extends PersistentMidiReceiver {
    private static final int NUM_INLETS = 8;
    private static final String[] INLET_NAMES = new String[]{
        "knob1 in",
        "knob2 in",
        "knob3 in",
        "knob4 in",
        "knob5 in",
        "knob6 in",
        "knob7 in",
        "knob8 in"
    };

    public KnobControlledMidiReceiver() {
        super(NUM_INLETS, INLET_NAMES);
    }

    public KnobControlledMidiReceiver(int numInlets, String[] inletNames) {
        super(NUM_INLETS + numInlets, ArrayUtils.addAll(INLET_NAMES, inletNames));
    }

    protected void sendString(String message) {
        super.sendString(message);
        if (message.equals("persist")) {
            persist();
            return;
        }
    }

    protected int sendDouble(double d) {
        int knobInd = getInlet() - super.sendDouble(d);
        if (knobInd >= 0 && knobInd < NUM_INLETS) {
            this.getKnobs().assignValue(knobInd, d);
        }
        return this.NUM_INLETS;
    }

    protected void persist() {
        System.out.println("I HAVE BEEN ALERTED TO BACKUP TO DISC");
        PersistentObject.save(this.getKnobs());
        System.out.println("discccccccccccc dun");
    }
    
    protected abstract CustomKnobControl getKnobs();
}