package midi.sequencing.patterns;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.*;
import java.io.*;
import org.mariuszgromada.math.mxparser.*;

import midi.sequencing.*;
import utils.global.*;
import datatypes.Note;
import interfaces.*;
import interfaces.custom.SequencerKnobControl;
import midi.Midi2;
import persistence.*;
import tools.modulators.ModulatedVariable;

public class GapFunctionSequencer extends FunctionSequencer {
    int trigger;
    ModulatedVariable gapFunc;

    protected void initFunctions() {
        super.initFunctions();

        gapFunc = new ModulatedVariable("g", 4);

        trigger = state;
    }

    // protected void endNote() {


    // }

    protected void tickPlayNote() {


    }

    protected void tick() {
        int pitchBefore = pitch;
        super.tick();
        // if (pitch != pitchBefore) {
        //     trigger = state;
        // }
        if (gapFunc != null) {
            double inp = ((double)state)/BASE_BEAT_LENGTH.getValue();
            gapFunc.setInpVal(inp);
            double calc = gapFunc.getValue();

            double curNoteLen = calc * BASE_BEAT_LENGTH.getValue();
            int elapsedSinceNoteStarted = state - trigger;

            if (elapsedSinceNoteStarted > curNoteLen) {
                super.tickPlayNote();
                // if (notes.size() > 0) {
                //     sendOut(notes.poll().asMessage(Midi2.noteOff));
                // }
                trigger = state;
            }
        }

    }


}
