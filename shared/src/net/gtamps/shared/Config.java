package net.gtamps.shared;

public class Config {

    private static final String TIL_IP_HOME = "192.168.1.27";
    private static final String TIL_IP_INI = "141.64.23.78";

    private Config() {
    }

    public static final boolean DEBUG_MODE = true;
    public static boolean renderContinuously = true;
    public static boolean ENABLE_FRAME_LIMITER = true;
    public static final long FPS = 1000/30;
    public static final boolean DISPLAY_FRAME_RATE = false;

    public static final String SERVER_HOST_ADDRESS = TIL_IP_HOME;
    public static final int SERVER_PORT = 8090;
}
