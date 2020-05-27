package utils.global;

import java.nio.channels.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class LockableFile {
    private File file;
    private FileLock lock;

    public LockableFile(String path) {
        this.file = new File(path);
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (Exception e) {}
            
        }
    }

    public void acquireLock() {
        try {
            if (lock == null) {
                FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
                lock = channel.lock();
            }
        } catch(Exception e) {}
    }

    public void releaseLock() {
        try {
            lock.release();
        } catch (Exception e) {}
        lock = null;
    }

    public void clear() {
        try {
            FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
            channel.truncate(0);
        } catch(Exception e) {}
    }

    public PrintWriter getWriteBuffer() {
        try {
            PrintWriter writer = new PrintWriter(
                new BufferedWriter(
                    new FileWriter(file)));
            return writer;
        } catch(Exception e) {}
        return null;
    }

    public BufferedReader getReadBuffer() {
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(file)));
            return reader;
        } catch(Exception e) {}
        return null;
    }
}