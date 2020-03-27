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

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;

public class SamplePlayer extends MSPPerformer implements MessageReceiver
{
    private Sample playing;
	private int indexInSample;
    private int startInd;
    private int endInd;

	private static final String[] INLET_ASSIST = new String[]{
		"messages in"
	};
	private static final String[] OUTLET_ASSIST = new String[]{
		"output L","output R"
	};
	public SamplePlayer(Sample sample)
	{
        declareInlets(new int[]{SIGNAL});
		declareOutlets(new int[]{SIGNAL,SIGNAL});

		setInletAssist(INLET_ASSIST);
		setOutletAssist(OUTLET_ASSIST);

        playing = sample;
        indexInSample = 0;
        startInd = 0;
        endInd = sample.length();

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
	}

    public void setStart(float f) {
        startInd = f * sample.length();
    }

    public void setEnd(float f) {
        endInd = f * sample.length();
    }

    public void retrig() {
        indexInSample = startInd;
    }

    private void incIndex() {
        indexInSample++;
        if (indexInSample > endInd) {
            indexInSample = startInd;
        }
    }

	public void perform(MSPSignal[] ins, MSPSignal[] outs)
	{
		float[] audioL = outs[0].vec;
		float[] audioR = outs[1].vec;

        if (sample.isStereo()) {
			for(int i = 0; i < audioL.length; i++) {
				audioL[i] = sample.left(indexInSample);
				audioR[i] = sample.right(indexInSample);
				incIndex();
			}
		}
	}
}