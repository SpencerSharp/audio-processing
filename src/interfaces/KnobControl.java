package interfaces;

import com.cycling74.max.*;
import java.util.*;
import java.lang.Math;
import java.io.*;

public class KnobControl extends MaxObject {
    private static final int NUM_KNOBS = 8;
    private Knob[] knobs;
    private int curKnob;

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
        super();
        declareInlets(new int[]{DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,DataTypes.ALL});
		declareOutlets(new int[]{DataTypes.ALL,DataTypes.ALL});

        setInletAssist(INLET_ASSIST);
		setOutletAssist(OUTLET_ASSIST);

        curKnob = 48;
    }

    protected void setup() {
        MaxPatcher parent = getParentPatcher();

        MaxBox ctl = parent.getNamedBox("ctl");
        MaxBox box = getMaxBox();

        if (ctl == null) {
            Atom[] things = new Atom[1];
            things[0] = Atom.newAtom("");
            ctl = parent.newObject("ctlin",things);
            ctl.setName("ctl");
        }

        parent.connect(ctl, 0, box, 0);
        parent.connect(ctl, 1, box, 1);

        knobs = new Knob[NUM_KNOBS];

        for (int i = 1; i <= NUM_KNOBS; i++) {
            Knob knob = new Knob("dial" + i, i + 1, ctl, this);
            knobs[i-1] = knob;
        }
    }

    // public void loadbang() {
    //     this.setup();
    // }

    public void bang() {
        this.setup();
    }

    public void inlet(int n) {
        if (getInlet() == 1) {
            curKnob = n - 48;
        } else if (getInlet() == 0) {
            knobs[curKnob].setValue(n);
        }
    }

    public void inlet(float f) {
        System.out.println("float " + f);
    }
}