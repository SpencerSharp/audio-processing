package utils;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Method;

public class Timer {
    private static HashMap<Integer,MaxClock> timers = new HashMap<Integer,MaxClock>();

    public static void sleep(int ms, Executable e) {
        int id = timers.size();
        timers.put(id,null);
        MaxClock timer = new MaxClock(new Executable() { 
            public void execute() { wrapper(id, e); }});
        timers.put(id, timer);
        timers.get(id).delay(ms);
    }

    private static void wrapper(int id, Executable e) {
        try {
            e.execute();
        } catch(Exception ex) {
            System.out.println("fatal exception " + ex);
            int n = 4 / 0;
        }
        timers.get(id).release();
        timers.remove(id);
    }
}