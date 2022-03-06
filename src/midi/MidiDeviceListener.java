package midi;

import datatypes.Note;
import midi.controlled.KnobControlledMidiSender;
import utils.ArrayUtils;
import interfaces.*;
import interfaces.custom.*;

public class MidiDeviceListener extends KnobControlledMidiSender {

    CustomKnobControl knobs;

    private static final int NUM_INLETS = 1;
    private static final String[] INLET_NAMES = new String[]{
        "midi 1.0 in"
    };

    public MidiDeviceListener() {
        super(NUM_INLETS, INLET_NAMES, 0, new String[0]);
    }

    public CustomKnobControl getKnobs() {
        return knobs;
    }

    protected void setup() {
        knobs = new MidiViewerKnobControl(this, 3);
        System.out.println("knobs now " + knobs);
        for (int i = 0; i < 8; i++) {
            outlet(2+i, this.getKnobs().getValue(i));
        }
    }

    protected int sendInt(int i) {
        int parent = super.sendInt(i);
        int knobInd = getInlet() - parent;
        if (knobInd >= 0 && knobInd < NUM_INLETS) {
            System.out.println("MidiDeviceListener sent int " + i);
        }
        return parent + this.NUM_INLETS;
    }


    protected void playNote(int pitch, int vel, int dur) {
    }
}