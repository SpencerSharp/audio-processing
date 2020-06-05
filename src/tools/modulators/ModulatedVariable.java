package tools.modulators;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import java.util.*;
import java.util.function.*;
import java.io.*;

import midi.*;
import midi.controlled.*;
import tools.effects.*;
import tools.modulators.*;
import viewers.custom.*;
import interfaces.*;
import interfaces.custom.*;
import samples.*;
import persistence.*;
import utils.global.*;

/*
How to decide names of functions?
For now, it goes in the constructor
Later can auto-declare it maybe
*/

public class ModulatedVariable {
    String name;
    String context;
    double inpVal;
    DomainBufferedFunction bufferedFunction;
    Function<Integer,Double> javaFunction;

    public ModulatedVariable(String name) {
        this(name, "device");
    }

    public ModulatedVariable(String name, String context) {
        this.name = name;
        this.context = context;
        this.reload();
        while (bufferedFunction == null) { Thread.yield(); }
    }

    public ModulatedVariable(Function<Integer,Double> func, int reso, int domainMultiplier) {
        javaFunction = func;
        bufferedFunction = new DomainBufferedFunction(func, reso, domainMultiplier);
        this.context = "memory";
    }

    public void reload() {
        if (javaFunction != null) {
            if (bufferedFunction != null) {
                bufferedFunction.setDomainFunction("l");
            }
            return;
        }
        int tempChannel = PersistentObject.channel;
        int tempInd = PersistentObject.ind;

        String myPath = GlobalFunction.GLOBAL_FUNCTION_FILE_PATH;

        if (!context.equals("global")) {
            myPath = PersistentInfo.getPath();
            if (myPath.charAt(myPath.length()-1) != '/') {
                myPath += '/';
            }
            if (!context.equals("project")) {
                myPath += tempChannel + "/";
                if (context.equals("device")) {
                    myPath += tempInd + "_";
                }
            }
        }
        myPath += "functions";
        
        LockableFile myFile = new LockableFile(myPath);
        if (!myFile.equals(GlobalFunction.functionFile)) {
            GlobalFunction.functionFile = myFile;
            GlobalFunction.refresh();
        }
        bufferedFunction = new DomainBufferedFunction(name);
    }

    public void setInpVal(double inpVal) {
        this.inpVal = inpVal;
    }

    public double value() {
        return bufferedFunction.getValueAt(inpVal);
    }

    public double valAt(double inpVal) {
        double oldInp = this.inpVal;
        this.setInpVal(inpVal);
        double ret = this.value();
        this.setInpVal(oldInp);
        return ret;
    }
}