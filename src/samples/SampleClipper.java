package samples;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.util.*;

import viewers.SampleViewer;

@Deprecated
public class SampleClipper extends MSPPerformer
{
    private SamplePlayer player;
    private SampleManager manager;
    private SampleViewer viewer;
    private HashSet<Integer> midiSent;
    private static final int CYCLE_SAMPLE = 48; // maybe C3? just guessing what C3 is lol
    
    /*
    KNOBS
        1 - File Start
        2 - File End
        3 - 
        4 - 
            5 - Play mode (off, loop)
            6 - Zoom level (default is 100%, min is 0%)
            7 - 
            8 - Volume
    */

    /*
    MIDI
        Device Special - Done/Get Next
        Device Special - Begin tagging (switch to tagging mode)
            Keys - Tagging process
    */
    /*

    File start position
    File end position
    "Next" button
    Tagger messages
    Loop
    Volume
    */

	private static final String[] INLET_ASSIST = new String[]{
		"file start pos",
        "file end pos",
        "play mode",
        "zoom percent",
        "gain",
        "midi"
	};

	private static final String[] OUTLET_ASSIST = new String[]{
		"output L",
        "output R",
        "sample info",
        "displayed tags"
    };

	public SampleClipper()
	{
		declareInlets(new int[]{DataTypes.ALL,SIGNAL,SIGNAL,SIGNAL,SIGNAL,SIGNAL});
		declareOutlets(new int[]{SIGNAL,SIGNAL,SIGNAL,SIGNAL});

		setInletAssist(INLET_ASSIST);
		setOutletAssist(OUTLET_ASSIST);

        manager = new SampleManager();
        midiSent = new HashSet<Integer>();

        cycleSample();
    }

    public void bang() {
        // this is working!
        if (player != null) {
            player.retrig();
        }
    }

    public void inlet(float f) {
        if (player == null) {
            return;
        }
        System.out.println("float in " + f);
        switch(getInlet()) {
            case 0:
                setSampleStartPos(f); break;
            case 1:
                setSampleEndPos(f); break;
            case 2:
                setPlayMode(f); break;
            case 3:
                setZoomPct(f); break;
            case 4:
                setGain(f); break;
        }
    }

    private void setSampleStartPos(float pos) {
        System.out.println("pos now " + pos);
        // player.setStart(pos);
        // viewer.setStart(pos);
    }

    private void setSampleEndPos(float pos) {
        // player.setEnd(pos);
        // viewer.setEnd(pos);
    }

    private void setPlayMode(float mode) {
        // player.setMode(mode);
    }

    private void setZoomPct(float pct) {
        // viewer.setZoom(pct);
    }

    private void setGain(float gain) {
        player.setGain(gain);
    }

    public void inlet(int n) {
        System.out.println("in comes " + n);
        if (midiSent.contains(n)) {
            // endNote(n);
            midiSent.remove(n);
        } else {
            startNote(n);
        }
    }

    public void perform(MSPSignal[] ins, MSPSignal[] outs) {
        if (player != null) {
            player.perform(ins, outs);
        }
    }

    private void startNote(int n) {
        if (n == CYCLE_SAMPLE) {
            cycleSample();
        }
        manager.performAction(n);
    }

    private void cycleSample() {
        if (player != null) {
            manager.save(player.getSample());
        }
        Sample newSample = manager.getNextUntrimmed();
        System.out.println(newSample.getName());
        if (newSample != null) {
            // viewer = new SampleViewer(newSample);
            player = new SamplePlayer(newSample);
        } else {
            viewer = null;
            player = null;
        }
    }
}
