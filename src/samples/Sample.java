package samples;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import java.io.*;

public class Sample implements MessageReceiver {
    public String path;
	private AudioFileBuffer _afb = null;
    private boolean isLoaded = false;

	public Sample(String path) {
        isLoaded = false;
        this.path = path;
    }

    public void load() {
        try {
            System.out.println("loading sample");
            _afb = new AudioFileBuffer(path,this);
            System.out.println("loaded sample");
        } catch (Exception exception) {
            System.out.println(exception);
        }
        while(!isLoaded){ Thread.yield(); }
    }

    public void save() {
        // _afb = null;
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

    public double time() {
        if (!isLoaded) {
            return 0.0;
        }
        return this.length() / 44.1;
    }

    //MessageReceiver interface
	public void messageReceived(Object src,int message_id,Object data) {
		switch (message_id)
		{
			case AudioFileBuffer.FINISHED_READING:
				isLoaded = true;
		}
	}
}
