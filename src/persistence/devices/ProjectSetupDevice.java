package persistence;

import com.cycling74.max.*;
import com.cycling74.msp.*;
import java.lang.*;
import java.nio.file.*;
import java.io.*;
import utils.*;

public class ProjectSetupDevice extends MaxObject {
    private static final int LOAD_TIME = 500;

    boolean hasReceivedPath = false;

    boolean isSaving = false;

    MaxClock loadingTimer;

    public ProjectSetupDevice() {
        declareInlets(new int[]{DataTypes.ALL,DataTypes.ALL});
        declareOutlets(new int[]{DataTypes.ALL});

        loadingTimer = new MaxClock(new Executable() {public void execute() { checkIfHasReceivedPath(); }});
        loadingTimer.delay(LOAD_TIME);

        PersistentInfo.markProjectAsSetup();
        PersistentInfo.markProjectPathAsNotSearchedFor();
        PersistentInfo.flush();

        System.out.println("CONST infoSetup " + PersistentInfo.checkIfProjectHasBeenSetup());
        System.out.println("CONST infoPath " + PersistentInfo.checkIfProjectPathHasBeenSearchedFor());
    }

    public void anything(String message, Atom args[]) {
        if (message.equals("none")) {
            return;
        }
        if (message.equals("persist")) {
            pingAllToPersist();
            return;
        }

        String path = message.substring(message.indexOf(":")+1,message.length());

        /*
        Path of file is like this:
        ProjectName Project/Samples/Processed/Consolidate/filename.aif
        */

        hasReceivedPath = true;
        
        PersistentInfo.markProjectAsSetup();
        PersistentInfo.flush();

        File persistedDirFile = getPersistedDirFile(path);

        if (!persistedDirFile.exists()) {
            persistedDirFile.mkdir();
        }

        PersistentInfo.setPath(persistedDirFile.toPath().toString());
        PersistentInfo.markProjectPathAsSearchedFor();
        PersistentInfo.flush();

        outlet(0, "setupfromdisc");
    }

    private File getPersistedDirFile(String path) {
        File audioFile = new File(path);

        File consolidateDir = new File(audioFile.getParent());

        File processedDir = new File(consolidateDir.getParent());

        File samplesDir = new File(processedDir.getParent());

        Path projectDir = new File(samplesDir.getParent()).toPath();

        Path projectInfoDir = projectDir.resolve("Ableton Project Info");

        Path persistedDir = projectInfoDir.resolve("SharpVSTMetadata");

        System.out.println("found path to be " + persistedDir);

        File persistedDirFile = persistedDir.toFile();

        return persistedDirFile;
    }

    private void checkIfHasReceivedPath() {
        if (!hasReceivedPath) {
            PersistentInfo.markProjectAsNotSetup();
            PersistentInfo.flush();
        }
        System.out.println("CHECK infoSetup " + PersistentInfo.checkIfProjectHasBeenSetup());
        System.out.println("CHECK infoPath " + PersistentInfo.checkIfProjectPathHasBeenSearchedFor());
        // System.out.println("received path status is " + hasReceivedPath);
    }

    private void pingAllToPersist() {
        if (!isSaving) {
            isSaving = true;
            File persistedDirFile = new File(PersistentInfo.getPath());
            if (persistedDirFile.getName().equals("SharpVSTMetadata") && persistedDirFile.exists()) {
                FileUtils.deleteDirectory(persistedDirFile);
            }
            persistedDirFile.mkdir();
            outlet(0, "persist");
            isSaving = false;
        }
    }

    protected void notifyDeleted() {
        super.notifyDeleted();
        loadingTimer.release();
    }
}

