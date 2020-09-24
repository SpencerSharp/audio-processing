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
import tools.modulators.ModulatedVariable;
import utils.*;

public class FunctionSequencer extends Sequencer {
    private static final int BASE_INLET = 1;

    static int BASE_PITCH = 72;
    Evaluatable BASE_BEAT_LENGTH;

    protected int pitch = BASE_PITCH - 6;
    protected int vel = 127;
    protected int dur = 0;

    private ModulatedVariable curPitch;

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
        curPitch = new ModulatedVariable("p", 4);
        BASE_BEAT_LENGTH = getKnobs().get(0);
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
            System.out.println("SERIOUS LEAK ERROR");
            notes.clear();
        }
        Note newNote = new Note(pitch, vel);
        System.out.println(newNote);
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
        if (this.state == 0) {
            initFunctions();
        }

        double inp = ((double)state)/BASE_BEAT_LENGTH.getValue();
        curPitch.setInpVal(inp);
        double calc = curPitch.getValue();

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
