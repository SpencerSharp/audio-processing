package interfaces;

import com.cycling74.max.*;
import java.io.*;

import persistence.PersistentObject;

public abstract class CustomKnobControl extends PersistentObject {
    transient protected MaxObject outputObj;
    transient int baseOutlet;
    transient protected Knob[] knobs;
    transient protected double[] values;

    public CustomKnobControl() {
    }

    public CustomKnobControl(MaxObject obj, int outlet) {
        this.init(PersistentObject.tryLoad(this), obj, outlet);
    }

    public CustomKnobControl(MaxObject obj, int outlet, boolean shouldTryLoading) {
        this.init(null, obj, outlet);
    }

    protected void init(PersistentObject pers, MaxObject obj, int outlet) {
        if (pers == null) {
            values = new double[KnobControl.NUM_KNOBS];
        } else {
            values = ((CustomKnobControl)pers).values;
        }

        knobs = new Knob[KnobControl.NUM_KNOBS];

        outputObj = obj;
        baseOutlet = outlet;
    }

    public void assignValue(int knob, double value) {
        values[knob] = knobs[knob].setValue(value);
    }

    public double getValue(int knob) {
        return values[knob];
    }

    protected void setup(String[] names, int[] ranges, int[] units) {
        for (int i = 1; i <= KnobControl.NUM_KNOBS; i++) {
            Knob knob = new Knob("dial" + i, outputObj);
            knobs[i-1] = knob;
        }
        for (int i = 0; i < names.length; i++) {
            Knob knob = knobs[i];
            // patcher.connect(knob.myBox, 0, outBox, baseOutlet + i);
            knob.setName(names[i]);
            double min = (double) ranges[i*2];
            double max = (double) ranges[i*2+1];
            knob.setRange(min, max);
            knob.setUnit(units[i]);
            assignValue(i, min);
            knobs[i] = knob;
        }
        System.out.println("KNOBBY WHY SLOW");
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();

        int dimension = values.length;

        s.writeInt(dimension);

        for (int i = 0; i < values.length; i++) {
            s.writeDouble(values[i]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException  {
        s.defaultReadObject();

        int dimension = s.readInt();

        values = new double[dimension];

        for (int i = 0; i < values.length; i++) {
            values[i] = s.readDouble();
        }
    }
}