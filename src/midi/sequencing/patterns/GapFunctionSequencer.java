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

public class GapFunctionSequencer extends FunctionSequencer {
    int pitchChanged;
    Function gapFunc;

    protected void initFunctions() {
        super.initFunctions();

        GlobalFunction gapFunction = new GlobalFunction("g(t)");
        if (gapFunction.isValid()) {
            gapFunc = gapFunction.asFunction();
        }
    }

    protected void tick() {
        int pitchBefore = pitch;
        super.tick();
        if (pitch != pitchBefore) {
            pitchChanged = state;
        }
        double inp = ((double)state)/BASE_BEAT_LENGTH;
        double calc = gapFunc.calculate(inp);

        double curNoteLen = calc * BASE_BEAT_LENGTH;
        int elapsedSinceNoteStarted = state - pitchChanged;

        if (elapsedSinceNoteStarted > curNoteLen) {
            endNote();
        }
    }
}
