package samples;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import modulators.Modulator;

public class MidiSampleLoader extends MSPPerformer {
    MidiSampler sampler;

    private static final String[] INLET_ASSIST = new String[]{
		"channel",
        "pitch",
        "velocity"
    };

	private static final String[] OUTLET_ASSIST = new String[]{
		"output L",
        "output R"
    };

    public MidiSampleLoader() {
        declareInlets(new int[]{DataTypes.ALL, DataTypes.ALL, DataTypes.ALL});
		declareOutlets(new int[]{SIGNAL,SIGNAL});

        setInletAssist(INLET_ASSIST);
        setOutletAssist(OUTLET_ASSIST);

        Sample sample = new Sample("/Users/spencersharp/Documents/Coding/Active/audio-processing/WhereWeAre.wav");
        sample.load();

        sampler = new MidiSampler(sample);
    }

    public void inlet(int i) {
        int theinlet = getInlet();
        System.out.println("inlet " + theinlet + " val " + i);
        if (theinlet == 1) {
			sampler.setVelocity(i);
		} else if (theinlet == 0) {
            sampler.setPitch(i);
		}
        sampler.show();
    }

    public void perform(MSPSignal[] ins, MSPSignal[] outs) {
        if (sampler != null) {
            sampler.perform(ins,outs);
        }
	}
}