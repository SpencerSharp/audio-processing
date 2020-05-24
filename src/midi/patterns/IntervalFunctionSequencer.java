package midi.patterns;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import java.io.*;
import org.mariuszgromada.math.mxparser.*;

import midi.sequencing.*;
import utils.global.*;
import interfaces.*;
import interfaces.custom.SequencerKnobControl;

public class IntervalFunctionSequencer extends ClockSequencer {
    static final int BASE_PITCH = 69;
    static int BASE_DELAY = 512;

    protected int pitch = BASE_PITCH - 6;
    protected int vel = 127;
    protected int dur = 0;

    private Function delayFunc;
    private Function pitchFunc;

    SequencerKnobControl knobs;

    public IntervalFunctionSequencer() {
        super();
        declareInlets(new int[]{DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,
        DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,
        DataTypes.ALL,DataTypes.ALL,DataTypes.ALL});
        knobs = new SequencerKnobControl(this, 2);
    }

    private void initFunctions() {
        if (super.numStates != 10) {
            knobs.setup();
        }
        super.numStates = 10;

        GlobalFunction.refresh();

        GlobalFunction delayFunction = new GlobalFunction("d(t)");
        if (delayFunction.isValid()) {
            delayFunc = delayFunction.asFunction();
        }

        GlobalFunction pitchFunction = new GlobalFunction("p(t)");
        if (pitchFunction.isValid()) {
            pitchFunc = pitchFunction.asFunction();
        }
    }

    public void inlet(int i) {
        
        if (getInlet() == 2) {
            BASE_DELAY = i;
        }
    }

    protected void startNotes() {
        if (state % numStates == 0) {
            initFunctions();
        }

        dur = (int) (BASE_DELAY * (1.0 + delayFunc.calculate(state)));

        double frac = dur / (BASE_DELAY/4);
        frac = Math.round(frac);
        dur = ((int)frac)*(BASE_DELAY/4);

        System.out.println("dur " + dur);

        pitch = BASE_PITCH;

        // pitch = BASE_PITCH + delayFunc.calculate(state);

        super.playNote(pitch,vel,BASE_DELAY);
        startClock.delay(dur);
    }
}
