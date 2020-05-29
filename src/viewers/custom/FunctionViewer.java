package viewers.custom;

import com.cycling74.max.*;
import com.cycling74.jitter.*;
import java.util.*;

import viewers.*;
import utils.Colors;
import org.mariuszgromada.math.mxparser.*;

public class FunctionViewer extends Viewer {
    private static final int matrix_cols = 100;
    private static final int matrix_rows = 100;

    JitterMatrix jm = new JitterMatrix(4, "char", matrix_cols, matrix_rows);

    private Function function;

    public FunctionViewer(Function f) {
        function = f;
        System.out.println("f " + function);
        System.out.println("fstr " + function.getFunctionExpressionString());
    }

    public void setYZoom(double d) {
        System.out.println("FunctionViewer setYZoom is NOT IMPLEMENTED");
    }

    public void setYOffset(double d) {
        System.out.println("FunctionViewer setYZoom is NOT IMPLEMENTED");
    }

    public String getMatrix() {
        // System.out.println("call me baby");
        int dim[] = jm.getDim();

        for (int i = 0; i < dim[0]; i++) {
            for (int j = 0; j < dim[1]; j++) {
                jm.setcell2d(i,j,Colors.black);
            }
            jm.setcell2d(i,(int)(function.calculate(i/10.0)*50)+50,Colors.white);
        }

        return jm.getName();
    }
}
