package midi;

import java.util.*;

public class BasicPlayer extends MidiReceiver
{
    protected float rightSignalAtTime(int i) {
        return (float) Math.sin(i/10000.0);
    }
    protected float leftSignalAtTime(int i) {
        return (float) Math.sin(i/10000.0);
    }
}
