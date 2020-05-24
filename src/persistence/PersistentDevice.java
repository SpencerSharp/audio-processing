package persistence;

import java.io.*;

// https://www.baeldung.com/java-serialization

public class PersistentDevice implements Serializable {
    private static final String path = "/Users/spencersharp/Documents/Coding/Active/audio-processing";
    public int channel = 2;
    public int ind = 3;

    public String name;
    public int inlet;
    public int appearance;

    public void freebang() {
        PersistentDevice.load(this);
    }

    private File getFile() throws IOException {
        String channelPath = path + "/" + channel;
        File channelFile = new File(channelPath);

        if (!channelFile.exists()) {
            channelFile.mkdir();
        }

        String myPath = channelPath + "/" + ind;
        File myFile = new File(myPath);

        if (myFile.exists()) {
            myFile.delete();
        }
        myFile.createNewFile();

        return myFile;
    }

    public static void save(PersistentDevice device) {
        try{
            FileOutputStream f = new FileOutputStream(device.getFile());
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(device);

            o.close();
            f.close();
        } catch(Exception e) {}
    }

    public static void load(PersistentDevice device) {
        try {
            FileInputStream fileInputStream = new FileInputStream(device.getFile());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            device = (PersistentDevice) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch(Exception e) {}

    }
}