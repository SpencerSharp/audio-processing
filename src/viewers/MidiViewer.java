
// https://docs.cycling74.com/max5/tutorials/jit-tut/jitterchapter51.html
package viewers;

import com.cycling74.max.*;
import com.cycling74.jitter.*;
import java.util.*;

import utils.Colors;
import midi.sequencing.MidiSender;
import datatypes.Note;

public class MidiViewer extends Viewer {
    private static final int matrix_cols = 128;
    private static final int matrix_rows = 128;

    JitterMatrix jm = new JitterMatrix(4, "char", matrix_cols, matrix_rows);

    PriorityQueue<Note> notes;
    HashSet<Note> displayed;

    public MidiViewer(PriorityQueue<Note> notes) {
        this.notes = notes;
        displayed = new HashSet<Note>();
    }

    public String getMatrix() {
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
                jm.setcell2d(matrix_cols-1, (int) (127-note.pitch), Colors.white);
            }
        }

        // System.out.println("rem " + toRemove.size());

        for (Note note : toRemove) {
            displayed.remove(note);
        }

        return jm.getName();
    }
}
