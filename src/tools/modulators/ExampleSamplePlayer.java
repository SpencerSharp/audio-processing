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

public class ExampleSamplePlayer extends KnobControlledMidiReceiver {
    private static final double GRAIN_MS_LENGTH_FRAC = 0.4;

    protected Sample sample;
	protected double indexInSample;
    protected ModulatedVariable startInd;
    protected ModulatedVariable endInd;
    protected ModulatedVariable stepSize;

    protected StepperKnobControl stepperKnobs;

    private float[] gainTable;

    public CustomKnobControl getKnobs() {
        return stepperKnobs;
    }

	public ExampleSamplePlayer() {
        super();
    }

    protected void setup() {
        this.sample = new Sample("/Users/spencersharp/Documents/Music/Files/Library/Audio/Electronic/WhereWeAre.wav");
        sample.load();
        try {
            PersistentObject.getFile(false);
        } catch (IOException ex) {
            System.out.println("yikers");
        }
        
        indexInSample = 0.0;

        gainTable = new float[256];

        for(int i = 0; i < 256; i++) {
            gainTable[i] = (float) Math.sin(Math.PI*(i / 256.0));
        }

        stepperKnobs = new StepperKnobControl(this, 3);

        // endInd = new ModulatedVariable("n");
        startInd = new ModulatedVariable(stepperKnobs, StepperKnobControl.MAX_VAL, 1);
        // stepSize = new ModulatedVariable("m");

        this.refresh();
    }

    private int getSampleEnd() {
        return 166154;
    }

    private double getCurLength() {
        return GRAIN_MS_LENGTH_FRAC;
    }

    public void retrig() {
        indexInSample = startInd.valAt(curTime);
        // System.out.println("indin is NOW " + indexInSample);
    }

    protected void step() {
        startInd.setInpVal(curTime);
        // endInd.setInpVal(curTime);
        // stepSize.setInpVal(curTime);
        // indexInSample += stepSize.value();
        indexInSample += 1.0;

        // double totalPct = indexInSample / getSampleEnd();
        // System.out.println(totalPct);
        if (indexInSample >= startInd.value()) {
            if (indexInSample >= (getSampleEnd()-1)) {
                retrig();
            }
        } else {
            retrig();
        }
        super.step();
    }

    private void refresh() {
        // Runnable runnable = () -> {
        //     System.out.println("startInd " + startInd + " endInd " + endInd + " stepSize " + stepSize);
        //     new GlobalFunction("l(t)","l(t) = " + sample.length());
        //     System.out.println("aaaaaaaaaaaaaa");
        //     endInd.reload();
        //     System.out.println("bbbbbbbbbbbbb");
        //     startInd.reload();
        //     System.out.println("ccccccccccc");
        //     stepSize.reload();
        //     System.out.println("ddddddddddd");
        // };
        // Thread thread = new Thread(runnable);
        // thread.setPriority(Thread.MIN_PRIORITY);
        // thread.start();
    }

    public boolean sendString(String str) {
        if (!super.sendString(str)) {
            if (str.equals("persist")) {
                this.refresh();
            } else if (str.equals("setupfromdisc")) {
                this.refresh();
            }
        }
        return true;
    }

    protected float leftSignal() {
        float sig = super.leftSignal();
        sig = 1.0f;
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

    // protected float getGain(double pct) {
        // if (pct > 1.0 || pct < 0.0) {
        //     System.out.println(pct);
        // }
        // float ret = gainTable[(int) (0.5 * 256)];
        // if (curTime % 50000 == 0) {
        //     System.out.println("gain at " + pct + " is " + ret + " startInd " + startInd.value() + " endInd " + endInd.value());
        // }
        // return ret;
    // }

    protected float rightSignal() {
        float sig = super.rightSignal();
        sig = 1.0f;
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
}