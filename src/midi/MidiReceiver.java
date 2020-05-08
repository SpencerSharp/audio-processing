package midi;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.util.*;

public abstract class MidiReceiver
{
    protected int curTime;
    protected int pitch;
    protected int velocity;

	public MidiReceiver()
	{
		// declareInlets(new int[]{DataTypes.ALL, DataTypes.ALL, DataTypes.ALL});
		// declareOutlets(new int[]{SIGNAL,SIGNAL});

		// setInletAssist(INLET_ASSIST);
		// setOutletAssist(OUTLET_ASSIST);

        this.curTime = 0;
    }

    public void setPitch(int i) {
        this.pitch = i;
    }

    public void setVelocity(int i) {
        this.velocity = i;
    }

	public void inlet(int i) {
		// if (getInlet() == 1) {
		// 	velocity = i;
		// } else if (getInlet() == 0) {
        //     pitch = i;
		// }
	}

    protected void step() {
        this.curTime++;
    }

    // public void perform(MSPSignal[] ins, MSPSignal[] outs) {
    //     float[] audioL = outs[0].vec;
	// 	float[] audioR = outs[1].vec;

    //     for(int i = 0; i < audioL.length; i++) {
    //         audioL[i] = leftSignalAtTime(curTime);
    //         audioR[i] = rightSignalAtTime(curTime);
    //         curTime++;
    //     }
	// }

    // protected abstract float rightSignalAtTime(int i);
    // protected abstract float leftSignalAtTime(int i);
}
