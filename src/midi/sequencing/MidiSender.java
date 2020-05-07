package midi.sequencing;

import com.cycling74.max.*;
import java.util.*;
import java.lang.Math;
import java.io.*;

public abstract class MidiSender extends MaxObject {
    private int tickIndex;
    private boolean isRunning;
    
    public MidiSender() {
        declareInlets(new int[]{DataTypes.ALL});
		declareOutlets(new int[]{DataTypes.INT,DataTypes.INT});
        declareAttribute("maxtaps");
        declareAttribute("periodfactor");
    }

    public void inlet(int i) {
    }

    protected void playNote(int pitch, int vel, int dur) {
        outlet(0,pitch);
        outlet(1,vel);
        outlet(2,dur);
    }

    protected void notifyDeleted() {
		// objectStillExists = false;
	}
}