package ro.ase.arduponic.Interfaces;

public interface BLECallback {
    void sendMessages(String _message);
    void recievedMessage(String _message);
    void callbackSet(BLECallback callback);
}
