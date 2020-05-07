// package midi;

// public class IntervalSequencer extends Sequencer {
//     protected void setup(int[] intervals) {
//         super.trigTimes = new int[intervals.length];
//         int cur = 0;
//         for (int i : intervals) {
//             cur += i;
//             super.trigTimes[i] = cur;
//         }
//     }

//     public boolean shouldTrig(int time) {
//         return false;
//     }
// }