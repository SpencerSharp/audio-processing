package interfaces;

import com.cycling74.max.*;
import java.util.*;
import java.lang.Math;
import java.io.*;

import utils.math.*;
import utils.Evaluatable;

class Knob implements Evaluatable {
    MaxObject mxjInstance;
    public MaxBox myBox;
    String name;
    String valType;

    double min;
    double max;

    int inlet;
    double val = Double.NaN;

    public Knob(String name, MaxObject parent) {
        this.name = name;
        mxjInstance = parent;
        System.out.println("a");
        MaxBox outBox = mxjInstance.getMaxBox();
        System.out.println("b");
        MaxPatcher patcher = mxjInstance.getParentPatcher();
        System.out.println("c");
        myBox = patcher.getNamedBox(name);
        System.out.println("d");
        this.setup();
    }

    private void setup() {
        // MaxPatcher patcher = mxjInstance.getParentPatcher();

        // patcher.connect(mxjInstance.getMaxBox(), inlet, myBox, 0);

        setAppearance(2);

        setType(0);

        setRange(0, 1000);

        setModMode(0);

        setUnit(3);
    }

    private void sendMsg(String msg, int i) {
        Atom[] ray = new Atom[1];
        ray[0] = Atom.newAtom(i);
        myBox.send(msg,ray);
    }

    private void sendMsg(String msg, double i, double j) {
        Atom[] ray = new Atom[2];
        ray[0] = Atom.newAtom(i);
        ray[1] = Atom.newAtom(j);
        myBox.send(msg,ray);
    }

    private void sendMsg(String msg, float f) {
        Atom[] ray = new Atom[1];
        ray[0] = Atom.newAtom(f);
        myBox.send(msg,ray);
    }

    private void sendMsg(String msg, String s) {
        Atom[] ray = new Atom[1];
        ray[0] = Atom.newAtom(s);
        myBox.send(msg,ray);
    }

    public void setModMode(int i) {
        sendMsg("_parameter_modmode",i);
    }

    public void setAppearance(int i) {
        sendMsg("appearance",i);
    }

    public void setType(int i) {
        sendMsg("_parameter_type",i);
    }

    public void setUnit(int i) {
        sendMsg("_parameter_unitstyle",i);
    }

    public void setRange(double i, double j) {
        this.min = i;
        this.max = j;
        sendMsg("_parameter_range",i,j);
    }

    public void setName(String s) {
        sendMsg("_parameter_shortname",s);
    }

    public void setRawValue(float f) {
        sendMsg("assign",f);
    }

    public double setValue(double d) {
        double f = d / 127.0;
        double conv = min + (f * (max - min));
        this.val = conv;
        float fmt = (float) conv;
        setRawValue(fmt);
        return conv;
    }

    public double getValue() {
        return val;
    }
}