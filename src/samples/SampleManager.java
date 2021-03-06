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
import java.io.*;

class SampleManager
{
    Sample nextSample = null;
    Process process;

	public SampleManager() {
        getNextSample();
    }

    private void getNextSample() {
        this.nextSample = null;
        ProcessBuilder process = new ProcessBuilder("python3","/Users/spencersharp/Documents/Coding/Active/spools/build/ezsample/ezsample","todo");
        process.redirectErrorStream(true);
        Runnable runnable = () -> {
            try {
                Process proc = process.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String path = reader.readLine();
                Sample mysample = new Sample(path);
                mysample.load();
                this.nextSample = mysample;
            } catch (IOException exception) {}
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
        System.out.println("untrimmed pls " + this.nextSample);
        while (this.nextSample == null) { Thread.yield(); }
        System.out.println("next is " + this.nextSample);
        Sample sample = this.nextSample;
        getNextSample();
        return sample;
    }

    public void performAction(int midipitch) {

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

        CmdRunner p1 = new AsyncCmdRunner("ezsample trim start end -f aiff");
        p1.run();
        CmdRunner p2 = new AsyncCmdRunner("ezsample tag x y z -f aiff");
        p2.run();
        CmdRunner p3 = new CmdRunner("ezsample todo -f aiff");
        p3.run();
    }
}
