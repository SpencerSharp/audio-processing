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
import interfaces.custom.SequencerKnobControl;
import midi.Midi2;
import midi.normalizing.Scale;

public class FunctionSequencer extends Sequencer {
    private static final int BASE_INLET = 1;

    static int BASE_PITCH = 72;
    static int BASE_BEAT_LENGTH = 1024;

    protected int pitch = BASE_PITCH - 6;
    protected int vel = 127;
    protected int dur = 0;

    private Function pitchFunc;
    private Scale scale;

    private MaxClock tickClock;

    protected int state = 0;

    SequencerKnobControl knobs;

    public FunctionSequencer() {
        // super();

        // knobs = new SequencerKnobControl(this, BASE_INLET);

        // tickClock = new MaxClock(new Executable() { 
        //     public void execute() { tick(); }});

        // tickClock.delay(1000);
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
        if (getInlet() == BASE_INLET + 3) {
            super.viewerClock.viewer.setYZoom((double)i);
        } else if (getInlet() == BASE_INLET + 7) {
            super.viewerClock.viewer.setYOffset((double)i);
        }
    }

    protected void playNote(int pitch, int vel, int dur) {
        if (notes.size() > 1) {
            notes.clear();
        }
        Note newNote = new Note(pitch, vel);
        sendOut(newNote.asMessage(Midi2.noteOn));
        if (notes.size() > 0) {
            sendOut(notes.poll().asMessage(Midi2.noteOff));
        }
        notes.add(newNote);
    }

    protected void tick() {
        if (this.state % BASE_BEAT_LENGTH == 0) {
            initFunctions();
        }
        double inp = ((double)state)/BASE_BEAT_LENGTH;
        double calc = pitchFunc.calculate(inp);

        // System.out.println(calc);

        pitch = (int) Math.round(BASE_PITCH + calc);

        if (notes.size() == 0 || pitch != ((int)(notes.peek().pitch))) {
            // System.out.println("base pitch " + BASE_PITCH + " base beat " + BASE_BEAT_LENGTH + " inp " + inp + " calc " + calc + " MIDI PITCH " + pitch);
            System.out.println("MIDI PITCH " + pitch);
            playNote(pitch, vel, dur);
        }
        state++;
        tickClock.delay(1);
    }

    protected void notifyDeleted() {
        super.notifyDeleted();
        tickClock.release();
	}
}
