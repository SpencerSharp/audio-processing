package interfaces.custom;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import java.io.*;
import java.util.function.*;
import interfaces.*;
import tools.modulators.*;

import samples.Sample;
import datatypes.Units;
import utils.math.*;

public class StepperKnobControl extends CustomKnobControl implements MutableFunction {
    public static final int MAX_VAL = 256;

    private double changedTime = 0.0;

    transient String[] KNOB_NAMES = {
        "Grain Length",
        "Base",
        "32",
        "16",
        "8",
        "4",
        "2",
        "1"
    };

    transient int[] KNOB_RANGES = new int[]{
        138,    2205*10,
        -64,    63,
        -64,    63,
        -64,    63,
        -64,    63,
        -64,    63,
        -64,    63,
        -64,    63
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
        super(obj, outlet, false);
        this.setup();
    }

    public void setup() {
        super.setup(KNOB_NAMES, KNOB_RANGES, KNOB_UNITS);
    }

    public void assignValue(int knob, double value) {
        super.assignValue(knob, value);
        changedTime = MaxClock.getTime();
    }

    public boolean hasChanged() {
        if (changedTime > 0 && MaxClock.getTime() - 50.0 > changedTime) {
            changedTime = 0;
            ExampleSamplePlayer.GRAIN_LENGTH = (int) this.getValue(0);
            ExampleSamplePlayer.alertVoicesOfChange();
            return true;
        }
        return false;
    }

    public Double apply(Integer ind) {
        double frac = 0.5 + (values[1]/128.0);
        int pow = 2;
        int[] powray = new int[KNOB_NAMES.length];
        for(int i = KNOB_NAMES.length - 1; i > 1; i--) {
            powray[i] = pow;
            pow *= 2;
        }
        for(int i = 2; i < KNOB_NAMES.length; i++) {
            if (ind % powray[i] < (powray[i]/2)) {
                double val = values[i];
                if (val >= 1.0) {
                    frac += (1.0 - frac) * (val / 64.0);
                } else if (val <= -1.0) {
                    frac += frac * (val/64.0);
                }
            }
        }
        if (frac >= 1.0) {
            frac = 0.999;
        }
        return frac;
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