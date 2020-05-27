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
import interfaces.custom.SamplerKnobControl;
import persistence.KnobControlledMidiReceiver;
import interfaces.*;

public class MidiSampleLoader extends KnobControlledMidiReceiver {
    Sample sample;

    private int retrigTime;

    public HashMap<Integer,MidiSampler> voicePlayers;
    public SampleViewer viewer;

    SamplerKnobControl knobs;

    private static final String[] OUTLET_NAMES = new String[]{
        "view matrix"
    };

    public MidiSampleLoader() {
        super();

        this.sample = new Sample("/Users/spencersharp/Documents/Music/Files/Library/Audio/Electronic/WhereWeAre.wav");
        this.sample.load();

        this.retrig();

        retrigTime = -1;
    }

    protected void setup() {
        knobs = new SamplerKnobControl(this, 3);
    }

    protected CustomKnobControl getKnobs() {
        return knobs;
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
            knobs = new SamplerKnobControl(this, 3, sample);
        }
        
        retrigTime = curTime;
    }

    public void anything(String message, Atom args[]) {
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
        super.inlet(msg);
        retrigTime = curTime;
        if (sample != null) {
            viewer.setBounds(knobs.getStartMin(), knobs.getStartMax(), knobs.getEndMin(), knobs.getEndMax(), sample.time());
        }
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
            outlet(2,"jit_matrix",name);
        }
        super.step();
    }
}