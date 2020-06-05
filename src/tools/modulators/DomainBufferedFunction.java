package tools.modulators;

import java.util.function.Function;

import utils.global.*;

public class DomainBufferedFunction {
    private GlobalFunction globalFunction;
    private Function<Integer,Double> javaFunction;
    private double[] values;
    public int resolution = 64;
    private int domain;
    public int mult;
    private double ratio;

        // Callable javaFunction;

    public DomainBufferedFunction(String name) {
        globalFunction = new GlobalFunction(name+"(t)");
        this.setDomainFunction("l");
    }

    public DomainBufferedFunction(Function<Integer,Double> func, int reso, int mult) {
        this.mult = mult;
        this.resolution = reso;
        this.javaFunction = func;
        this.setDomainFunction("l");
    }

    public void setDomainFunction(String name) {
        GlobalFunction domainFunc = new GlobalFunction(name+"(t)");
        domain = Integer.parseInt(domainFunc.getRightSide());
        if (mult != 0) {
            domain *= mult;
        }
        ratio = ((double)domain)/resolution;
        values = new double[domain];
        populateFunction();
    }

    private void populateFunction() {
        if (globalFunction != null) {
            org.mariuszgromada.math.mxparser.Function myFunction = globalFunction.asFunction();
            for(int i = 0; i < resolution; i++) {
                values[i] = myFunction.calculate(i*ratio);
            }
        } else {
            System.out.println(globalFunction);
            System.out.println(javaFunction);
            for(int i = 0; i < resolution; i++) {
                values[i] = javaFunction.apply((int) (i*ratio));
            }
        }
    }

    public double getValueAt(double ind) {
        // return globalFunction.asFunction().calculate();
        return values[((int) (ind / ratio)) % resolution];
    }
}