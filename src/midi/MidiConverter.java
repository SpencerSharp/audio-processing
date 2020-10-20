package midi;

import datatypes.Note;
import midi.controlled.KnobControlledMidiReceiver;
import utils.ArrayUtils;

public abstract class MidiConverter extends KnobControlledMidiReceiver {

    private static final int NUM_OUTLETS = 1;
    private static final String[] OUTLET_NAMES = new String[]{
		"midi out"
	};

    public MidiConverter() {
        super(0, new String[0], NUM_OUTLETS, OUTLET_NAMES);
    }

    public MidiConverter(int numInlets, String[] inletNames, int numOutlets, String[] outletNames) {
        super(numInlets, inletNames, NUM_OUTLETS + numOutlets, ArrayUtils.addAll(OUTLET_NAMES, outletNames));
    }

    public void handleMidiMsg(long msg) {
        super.handleMidiMsg(msg);

        Note note = Note.fromMessage(msg);

        byte[] midiMessage = note.asSimpleMessage(Midi2.noteOn);

        for (byte b : midiMessage) {

            outlet(10, (int) b);
        }

        // outlet(10, midiMessage);
    }
}