
// https://docs.cycling74.com/max5/tutorials/jit-tut/jitterchapter51.html
package viewers;

import util.Colors;
import midi.sequencing.MidiSender;
import datatypes.Note;

import com.cycling74.max.*;
import com.cycling74.jitter.*;
import java.util.*;

public class MidiViewer extends MidiReceiver {
    private static final int matrix_cols = 128;
    private static final int matrix_rows = 128;

    JitterMatrix jm = new JitterMatrix(4, "char", matrix_cols, matrix_rows);

    HashSet<Note> notes;
    HashSet<Note> displayed;

    public MidiViewer(PriorityQueue<Note> notes) {
        this.notes = notes;
        displayed = new HashSet<Note>();
    }

    public void handleMidiMsg(long msg) {
        // System.out.println("msg is " + msg);
        int id = Midi2.getNoteId(msg);
        Note note = Note.fromMessage(msg);
        if (note.velocity > 0) {
            notes.add(note);
        } else {
            notes.remove(note);
        }
    }

    public String getMatrix() {
        int dim[] = jm.getDim();

        int[] color;

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

        for (Note note : displayed) {
            if (!current.contains(note)) {
                // note isn't in current played notes
                toRemove.add(note);
            } else {
                jm.setcell2d(matrix_cols-1, (int) note.pitch, Colors.white);
            }
        }

        System.out.println("rem " + toRemove.size());

        for (Note note : toRemove) {
            displayed.remove(note);
        }

        return jm.getName();
    }
}
