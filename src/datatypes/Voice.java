package datatypes;

import midi.Midi2;

public class Voice {
    public int id;
    public int pitch;

    public Voice(int id, int pitch) {
        this.id = id;
        this.pitch = pitch;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) 
            return true;

        if(other == null || other.getClass() != this.getClass()) 
            return false; 

        Voice otherVoice = (Voice) other;

        // comparing the state of argument with  
        // the state of 'this' Object. 
        return otherVoice.hashCode() == this.hashCode();
    } 

    @Override
    public int hashCode() {
        return this.id; 
    } 
}