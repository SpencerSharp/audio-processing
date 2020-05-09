package midi.sequencing;

import audio.Note;
import midi.Midi2;

import com.cycling74.max.*;
import java.util.*;
import java.lang.Math;
import java.io.*;

public abstract class ClockSequencer extends MidiSender {
    private MaxClock endClock;
    protected MaxClock startClock;

    protected int state = 0;
    protected int numStates = 32;
    // private int step = 128;
    // private int interval = 42;

    public ClockSequencer() {
        super();

        state = 0;
        endClock = new MaxClock(new Executable() { 
            public void execute() { endNotes(); }});
        startClock = new MaxClock(new Executable() { 
            public void execute() { startNotes(); }});
        endClock.delay(10000);
        startClock.delay(10000);
    }

    protected void playNote(int pitch, int vel, int dur) {
        state++;
        if (state == numStates) {
            state = 0;
        }
        double clockTime = endClock.getTime();
        Note note = new Note(pitch, vel, clockTime + dur);
        sendOut(note.asMessage(Midi2.noteOn));
        notes.add(note);
    }

    protected void endNotes() {
        double clockTime = endClock.getTime();
        while (notes.size() > 0 && notes.peek().endTime <= clockTime) {
            sendOut(notes.poll().asMessage(Midi2.noteOff));
        }
        if (notes.size() > 0) {
            endClock.delay(notes.peek().endTime - clockTime);
        } else {
            endClock.delay(10);
        }
    }

    abstract protected void startNotes();

    protected void notifyDeleted() {
        endClock.release();
        startClock.release();
		// objectStillExists = false;
	}
}