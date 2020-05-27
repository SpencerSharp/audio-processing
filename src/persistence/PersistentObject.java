package persistence;

import java.io.*;
import java.nio.file.*;

// https://www.baeldung.com/java-serialization

public class PersistentObject implements Serializable {
    public static int channel = 2;
    public static int ind = 3;

    public String name;
    public int inlet;
    public int appearance;

    public static File getFile(boolean shouldOverwrite) throws IOException {
        System.out.println("start");
        File parentDir = new File(PersistentInfo.getPath());
        Path parentPath = parentDir.toPath();
        Path channelPath = parentPath.resolve(""+channel);
        File channelFile = channelPath.toFile();

        System.out.println("inprosition");

        if (!channelFile.exists()) {
            channelFile.mkdir();
        }

        Path myFilePath = channelPath.resolve(""+ind);
        File myFile = myFilePath.toFile();

        System.out.println("partway thru");

        if (shouldOverwrite) {
            if (myFile.exists()) {
                myFile.delete();
            }
            myFile.createNewFile();
        } else {
            System.out.println("myFilePath " + myFilePath);
            if (!myFile.exists()) {
                throw new IOException("File does not yet exist");
            }
        }

        return myFile;
    }

    public static void save(PersistentObject device) {
        try{
            FileOutputStream f = new FileOutputStream(getFile(true));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(device);

            o.close();
            f.close();
        } catch(Exception e) {}
    }

    public static PersistentObject tryLoad(PersistentObject device) {
        // For loadpings that occur after ProjectSetupDevice is done running
        if (!PersistentInfo.checkIfProjectHasBeenSetup()) {
            System.out.println("LOAD infoSetup " + PersistentInfo.checkIfProjectHasBeenSetup());
            System.out.println("LOAD infoPath " + PersistentInfo.checkIfProjectPathHasBeenSearchedFor());
            return null;
        }
        // For loadpings that occur before ProjectSetupDevice is done running
        // BUT before it has resolved the path and set it
        while (!PersistentInfo.checkIfProjectPathHasBeenSearchedFor()) {
            // This allows ProjectSetupDevice to notify us if the project isn't set up yet
            if (!PersistentInfo.checkIfProjectHasBeenSetup()) {
                return null;
            }
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {}
            }
        }
        System.out.println("path is " + PersistentInfo.getPath());
        // Only runs once path has been resolved and set
        // Checks if the project has been saved yet, cuz then ProjectSetupDevice leaves the path as empty string
        // If it hasn't been saved yet, then there is nothing to load, duh
        if (PersistentInfo.getPath().equals("")) {
            return null;
        }
        try {
            System.out.println("starting the load op");
            FileInputStream fileInputStream = new FileInputStream(getFile(false));
            System.out.println("got device file successfully");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            device = (PersistentObject) objectInputStream.readObject();
            System.out.println("obj is " + device);
            objectInputStream.close();
            fileInputStream.close();
        } catch(Exception e) {
            System.out.println("THE LOAD OP HAS FAILED");
            return null;
        }
        return device;
    }
}