/*
Parent of SamplePlayer

What is relationship to MidiReceiver?
    MidiReceiver should prob be an "instrument", so it can inherit from AudioPlayer

PARAMS
Pan
Gain/volume
*/

package audio;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.util.*;
import java.io.*;
import java.lang.*;

public abstract class AudioPlayer extends StereoSignalDevice {
    private static final int NUM_OUTLETS = 2;
    private static final String[] OUTLET_NAMES = new String[]{
		"output L",
        "output R"
    };

    protected int curTime;
    private double gain;
    private double pan;

    public AudioPlayer() {
        super();
        this.setup(0, new String[0]);
    }

    public AudioPlayer(int numOutlets, String[] outletNames) {
        super();
        this.setup(numOutlets, outletNames);
    }

    private void setup(int numOutlets, String[] outletNames) {
        int[] outletInfo = new int[numOutlets + NUM_OUTLETS];
        for(int i = 0; i < NUM_OUTLETS; i++) {
            outletInfo[i] = SIGNAL;
        }
        for(int i = NUM_OUTLETS; i < outletInfo.length; i++) {
            outletInfo[i] = DataTypes.ALL;
        }
        declareOutlets(outletInfo);

        ArrayList<String> outletNamesList = new ArrayList<String>();
        for(String s : this.OUTLET_NAMES) {
            outletNamesList.add(s);
        }
        for (String s : outletNames) {
            outletNamesList.add(s);
        }

        String[] outletNamesRay = new String[outletNamesList.size()]; 
        outletNamesRay = outletNamesList.toArray(outletNamesRay); 

		setOutletAssist(outletNamesRay);

        this.gain = 1.0;
        this.pan = 0.0;
    }

    protected String[] getOutletNames() {
        ArrayList<String> outletNames = new ArrayList<String>();
        for (String s : super.getOutletNames()) {
            outletNames.add(s);
        }
        for(String s : this.OUTLET_NAMES) {
            outletNames.add(s);
        }
        String[] outletNamesRay = new String[outletNames.size()]; 
        outletNamesRay = outletNames.toArray(outletNamesRay); 
        return outletNamesRay;
    }

    public void setGain(double volume) {
        if (volume > 1.0) {
            volume = 1.0;
        }
        gain = volume;
    }

    public float getGain() {
        return (float)gain;
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

    protected float leftSignal() {
        return getGain();
    }

    protected float rightSignal() {
        return getGain();
    }

    public void perform(MSPSignal[] ins, MSPSignal[] outs) {
		float[] audioL = outs[0].vec;
		float[] audioR = outs[1].vec;

        for(int i = 0; i < audioL.length; i++) {
            /*
            INNER LOOP FUNCTION SOMEHOW
            */
            audioL[i] = leftSignal();
            audioR[i] = rightSignal();

            step();
        }
	}
}