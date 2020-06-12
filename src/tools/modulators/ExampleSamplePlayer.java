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
    public static int GRAIN_LENGTH = 840;

    protected Sample sample;
	protected double indexInSample;
    protected ModulatedVariable startInd;
    protected ModulatedVariable endInd;
    protected ModulatedVariable stepSize;

    protected StepperKnobControl stepperKnobs;

    private float[] gainTable;

    protected static ArrayList<SampleVoice> voices = new ArrayList<SampleVoice>();

    public CustomKnobControl getKnobs() {
        return stepperKnobs;
    }

	public ExampleSamplePlayer() {
        super();
    }

    protected void setup() {
        this.sample = new Sample("/Users/spencersharp/Music/Ableton/User Library/Samples/Library/Audio/Instruments/Guitar/DopeStrings.wav");
        sample.load();
        try {
            PersistentObject.getFile(false);
        } catch (IOException ex) {
            System.out.println("yikers");
        }

        
        indexInSample = 0.0;

        gainTable = new float[128];

        for(int i = 0; i < 128; i++) {
            gainTable[i] = (float) Math.sin(Math.PI*(((double)i) / 128));
        }

        stepperKnobs = new StepperKnobControl(this, 3);

        startInd = new ModulatedVariable(stepperKnobs, StepperKnobControl.MAX_VAL, 8);
        new GlobalFunction("l(t)","l(t) = " + sample.length());
        startInd.reload();


        // endInd = new ModulatedVariable("n");
        
        // stepSize = new ModulatedVariable("m");

        this.refresh();
    }

    private int getSampleEnd() {
        return sample.length();
    }

    // private double getCurLength() {
    //     return GRAIN_MS_LENGTH_FRAC;
    // }

    public void retrig() {
        indexInSample = startInd.value();
        // System.out.println("indin is NOW " + indexInSample);
    }

    protected void step() {
        if (startInd != null) {
            startInd.setInpVal(curTime);
        }
        for (SampleVoice voice : voices) {
            voice.step();
        }
        super.step();
    }

    public static void alertVoicesOfChange() {
        for (SampleVoice voice : voices) {
            voice.setGrainLength(GRAIN_LENGTH);
        }
    }

    private void refresh() {
        ArrayList<SampleVoice> tmpVoices = new ArrayList<SampleVoice>();
        SampleVoice base = new SampleVoice(sample, gainTable, startInd, GRAIN_LENGTH, 0);
        tmpVoices.add(base);
        SampleVoice complement = new SampleVoice(sample, gainTable, startInd, GRAIN_LENGTH, GRAIN_LENGTH/2);
        tmpVoices.add(complement);
        voices = tmpVoices;

        // int three = 4 / 0;
        /*
        Runnable runnable = () -> {

            // System.out.println("startInd " + startInd + " endInd " + endInd + " stepSize " + stepSize);
            
            // System.out.println("aaaaaaaaaaaaaa");
            // endInd.reload();
            // System.out.println("bbbbbbbbbbbbb");
            // startInd.reload();
            // System.out.println("ccccccccccc");
            // stepSize.reload();
            // System.out.println("ddddddddddd");

        };
        Thread thread = new Thread(runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
        */
    }

    // public boolean sendString(String str) {
    //     if (!super.sendString(str)) {
    //         if (str.equals("persist")) {
    //             this.refresh();
    //         } else if (str.equals("setupfromdisc")) {
    //             this.refresh();
    //         }
    //     }
    //     return true;
    // }




    protected float leftSignal() {
        float total = 0.0f;
        for (SampleVoice voice : voices) {
            total += voice.leftSignal();
        }
        return total;
    }

    protected float rightSignal() {
        float total = 0.0f;
        for (SampleVoice voice : voices) {
            total += voice.rightSignal();
        }
        return total;
    }
}