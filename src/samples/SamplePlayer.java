/*
	Copyright (c) 2012 Cycling '74

	Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
	and associated documentation files (the "Software"), to deal in the Software without restriction, 
	including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
	and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
	subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all copies 
	or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
	INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
	IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
	WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
	OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package samples;
import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;

class SamplePlayer extends MSPPerformer
{
    private Sample sample;
	private int indexInSample;
    private int startInd;
    private int endInd;
    private double gain;

	private static final String[] INLET_ASSIST = new String[]{};
	private static final String[] OUTLET_ASSIST = new String[]{
		"output L","output R"
	};

	public SamplePlayer(Sample sample)
	{
        declareInlets(new int[]{});
		declareOutlets(new int[]{SIGNAL,SIGNAL});

		setInletAssist(INLET_ASSIST);
		setOutletAssist(OUTLET_ASSIST);

        this.sample = sample;
        indexInSample = 0;
        startInd = 0;
        endInd = sample.length();
        gain = 1.0;

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
	}

    public Sample getSample() {
        return sample;
    }

    public void setStart(float f) {
        startInd = (int) (f * sample.length());
    }

    public void setEnd(float f) {
        endInd = (int) (f * sample.length());
    }

    public void retrig() {
        indexInSample = startInd;
    }

    public void setGain(double volume) {
        if (volume > 1.0) {
            volume = 1.0;
        }
        gain = volume;
    }

    private void incIndex() {
        if (indexInSample >= 0) {
            indexInSample++;
            if (indexInSample >= endInd) {
                indexInSample = -1;
            }
        }
    }

	public void perform(MSPSignal[] ins, MSPSignal[] outs)
	{
		float[] audioL = outs[0].vec;
		float[] audioR = outs[1].vec;

        if (sample.isStereo()) {
			for(int i = 0; i < audioL.length; i++) {
                if (indexInSample >= 0) {
                    audioL[i] = ((float)gain) * sample.left(indexInSample);
                    audioR[i] = ((float)gain) * sample.right(indexInSample);
                } else {
                    audioL[i] = (float) 0.0;
                    audioR[i] = (float) 0.0;
                }
				incIndex();
			}
		}
	}
}