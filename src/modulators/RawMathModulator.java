package modulators;

import org.mariuszgromada.math.mxparser.*;
import com.cycling74.max.*;
import com.cycling74.msp.*;

import utils.global.GlobalFunction;
import viewers.ModulatorViewer;
import viewers.ViewerClock;

public class RawMathModulator extends MaxObject {
    private static final String testVal = "3";

    private static final String[] INLET_ASSIST = new String[]{
        "none",
		"function"
	};

    private static final String[] OUTLET_ASSIST = new String[]{
		"none",
        "function val at " + testVal,
        "matrix out"
    };

    private GlobalFunction globalFunction;
    private ModulatorViewer viewer;
    private ViewerClock viewerClock;

    public RawMathModulator() {
        declareInlets(new int[]{DataTypes.ALL,DataTypes.ALL});
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
        viewer = new ModulatorViewer(globalFunction.asFunction());
        System.out.println("idiot");
        if (viewerClock != null) {
            viewerClock.notifyDeleted();
        }
        viewerClock = new ViewerClock(viewer,this,2,20.0);
        System.out.println("goddamnit");
    }

    protected void notifyDeleted() {
        if (viewerClock != null) {
            viewerClock.notifyDeleted();
        }
		
	}
}