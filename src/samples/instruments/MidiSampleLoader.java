package samples;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import java.util.*;
import java.io.*;

import midi.MidiReceiver;
import midi.Midi2;
import modulators.Modulator;
import viewers.SampleViewer;
import interfaces.SamplerKnobControl;

public class MidiSampleLoader extends MidiReceiver {
    Sample sample;
    // MidiSampler sampler;
    private static double startPeriod = 1024.0;
    private static double endPeriod = 1024.0;
    private static double startMin = 0.0;
    private static double startMax = 512.0;
    private static double endMin = 128.0;
    private static double endMax = 1024.0;
    private static double delay = 0.0;

    private static float leftSignal = 0.5f;
    private static float rightSignal = 0.5f;

    private static int retrigTime;

    public HashMap<Integer,MidiSampler> voicePlayers;
    public SampleViewer viewer;
    public SamplerKnobControl knobs;

    private static final String[] INLET_ASSIST = new String[]{
        "none",
		"midi 2.0 in (last 32 bits)",
        "midi 2.0 in (first 32 bits)",
        "start period",
        "end period",
        "start min",
        "start max",
        "end min",
        "end max"
	};

    private static final String[] OUTLET_ASSIST = new String[]{
		"output L",
        "output R",
        "view matrix"
    };

    public MidiSampleLoader() {
        super();

        declareInlets(new int[]{DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,
        DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,
        DataTypes.ALL,DataTypes.ALL,DataTypes.ALL,
        DataTypes.ALL,DataTypes.ALL});

        setInletAssist(INLET_ASSIST);

        declareOutlets(new int[]{SIGNAL,SIGNAL,DataTypes.ALL});
        setOutletAssist(OUTLET_ASSIST);

        this.sample = new Sample("/Users/spencersharp/Documents/Music/Files/Library/Audio/Electronic/WhereWeAre.wav");
        this.sample.load();

        System.out.println("retrig");

        this.retrig();

        retrigTime = -1;

        // this.retrig();
    }

    // public void loadbang() {
    //     System.out.println("BANG NANANA");
    // }

    private void trySetup() {
        if (retrigTime == -1) {
            retrigTime = curTime;
            if (knobs != null) {
                try {
                    for (int i = 0; i < 200; i++) {
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    System.out.println("interrupted rip");
                }
                knobs.setup();
            }
        }
    }

    public void handleMidiMsg(long msg) {
        // updates voices to be correct
        super.handleMidiMsg(msg);

        int noteId = Midi2.getNoteId(msg);
        // System.out.println("current keys " + voicePlayers.keySet());
        if (sample != null && !voicePlayers.containsKey(noteId)) {
            // the note is new
            // let's add it
            if (Midi2.isNoteOn(msg)) {
                MidiSampler sampler = new MidiSampler(sample);
                sampler.setStartPeriod(startPeriod);
                sampler.setEndPeriod(endPeriod);
                sampler.setStartMin(startMin);
                sampler.setStartMax(startMax);
                sampler.setEndMin(endMin);
                sampler.setEndMax(endMax);
                sampler.setDelay(delay);
                int spitch = Midi2.getPitch(msg);
                // System.out.println("sPitch " + spitch);
                sampler.setPitch(spitch);
                System.out.println("MIDI IN");
                voicePlayers.put(noteId, sampler);
            }
        } else {
            // System.out.println("num voices " + voicePlayers.keySet().size());
            // voicePlayers.remove(noteId);
            voicePlayers.get(noteId).end();
        }
    }

    private void retrig() {
        if (voicePlayers != null) {
            voicePlayers.clear();
        } else {
            voicePlayers = new HashMap<Integer,MidiSampler>();
            viewer = new SampleViewer(voicePlayers);
            knobs = new SamplerKnobControl(this, 3);
        }
        if (sample != null) {
            knobs.setSample(sample);
        }
        
        retrigTime = curTime;
    }

    public void anything(String message, Atom args[]) {
        // System.out.println("I AM ANYTHING " + message + " " + (message == null) + " " + ("none".equals(message)));
        trySetup();
        if (message == null || message.equals("none")) {
            return;
        }
        String path = message.substring(message.indexOf(":")+1,message.length());
        if (sample == null || sample.path.equals(path)) {
            return;
        }
        System.out.println("Loading " + path);
        this.sample = null;
        Runnable runnable = () -> {
            Sample mysample = new Sample(path);
            mysample.load();
            this.sample = mysample;
            this.retrig();
            System.out.println("Sample is now " + path);
        };
        Thread thread = new Thread(runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public void inlet(float msg) {
        trySetup();
        if (getInlet() == 2) {
            System.out.println("Set start period to " + msg);
            startPeriod = (double) msg;
        } else if (getInlet() == 6) {
            System.out.println("Set end period to " + msg);
            endPeriod = (double) msg;
        } else if (getInlet() == 3) {
            System.out.println("Set start min to " + msg);
            startMin = (double) msg;
        } else if (getInlet() == 4) {
            System.out.println("Set start max to " + msg);
            startMax = (double) msg;
        } else if (getInlet() == 5) {
            System.out.println("Set delay to " + msg);
            delay = (double) msg;
        } else if (getInlet() == 7) {
            System.out.println("Set end min to " + msg);
            endMin = (double) msg;
        } else if (getInlet() == 8) {
            System.out.println("Set end max to " + msg);
            endMax = (double) msg;
        } else {
            return;
        }
        retrigTime = curTime;
        if (sample != null) {
            viewer.setBounds(startMin, startMax, endMin, endMax, sample.time());
        }
    }

    private float regressRate = 0.97f;

    protected float leftSignal() {
        float total = 0.0f;
        for (MidiSampler sampler : voicePlayers.values()) {
            total += sampler.leftSignal();
        }
        return total;
    }

    protected float rightSignal() {
        float total = 0.0f;
        for (MidiSampler sampler : voicePlayers.values()) {
            total += sampler.rightSignal();
        }
        return rightSignal;
    }

    protected void step() {
        HashSet<Integer> toRemove = new HashSet<Integer>();
        for (int id : voicePlayers.keySet()) {
            MidiSampler sampler = voicePlayers.get(id);
            if (sampler.hasEnded()) {
                toRemove.add(id);
            } else {
                sampler.step();
            }
        }
        for (int id : toRemove) {
            voicePlayers.remove(id);
        }
        if (curTime % 1000 == 0) {
            boolean shouldShowVoices = curTime - retrigTime > 0.5 * (44.1 * 1000);
            viewer.setShowVoices(shouldShowVoices);
            String name = viewer.getMatrix();
            // System.out.println("matrix is named " + name);
            outlet(2,"jit_matrix",name);
        }
        super.step();
    }
}