package modulators;

import org.mariuszgromada.math.mxparser.*;
import com.cycling74.max.*;
import com.cycling74.msp.*;

import utils.global.GlobalFunction;
import viewers.*;

public class RawMathModulator extends MaxObject {
    private static final String testVal = "3";

    private static final String[] INLET_ASSIST = new String[]{
        "none",
		"function",
        "application"
	};

    private static final String[] OUTLET_ASSIST = new String[]{
		"none",
        "function val at " + testVal,
        "matrix out"
    };

    private GlobalFunction globalFunction;
    private FunctionViewer viewer;
    private ViewerClock viewerClock;

    public RawMathModulator() {
        declareInlets(new int[]{DataTypes.ALL,DataTypes.ALL,DataTypes.ALL});
        setInletAssist(INLET_ASSIST);

        declareOutlets(new int[]{DataTypes.ALL,DataTypes.ALL,DataTypes.ALL});
        setOutletAssist(OUTLET_ASSIST);
    }

    public void inlet(int i) {
        if (getInlet() == 2) {
            if (globalFunction != null) {
                globalFunction.reload();
            }
        }
    }

    public void anything(String message, Atom args[]) {
        if (getInlet() == 1) {
            String fString = "";
            String fName = "";
            for (Atom word : args) {
                if (fName.equals("")) {
                    fName = word.toString();
                }
                fString += word.toString() + " ";
            }
            fString = fString.substring(0, fString.length()-1);
            globalFunction = new GlobalFunction(fName, fString);
            viewer = new FunctionViewer(globalFunction.asFunction());
            System.out.println("idiot");
            if (viewerClock != null) {
                viewerClock.notifyDeleted();
            }
            viewerClock = new ViewerClock(viewer,this,2,1.0);
            System.out.println("goddamnit");
        } else if (getInlet() == 2) {
            
        }

    }

    protected void notifyDeleted() {
        if (viewerClock != null) {
            viewerClock.notifyDeleted();
        }
	}
}