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
import utils.math.*;
import utils.Evaluatable;

/*
How to decide names of functions?
For now, it goes in the constructor
Later can auto-declare it maybe
*/

public class ModulatedVariable implements Evaluatable {
    String name;
    String context;
    double inpVal = 0;
    DomainBufferedFunction bufferedFunction;
    MutableFunction javaFunction;

    public ModulatedVariable(String name) {
        this(name, "device");
    }

    public ModulatedVariable(String name, int domain) {
        this(name, "device", domain);
    }

    public ModulatedVariable(String name, String context) {
        this.name = name;
        this.context = context;
        this.reload();
        bufferedFunction = new DomainBufferedFunction(name);
    }

    public ModulatedVariable(String name, String context, int domain) {
        this.name = name;
        this.context = context;
        // this.inpVal = domain;
        this.reload();
        bufferedFunction = new DomainBufferedFunction(name, domain);
    }

    public ModulatedVariable(MutableFunction func, int reso, int domainMultiplier) {
        javaFunction = func;
        this.context = "memory";
        this.reload();
        bufferedFunction = new DomainBufferedFunction(func, reso, domainMultiplier);
    }

    public void reload() {
        if (javaFunction != null) {
            if (bufferedFunction != null) {
                bufferedFunction.setDomainFunction("l");
            }
            return;
        }
        System.out.println("contextual opportunity");
        int tempChannel = PersistentObject.channel;
        int tempInd = PersistentObject.ind;

        String myPath = GlobalFunction.GLOBAL_FUNCTION_FILE_PATH;

        if (!context.equals("global")) {
            myPath = PersistentInfo.getPath();
            if (myPath.charAt(myPath.length()-1) != '/') {
                myPath += '/';
            }
            if (!context.equals("project")) {
                myPath += tempChannel;
                File channelFile = new File(myPath);

                if (!channelFile.exists()) {
                    channelFile.mkdir();
                }

                myPath += "/";

                if (context.equals("device")) {
                    myPath += tempInd + "_";
                }
            }
        }
        myPath += "functions";
        
        LockableFile myFile = new LockableFile(myPath);
        if (!(myFile.compareTo(GlobalFunction.functionFile) == 0)) {
            System.out.println("DIFFFFFFFFFF!!!!!!   " + myPath );
            GlobalFunction.functionFile = myFile;
            GlobalFunction.refresh();
        }
    }

    public void setInpVal(int inpVal) {
        this.inpVal = inpVal;
    }

    public void setInpVal(double inpVal) {
        this.inpVal = inpVal;
    }

    public double getValue() {
        return bufferedFunction.getValueAt(inpVal);
    }

    public double valAt(int pVal) {
        return bufferedFunction.getValueAt(pVal);
    }
}