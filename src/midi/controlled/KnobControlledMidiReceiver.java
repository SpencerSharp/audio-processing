package midi.controlled;

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
import persistence.*;

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

    private static final int NUM_OUTLETS = 8;
    private static final String[] OUTLET_NAMES = new String[]{
        "knob1 out",
        "knob2 out",
        "knob3 out",
        "knob4 out",
        "knob5 out",
        "knob6 out",
        "knob7 out",
        "knob8 out"
    };

    public KnobControlledMidiReceiver() {
        super(NUM_INLETS, INLET_NAMES, NUM_OUTLETS, OUTLET_NAMES);
    }

    public KnobControlledMidiReceiver(int numInlets, String[] inletNames, int numOutlets, String[] outletNames) {
        super(NUM_INLETS + numInlets, ArrayUtils.addAll(INLET_NAMES, inletNames), NUM_OUTLETS + numOutlets, ArrayUtils.addAll(OUTLET_NAMES, outletNames));
    }

    private void tryAssignValue(int knob, double val) {
        CustomKnobControl knobs = this.getKnobs();
        if (knobs != null) {
            
            knobs.assignValue(knob, val);
            outlet(2+knob, knobs.getValue(knob));
        }
    }

    protected int sendDouble(double d) {
        int parent = super.sendDouble(d);
        int knobInd = getInlet() - parent;
        // System.out.println("Trying to assign value " + d + " to knob " + knobInd + " of " + getKnobs());
        if (knobInd >= 0 && knobInd < NUM_INLETS) {
            tryAssignValue(knobInd, d);
        }
        return parent + this.NUM_INLETS;
    }

    protected int sendInt(int i) {
        int parent = super.sendInt(i);
        int knobInd = getInlet() - parent;
        // System.out.println("Trying to assign value " + i + " to knob " + knobInd + " of " + getKnobs());

        if (knobInd >= 0 && knobInd < NUM_INLETS) {
            tryAssignValue(knobInd, (double) i);
        }
        return parent + this.NUM_INLETS;
    }

    protected void persist() {
        // PersistentObject.save(this.getKnobs());
        // try {
        //     Thread.sleep(1100);
        // } catch (Exception e) {}
    }
    
    protected abstract CustomKnobControl getKnobs();
}