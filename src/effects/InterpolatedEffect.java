/*
Parent of SamplePlayer

What is relationship to MidiReceiver?
    MidiReceiver should prob be an "instrument", so it can inherit from AudioPlayer

PARAMS
Pan
Gain/volume
*/

package effects;

import com.cycling74.max.*;
import com.cycling74.msp.*;

public abstract class InterpolatedEffect extends AudioEffect {
    private double prevGain;
    private double target;
    private boolean interpolating;
    private int startTime;
    private static final int interpsteps = 100;

    public InterpolatedEffect() {
        super();
        interpolating = false;
        startTime = curTime;
        prevGain = 1.0;
        target = 1.0;
    }

    protected void step() {
        double curGain = getGain();
        if (interpolating) {
            int steps = curTime-startTime;
            if (steps == interpsteps) {
                interpolating = false;
                prevGain = target;
                setGain(target);
            } else {
                double interpGain = prevGain;
                double pct = ((double)steps)/interpsteps;
                interpGain += (target - prevGain) * pct;
                setGain(interpGain);
            }
        } else if (curGain != prevGain) {
            interpolating = true;
            System.out.println(curTime);
            target = curGain;
            startTime = curTime;
            setGain(prevGain);
        } else {
            prevGain = curGain;
        }

        super.step();
    }
}