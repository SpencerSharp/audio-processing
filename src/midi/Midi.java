package midi;

public class Midi {


    public static byte[] prepareMessage(long messageType, int id, double semitone, char velocity) {

        // FIRST BYTE
        byte status = (byte) (messageType << 4);

        // SECOND BYTE
        byte noteNum = (byte) semitone;

        // THIRD BYTE
        byte vel = (byte) velocity;

        byte[] message = new byte[3];

        message[0] = status;
        message[1] = noteNum;
        message[2] = vel;

        return message;
    }
}


