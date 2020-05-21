package interfaces;

import com.cycling74.max.*;
import java.io.*;

public class CustomKnobControl extends KnobControl {
    protected MaxObject outputObj;
    int baseOutlet;

    public CustomKnobControl(MaxObject obj, int outlet) {
        System.out.println("i am the parent");
        outputObj = obj;
        baseOutlet = outlet;

        knobs = new Knob[NUM_KNOBS];
    }

    protected void setup(String[] names, int[] ranges, int[] units) {
        isSetup = true;
        MaxBox knobControl = outputObj.getParentPatcher().getNamedBox("knobControl");
        int zero = 0;
        knobControl.send(ranges[1]);
        try {
            for (int i = 0; i < 200; i++) {
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            System.out.println("interrupted rip");
        }

        MaxBox outBox = outputObj.getMaxBox();
        MaxPatcher patcher = outputObj.getParentPatcher();
        for (int i = 1; i <= NUM_KNOBS; i++) {
            Knob knob = new Knob("dial" + i, patcher);
            knobs[i-1] = knob;
        }
        for (int i = 0; i < names.length; i++) {
            Knob knob = knobs[i];
            patcher.connect(knob.myBox, 0, outBox, baseOutlet + i);
            knob.setName(names[i]);
            double min = (double) ranges[i*2];
            double max = (double) ranges[i*2+1];
            knob.setRange(min, max);
            knob.setUnit(units[i]);
            knobs[i] = knob;
        }
    }
}