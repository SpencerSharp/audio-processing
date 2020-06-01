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
    public transient Sample sample = new Sample("");

    transient String[] KNOB_NAMES = {
        "Start Pd",
        "Start Min",
        "Start Max",
        "Delay",
        "End Pd",
        "End Min",
        "End Max",
        "NONE"
    };

    transient int[] KNOB_RANGES = new int[]{
        0,    (int)sample.time(),
        0,    (int)sample.time(),
        0,    (int)sample.time(),
      -20,                    20,
        0,    (int)sample.time(),
        0,    (int)sample.time(),
        0,    (int)sample.time(),
        0,          0
    };

    transient int[] KNOB_UNITS = {
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
        this.setup();
    }

    public SamplerKnobControl(MaxObject obj, int outlet, Sample sample) {
        super(obj, outlet);
        this.setSample(sample);
        this.setup();
    }

    public void setSample(Sample s) {
        sample = s;
        sample.load();
        this.KNOB_RANGES = new int[]{
            1,    (int)sample.time(),
            1,    (int)sample.time(),
            1,    (int)sample.time(),
          -20,                  20,
            1,    (int)sample.time(),
            1,    (int)sample.time(),
            1,    (int)sample.time(),
            0,          0
        };
    }

    public void setup() {
        super.setup(KNOB_NAMES, KNOB_RANGES, KNOB_UNITS);
    }

    public double getStartPeriod() {
        return getValue(0);
    }

    public double getStartMin() {
        return getValue(1);
    }

    public double getStartMax() {
        return getValue(2);
    }

    public double getDelay() {
        return getValue(3);
    }

    public double getEndPeriod() {
        return getValue(4);
    }

    public double getEndMin() {
        return getValue(5);
    }

    public double getEndMax() {
        return getValue(6);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();

        int dimension = values.length;

        s.writeInt(dimension);

        for (int i = 0; i < values.length; i++) {
            s.writeDouble(values[i]);
        }

        s.writeUTF(sample.getName());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException  {
        s.defaultReadObject();

        int dimension = s.readInt();

        values = new double[dimension];

        for (int i = 0; i < values.length; i++) {
            values[i] = s.readDouble();
        }

        sample = new Sample(s.readUTF());
    }
}