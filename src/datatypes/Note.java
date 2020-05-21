package datatypes;

import midi.Midi2;

public class Note implements Comparable<Note> {
    private static char maxNoteNum = 0;

    public int id;
    public double pitch;
    public char velocity;
    public double endTime;

    private Note() {

    }

    public Note(int pitch, int vel) {
        this.id = (int) (maxNoteNum++);
        this.pitch = (double) pitch;
        this.velocity = (char) vel;
    }

    public Note(int pitch, int vel, double end) {
        this.id = (int) (maxNoteNum++);
        this.pitch = (double) pitch;
        this.velocity = (char) vel;
        this.endTime = end;
    }

    public long asMessage(long messageType) {
        return Midi2.prepareMessage(messageType,id,pitch,velocity);
    }

    public static Note fromMessage(long message) {
        Note note = new Note();
        note.id = Midi2.getNoteId(message);
        note.pitch = (double) Midi2.getPitch(message);
        if (Midi2.isNoteOn(message)) {
            note.velocity = (char) 1;
        } else if (Midi2.isNoteOff(message)) {
            note.velocity = (char) 0;
        }
        return note;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) 
            return true;

        if(other == null || other.getClass() != this.getClass()) 
            return false;

        Note otherNote = (Note) other;
          
        // comparing the state of argument with  
        // the state of 'this' Object. 
        return otherNote.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return this.id;
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