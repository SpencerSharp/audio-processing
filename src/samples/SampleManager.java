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

public class SampleSaver
{
    Sample nextSample;

	public SampleManager() {
        nextSample = getNextSample();
    }

    private Sample getNextSample() {
        nextSample = null;
        ProcessBuilder process = new ProcessBuilder("ezsample","todo");
        Runnable runnable = () -> {
            process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            path = reader.readLine();
            nextSample = new Sample(path);
            nextSample.load();
        };
        Thread thread = new Thread(runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public Sample getNextUntrimmed() {
        /*
        get file path of an untrimmed sample
        ezsample todo
        */
        while (nextSample == null) { Thread.yield(); }
        Sample sample = nextSample;
        nextSample = getNextSample();
        return sample;
    }
    
    private void load(String aiff) {

    }

    public void save(Sample aiff) {
        /*
        ezsample trim start end -f aiff
        ezsample tag x y z -f aiff
        ezsample todo -f aiff
        */
        aiff.save();

        CmdRunner p1 = new AsyncCmdRunner("ezsample trim start end -f aiff").run();
        CmdRunner p2 = new AsyncCmdRunner("ezsample tag x y z -f aiff").run();
        CmdRunner p3 = new CmdRunner("ezsample todo -f aiff").run();
    }
}
