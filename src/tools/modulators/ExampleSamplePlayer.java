public class ExampleSamplePlayer {
    protected Sample sample;
	protected double indexInSample;
    protected ModulatedVariable startInd;
    protected ModulatedVariable endInd;
    protected double stepSize;

	public SamplePlayer(Sample sample) {
        super();

        this.sample = sample;
        indexInSample = 0.0;
        startInd = 0;
        stepSize = 1.0;
        endInd = sample.length();
    }

    public Sample getSample() {
        return sample;
    }

    public int getSampleLength() {
        return sample.length();
    }

    public int getStart() {
        return startInd;
    }

    public int getEnd() {
        return endInd;
    }

    public int getPos() {
        return ((int)indexInSample);
    }

    public void setStart(double f) {
        if (f > 1.0) {
            f = 1.0;
        } else if (f < 0.0) {
            f = 0.0;
        }
        startInd = (int) (f * sample.length());
        if (indexInSample < startInd) {
            retrig();
        }
    }

    public void setEnd(double f) {
        if (f > 1.0) {
            f = 1.0;
        } else if (f < 0.0) {
            f = 0.0;
        }
        endInd = (int) (f * sample.length());
    }

    public void retrig() {
        indexInSample = startInd;
    }

    protected void step() {
        if (indexInSample >= 0) {
            indexInSample += stepSize;
            if (indexInSample >= endInd) {
                retrig();
            }
        }
        super.step();
    }

    protected float leftSignal() {
        float sig = super.leftSignal();
        if (indexInSample >= 0.0) {
            int floor = (int) indexInSample;
            int ceil = floor + 1;
            if (ceil >= getEnd()) {
                sig *= sample.left(floor);
            } else {
                double w1 = (1 - (indexInSample - floor)) * sample.left(floor);
                double w2 = (1 - (ceil - indexInSample)) * sample.left(ceil);
                sig *= (float) (w1 + w2);
            }
        } else {
            sig *= 0.0f;
        }
        return sig;
    }

    protected float rightSignal() {
        float sig = super.rightSignal();
        if (indexInSample >= 0.0) {
            int floor = (int) indexInSample;
            int ceil = floor + 1;
            if (ceil >= getEnd()) {
                sig *= sample.right(floor);
            } else {
                double w1 = (1 - (indexInSample - floor)) * sample.right(floor);
                double w2 = (1 - (ceil - indexInSample)) * sample.right(ceil);
                sig *= (float) (w1 + w2);
            }
        } else {
            sig *= 0.0f;
        }
        return sig;
    }
}

}