/*
WORK CLOSELY WITH MIDIRECEIVER


*/

package audio;

import midi.Midi2;

public class Note implements Comparable<Note> {
    private static char maxNoteNum = 0;

    public int id;
    public double pitch;
    public char velocity;
    public double endTime;

    public Note(int pitch, int vel, double end) {
        this.id = (int) (maxNoteNum++);
        this.pitch = (double) pitch;
        this.velocity = (char) vel;
        this.endTime = end;
    }

    public long asMessage(long messageType) {
        return Midi2.prepareMessage(messageType,id,pitch,velocity);
    }

    @Override
    public int compareTo(Note other){
        if (this.endTime > other.endTime) {
            return 1;
        } else if (this.endTime < other.endTime) {
            return -1;
        }
        return 0;
    }
}