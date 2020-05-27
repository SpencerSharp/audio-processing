package interfaces.custom;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import java.io.*;
import interfaces.*;

import samples.Sample;
import datatypes.Units;

public class SamplerKnobControl extends CustomKnobControl {
    Sample sample = new Sample("");

    String[] KNOB_NAMES = {
        "Start Pd",
        "Start Min",
        "Start Max",
        "Delay",
        "End Pd",
        "End Min",
        "End Max",
        "NONE"
    };

    int[] KNOB_RANGES = new int[]{
        0,    (int)sample.time(),
        0,    (int)sample.time(),
        0,    (int)sample.time(),
      -20,                    20,
        0,    (int)sample.time(),
        0,    (int)sample.time(),
        0,    (int)sample.time(),
        0,          0
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

    public SamplerKnobControl(MaxObject obj, int outlet) {
        super(obj, outlet);
    }

    public void setSample(Sample s) {
        sample = s;
        KNOB_RANGES = new int[]{
            0,    (int)sample.time(),
            0,    (int)sample.time(),
            0,    (int)sample.time(),
          -20,                  20,
            0,    (int)sample.time(),
            0,    (int)sample.time(),
            0,    (int)sample.time(),
            0,          0
        };
        MaxBox knobControl = outputObj.getParentPatcher().getNamedBox("knobControl");
        knobControl.send(KNOB_RANGES[1]);
    }

    public void setup() {
        System.out.println("BOT");
        super.setup(KNOB_NAMES, KNOB_RANGES, KNOB_UNITS);
    }
}