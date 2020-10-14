package tools.modulators;

import java.util.Arrays;
import java.util.function.Function;
import java.lang.Double;

import utils.global.*;
import utils.math.*;

public class DomainBufferedFunction {
    private GlobalFunction globalFunction;
    private MutableFunction javaFunction;
    private double[] values = null;
    public int resolution = 1024;
    private int domain;
    public int mult;
    private double ratio;

    private boolean isValid = false;
    private double lastInvalidTime = 0.0;

        // Callable javaFunction;

    public DomainBufferedFunction(String name) {
        this.globalFunction = new GlobalFunction(name+"(t)");
        this.setDomainFunction("l");
        while (values == null) { Thread.yield(); }
    }

    public DomainBufferedFunction(String name, int domain) {
        this.globalFunction = new GlobalFunction(name+"(t)");
        this.domain = domain;
        this.invalidate();
        // while (values == null) { Thread.yield(); }
    }

    public DomainBufferedFunction(MutableFunction func, int reso, int mult) {
        this.mult = mult;
        this.resolution = reso;
        this.javaFunction = func;
    }

    public void invalidate() {
        if (domain < resolution) {

            // domain 4
            // reso   64

            ratio = ((double)domain)/resolution;
        } else {
            ratio = (double) ((int) Math.ceil(((double)domain)/resolution));
        }

        values = new double[resolution];
        
        // Runnable runnable = () -> {
            // double[] 

        System.out.println(globalFunction.asFunction().checkSyntax());

            for(int i = 0; i < resolution; i++) {
                if (globalFunction != null) {
                    values[i] = globalFunction.asFunction().calculate(i*ratio);
                } else {
                    values[i] = (domain/mult) * javaFunction.apply(i);
                }
            }

            // values = newvalues;
        // };
        // Thread thread = new Thread(runnable);
        // thread.setPriority(Thread.MAX_PRIORITY);
        // thread.start();
    }

    public void setDomainFunction(String name) {
        GlobalFunction domainFunc = new GlobalFunction(name+"(t)");
        if (domainFunc.isValid()) {
            domain = Integer.parseInt(domainFunc.getRightSide());
            // domain = 166154;
            if (mult != 0) {
                domain *= mult;
            }
            this.invalidate();
        } else {
            values = new double[resolution];
        }
        System.out.println("max ind is " + ((domain-1) / ratio) + " so dont worry");
    }

    public double getValueAt(double ind) {
        if (ind % 4410 == 0) {
            if (javaFunction != null) {
                if (javaFunction.hasChanged()) {
                    this.invalidate();
                }
            }
        }
        int modind = (int) ((ind % domain) / ratio);

        if (modind < 0 || modind > values.length) {
            modind = 0;
        }

        return values[modind];
        // return 0.0;
        // return (domain * 0.5);
    }
}