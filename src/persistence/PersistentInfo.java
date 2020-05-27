package persistence;

import utils.global.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class PersistentInfo {
    private static final String globalPath = 
        "/Users/spencersharp/Documents/Coding/Active/audio-processing/global/persistent";
    private static LockableFile file = new LockableFile(globalPath);
    private static volatile ArrayList<String> fileContents = null;
    private static ArrayList<String> lines;
    private static boolean fileIsPopulated = false;
    private static volatile boolean mustWait = true;

    private static String path = "";
    private static boolean projectHasBeenSetup = true;
    private static boolean projectPathHasBeenSearchedFor = false;

    private static void parseFileContents(boolean isUpdated) {
        if (isUpdated) {
            fileContents = lines;
            mustWait = true;
            fileIsPopulated = true;
            return;
        }
        if (!mustWait) {
            return;
        }
        if (fileContents == null) {
            reload();
        }
        mustWait = false;
        path = fileContents.get(0);
        projectHasBeenSetup = Boolean.parseBoolean(fileContents.get(1));
        projectPathHasBeenSearchedFor = Boolean.parseBoolean(fileContents.get(2));
    }

    private static void parseFile() {
        file.acquireLock();
        BufferedReader reader = file.getReadBuffer();
        int ind = 0;
        String line;
        lines = new ArrayList<String>();
        try {
            while ((line = reader.readLine()) != null) {
                lines.add(line);
                ind++;
            }
            reader.close();
        } catch (IOException e) {}
        file.releaseLock();
        System.out.println("size of lines is " + lines.size());
        if (lines.size() >= 3) {
            parseFileContents(true);
        }
        System.out.println("content of lines is " + lines);
    }

    private static void saveToFile() {
        file.acquireLock();
        PrintWriter writer = file.getWriteBuffer();
        writer.println(path);
        writer.println(projectHasBeenSetup);
        writer.println(projectPathHasBeenSearchedFor);
        writer.close();
        file.releaseLock();
    }

    private static void waitForFileToBePopulated() {
        while (!fileIsPopulated) {
            parseFile();
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {}
            }
        }
    }

    public static void reload() {
        fileIsPopulated = false;
        waitForFileToBePopulated();
    }

    public static void flush() {
        saveToFile();
    }

    public static void setPath(String s) {
        path = s;
    }

    public static String getPath() {
        parseFileContents(false);
        return path;
    }

    public static void markProjectAsSetup() {
        projectHasBeenSetup = true;
    }

    public static void markProjectAsNotSetup() {
        projectHasBeenSetup = false;
    }

    public static boolean checkIfProjectHasBeenSetup() {
        parseFileContents(false);
        return projectHasBeenSetup;
    }

    public static void markProjectPathAsSearchedFor() {
        projectPathHasBeenSearchedFor = true;
    }

    public static void markProjectPathAsNotSearchedFor() {
        projectPathHasBeenSearchedFor = false;
    }

    public static boolean checkIfProjectPathHasBeenSearchedFor() {
        parseFileContents(false);
        return projectPathHasBeenSearchedFor;
    }
}