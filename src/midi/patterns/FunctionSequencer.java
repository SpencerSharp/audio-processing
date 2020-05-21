package midi.patterns;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.*;
import java.io.*;
import org.mariuszgromada.math.mxparser.*;

import midi.sequencing.*;
import utils.global.*;
import datatypes.Note;
import interfaces.*;
import midi.Midi2;
import midi.normalizing.Scale;

public class FunctionSequencer extends Sequencer {
    static final int BASE_PITCH = 69;
    static int BASE_DELAY = 512;

    protected int pitch = BASE_PITCH - 6;
    protected int vel = 127;
    protected int dur = 0;

    private Function pitchFunc;
    private Scale scale;

    private MaxClock tickClock;

    protected int state = 0;
    protected int numStates = 4096;

    SequencerKnobControl knobs;

    public FunctionSequencer() {
        super();
        declareInlets(new int[]{DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,
        DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,
        DataTypes.ALL,DataTypes.ALL,DataTypes.ALL});
        knobs = new SequencerKnobControl(this, 2);

        tickClock = new MaxClock(new Executable() { 
            public void execute() { tick(); }});

        tickClock.delay(1000);
    }

    private void initFunctions() {
        if (!knobs.isSetup) {
            knobs.setup();
        }
        GlobalFunction.refresh();

        GlobalFunction pitchFunction = new GlobalFunction("p(t)");
        if (pitchFunction.isValid()) {
            pitchFunc = pitchFunction.asFunction();
        }
    }

    public void inlet(int i) {
        // if (getInlet() == 2) {
        //     BASE_DELAY = i;
        // }
    }

    protected void playNote(int pitch, int vel, int dur) {
        if (notes.size() > 0) {
            sendOut(notes.poll().asMessage(Midi2.noteOff));
        }
        if (notes.size() > 1) {
            notes.clear();
        }
        notes.add(new Note(pitch, vel));
        sendOut(notes.peek().asMessage(Midi2.noteOn));
    }

    protected void tick() {
        if (this.state == 0) {
            initFunctions();
        }
        pitch = (int) Math.round(pitchFunc.calculate(state));

        if (notes.size() == 0 || pitch != ((int)(notes.peek().pitch))) {
            System.out.println("MIDI PITCH " + pitch);
            playNote(pitch, vel, dur);
        }
        state++;
        if (state == numStates) {
            state = 0;
        }
        tickClock.delay(1);
    }

    protected void notifyDeleted() {
        super.notifyDeleted();
        tickClock.release();
	}
}
