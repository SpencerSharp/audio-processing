package viewers.custom;

import viewers.ViewerClock;
import midi.MidiForwarder;
import interfaces.custom.MidiViewerKnobControl;
import interfaces.CustomKnobControl;
import utils.*;
import audio.*;
import datatypes.Note;
import tools.modulators.ModulatedVariable;

public class FunctionMapperDevice extends FunctionViewerDevice {

    boolean isSetup = false;

    public FunctionMapperDevice() {
        super(0, new String[0], 0, new String[0]);
    }

    public FunctionMapperDevice(int numInlets, String[] inletNames, int numOutlets, String[] outletNames) {
        super(numInlets, inletNames, 0 + numOutlets, ArrayUtils.addAll(new String[0], outletNames));
    }

    protected void setup() {

        isSetup = false;

        super.setup();

        isSetup = true;
    }

    protected float leftSignal() {

        if (!isSetup) {

            return 0.0f;
        }

        double inp = ((double)this.curTime)/44100;
        viewer.func.setInpVal(inp);
        double calc = viewer.func.getValue();

        return (float) calc;

        // return 1.0f;
    }
}