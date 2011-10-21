package android.normalizer;


import javax.swing.UIManager;

final public class Config {

	private static int UID = 0;
	public final static int WINDOW_WIDTH = 150;
	public final static int WINDOW_HEIGHT = 100;
	public static final int FPS = 1000/30;
	
	public static String DEFAULT_WINDOW_THEME = UILookAndFeel.SYSTEM.value;
	public static String DEFAULT_SAVE_DIR = "C:/Users/Kibotu/Desktop";
	public static String APP_NAME = "Texture Coordinate Normalizer";

//	public static String DEFAULT_SAVE_DIR = "D:/Dropbox/medienprojekt/trunk/res/raw";
	public final static String DEFAULT_RESOURCE_DIR = "files/";

	public enum UILookAndFeel {
		SYSTEM(UIManager.getSystemLookAndFeelClassName()),
		JTATTOO("com.jtattoo.plaf.smart.SmartLookAndFeel"),
		WINDOWS("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"),
		METAL("javax.swing.plaf.metal.MetalLookAndFeel"),
		MOTIF("com.sun.java.swing.plaf.motif.MotifLookAndFeel"),
		CROSSPLATTFORM(UIManager.getCrossPlatformLookAndFeelClassName());
		public final String value;
		private UILookAndFeel(final String value) {
			this.value = value;
		}
	}

	public static final int getUID() {
		return UID++;
	}

	private Config() {
	}

	public static void resetUID() {
		UID = 0;
	}
}