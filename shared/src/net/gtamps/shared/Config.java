package net.gtamps.shared;

import net.gtamps.shared.Utils.Logger;

final public class Config {

    /**
     * DEBUG
     */
    public static Logger.Level LOG_LEVEL = Logger.Level.DEBUG;
    public static final boolean DEBUG_MODE = true;
    public static final boolean DEBUG_MODE_SHOW_NULL_EXCEPTIONS = false;

    /**
     * RENDERER
     */
    public static boolean renderContinuously = true;
    public static boolean ENABLE_FRAME_LIMITER = true;
    public static final long FPS = 1000/30;
    public static final boolean DISPLAY_FRAME_RATE = false;

    /**
     * CAMERA
     */
    public static final float MIN_ZOOM = 0;
    public static final float MAX_ZOOM = 90;
    public static final float PIXEL_TO_NATIVE = 0.3f;

    /**
     * SOCKET
     */
    private static final String TIL_IP_HOME = "192.168.1.27";
    private static final String TIL_IP_JAN_HOME = "192.168.2.102";
    private static final String TIL_IP_INI = "141.64.23.78";
    public static final String SERVER_HOST_ADDRESS = TIL_IP_JAN_HOME;
    public static final int SERVER_PORT = 8095;
    public static final int SOCKET_MAX_SEND_BUFFER_SIZE = 0xFFFF;
    public static final int SOCKET_MAX_RECEIVE_BUFFER_SIZE = 0xFFFF;
    public static final int SOCKET_TIMEOUT = 3000;
    public static final boolean SOCKET_KEEP_ALIVE_ENABLED = true;
    public static final boolean SOCKET_TCP_NO_DELAY = true;
    public static final long SOCKET_INBOX_LATENCY = 0;
    public static final long SOCKET_OUTBOX_LATENCY = 0;
    public static final long IMPULS_FREQUENCY = 0;


    private Config() { }
}
