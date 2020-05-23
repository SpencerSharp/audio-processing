package midi.normalizing;

public class Scale {
    public static int getOffset(int steps) {
        int semis = steps + 5*(steps/7);
        if (steps > 0) {
            switch(steps % 7) {
                case 6: semis++;
                case 5: semis++;
                case 4: semis++;
                case 3:
                case 2: semis++;
                case 1: semis++;
            }
        } else {
            int o = getOffset(-1 * (steps % 7));
            semis -= o + steps;
        }
        return semis;
    }
}