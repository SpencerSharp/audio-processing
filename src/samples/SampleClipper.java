/*
	Copyright (c) 2012 Cycling '74

	Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
	and associated documentation files (the "Software"), to deal in the Software without restriction, 
	including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
	and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
	subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all copies 
	or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
	INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
	IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
	WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
	OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;

public class SampleClipper extends MSPPerformer implements MessageReceiver
{
    private SamplePlayer player = null;
    private SampleManager manager = null;
    private SampleViewer viewer = null;
    private static final CYCLE_SAMPLE = 47; // maybe C3? just guessing what C3 is lol
    
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

    private HashSet<Integer> midiSent;

	public SampleSaver()
	{
		declareInlets(new int[]{SIGNAL});
		declareOutlets(new int[]{SIGNAL,SIGNAL});

		setInletAssist(INLET_ASSIST);
		setOutletAssist(OUTLET_ASSIST);

        manager = new SampleManager();
    }

    public void inlet(float f) {
        if (player == null) {
            return;
        }
        switch(getInlet()) {
            case 1:
                setFileStartPos(f); break;
            case 2:
                setFileEndPos(f); break;
            case 3:
                setPlayMode(f); break;
            case 4:
                setZoomPct(f); break;
            case 5:
                setGain(f); break;
        }
    }

    private void setSampleStartPos(float pos) {
        player.setStart(pos);
        viewer.setStart(pos);
    }

    private void setSampleEndPos(float pos) {
        player.setEnd(pos);
        viewer.setEnd(pos);
    }

    private void setPlayMode(float mode) {
        // player.setMode(mode);
    }

    private void setZoomPct(float pct) {
        viewer.setZoom(pct);
    }

    private void setGain(float gain) {
        player.setGain(gain);
    }

    public void inlet(int n) {
        if midiSent.contains(n) {
            // endNote(n);
            midiSent.remove(n);
        } else {
            startNote(n);
        }
    }

    public void perform(MSPSignal[] ins, MSPSignal[] outs) {
        player.perform(ins, outs);
    }

    private void startNote(int n) {
        if (n == CYCLE_SAMPLE) {
            cycleSample();
        }
        manager.performAction(n);
    }

    private void cycleSample() {
        manager.save(player.getSample());
        Sample newSample = manager.getNextUntrimmed();
        if (newSample != null) {
            viewer = new SampleViewer(newSample);
            player = new SamplePlayer(newSample);
        } else {
            viewer = null;
            player = null;
        }
    }
}
