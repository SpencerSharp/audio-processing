package samples;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;

import midi.MidiReceiver;
import audio.DelayAudio;

class SamplePlayer extends MidiReceiver {
    protected Sample sample;
	protected double indexInSample;
    protected int startInd;
    protected int endInd;
    protected double stepSize;

	private static final String[] INLET_ASSIST = new String[]{};
	private static final String[] OUTLET_ASSIST = new String[]{
		"output L","output R"
	};

	public SamplePlayer(Sample sample) {
        super();
        // declareInlets(new int[]{});
		// declareOutlets(new int[]{SIGNAL,SIGNAL});

		// setInletAssist(INLET_ASSIST);
		// setOutletAssist(OUTLET_ASSIST);

        this.sample = sample;
        indexInSample = 0.0;
        startInd = 0;
        stepSize = 1.0;
        endInd = sample.length();


        // Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
    }

    public Sample getSample() {
        return sample;
    }

    public int getSampleLength() {
        return sample.length();
    }

    public int getStart() {
        return startInd;
    }

    public int getEnd() {
        return endInd;
    }

    public int getPos() {
        return ((int)indexInSample);
    }

    public void setStart(double f) {
        if (f > 1.0) {
            f = 1.0;
        } else if (f < 0.0) {
            f = 0.0;
        }
        startInd = (int) (f * sample.length());
        if (indexInSample < startInd) {
            retrig();
        }
    }

    public void setEnd(double f) {
        if (f > 1.0) {
            f = 1.0;
        } else if (f < 0.0) {
            f = 0.0;
        }
        endInd = (int) (f * sample.length());
    }

    public void retrig() {
        // System.out.println("rt");
        indexInSample = startInd;
    }

    protected void step() {
        // if (curTime % 10000 == 0) {
        //     System.out.println("time " + curTime + " ind " + indexInSample);
        // }
        if (indexInSample >= 0) {
            indexInSample += stepSize;
            if (indexInSample >= endInd) {
                retrig();
            }
        }
        super.step();
    }

    protected float leftSignal() {
        float sig = super.leftSignal();
        if (indexInSample >= 0.0) {
            sig *= sample.left((int)indexInSample);
        } else {
            sig *= 0.0f;
        }
        return sig;
    }

    protected float rightSignal() {
        float sig = super.rightSignal();
        if (indexInSample >= 0.0) {
            sig *= sample.right((int)indexInSample);
        } else {
            sig *= 0.0f;
        }
        return sig;
    }
}