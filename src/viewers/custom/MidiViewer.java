
// https://docs.cycling74.com/max5/tutorials/jit-tut/jitterchapter51.html
package viewers.custom;

import com.cycling74.max.*;
import com.cycling74.jitter.*;
import java.util.*;

import viewers.*;
import datatypes.Note;
import utils.*;

public class MidiViewer extends Viewer {
    private static final int matrix_cols = 128;
    private static final int matrix_rows = 128;

    JitterMatrix jm = new JitterMatrix(4, "char", matrix_cols, matrix_rows);

    PriorityQueue<Note> notes;
    HashSet<Note> displayed;

    public Evaluatable yZoom;
    public Evaluatable yOffset;

    private int minPitch;
    private int maxPitch;

    private double zoomPct;

    public MidiViewer(PriorityQueue<Note> notes) {
        this.notes = notes;
        displayed = new HashSet<Note>();
        yZoom = new Constant(100.0);
        yOffset = new Constant(0);
    }

    private void updateVals() {
        zoomPct = yZoom.getValue();
        minPitch = (int) yOffset.getValue();
        maxPitch = minPitch + ((int)((127 - minPitch) * (zoomPct/100.0)));
    }

    public String getMatrix() {
        updateVals();

        int dim[] = jm.getDim();

        HashSet<Note> current = new HashSet<Note>();

        for (Note note : notes) {
            if (displayed.contains(note)) {
                // note is on already
            } else {
                // note isnt on
                displayed.add(note);
            }
            current.add(note);
        }

        // SHIFT ALL LEFT ONE
        int planecount = jm.getPlanecount();
        int len = dim[1]*planecount;
        int[] offset = new int[]{0,0};
        int[] row = new int[matrix_rows];
        int totalLoaded = 0;
        int totalSaved = 0;

        for (int i = 0; i < matrix_cols; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != matrix_cols-1) {
                    offset[0] = i + 1;
                    int loaded = jm.copyVectorToArrayPlanar(j, 1, offset, row, matrix_rows, 0);
                    totalLoaded += loaded;
                } else {
                    row = new int[matrix_rows];
                }
                
                offset[0] = i;
                int saved = jm.copyArrayToVectorPlanar(j, 1, offset, row, matrix_rows, 0);
                totalSaved += saved;
            }
        }

        HashSet<Note> toRemove = new HashSet<Note>();

        for (Note note : displayed) {
            if (!current.contains(note)) {
                // note isn't in current played notes
                toRemove.add(note);
            } else {
                int steps = (int) (note.pitch - minPitch);
                int numSteps = maxPitch - minPitch + 1;
                int rowsPerStep = matrix_rows/numSteps;
                if (steps >= 0 && steps < numSteps) {
                    for (int i = 0; i < rowsPerStep; i++) {
                        jm.setcell2d(matrix_cols-1, matrix_rows - (steps * rowsPerStep + i), Colors.white);
                    }
                }
            }
        }

        // System.out.println("rem " + toRemove.size());

        for (Note note : toRemove) {
            displayed.remove(note);
        }

        return jm.getName();
    }
}
