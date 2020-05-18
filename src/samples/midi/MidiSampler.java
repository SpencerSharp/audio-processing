package samples;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import org.mariuszgromada.math.mxparser.*;

import modulators.Modulator;
import utils.*;
import utils.global.GlobalFunction;
import audio.DelayAudio;


public class MidiSampler extends SamplePlayer {
    Function startFunc;
    Function endFunc;
    Modulator startMod;
    Modulator endMod;
    Modulator panMod;

    int pitch;

    DelayAudio delayer;

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
        // GlobalFunction.refresh();

        
        // GlobalFunction tryEnd = new GlobalFunction("n(t)");
        // if (tryEnd.isValid()) {
        //     endFunc = tryEnd.asFunction();
        // }
        endMod = new Modulator(0, (int) (64 * 44.1), 0.001, 0.002, 0);

        // GlobalFunction tryStart = new GlobalFunction("s(t)");
        // if (tryStart.isValid()) {
        //     startFunc = tryStart.asFunction();
        // }
        startMod = new Modulator(0, (int) (4096 * 44.1), 0.0, 0.05, 0);

        panMod = new Modulator(0, (int) (sample.length() * 0.5), -50.0, 50.0, 0);

        delayer = new DelayAudio();
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

    public void setDelay(double d) {
        delayer.setDelay(d);
    }

    protected float leftSignal() {
        return delayer.getLeftDelayed(super.leftSignal());
    }

    protected float rightSignal() {
        return delayer.getRightDelayed(super.rightSignal());
    }

    protected void step() {
        // end is set based on a time-oscillating LFO
        double start = 0.0;
        if (startFunc != null) {
            start = startFunc.calculate(curTime);
        } else {
            start = startMod.getValAt(curTime); // curtime doesnt use stepsize!
        }
        double end = 0.0;
        if (endFunc != null) {
            end = endFunc.calculate(curTime);
        } else {
            end = endMod.getValAt(curTime);
        }
        if (curTime % 44100 == 0) {
            System.out.println("start " + start + "|end " + end + "|start + end " + (start + end) + "|");
        }
        setStart(start);
        setEnd(start + end);
        // panning based on where in playing back the sample we are
        // this.setPan(panMod.getValAt(indexInSample));
        stepSize = Pitch.stepSize(pitch);
        super.step();
    }
}