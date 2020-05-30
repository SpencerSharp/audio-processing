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

    SamplerKnobControl knobs;

    public SimplePersistentReceiver() {
        super();
        for (int i = 0; i < 8; i++) {
            outlet(2+i, -47.0);
        }
    }

    protected CustomKnobControl getKnobs() {
        return knobs;
    }

    protected void setup() {
        knobs = new SamplerKnobControl(this, 3);
        for (int i = 0; i < 8; i++) {
            outlet(2+i, this.getKnobs().getValue(i));
        }
    }

    protected float leftSignal() {
        return 0.0f;
    }

    protected float rightSignal() {
        return 0.0f;
    }
}