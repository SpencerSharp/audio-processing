package midi.sequencing.patterns;

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
import persistence.*;

public class FunctionSequencer extends Sequencer {
    private static final int BASE_INLET = 1;

    static int BASE_PITCH = 72;
    static int BASE_BEAT_LENGTH = 1024;

    protected int pitch = BASE_PITCH - 6;
    protected int vel = 127;
    protected int dur = 0;

    private Function pitchFunc;

    private MaxClock tickClock;

    protected int state = 0;

    SequencerKnobControl knobs;

    public FunctionSequencer() {
        super();
        this.setup();
        tickClock = new MaxClock(new Executable() { 
            public void execute() { tick(); }});
        tickClock.delay(10000);
    }

    protected void initFunctions() {
        GlobalFunction.refresh();

        GlobalFunction pitchFunction = new GlobalFunction("p(t)");
        if (pitchFunction.isValid()) {
            pitchFunc = pitchFunction.asFunction();
        }
    }

    protected void setup() {
        knobs = new SequencerKnobControl(this, 3);
        PersistentObject.channel = 1;
    }

    protected CustomKnobControl getKnobs() {
        return knobs;
    }

    protected void playNote(int pitch, int vel, int dur) {
        if (notes.size() > 1) {
            notes.clear();
        }
        Note newNote = new Note(pitch, vel);
        sendOut(newNote.asMessage(Midi2.noteOn));
        endNote();
        notes.add(newNote);
    }

    protected void endNote() {
        if (notes.size() > 0) {
            sendOut(notes.poll().asMessage(Midi2.noteOff));
        }
    }

    protected void tick() {
        if (this.state % BASE_BEAT_LENGTH == 0) {
            initFunctions();
        }
        double inp = ((double)state)/BASE_BEAT_LENGTH;
        double calc = pitchFunc.calculate(inp);

        pitch = (int) Math.round(BASE_PITCH + calc);

        if (notes.size() == 0 || pitch != ((int)(notes.peek().pitch))) {
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
