package midi.patterns;

import midi.sequencing.*;

public class SlowSequencer extends ClockSequencer {
    static final int BASE_PITCH = 69;

    protected int pitch = BASE_PITCH - 6;
    protected int vel = 127;
    protected int dur = 0;

    protected void startNotes() {
        super.numStates = 12;
        pitch = BASE_PITCH + state;
        dur = 1024;
        super.playNote(pitch,vel,1024);
        startClock.delay(dur);
    }
}
