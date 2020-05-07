package midi.sequencing;
/*

SWING
    1/4
        51.2
        93.7
SUBDIV
    Always sums to 128ms
PITCH
    Attempting to do step stutter, 1/2 rate for the up/down LFO
        flips every 1024ms
        period of 2048ms for 1/2
        so 1024ms period for 1/4

ONE BEAT IS 1024 ms

state += 2 takes 128ms
state += 4 takes 256ms
state += 8 takes 512ms
state += 16 takes 1024ms
*/



public class SnareSequencer extends ClockSequencer {
    static final int BASE_PITCH = 60;

    protected int pitch = BASE_PITCH + 7;
    protected int vel = 127;
    protected int dur = 0;

    protected void noteTick() {
        if (state % 32 == 0) {
            // add big pitch
            pitch += 14;
        } else if (state % 32 == 16) {
            pitch -= 14;
        }

        // little stutter pitch
        // gains 1 every 4
        // gains 4 every 16
        if (state % 16 == 0) {
            // reset stutter pitch gained
            pitch -= 4;
        }

        if (state % 4 == 0) {
            pitch += 2;
        } else if (state % 4 == 2) {
            pitch++;
        } else {
            pitch--;
        }

        if (state % 4 == 0) {
            // swing 51.2 first half
            dur = 66;
        } else if (state % 4 == 1) {
            // swing 51.2 second half
            dur = 62;
        } else if (state % 4 == 2) {
            // swing 93.7 first half
            dur = 120;
        } else {
            // swing 93.7 second half
            dur = 8;
        }
        super.playNote(pitch,vel,dur);
    }
}