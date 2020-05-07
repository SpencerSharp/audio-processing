package midi.sequencing;

import com.cycling74.max.*;
import java.util.*;
import java.lang.Math;
import java.io.*;

public class ClockSequencer extends MidiSender {
    private MaxClock myClock;

    protected int state = 0;
    protected int numStates = 32;
    // private int step = 128;
    // private int interval = 42;

    public ClockSequencer() {
        state = 0;
        myClock = new MaxClock(new Executable() { 
            public void execute() { noteTick(); }});
        noteTick();
    }

    protected void playNote(int pitch, int vel, int dur) {
        state++;
        if (state == numStates) {
            state = 0;
        }
        super.playNote(pitch, vel, dur);
        myClock.delay(dur);
    }

    protected void noteTick() {}

    protected void notifyDeleted() {
        myClock.release();
		// objectStillExists = false;
	}
}