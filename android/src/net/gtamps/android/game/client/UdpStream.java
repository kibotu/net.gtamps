package net.gtamps.android.game.client;

public class UdpStream implements IStream {
    @Override
    public boolean send(String message) {
        return false;
    }

    @Override
    public boolean send(byte[] message) {
        return false;
    }

    @Override
    public boolean connect(String host, int port) {
        return false;
    }

    @Override
    public boolean disconnect() {
        return false;
    }

    @Override
    public void onData(byte[] response) {

    }

    @Override
    public void startListening() {

    }
}
