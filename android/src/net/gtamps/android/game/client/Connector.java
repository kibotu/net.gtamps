package net.gtamps.android.game.client;

public class Connector implements Runnable {

    private static final String TAG = Connector.class.getSimpleName();

    private boolean isRunning;
    private IStream stream;

    public Connector(IStream stream) {
        this.stream = stream;
        isRunning = false;
    }

    @Override
    public void run() {
        while(isRunning) {

            stream = new TcpStream();
            String ip = "141.64.23.78";
            int port = 8090;
            Utils.log(TAG, "\n\n\n\n\nConnecting to " + ip + ":" + port + " " + (stream.connect(ip, port) ? "successful." : "failed.") + "\n\n\n\n\n");
            stream.startListening();

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
