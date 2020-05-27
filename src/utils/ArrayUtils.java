package utils;

public class ArrayUtils {
    public static String[] addAll(String[] one, String[] two) {
        String[] result = new String[one.length + two.length];
        for (int i = 0; i < one.length; i++) {
            result[i] = one[i];
        }
        for (int i = 0; i < two.length; i++) {
            result[i + one.length] = two[i];
        }
        return result;
    }
}