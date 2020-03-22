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
import java.util.Vector;
import java.util.Random;

// public class simplesynth extends MSPPerformer {
//     private String bufname;
//     private double sr = 0;

//     private static final String[] INLET_ASSIST = new String[] { "messages in" };

//     private static final String[] OUTLET_ASSIST = new String[] { "signal out L", "signal out R", };

//     public simplesynth() {
//         bail("(mxj~ simplesynth) must provide buffer name as argument.");
//     }

//     public simplesynth(String buf) {
//         declareInlets(new int[] { SIGNAL });
//         declareOutlets(new int[] { SIGNAL, SIGNAL });

//         setInletAssist(INLET_ASSIST);
//         setOutletAssist(OUTLET_ASSIST);

//         bufname = buf;
//     }

//     public void dspsetup(MSPSignal[] ins, MSPSignal[] outs) {
//         sr = outs[0].sr;
//     }

//     public void perform(MSPSignal[] ins, MSPSignal[] outs) {
//         int i, q, l;
//         float[] in = ins[0].vec;
//         float[] outl = outs[0].vec;
//         float[] outr = outs[1].vec;
//         float p;

//         float s1, s2;

//         amp = Math.sin(2.0 * Math.PI * j * 440.0 / 44100.0);

//         for (i = 0; i < outl.length; i++) {
//             outl[i] = amp;
//             outr[i] = amp;
//         }
//     }
// }