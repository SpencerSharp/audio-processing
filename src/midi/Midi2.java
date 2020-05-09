/*

MIDI 2.0 SPECIFICATION IMPLEMENTATION

https://www.midi.org/articles-old/details-about-midi-2-0-midi-ci-profiles-and-property-exchange

The first 4 bits of every message contain a Message Type. The Message Type is used as a classification of message functions.  

mt 4
    value of 0x4
group 4
1001 4
channel 4
note number 8
attribute type 8
    0x3 for pitch 7.9
velocity 16
    whatever i want
attribute 16
    first 7 bits semitone
    last 9 bits fractional semitone

*/

package midi;

public class Midi2 {
    public static final long noteOn = 0b1001L;
    public static final long noteOff = 0b1000L;

    /*
    FRACTIONAL SEMITONES ARE CURRENTLY NOT SUPPORTED
    */
    public static long prepareMessage(long messageType, int id, double semitone, char velocity) {
        long wholeSemi = Math.round(semitone);
        long vel = (long) velocity;

        byte first = 0x40;

        // mt
        long ret = 0x40L << 56;

        // 1001
        ret |= messageType << 52;
        // System.out.println("\n"+String.format("0x%016X", ret));
        // note number
        long myNoteNum = (long) id;
        ret |= myNoteNum << 32;
        // System.out.println("\n"+String.format("0x%016X", ret));

        // velocity
        ret |= vel << 16;
        // System.out.println("\n"+String.format("0x%016X", ret));

        // first 7 bits of pitch
        ret |= wholeSemi << 9;

        return ret;
    }

    public static boolean isNoteOn(long midi2msg) {
        return ((midi2msg >> 52) & 0b1111) == Midi2.noteOn;
    }

    public static boolean isNoteOff(long midi2msg) {
        return ((midi2msg >> 52) & 0b1111) == 0b1000;
    }

    public static int getNoteId(long midi2msg) {
        return (int) (midi2msg >>> 32L & 0xFFFFL);
    }

    public static int getPitch(long midi2msg) {
        return (int) ((midi2msg >>> 9L) & 0x7FL);
    }

    // public static boolean isNoteOn()

    /*
    id = getNoteId();
    if (isNoteOn()) {
        voices.add(new Voice(id, getPitch()))
    } else if (isNoteOff()) {
        // HOW TO HAVE MULTIPLE VOICES WITH SAME PITCH???
        // use note #
        voices.remove(id);
    }
    */

    // public static void main(String[] args) {
    //     long val = prepareMessage(62.0, 65_535);

    //     System.out.println("\n"+String.format("0x%016X", val));

    //     System.out.println(""+getNoteId(val) + " " + getPitch(val) + " " + isNoteOn(val));
    // }
}