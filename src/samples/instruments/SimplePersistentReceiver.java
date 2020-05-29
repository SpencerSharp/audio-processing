package persistence;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import java.util.*;
import java.io.*;

import midi.*;
import midi.controlled.*;
import tools.effects.*;
import tools.modulators.*;
import viewers.custom.*;
import interfaces.*;
import interfaces.custom.*;

public class SimplePersistentReceiver extends KnobControlledMidiReceiver {
    private static final int NUM_INLETS = 0;
    private static final String[] INLET_NAMES = new String[]{};

    private static final int NUM_OUTLETS = 8;
    private static final String[] OUTLET_NAMES = new String[]{
        "knob1 val",
        "knob2 val",
        "knob3 val",
        "knob4 val",
        "knob5 val",
        "knob6 val",
        "knob7 val",
        "knob8 val"
    };

    SamplerKnobControl knobs;

    public SimplePersistentReceiver() {
        super(NUM_INLETS, INLET_NAMES, NUM_OUTLETS, OUTLET_NAMES);
    }

    protected CustomKnobControl getKnobs() {
        if (knobs != null) {
            for (int i = 0; i < 8; i++) {
                outlet(2+i, knobs.getValue(i));
            }
        } else {
            for (int i = 0; i < 8; i++) {
                outlet(2+i, -47.0);
            }
        }
        return knobs;
    }

    protected void setup() {
        knobs = new SamplerKnobControl(this, 3);
    }

    protected float leftSignal() {
        return 0.0f;
    }

    protected float rightSignal() {
        return 0.0f;
    }
}