package net.gtamps.android.core.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import net.gtamps.android.core.net.IStream;

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

	@Override
	public byte[] receive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean send(byte[] buffer, int length) {
		// TODO Auto-generated method stub
		return false;
	}
}
