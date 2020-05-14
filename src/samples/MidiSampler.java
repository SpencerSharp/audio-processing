package samples;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;

import modulators.Modulator;
import util.Pitch;

public class MidiSampler extends Interpolator {
    Modulator startMod;
    Modulator endMod;
    Modulator panMod;

    int pitch;

    public MidiSampler(Sample sample) {
        super(sample);

        /*
        t is time in ms, i is index in samples (curTime)
        t = (i / (44.1 * 1000)) * 1000
        t / 1000 = i / (44.1 * 1000)
        (t / 1000) * (44.1 * 1000) = i
        44.1 * t = i
        i = t * 44.1
        t = i / 44.1
        */

        startMod = new Modulator(0, (int) (4096 * 44.1), 0.0, 0.05, 0);
        endMod = new Modulator(0, (int) (64 * 44.1), 0.001, 0.002, 0);
        panMod = new Modulator(0, (int) (sample.length() * 0.5), -50.0, 50.0, 0);
    }

    public void show() {
        System.out.println("pitch " + pitch + " vel " + velocity);
    }

    public void setStartPeriod(double ms) {
        startMod.setDomainMax((int)(ms * 44.1));
    }

    public void setEndPeriod(double ms) {
        endMod.setDomainMax((int)(ms * 44.1));
    }

    public void setStartMin(double ms) {
        startMod.setRangeMin(ms / sample.time());
    }

    public double getStartMin() {
        return startMod.getRangeMin();
    }

    public void setStartMax(double ms) {
        startMod.setRangeMax(ms / sample.time());
    }

    public double getStartMax() {
        return startMod.getRangeMax();
    }

    public void setEndMin(double ms) {
        endMod.setRangeMin(ms / sample.time());
    }

    public double getEndMin() {
        return endMod.getRangeMin();
    }

    public void setEndMax(double ms) {
        endMod.setRangeMax(ms / sample.time());
    }

    public double getEndMax() {
        return endMod.getRangeMax();
    }

    public void setPitch(int p) {
        pitch = p;
    }

    protected void step() {
        // end is set based on a time-oscillating LFO
        double start = startMod.getValAt(curTime);
        double end = endMod.getValAt(curTime);
        if (curTime % 5000 == 0) {
            // System.out.println("start " + start + " end " + end);
        }
        // 
        setStart(start);
        setEnd(start + end);
        // panning based on where in playing back the sample we are
        // this.setPan(panMod.getValAt(indexInSample));
        stepSize = Pitch.stepSize(pitch);
        super.step();
    }
}