package samples;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import midi.MidiReceiver;

class SamplePlayer extends MidiReceiver
{
    private Sample sample;
	protected int indexInSample;
    private int startInd;
    private int endInd;
    private double gain;
    private double pan;

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
        gain = 1.0;
        this.pan = 0.0;

        // Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
	}

    public void inlet(int i) {
        super.inlet(i);
    }

    public Sample getSample() {
        return sample;
    }

    public void setStart(double f) {
        startInd = (int) (f * sample.length());
    }

    public void setEnd(double f) {
        endInd = (int) (f * sample.length());
    }

    public void retrig() {
        indexInSample = startInd;
    }

    public void setGain(double volume) {
        if (volume > 1.0) {
            volume = 1.0;
        }
        this.gain = volume;
    }

    public void setPan(double pan) {
        if (pan < -50.0) {
            pan = -50.0;
        } else if (pan > 50.0) {
            pan = 50.0;
        }
        this.pan = pan;
    }

    private double getPan(int channel) {
        // left channel
        if (channel == 0) {
            // pan away from left channel
            if (pan > 0) {
                return (50.0 - pan) / 50.0;
            } else {
                return 1.0;
            }
        } else {
            // pan towards right channel
            if (pan > 0) {
                return 1.0;
            } else {
                return (50.0 - (-1 * pan)) / 50.0;
            }
        }
    }

    protected void step() {
        super.step();
        if (curTime % 10000 == 0) {
            System.out.println("time " + curTime + " ind " + indexInSample);
        }
        if (indexInSample >= 0) {
            indexInSample++;
            if (indexInSample >= endInd) {
                this.retrig();
            }
        }
    }

	public void perform(MSPSignal[] ins, MSPSignal[] outs)
	{
		float[] audioL = outs[0].vec;
		float[] audioR = outs[1].vec;

        if (sample.isStereo()) {
			for(int i = 0; i < audioL.length; i++) {
                if (indexInSample >= 0) {
                    audioL[i] = (float)getPan(0) * sample.left(indexInSample);
                    audioR[i] = (float)getPan(1) * sample.right(indexInSample);
                } else {
                    audioL[i] = (float) 0.0;
                    audioR[i] = (float) 0.0;
                }
				this.step();
			}
		}
	}
}