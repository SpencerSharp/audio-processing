package persistence.devices;

import java.util.*;

import com.cycling74.max.*;
import com.cycling74.msp.*;

import persistence.PersistentObject;

public class ChannelIndexAccessorDevice extends MaxObject {

    MaxClock loadingTimer;

    MaxBox liveObjectBox;
    MaxBox livePathBox;

    int refreshInterval = 100;

    boolean requestedDeviceId = false;
    int deviceId = -1;
    boolean requestedLiveSetId = false;
    int liveSetId = -1;
    boolean requestedTrackIds = false;
    int[] tracks = null;

    int curIndex = 0;
    HashMap<Integer, int[]> trackDevices = new HashMap<Integer, int[]>();

    int myChannel = -1;
    int myIndex = -1;

    boolean setupComplete = false;
    boolean isDead = false;

    public ChannelIndexAccessorDevice() {

        declareInlets(new int[]{DataTypes.ALL, DataTypes.ALL, DataTypes.ALL, DataTypes.ALL});
        declareOutlets(new int[]{DataTypes.ALL,DataTypes.ALL});

        loadingTimer = new MaxClock(new Executable() {public void execute() { setup(); }});
        loadingTimer.delay(200);
    }

    private void setup() {

        MaxPatcher patcher = getParentPatcher();

        liveObjectBox = patcher.getNamedBox("liveObject");
        livePathBox = patcher.getNamedBox("livePath");

        if (isDead) {

            return;
        }

        if (deviceId == -1) {

            loadingTimer.setExecutable(new Executable() {public void execute() { getDeviceId(); }});
            loadingTimer.tick();
        }

        else if (liveSetId == -1) {

            loadingTimer.setExecutable(new Executable() {public void execute() { getLiveSetId(); }});
            loadingTimer.tick();
        }

        else if (tracks == null) {

            loadingTimer.setExecutable(new Executable() {public void execute() { getTrackIds(); }});
            loadingTimer.tick();
        }

        else if (trackDevices.size() < tracks.length) {

            System.out.println("getDevicesForTrack " + curIndex);

            loadingTimer.setExecutable(new Executable() {public void execute() { getDevicesForTrack(); }});
            loadingTimer.tick();
        }
        
        else {

            for(int i = 0; i < tracks.length; i++) {

                int trackId = tracks[i];

                int[] devices = trackDevices.get(trackId);

                for (int j = 0; j < devices.length; j++) {

                    int deviceId = devices[j];

                    if (deviceId == this.deviceId) {

                        PersistentObject.channel = i;
                        PersistentObject.ind     = j;
                    }
                }
            }

            System.out.println("Channel is " + PersistentObject.channel);
            outlet(0, PersistentObject.channel);
            System.out.println("Index is " + PersistentObject.ind);
            outlet(1, PersistentObject.ind);

            setupComplete = true;
        }

        // if (devices.size() < tracks.length) {


        // }
    }

    private String showTracks() {

        String res = "";

        for (int i : tracks) {

            res += i + " ";
        }

        return res;
    }

    // private void d

    private void sendMessage(MaxBox box, String msg) {

        String[] words = msg.split(" ");

        String message = words[0];

        Atom[] args = new Atom[words.length-1];

        for(int i = 0; i < args.length; i++) {

            try {

                args[i] = Atom.newAtom(Integer.parseInt(words[i+1]));
            } catch(NumberFormatException e) {

                args[i] = Atom.newAtom(words[i+1]);
            }
        }

        box.send(message, args);
    }

    private void getDeviceId() {

        if (!requestedDeviceId) {

            requestedDeviceId = true;

            sendMessage(livePathBox, "path this_device");
            
        }
        System.out.println("refresh says deviceId is " + deviceId);
        if (deviceId == -1) {

            loadingTimer.delay(refreshInterval);
        } else {

            setup();
        }
    }


    private void getLiveSetId() {

        if (!requestedLiveSetId) {

            requestedLiveSetId = true;

            sendMessage(livePathBox, "path live_set");
            
        }
        if (liveSetId == -1) {

            loadingTimer.delay(refreshInterval);
        } else {

            setup();
        }
    }


    private void getTrackIds() {

        if (!requestedTrackIds) {

            requestedTrackIds = true;

            System.out.println("set id " + liveSetId);

            sendMessage(liveObjectBox, "id " + liveSetId);

            sendMessage(liveObjectBox, "get tracks");
        }
        if (tracks == null) {

            loadingTimer.delay(refreshInterval);
        } else {

            setup();
        }
    }


    private void getDevicesForTrack() {

        int trackId = tracks[curIndex];

        if (!trackDevices.containsKey(trackId)) {

            trackDevices.put(trackId, null);

            sendMessage(liveObjectBox, "id " + tracks[curIndex]);

            sendMessage(liveObjectBox, "get devices");
        } 
        if (trackDevices.get(trackId) == null) {
            
            loadingTimer.delay(refreshInterval);
        } else {

            curIndex++;
            setup();
        }
    }

    public void inlet(int i) {
        System.out.println("INT: " + i);
    }

    public void anything(String message, Atom args[]) {
        System.out.println("MESSAGE: " + message);

        int ind = 0;

        for (Atom atom : args) {

            System.out.println("arg" + ind++ + ": " + atom);
        }

        if (message.equals("id")) {

            if (deviceId == -1) {

                deviceId = args[0].toInt();
            } else if (liveSetId == -1) {

                liveSetId = args[0].toInt();
            }
        }

        if (message.equals("tracks")) {

            int[] tracks = new int[args.length/2];

            for (int i = 0; i < tracks.length; i++) {

                int arg = (i*2)+1;

                tracks[i] = args[arg].toInt();
            }

            this.tracks = tracks;
        }

        if (message.equals("devices")) {

            int[] devices = new int[args.length/2];

            for (int i = 0; i < devices.length; i++) {

                int arg = (i*2)+1;

                devices[i] = args[arg].toInt();
            }

            trackDevices.put(tracks[curIndex], devices);
        }

        if (message.equals("persist")) {

            if (setupComplete) {

                setupComplete = false;

                setup();
            }
        }
    }

    protected void notifyDeleted() {

        super.notifyDeleted();

        isDead = true;

        loadingTimer.release();
    }
}