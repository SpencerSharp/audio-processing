package modulators;

import org.mariuszgromada.math.mxparser.*;
import com.cycling74.max.*;
import com.cycling74.msp.*;

import utils.global.GlobalFunction;

public class RawMathModulator extends MaxObject {

    private static final String testVal = "3";

    private static final String[] INLET_ASSIST = new String[]{
        "none",
		"function"
	};

    private static final String[] OUTLET_ASSIST = new String[]{
		"none",
        "function val at " + testVal
    };

    public RawMathModulator() {
        declareInlets(new int[]{DataTypes.ALL,DataTypes.ALL});
        setInletAssist(INLET_ASSIST);

        declareOutlets(new int[]{DataTypes.ALL,DataTypes.ALL});
        setOutletAssist(OUTLET_ASSIST);
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
        GlobalFunction f = new GlobalFunction(fName, fString);
    }
}