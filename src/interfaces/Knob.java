package interfaces;

import com.cycling74.max.*;
import java.util.*;
import java.lang.Math;
import java.io.*;

class Knob {
    MaxBox controller;
    MaxObject output;
    MaxBox myBox;
    String name;
    int inlet;
    String valType;
    Object val;

    public Knob(String name, int inlet, MaxBox controller, MaxObject output) {
        this.name = name;
        this.inlet = inlet;
        this.controller = controller;
        this.output = output;
        this.setup();
    }

    private void setup() {
        MaxPatcher patcher = output.getParentPatcher();
        
        MaxBox outputBox = output.getMaxBox();
        myBox = patcher.getNamedBox(name);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                patcher.disconnect(myBox, j, outputBox, i);
            }
        }
        patcher.connect(myBox, 0, outputBox, inlet);

        setAppearance(2);

        setType(0);

        setRange(0, 1000);

        setUnitStyle(3);
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

    public void setAppearance(int i) {
        sendMsg("appearance",i);
    }

    public void setType(int i) {
        sendMsg("_parameter_type",i);
    }

    public void setUnitStyle(int i) {
        sendMsg("_parameter_unitstyle",i);
    }

    public void setRange(double i, double j) {
        sendMsg("_parameter_range",i,j);
    }

    public void setValue(Object o) {

    }

    // Atom[] thing = new Atom[1];
    // thing[0] = Atom.newAtom(0);
}