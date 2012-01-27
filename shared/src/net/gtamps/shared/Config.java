package net.gtamps.shared;

import java.util.ArrayList;

import net.gtamps.shared.Utils.Logger;

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
	public static final long FPS = 1000 / 60;
	public static final boolean DISPLAY_FRAME_RATE = false;
	public static final float ALPHA_KILL_FRAGMENTS_TOLERANCE = 0.3f;
	public static final boolean USEVBO = true;
	public static final boolean FORCE_GL10 = false;
	public static final boolean FORCE_GTA_2D = true;

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
		IPS.add("192.168.1.27");
		IPS.add("192.168.214.1");
		IPS.add("192.168.1.51");
		IPS.add("192.168.38.1");
		IPS.add("192.168.178.23");
		//        IPS.add("192.168.178.24");
		IPS.add("192.168.178.25");
		IPS.add("192.168.1.55");
		IPS.add("192.168.2.102");
		IPS.add("192.168.2.101");
		IPS.add("192.168.1.10");

	}
	public static final int MAX_MESSAGES_INBOX = 2;
	public static final int MAX_MESSAGES_OUTBOX = 2;

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
	public static final long HUD_INPUT_MESSAGE_TIMOUT = 20;
	public static String DEFAULT_USERNAME = "blub";
	public static final String DEFAULT_PASSWORD = "blub";

	public static final String TEST_LEVEL_PATH = "../assets/map1.map.lvl";

	public static final boolean SHOW_NETWORK_BANDWITH_USAGE = false;

	private Config() {
	}
}
