/*
Parent of SamplePlayer

What is relationship to MidiReceiver?
    MidiReceiver should prob be an "instrument", so it can inherit from AudioPlayer

PARAMS
Pan
Gain/volume
*/

package players;

import com.cycling74.max.*;
import com.cycling74.msp.*;

public abstract class AudioPlayer extends MSPPerformer {
    protected int curTime;
    private double gain;
    private double pan;

    private static final String[] OUTLET_ASSIST = new String[]{
		"output L",
        "output R"
    };

    public AudioPlayer() {
        declareOutlets(new int[]{SIGNAL,SIGNAL});
        setOutletAssist(OUTLET_ASSIST);

        this.gain = 1.0;
        this.pan = 0.0;
    }

    public void setGain(double volume) {
        if (volume > 1.0) {
            volume = 1.0;
        }
        this.gain = volume;
    }

    public double getGain() {
        return this.gain;
    }

    public void setPan(double pan) {
        if (pan < -50.0) {
            pan = -50.0;
        } else if (pan > 50.0) {
            pan = 50.0;
        }
        this.pan = pan;
    }

    public double getPan(int channel) {
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
        this.curTime++;
    }

    protected abstract float leftSignal();
    protected abstract float rightSignal();

    public void perform(MSPSignal[] ins, MSPSignal[] outs)
	{
		float[] audioL = outs[0].vec;
		float[] audioR = outs[1].vec;

        for(int i = 0; i < audioL.length; i++) {
            /*
            INNER LOOP FUNCTION SOMEHOW
            */
            audioL[i] = leftSignal();
            audioR[i] = rightSignal();

            this.step();
        }
	}
}