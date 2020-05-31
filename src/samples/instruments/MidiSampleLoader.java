package samples.instruments;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.reflect.*;
import java.lang.*;
import java.util.*;
import java.io.*;

import midi.*;
import midi.controlled.*;
import tools.effects.*;
import tools.modulators.*;
import viewers.custom.*;
import interfaces.*;
import interfaces.custom.*;
import samples.*;

public class MidiSampleLoader extends KnobControlledMidiReceiver {
    private int retrigTime;

    public HashMap<Integer,MidiSampler> voicePlayers;
    public SampleViewer viewer;

    SamplerKnobControl knobs;

    private static final int NUM_OUTLETS = 1;
    private static final String[] OUTLET_NAMES = new String[]{
        "view matrix"
    };

    public MidiSampleLoader() {
        super(0, new String[0], NUM_OUTLETS, OUTLET_NAMES);

        this.retrig();

        retrigTime = -1;

        for (int i = 0; i < 8; i++) {
            outlet(2+i, -47.0);
        }
    }

    protected void setup() {
        System.out.println("setter");
        knobs = new SamplerKnobControl(this, 3);
        for (int i = 0; i < 8; i++) {
            outlet(2+i, this.getKnobs().getValue(i));
        }
    }

    protected CustomKnobControl getKnobs() {
        return knobs;
    }

    private Sample getSample() {
        if (knobs == null) {
            return null;
        }
        return knobs.sample;
    }

    public void handleMidiMsg(long msg) {
        // updates voices to be correct
        super.handleMidiMsg(msg);

        int noteId = Midi2.getNoteId(msg);
        // System.out.println("current keys " + voicePlayers.keySet());
        if (getSample() != null && !voicePlayers.containsKey(noteId)) {
            // the note is new
            // let's add it
            if (Midi2.isNoteOn(msg)) {
                MidiSampler sampler = new MidiSampler(getSample());
                sampler.setStartPeriod(knobs.getStartPeriod());
                sampler.setEndPeriod(knobs.getEndPeriod());
                sampler.setStartMin(knobs.getStartMin());
                sampler.setStartMax(knobs.getStartMax());
                sampler.setEndMin(knobs.getEndMin());
                sampler.setEndMax(knobs.getEndMax());
                sampler.setDelay(knobs.getDelay());
                int spitch = Midi2.getPitch(msg);
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
        }
        retrigTime = curTime;
    }

    public boolean sendString(String message) {
        if (super.sendString(message)) {
            return true;
        }
        String path = message.substring(message.indexOf(":")+1,message.length());
        Sample sample = getSample();
        System.out.println("yo path is " + path);
        if (sample == null || sample.path.equals(path)) {
            return true;
        }
        System.out.println("Loading " + path);
        this.knobs.sample = null;
        Runnable runnable = () -> {
            Sample mysample = new Sample(path);
            mysample.load();
            this.knobs.sample = mysample;
            this.retrig();
        };
        Thread thread = new Thread(runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
        return true;
    }

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
        return total;
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
            outlet(10,"jit_matrix",name);
        }
        super.step();
    }
}