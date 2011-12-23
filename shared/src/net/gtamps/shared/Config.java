package net.gtamps.shared;

import net.gtamps.shared.Utils.Logger;

import java.util.ArrayList;

final public class Config {

    /**
     * DEBUG
     */
    public final static Logger.Level LOG_LEVEL = Logger.Level.DEBUG;

    /**
     * RENDERER
     */
    public static boolean renderContinuously = true;
    public static boolean ENABLE_FRAME_LIMITER = true;
    public static final long FPS = 1000/60;
    public static final boolean DISPLAY_FRAME_RATE = false;
    public static final float ALPHA_KILL_FRAGMENTS_TOLERANCE = 0.3f;

    /**
     * FONT
     */
    public static final float DEFAULT_LETTER_SPACING = 0.01f;
    public static final float DEFAULT_LETTER_SIZE = 1f;

    /**
     * PARSER
     */
    public static String PACKAGE_NAME = "net.gtamps.android:raw/";

    /**
     * CAMERA
     */
    public static final float MIN_ZOOM = 0;
    public static final float MAX_ZOOM = 90;
    public static final float PIXEL_TO_NATIVE = 0.1f;

    /**
     * SOCKET
     */
    public static final ArrayList<String> IPS;

    static {
        IPS = new ArrayList<String>(10);
        IPS.add("192.168.1.51");
        IPS.add("192.168.178.23");
        IPS.add("192.168.178.24");
        IPS.add("192.168.178.25");
        IPS.add("192.168.1.27");
        IPS.add("192.168.2.102");
        IPS.add("192.168.2.101");
    }

    public static final String SERVER_DEFAULT_HOST_ADDRESS = "localhost";
    public static final int SERVER_DEFAULT_PORT = 8095;

    public static final int SOCKET_MAX_SEND_BUFFER_SIZE = 0xFFFF;
    public static final int SOCKET_MAX_RECEIVE_BUFFER_SIZE = 0xFFFF;
    public static final int SOCKET_TIMEOUT = 300;
    public static final boolean SOCKET_KEEP_ALIVE_ENABLED = true;
    public static final boolean SOCKET_TCP_NO_DELAY = true;
    public static final long SOCKET_INBOX_LATENCY = 0;
    public static final long SOCKET_OUTBOX_LATENCY = 0;
    public static final int MAX_SOCKET_TIMEOUT = 14000;
    public static final int SERVER_ANGLE_TO_NATIVE = 270;
    public static final long HUD_INPUT_MESSAGE_TIMOUT = 30;
    public static String DEFAULT_USERNAME = ""+ Math.random()*64;
    public static final String DEFAULT_PASSWORD = ""+ Math.random()*64;

    private Config() {
    }
}
