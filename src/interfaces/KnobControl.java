package interfaces;

import com.cycling74.max.*;
import java.util.*;
import java.lang.Math;
import java.io.*;

public class KnobControl extends MaxObject {
    protected static final int NUM_KNOBS = 8;
    protected Knob[] knobs;
    protected MaxBox ctl;

    private int curKnob;
    private int max = -1;

    private static final String[] INLET_ASSIST = new String[]{
        "ctlin",
        "ctlin again",
        "knob1 in",
        "knob2 in",
        "knob3 in",
        "knob4 in",
        "knob5 in",
        "knob6 in",
        "knob7 in",
        "knob8 in"
    };

	private static final String[] OUTLET_ASSIST = new String[]{
		"messages",
        "amsgs"
    };

    public KnobControl() {
        declareInlets(new int[]{DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL});
		declareOutlets(new int[]{DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL});

        setInletAssist(INLET_ASSIST);
		setOutletAssist(OUTLET_ASSIST);
    }

    public void setup() {
        System.out.println("TOP");
        MaxPatcher parent = getParentPatcher();

        ctl = parent.getNamedBox("ctl");
        MaxBox box = getMaxBox();

        parent.connect(ctl, 0, box, 2);
        parent.connect(ctl, 1, box, 3);

        knobs = new Knob[NUM_KNOBS];

        knobSetup();
    }

    protected void knobSetup() {
        System.out.println("heLL on eARth");
        for (int i = 1; i <= NUM_KNOBS; i++) {
            Knob knob = new Knob("dial" + i, i + 1, this);
            knobs[i-1] = knob;
        }
    }

    public void inlet(int in) {
        System.out.println("int " + in + " inl " + getInlet());
        if (getInlet() == 0) {
            if (max != -1) {
                max = in;
                for (int i = 0; i < NUM_KNOBS; i++) {
                    knobs[i].setRange(0,max);
                }
            } else {
                max = in;
                setup();
            }
        }
        if (getInlet() == 2) {
            knobs[curKnob].setValue((float)(Math.pow(in / 127.0,2) * max));
        }
        if (getInlet() == 3) {
            curKnob = in - 48;
        }
    }

    // public void bang() {
    //     this.setup();
    // }

    public void inlet(float f) {
        System.out.println("float " + f + " inl" + getInlet());
        this.setup();
    }
}