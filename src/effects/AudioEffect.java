/*
Parent of SamplePlayer

What is relationship to MidiReceiver?
    MidiReceiver should prob be an "instrument", so it can inherit from AudioPlayer

PARAMS
Pan
Gain/volume
*/

package effects;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import players.AudioPlayer;

public class AudioEffect extends AudioPlayer {

    private static final String[] INLET_ASSIST = new String[]{
		"input L",
        "input R"
    };

    public AudioEffect() {
        super();
        declareInlets(new int[]{SIGNAL,SIGNAL});
        setInletAssist(INLET_ASSIST);
    }

    protected void step() {
        super.step();
    }

    public void perform(MSPSignal[] ins, MSPSignal[] outs) {
        float[] inL = ins[0].vec;
        float[] inR = ins[1].vec;
        
		float[] audioL = outs[0].vec;
		float[] audioR = outs[1].vec;

        for(int i = 0; i < audioL.length; i++) {
            /*
            INNER LOOP FUNCTION SOMEHOW
            */
            audioL[i] = leftSignal(inL[i]);
            audioR[i] = rightSignal(inR[i]);

            step();
        }
	}
}