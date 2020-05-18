package interfaces;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import java.io.*;

import samples.Sample;
import datatypes.Units;

public class SequencerKnobControl extends CustomKnobControl {
    Sample sample = new Sample("");

    String[] KNOB_NAMES = {
        "Base delay",
        "NONE",
        "NONE",
        "NONE",
        "NONE",
        "NONE",
        "NONE",
        "NONE"
    };

    int[] KNOB_RANGES = new int[]{
        0,    1024,
        0,    0,
        0,    0,
        0,    0,
        0,    0,
        0,    0,
        0,    0,
        0,    0
    };

    int[] KNOB_UNITS = {
        Units.ms,
        Units.ms,
        Units.ms,
        Units.ms,
        Units.ms,
        Units.ms,
        Units.ms,
        Units.ms
    };

    public SequencerKnobControl(MaxObject obj, int outlet) {
        super(obj, outlet);
        System.out.println("super done");
    }

    public void setup() {
        System.out.println("BOT");
        super.setup(KNOB_NAMES, KNOB_RANGES, KNOB_UNITS);
    }
}