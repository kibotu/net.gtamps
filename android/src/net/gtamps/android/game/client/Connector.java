package net.gtamps.android.game.client;

import net.gtamps.android.core.math.Plane;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class Connector implements Runnable {

    private boolean isRunning;
    private IStream stream;

    public Connector(IStream stream) {
        this.stream = stream;
        isRunning = false;
    }

    @Override
    public void run() {
        while(isRunning) {



        }
    }

    public void start() {
        isRunning = true;
        new Thread(this).start();

    }

    public void stop() {
        isRunning = false;
    }
}
