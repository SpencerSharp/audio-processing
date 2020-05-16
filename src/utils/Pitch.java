package utils;

public class Pitch {
    public static double stepSize(int pitch) {
        return Math.pow(2.0,(pitch - 69)/12.0);
    }
}