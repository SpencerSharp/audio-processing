
// https://docs.cycling74.com/max5/tutorials/jit-tut/jitterchapter51.html
package samples;


import com.cycling74.max.*;
import com.cycling74.jitter.*;
import java.util.*;



class SampleViewer {
    JitterMatrix jm = new JitterMatrix(4, "char", 200, 10);

    private static final int[] black = new int[] { 255, 0, 0, 0 };
    private static final int[] green = new int[] {100, 64, 227, 32};
    private static final int[] red = new int[] {100, 255, 74, 38};
    private static final int[] blue = new int[] {200, 21, 0, 255};
    private static final int[] orange = new int[] {200, 247, 182, 2};
    private static final int[] purple = new int[] {200, 94, 3, 252};
    private static final int[] yellow = new int[] {200, 255, 255, 0};

    HashMap<Integer,MidiSampler> map;
    HashSet<Integer> startMins;
    HashSet<Integer> startMaxes;
    HashSet<Integer> endMins;
    HashSet<Integer> endMaxes;
    // HashSet<Integer> starts;
    // HashSet<Integer> ends;

    public SampleViewer(HashMap<Integer,MidiSampler> map) {
        this.map = map;
        this.resetBounds();
    }

    private void resetBounds() {
        if (this.startMins == null) {
            this.startMins = new HashSet<Integer>();
            this.startMaxes = new HashSet<Integer>();
            this.endMins = new HashSet<Integer>();
            this.endMaxes = new HashSet<Integer>();
        } else {
            this.startMins.clear();
            this.startMaxes.clear();
            this.endMins.clear();
            this.endMaxes.clear();
        }
    }

    public void setBounds(double startMin, double startMax, double endMin, double endMax, double sampleTime) {
        this.resetBounds();
        int dim[] = jm.getDim();
        startMins.add((int)((startMin/sampleTime) * dim[0]));
        startMaxes.add((int)((startMax/sampleTime) * dim[0]));
        endMins.add((int)(((startMin+endMin)/sampleTime) * dim[0]));
        endMaxes.add((int)(((startMax+endMax)/sampleTime) * dim[0]));
    }

    public String getMatrix(boolean showVoices) {
        int dim[] = jm.getDim();

        // if (map != null && map.values().size() > 0) {
        //     this.resetBounds();
        // }

        int color[];

        HashSet<Integer> starts = new HashSet<Integer>();
        HashSet<Integer> ends = new HashSet<Integer>();

        for (MidiSampler sampler : map.values()) {
            int start = sampler.getStart();
            double startMin = sampler.getStartMin();
            double startMax = sampler.getStartMax();
            int end = sampler.getEnd();
            double endMin = sampler.getEndMin();
            double endMax = sampler.getEndMax();
            int len = sampler.getSample().length();

            int startInd = (int) ((((double)start) / len) * dim[0]);
            int startMinInd = (int) (startMin * dim[0]);
            int startMaxInd = (int) (startMax * dim[0]);
            int endInd = (int) ((((double)end) / len) * dim[0]);
            int endMinInd = (int) ((startMin + endMin) * dim[0]);
            int endMaxInd = (int) ((startMax + endMax) * dim[0]);

            if (showVoices) {
                starts.add(startInd);
                ends.add(endInd);
                startMins.add(startMinInd);
                startMaxes.add(startMaxInd);
                endMins.add(endMinInd);
                endMaxes.add(endMaxInd);
            }
        }

        // System.out.println(startMins);
        // System.out.println(startMaxes);

        for (int i = 0; i < dim[0]; i++) {
            if (startMins.contains(i) || startMins.contains(i - 1)) {
                color = blue;
            } else if (startMaxes.contains(i) || startMaxes.contains(i - 1)) {
                color = orange;
            } else if (endMins.contains(i) || endMins.contains(i - 1)) {
                color = yellow;
            } else if (endMaxes.contains(i) || endMaxes.contains(i - 1)) {
                color = purple;
            } else if (starts.contains(i)) {
                color = green;
            } else if (ends.contains(i)) {
                color = red;
            } else {
                color = black;
            }
            for (int j = 0; j < dim[1]; j++) {
                jm.setcell2d(i,j,color);
            }
        }

        return jm.getName();
    }
}
