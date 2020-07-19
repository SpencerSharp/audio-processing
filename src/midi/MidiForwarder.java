package midi;

import midi.controlled.KnobControlledMidiReceiver;
import utils.ArrayUtils;

public abstract class MidiForwarder extends KnobControlledMidiReceiver {

    private static final int NUM_OUTLETS = 2;
    private static final String[] OUTLET_NAMES = new String[]{
		"midi 2.0 out (last 32 bits)",
        "midi 2.0 out (first 32 bits)"
	};

    public MidiForwarder() {
        super(0, new String[0], NUM_OUTLETS, OUTLET_NAMES);
    }

    public MidiForwarder(int numInlets, String[] inletNames, int numOutlets, String[] outletNames) {
        super(numInlets, inletNames, NUM_OUTLETS + numOutlets, ArrayUtils.addAll(OUTLET_NAMES, outletNames));
    }



    public void handleMidiMsg(long msg) {
        super.handleMidiMsg(msg);
        outlet(0,(int)msg);
        outlet(1,(int)(msg >>> 32));
    }
}