package audio;

import com.cycling74.max.*;
import com.cycling74.msp.*;

public abstract class StereoSignalDevice extends MSPPerformer {
    protected abstract float leftSignal();
    protected abstract float rightSignal();

    protected String[] getOutletNames() {
        return new String[0];
    }
    protected int numOutlets() {
        return 0;
    }
    protected String[] getInletNames() {
        return new String[0];
    }
    protected int numInlets() {
        return 0;
    }
    protected int numInlets(int total) {
        return total;
    }
}
