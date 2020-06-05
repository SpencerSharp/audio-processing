package interfaces.custom;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import java.io.*;
import java.util.function.*;
import interfaces.*;

import samples.Sample;
import datatypes.Units;

public class StepperKnobControl extends CustomKnobControl implements Function<Integer,Double> {
    public static final int MAX_VAL = 256;
    public int ind;

    transient String[] KNOB_NAMES = {
        "128",
        "64",
        "32",
        "16",
        "8",
        "4",
        "2",
        "1"
    };

    transient int[] KNOB_RANGES = new int[]{
        0,    64,
        0,    64,
        0,    64,
        0,    64,
        0,    64,
        0,    64,
        0,    64,
        0,    64
    };

    transient int[] KNOB_UNITS = {
        Units.intt,
        Units.intt,
        Units.intt,
        Units.intt,
        Units.intt,
        Units.intt,
        Units.intt,
        Units.intt
    };

    public StepperKnobControl(MaxObject obj, int outlet) {
        super(obj, outlet);
        this.setup();
    }

    public void setup() {
        super.setup(KNOB_NAMES, KNOB_RANGES, KNOB_UNITS);
    }

    public Double apply(Integer ind) {
        int totalVal = 1;
        int val = 0;
        int pow = 2;
        for(int i = KNOB_NAMES.length - 1; i >= 0; i--) {
            if (ind % pow >= pow/2) {
                val += values[i];
            }
            totalVal += values[i];
            pow *= 2;
        }
        double res = ((double)val) / totalVal;
        System.out.println(res);
        return res;
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