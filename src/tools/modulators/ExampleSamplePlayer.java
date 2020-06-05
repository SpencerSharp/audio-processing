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

public class ExampleSamplePlayer extends KnobControlledMidiReceiver {
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

        endInd = new ModulatedVariable("n");
        startInd = new ModulatedVariable(stepperKnobs, StepperKnobControl.MAX_VAL, 8);
        stepSize = new ModulatedVariable("m");

        this.refresh();
    }

    private int getEnd() {
        return (int) endInd.value();
    }

    public void retrig() {
        indexInSample = startInd.valAt(curTime);
    }

    protected void step() {
        startInd.setInpVal(curTime);
        endInd.setInpVal(curTime);
        stepSize.setInpVal(curTime);
        if (indexInSample >= startInd.value()) {
            indexInSample += stepSize.value();
            if (indexInSample >= endInd.value()) {
                retrig();
            }
        } else {
            retrig();
        }
        super.step();
    }

    private void refresh() {
        Runnable runnable = () -> {
            new GlobalFunction("l(t)","l(t) = " + sample.length());
            endInd.reload();
            startInd.reload();
            stepSize.reload();
        };
        Thread thread = new Thread(runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
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
        if (indexInSample >= 0.0) {
            double pct = (indexInSample - startInd.value()) / (endInd.value() - startInd.value());
            sig = getGain(pct);
            int floor = (int) indexInSample;
            int ceil = floor + 1;
            if (ceil >= getEnd()) {
                sig *= sample.left(floor);
            } else {
                double w1 = (1 - (indexInSample - floor)) * sample.left(floor);
                double w2 = (1 - (ceil - indexInSample)) * sample.left(ceil);
                sig *= (float) (w1 + w2);
            }
            indexInSample *= -1;
            if (indexInSample < 0.0) {
                sig += leftSignal();
                // sig *= 0.5f;
            } else {
                indexInSample *= -1;
            }
        } else {
            indexInSample *= -1;
            double pct = (indexInSample - startInd.value()) / (endInd.value() - startInd.value());
            if (pct < 0.5) {
                pct += 0.5;
            } else {
                pct -= 0.5;
            }
            sig = getGain(pct);
            double indIn = startInd.value() + (pct * (endInd.value() - startInd.value()));
            int floor = (int) indIn;
            int ceil = floor + 1;
            if (ceil >= getEnd()) {
                sig *= sample.left(floor);
            } else {
                double w1 = (1 - (indIn - floor)) * sample.left(floor);
                double w2 = (1 - (ceil - indIn)) * sample.left(ceil);
                sig *= (float) (w1 + w2);
            }
        }
        return sig;
    }

    protected float getGain(double pct) {
        float ret = gainTable[(int) (pct * 256)];
        if (curTime % 50000 == 0) {
            System.out.println("gain at " + pct + " is " + ret + " startInd " + startInd.value() + " endInd " + endInd.value());
        }
        return ret;
    }

    protected float rightSignal() {
        float sig = super.rightSignal();
        if (indexInSample >= 0.0) {
            double pct = (indexInSample - startInd.value()) / (endInd.value() - startInd.value());
            sig = getGain(pct);
            int floor = (int) indexInSample;
            int ceil = floor + 1;
            if (ceil >= getEnd()) {
                sig *= sample.right(floor);
            } else {
                double w1 = (1 - (indexInSample - floor)) * sample.right(floor);
                double w2 = (1 - (ceil - indexInSample)) * sample.right(ceil);
                sig *= (float) (w1 + w2);
            }
            indexInSample *= -1;
            if (indexInSample < 0.0) {
                sig += rightSignal();
                // sig *= 0.5f;
            } else {
                indexInSample *= -1;
            }
        } else {
            indexInSample *= -1;
            double pct = (indexInSample - startInd.value()) / (endInd.value() - startInd.value());
            if (pct < 0.5) {
                pct += 0.5;
            } else {
                pct -= 0.5;
            }
            sig = getGain(pct);
            double indIn = startInd.value() + (pct * (endInd.value() - startInd.value()));
            int floor = (int) indIn;
            int ceil = floor + 1;
            if (ceil >= getEnd()) {
                sig *= sample.right(floor);
            } else {
                double w1 = (1 - (indIn - floor)) * sample.right(floor);
                double w2 = (1 - (ceil - indIn)) * sample.right(ceil);
                sig *= (float) (w1 + w2);
            }
        }
        return sig;
    }
}