package samples;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import modulators.Modulator;

public class MidiSampler extends SamplePlayer {
    Modulator startMod;
    Modulator endMod;
    Modulator panMod;

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

        startMod = new Modulator(0, (int) (4096 * 44.1), 0.0, 0.5, 0);
        endMod = new Modulator(0, (int) (128 * 44.1), 0.01, 0.03, 0);
        panMod = new Modulator(0, (int) (sample.length() * 0.5), -50.0, 50.0, 0);
    }

    public void inlet(int i) {
        super.inlet(i);
        if (velocity == 0) {
            this.setGain(0.0);
        } else {
            this.setGain(0.2);
        }
        this.retrig();
    }

    public void show() {
        System.out.println("pitch " + pitch + " vel " + velocity);
    }

    protected void step() {
        // System.out.println("step to it");
        super.step();
        // end is set based on a time-oscillating LFO
        double start = startMod.getValAt(curTime);
        double end = endMod.getValAt(curTime);
        this.setStart(start);
        this.setEnd(start + end);
        // panning based on where in playing back the sample we are
        this.setPan(panMod.getValAt(indexInSample));
    }
}