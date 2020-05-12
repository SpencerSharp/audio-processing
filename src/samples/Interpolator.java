/*
Allows for simple and complex interpolation

Extend from end of loop certain amount

Linear extrapolation to "slide" back to new loop value

Could be an audio effect MAYBE
    have to consider ramifications of this (interpolating audio when it's not desired)
    not able to "know" when it should interpolate?

*/

package samples;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;

import modulators.Modulator;

class Interpolator extends SamplePlayer {
    private static final double dist = 44.1 * 0.05 * 1000;;

    private int threshold;
    private float startL;
    private float startR;
    private float targetL;
    private float targetR;

    private Modulator leftMod;
    private Modulator rightMod;

    private boolean interpolating;

    public Interpolator(Sample sample) {
        super(sample);

        interpolating = false;
        this.setEnd(endInd);
    }

    private void initModulators() {
        leftMod = new Modulator(threshold, endInd, startL, targetL, Modulator.LINEAR);
        rightMod = new Modulator(threshold, endInd, startR, targetR, Modulator.LINEAR);
    }

    public void setEnd(double f) {
        super.setEnd(f);
        threshold = endInd - (int)dist;
        if (threshold < 0) {
            threshold = endInd / 2;
        }
    }

    public void retrig() {
        super.retrig();
        interpolating = false;
    }

    protected void step() {
        if (indexInSample >= threshold) {
            if (!interpolating) {
                interpolating = true;
                startL = super.leftSignal();
                startR = super.rightSignal();
                targetL = sample.left(startInd);
                targetR = sample.right(startInd);
                initModulators();
            }
        }
        super.step();
    }

    protected float leftSignal() {
        if (!interpolating) {
            return super.leftSignal();
        }
        return (float) leftMod.getValAt(indexInSample);
    }

    protected float rightSignal() {
        if (!interpolating) {
            return super.rightSignal();
        }
        return (float) rightMod.getValAt(indexInSample);
    }
}