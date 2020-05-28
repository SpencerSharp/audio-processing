package utils;

import java.io.*;
import java.util.*;

public class FileUtils {
    public static void deleteDirectory(File file) {
        deleteDirectory(file, 0);

    }

    private static void deleteDirectory(File file, int depth) {
        System.out.println(file);
        if (file.isDirectory()) {
            for (File sub : file.listFiles()) {
                FileUtils.deleteDirectory(sub,depth+1);
            }
        }

        file.delete();
    }
}