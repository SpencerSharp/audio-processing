package utils;

public class Constant implements Evaluatable {
    private double inner;

    public Constant(double d) {
        inner = d;
    }

    public double getValue() {
        return inner;
    }
}