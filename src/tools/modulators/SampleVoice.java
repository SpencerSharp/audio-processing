package tools.modulators;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import java.util.*;
import java.io.*;

import audio.*;
import midi.*;
import midi.controlled.*;
import tools.effects.*;
import tools.modulators.*;
import viewers.custom.*;
import interfaces.*;
import interfaces.custom.*;
import samples.*;
import persistence.*;
import utils.global.*;
import utils.math.*;
import datatypes.*;

public class SampleVoice extends Voice {
    private static final double GRAIN_MS_LENGTH_FRAC = 0.4;

    protected Sample sample;
	protected double indexInSample;

    private float[] gainTable;

    protected ModulatedVariable startInd;

    private int grainLength;
    private int curTime;

	public SampleVoice(Sample sample, float[] gainTable, ModulatedVariable startInd, int length, int start) {
        this.sample = sample;
        this.gainTable = gainTable;
        this.startInd = startInd;
        this.grainLength = length;
        this.indexInSample = start;
    }

    private int getSampleEnd() {
        return sample.length();
    }

    public void retrig() {
        indexInSample = startInd.value();
    }

    public void setGrainLength(int len) {
        grainLength = len;
    }

    protected float getGain(double ind) {
        // todo: round at end instead of start
        // might hurt speed?
        long base = (long)ind * 127l;
        int gainInd = (int) (base / ((long)grainLength));
        if (gainInd < 0 || gainInd > gainTable.length) {
            // System.out.println(ind + " " + base + " " + gainInd);
            gainInd = 0;
        }
        float ret = gainTable[gainInd];
        return ret;
    }

    protected void step() {
        // endInd.setInpVal(curTime);
        // stepSize.setInpVal(curTime);
        // indexInSample += stepSize.value();
        indexInSample += 1.0;

        if (indexInSample >= startInd.value()) {
            if (indexInSample >= (getSampleEnd()-3)) {
                retrig();
            } else if (indexInSample - startInd.value() >= grainLength) {
                retrig();
            }
        } else {
            retrig();
        }
    }

    protected float leftSignal() {
        float sig = getGain(indexInSample - startInd.value());
        int floor = (int) indexInSample;
        int ceil = floor + 1;
        if (ceil >= getSampleEnd()) {
            sig *= sample.left(floor);
        } else {
            double w1 = (1 - (indexInSample - floor)) * sample.left(floor);
            double w2 = (1 - (ceil - indexInSample)) * sample.left(ceil);
            sig *= (float) (w1 + w2);
        }
        return sig;
    }

    protected float rightSignal() {
        float sig = getGain(indexInSample - startInd.value());
        int floor = (int) indexInSample;
        int ceil = floor + 1;
        if (ceil >= getSampleEnd()) {
            sig *= sample.right(floor);
        } else {
            double w1 = (1 - (indexInSample - floor)) * sample.right(floor);
            double w2 = (1 - (ceil - indexInSample)) * sample.right(ceil);
            sig *= (float) (w1 + w2);
        }
        return sig;
    }

    public void perform(MSPSignal[] ins, MSPSignal[] outs) {}
}