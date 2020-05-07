// package midi.sequencing;

// import com.cycling74.max.*;
// import java.util.*;
// import java.lang.Math;
// import java.io.*;

// public class BasicSequencer extends MidiSender {

//     pitches = [
//         0,
//         1,
//         2
//     ];


//     public BasicSequencer() {
//         numStates = 92;
//         interval = 64;
//     }

//     private f(int inp) {
//         // https://i.postimg.cc/5tmb4FBJ/Screen-Shot-2020-05-03-at-10-07-33-PM.png
//     }

//     private void noteTick() {
//         if (state == 0) {
//             playNote(0);
//         }
//         if (state == 64) {
//             playNote(1);
//         }
//         if (state == 96) {
//             playNote(-3);
//         }
//         if (state >= 128) {
//             // sine wave period of 8
//             // start at -5
//             // go up to 2
//             // down to 0
//             inp = x - 128;
//             pitch = f(inp);
//             playNote() // generate additional voice
//         }
//         switch (state) {
//             case 0:
//                 pitch = base;
//                 vel = 127;
//                 dur = base / 4;
//                 break;
            
//         }
//         super.noteTick();
//     }

//     protected void notifyDeleted() {
// 		// objectStillExists = false;
// 	}
// }