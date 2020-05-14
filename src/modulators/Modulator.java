package modulators;

/*
derivative viewer
https://www.desmos.com/calculator/mslfnckxs6

i think i want like f(g(x)) or f'(g(x)) or something
*/

public class Modulator {
    public static final int LINEAR = 0;

    private static Modulator[] modulators = new Modulator[128];
    protected int id;
    // ModulatingFunction function;

    protected int domainMin;
    protected int domainMax;
    protected double rangeMin;
    protected double rangeMax;

    public Modulator(int domainMin, int domainMax, double rangeMin, double rangeMax, int type) {
        this.id = 1;

        while (this.id < modulators.length && modulators[this.id] != null) {
            this.id++;
        }

        this.domainMin = domainMin;
        this.domainMax = domainMax;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    public double getValAt(int ind) {
        ind = ind % (domainMax - domainMin);
        double pct = ((double) ind) / (domainMax - domainMin);
        double rangeDist = rangeMax - rangeMin;
        return rangeMin + (pct * rangeDist);
    }

    public void setDomainMin(int numSamples) {
        this.domainMin = numSamples;
    }
    
    public int getDomainMin() {
        return this.domainMin;
    }

    public void setDomainMax(int numSamples) {
        this.domainMax = numSamples;
    }

    public int getDomainMax() {
        return this.domainMax;
    }

    public void setRangeMin(double num) {
        this.rangeMin = num;
    }

    public double getRangeMin() {
        return this.rangeMin;
    }

    public void setRangeMax(double num) {
        this.rangeMax = num;
    }

    public double getRangeMax() {
        return this.rangeMax;
    }

    // public float get(int step) {
    //     value = function.get(step);
    //     for(ModulationRelationship relationship : modulatedBy) {
    //         value = relationship.applyTo(step, value);
    //     }
    //     return value;
    // }

    public static Modulator get(int id) {
        return modulators[id];
    }
}