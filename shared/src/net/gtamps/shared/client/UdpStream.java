package net.gtamps.shared.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;

@Deprecated
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
    public boolean isConnected() {
        return false;
    }

    @Override
    public DataInputStream getInputStream() {
        return null;
    }

    @Override
    public DataOutputStream getOutputStream() {
        return null;
    }
}
