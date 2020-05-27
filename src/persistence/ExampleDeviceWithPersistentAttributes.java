package persistence;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.*;
import java.nio.file.*;
import java.io.*;

public class ExampleDeviceWithPersistentAttributes extends MaxObject {
    private static final int LOAD_TIME = 11000;

    ExamplePersistentObject myAttrs;

    MaxClock loadingTimer;

    public ExampleDeviceWithPersistentAttributes() {
        declareInlets(new int[]{DataTypes.ALL,DataTypes.ALL,DataTypes.ALL});
        declareOutlets(new int[]{});

        loadingTimer = new MaxClock(new Executable() {public void execute() { setup(); }});
        loadingTimer.delay(LOAD_TIME);
    }

    public void inlet(int i) {
        if (getInlet() == 2) {
            System.out.println("Channel is " + i);
        } else if (getInlet() == 1) {
            System.out.println("Index is " + i);
        }
    }

    private void setup() {
        // myAttrs = (ExamplePersistentObject) PersistentObject.tryLoad(myAttrs);

        // if (myAttrs == null) {
        //     myAttrs = new ExamplePersistentObject();
        // }

        // if (myAttrs == null) {
        //     System.out.println("yeah this isnt good");
        // } else {
        //     myAttrs.showNumber();
        // }
    }
}