package interfaces.custom;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import java.io.*;
import interfaces.*;

import samples.Sample;
import datatypes.Units;

public class SequencerKnobControl extends CustomKnobControl {
    Sample sample = new Sample("");

    String[] KNOB_NAMES = {
        "Base delay",
        "NONE",
        "NONE",
        "Y Zoom Pct",
        "NONE",
        "NONE",
        "NONE",
        "Y Offset"
    };

    int[] KNOB_RANGES = new int[]{
        1024,    4096,
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

    public SequencerKnobControl(MaxObject obj, int outlet) {
        super();
        super.init(null, obj, outlet);
        // this.setup();
        // System.out.println("super done");
    }

    public void setup() {
        // System.out.println("BOT");
        // super.setup(KNOB_NAMES, KNOB_RANGES, KNOB_UNITS);
    }
}