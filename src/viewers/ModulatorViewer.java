package viewers;

import com.cycling74.max.*;
import com.cycling74.jitter.*;
import java.util.*;

import utils.Colors;
import org.mariuszgromada.math.mxparser.*;

public class ModulatorViewer extends Viewer {
    private static final int matrix_cols = 128;
    private static final int matrix_rows = 128;

    JitterMatrix jm = new JitterMatrix(4, "char", matrix_cols, matrix_rows);

    private Function function;
    private int t;

    public ModulatorViewer(Function f) {
        function = f;
        System.out.println("f " + function);
        System.out.println("fstr " + function.getFunctionExpressionString());
    }

    public String getMatrix() {
        // System.out.println("call me baby");
        int dim[] = jm.getDim();

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

        // System.out.println("call me maybe");

        double funcVal = function.calculate(t++);
        int matVal = (int) (funcVal * matrix_rows);

        // System.out.println("f " + function + " true " + funcVal + " rep " + matVal);
        // System.out.println("fstr " + function.getFunctionExpressionString());

        jm.setcell2d(matrix_cols-1, matVal, Colors.white);

        return jm.getName();
    }
}
