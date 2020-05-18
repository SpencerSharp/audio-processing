package viewers;

import com.cycling74.max.*;
import com.cycling74.jitter.*;
import java.util.*;
import java.io.*;

public class ViewerClock {
    private static final double FRAME_RATE = 60.0;
    private static final int FRAME_DELAY = (int) (1000.0 / FRAME_RATE);

    MaxClock internal;
    Viewer viewer;
    MaxObject parent;
    int myOutlet;

    public ViewerClock(Viewer v, MaxObject p, int matrixOutlet) {
        viewer = v;
        parent = p;
        myOutlet = matrixOutlet;
        internal = new MaxClock(new Executable() { 
            public void execute() { updateView(); }});
        internal.delay(FRAME_DELAY);
    }

    public ViewerClock(Viewer v, MaxObject p, int matrixOutlet, double framerate) {
        viewer = v;
        parent = p;
        myOutlet = matrixOutlet;
        internal = new MaxClock(new Executable() { 
            public void execute() { updateView(); }});
        internal.delay((int)1000.0 / framerate);
    }

    private void updateView() {
        String matrixName = viewer.getMatrix();
        parent.outlet(myOutlet, "jit_matrix", matrixName);
        internal.delay(FRAME_DELAY);
    }

    public void notifyDeleted() {
        internal.release();
        viewer = null;
		// objectStillExists = false;
	}
}