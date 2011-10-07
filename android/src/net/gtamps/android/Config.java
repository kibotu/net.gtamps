package net.gtamps.android;

public class Config {

    private Config() {
    }

    public static final boolean DEBUG_MODE = true;
    public static boolean renderContinuously = true;
    public static boolean ENABLE_FRAME_LIMITER = true;
    public static final long FPS = 1000/30;
    public static final boolean DISPLAY_FRAME_RATE = false;

    public static final String SERVER_HOST_ADDRESS = "141.64.23.78";
    public static final int SERVER_PORT = 8090;
}
