
// https://docs.cycling74.com/max5/tutorials/jit-tut/jitterchapter51.html
package samples;

import com.cycling74.max.*;
import com.cycling74.jitter.*;

class SampleViewer extends MaxObject {
    JitterMatrix jm = new JitterMatrix(4, "char", 320, 240);
    int frgb[] = new int[] { 255, 255, 255, 255 };
    int on = 2, off = 1;

    public SampleViewer() {

    }

    public SampleViewer(Sample sample) {
        
    }

    public void bang() {
        jit_matrix();
    }

    public void setStart(float f) {

    }

    public void setEnd(float f) {

    }

    public void setZoom(float f) {

    }

    public void jit_matrix() {
        int dim[] = jm.getDim();
        int count = 0;
        boolean notoff = true;
        for (int i = 0; i < dim[1]; i++)
            for (int j = 0; j < dim[0]; j++) {
                jm.setcell2d(j,i,frgb);
                // if (notoff)
                //     jm.setcell2d(j, i, frgb);
                // if ((notoff && (++count > on)) || (!notoff && (++count > off))) {
                //     count = 0;
                //     notoff = !notoff;
                // }
            }
        System.out.println("sent matrix");
        outlet(0, "jit_matrix", jm.getName());
    }
}
