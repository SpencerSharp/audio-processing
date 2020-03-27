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

class Sample implements MessageReceiver
{
    private String path;
	private AudioFileBuffer _afb = null;
    private boolean isLoaded = false;

	public Sample(String path) {
        isLoaded = false;
        this.path = path;
    }

    public void load() {
        // try {
        //     _afb = new AudioFileBuffer(path,this);
        // } catch (Exception exception) {}
        // while(!isLoaded){ Thread.yield(); }
    }

    public void save() {
        _afb = null;
    }

    public String getName() {
        return path;
    }

    public boolean isStereo() {
        return true;
    }

    public float left(int ind) {
        return _afb.buf[0][ind];
    }

    public float right(int ind) {
        return _afb.buf[1][ind];
    }

    public int length() {
        return _afb.buf[0].length;
    }

    //MessageReceiver interface
	public void messageReceived(Object src,int message_id,Object data)
	{
		switch (message_id)
		{
			case AudioFileBuffer.FINISHED_READING:
				isLoaded = true;
		}
	}
}
