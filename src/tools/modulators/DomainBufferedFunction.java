package tools.modulators;

import java.util.Arrays;
import java.util.function.Function;
import java.lang.Double;

import utils.global.*;
import utils.math.*;

public class DomainBufferedFunction {
    private GlobalFunction globalFunction;
    private MutableFunction javaFunction;
    private double[] values;
    public int resolution = 64;
    private int domain;
    public int mult;
    private int ratio;

    private boolean isValid = false;
    private double lastInvalidTime = 0.0;

        // Callable javaFunction;

    public DomainBufferedFunction(String name) {
        this.globalFunction = new GlobalFunction(name+"(t)");
        this.setDomainFunction("l");
        while (values == null) { Thread.yield(); }
    }

    public DomainBufferedFunction(MutableFunction func, int reso, int mult) {
        this.mult = mult;
        this.resolution = reso;
        this.javaFunction = func;
    }

    public void invalidate() {
        Runnable runnable = () -> {
            double[] newvalues = new double[resolution];

            for(int i = 0; i < resolution; i++) {
                if (globalFunction != null) {
                    newvalues[i] = globalFunction.asFunction().calculate(i*ratio);
                } else {
                    newvalues[i] = (domain/mult) * javaFunction.apply(i);
                }
            }

            values = newvalues;
        };
        Thread thread = new Thread(runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public void setDomainFunction(String name) {
        GlobalFunction domainFunc = new GlobalFunction(name+"(t)");
        if (domainFunc.isValid()) {
            domain = Integer.parseInt(domainFunc.getRightSide());
            // domain = 166154;
            if (mult != 0) {
                domain *= mult;
            }
            ratio = (int) Math.ceil(((double)domain)/resolution);
            this.invalidate();
        } else {
            values = new double[resolution];
        }
        System.out.println("max ind is " + ((domain-1) / ratio) + " so dont worry");
    }

    public double getValueAt(int ind) {
        if (ind % 4410 == 0) {
            if (javaFunction != null) {
                if (javaFunction.hasChanged()) {
                    this.invalidate();
                }
            }
        }
        int modind = (ind % domain) / ratio;

        if (modind < 0 || modind > values.length) {
            modind = 0;
        }

        return values[modind];
        // return 0.0;
        // return (domain * 0.5);
    }
}