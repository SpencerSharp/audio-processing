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

public abstract class GainStutter extends InterpolatedEffect {
    private double prevGain;

    protected void step() {
        if (this.curTime % 4410 == 0) {
            double curGain = getGain();
            if (curGain > 0.0) {
                prevGain = curGain;
                setGain(0.0);
            } else {
                setGain(prevGain);
            }
        }
        super.step();
    }
}