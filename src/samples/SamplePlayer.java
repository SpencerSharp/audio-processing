package samples;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import midi.MidiReceiver;

class SamplePlayer extends MidiReceiver
{
    protected Sample sample;
	protected int indexInSample;
    private int startInd;
    private int endInd;

	private static final String[] INLET_ASSIST = new String[]{};
	private static final String[] OUTLET_ASSIST = new String[]{
		"output L","output R"
	};

	public SamplePlayer(Sample sample)
	{
        super();
        // declareInlets(new int[]{});
		// declareOutlets(new int[]{SIGNAL,SIGNAL});

		// setInletAssist(INLET_ASSIST);
		// setOutletAssist(OUTLET_ASSIST);

        this.sample = sample;
        indexInSample = 0;
        startInd = 0;
        endInd = sample.length();


        // Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
    }

    public Sample getSample() {
        return sample;
    }

    public int getStart() {
        return startInd;
    }

    public int getEnd() {
        return endInd;
    }

    public void setStart(double f) {
        if (f > 1.0) {
            f = 1.0;
        } else if (f < 0.0) {
            f = 0.0;
        }
        startInd = (int) (f * sample.length());
        if (indexInSample < startInd) {
            this.retrig();
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
        indexInSample = startInd;
    }

    protected void step() {
        // if (curTime % 10000 == 0) {
        //     System.out.println("time " + curTime + " ind " + indexInSample);
        // }
        if (indexInSample >= 0) {
            indexInSample++;
            if (indexInSample >= endInd) {
                this.retrig();
            }
        }
        super.step();
    }

    protected float leftSignal() {
        if (indexInSample >= 0) {
            return sample.left(indexInSample);
        }
        return 0.0f;
    }

    protected float rightSignal() {
        if (indexInSample >= 0) {
            return sample.right(indexInSample);
        }
        return 0.0f;
    }
}