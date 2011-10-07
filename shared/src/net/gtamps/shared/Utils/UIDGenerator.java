package net.gtamps.shared.Utils;

/**
 * Outsourced from SlackAndHayMain to reduce dependencies, which makes
 * unit testing a little easier.
 * 
 * @author til
 *
 */
public class UIDGenerator {

	@SuppressWarnings("unused")
	private static final String TAG = "StaticUIDGenerator";
	private static int nextUID = 0;

	public static int getNewUID() {
		if (nextUID == Integer.MAX_VALUE)
			throw new IllegalStateException("UID pool depleted");
		return nextUID++;
	}
}
