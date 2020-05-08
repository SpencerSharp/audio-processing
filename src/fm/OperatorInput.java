package fm;

import com.cycling74.max.*;
import com.cycling74.msp.*;

public class OperatorInput extends MSPPerformer
{
    private static final double inp_hz = 16.35;
    private static final double target_hz = 1 / 2.048;

    private static final double ratio = inp_hz / target_hz;

    private static final int backlogSize = 3000000;

    private float[] backlog;
    private int inInd = 0;
    private int outInd = 0;

    private double startTime = -1.0;
    private double endTime = -1.0;

    public OperatorInput()
    {
		declareInlets(new int[]{SIGNAL});
		declareOutlets(new int[]{SIGNAL});
        this.init();
    }

    public void bang() {
        this.init();
    }

    private void init() {
        System.out.println("init\n");
        backlog = new float[backlogSize];
        inInd = 0;
        outInd = 0;
        startTime = -1.0;
        endTime = -1.0;
    }

    public void perform(MSPSignal[] ins, MSPSignal[] outs)
    {
        if (startTime < 0) {
            startTime = System.currentTimeMillis();
        }
        if (endTime < 0 && inInd > backlogSize - 128) {
            endTime = System.currentTimeMillis();
            System.out.println(inInd + " steps took " + (1.0/1000 * (endTime - startTime)) + " seconds\n");
        }
        if (inInd % 10000 == 0) {
            System.out.println("curstep " + inInd + "\n");
        }
        float[] in  = ins[0].vec;
        float[] out = outs[0].vec;

		for(int i = 0; i < in.length;i++) {
            if (inInd < backlogSize) {
                backlog[inInd++] = in[i];
            }

            int divInd = (int) (outInd++ / ratio);

            out[i] = backlog[divInd];
		}
        // System.out.println("time is " + MaxClock.getTime() + "\n");
    }
}
