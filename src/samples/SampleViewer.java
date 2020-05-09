
// https://docs.cycling74.com/max5/tutorials/jit-tut/jitterchapter51.html
package samples;

import com.cycling74.max.*;
import com.cycling74.jitter.*;

class SampleViewer {
    JitterMatrix jm = new JitterMatrix(4, "char", 320, 240);
    int frgb[] = new int[] { 255, 255, 255, 255 };

    HashMap<Integer,MidiSampler> map;
    HashSet<Integer> starts;
    HashSet<Integer> ends;

    public SampleViewer(HashMap<Integer,MidiSampler> map) {
        this.map = map;
    }

    public void getMatrix() {
        int dim[] = jm.getDim();

        int color[];

        starts = new HashSet<Integer>();
        ends = new HashSet<Integer>();

        for (SamplePlayer sampler : map.values()) {
            int start = player.getStart();
            int end = player.getEnd();
            int len = player.getSample().length();

            int startInd = (((double)start) / len) * dim[1];
            int endInd = (((double)start) / len) * dim[1];

            starts.add(sampler.getStart());
            ends.add(sampler.getEnd());
        }

        for (int i = 0; i < dim[1]; i++) {
            if (starts.contains(i)) {
                color = new int[] {150, 64, 227, 32};
            } else if (ends.contains(i)) {
                color = new int[] {150, 255, 74, 38};
            } else {
                color = frgb;
            }
            for (int j = 0; j < dim[0]; j++) {
                jm.setcell2d(j,i,color);
            }
        }

        return jm.getName();
    }
}
