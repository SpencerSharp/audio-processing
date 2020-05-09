package modulators;

public class Modulator {
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

    public void setDomainMax(int numSamples) {
        this.domainMax = numSamples;
    }

    public void setRangeMin(double num) {
        this.rangeMin = num;
    }

    public void setRangeMax(double num) {
        this.rangeMax = num;
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