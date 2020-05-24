package players;

import com.cycling74.max.*;
import com.cycling74.msp.*;

public abstract class StereoSignalDevice extends MSPPerformer{
    protected abstract float leftSignal();
    protected abstract float rightSignal();
}
