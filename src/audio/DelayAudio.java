package audio;

import java.lang.*;

import audio.AudioPlayer;

public class DelayAudio {
    protected int delay;
    private int ind;
    private boolean isFull;

    private float[] delayBuf;

    public void setDelay(double ms) {
        delay = (int) (44.1 * ms);
        delayBuf = new float[Math.abs(delay)];
        ind = 0;
        isFull = false;
    }

    public float getLeftDelayed(float sig) {
        if (delay < 0) {
            if (ind == -1*delay) {
                if (!isFull) {
                    isFull = true;
                }
                ind = 0;
            }
            if (!isFull) {
                delayBuf[ind] = sig;
                return delayBuf[ind++];
            } else {
                float ret = delayBuf[ind];
                delayBuf[ind++] = sig;
                return ret;
            }
        }
        return sig;
    }

    public float getRightDelayed(float sig) {
        if (delay > 0) {
            if (ind == delay) {
                if (!isFull) {
                    isFull = true;
                }
                ind = 0;
            }
            if (!isFull) {
                delayBuf[ind] = sig;
                return delayBuf[ind++];
            } else {
                float ret = delayBuf[ind];
                delayBuf[ind++] = sig;
                return ret;
            }
        }
        return sig;
    }
}

