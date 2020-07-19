package interfaces.custom;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import java.io.*;
import interfaces.*;

import datatypes.Units;

public class MidiViewerKnobControl extends CustomKnobControl {

    String[] KNOB_NAMES = {
        "NONE",
        "NONE",
        "NONE",
        "Y Zoom Pct",
        "NONE",
        "NONE",
        "NONE",
        "Y Offset"
    };

    int[] KNOB_RANGES = new int[]{
        0,          0,
        0,          0,
        0,          0,
        0,        100,
        0,          0,
        0,          0,
        0,          0,
        0,        127
    };

    int[] KNOB_UNITS = {
        Units.ms,
        Units.ms,
        Units.ms,
        Units.percent,
        Units.ms,
        Units.ms,
        Units.ms,
        Units.semitones
    };

    public MidiViewerKnobControl(MaxObject obj, int outlet) {
        super();
        super.init(null, obj, outlet);
        this.setup();
        System.out.println("super done");
    }

    public void setup() {
        System.out.println("BOT");
        super.setup(KNOB_NAMES, KNOB_RANGES, KNOB_UNITS);
    }
}