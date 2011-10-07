package net.gtamps.android.core.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import net.gtamps.shared.Config;
import net.gtamps.android.GTA;
import net.gtamps.android.Registry;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

    private Utils() {
    }

    public static final float DEG = (float)(Math.PI / 180f);

    public static void showMsg(String message) {
        Toast t = Toast.makeText(Registry.getContext(), message, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
        t.show();
    }

    public static void log(String tag, String message) {
        if(Config.DEBUG_MODE) Log.i(tag, message);
    }

    public static void logAvailableMemory() {
        log(GTA.TAG, "Available Memory: " + Registry.getAvailableMemory() / 1048576 + " MB");
    }

    /**
     * <p>Clamps a value to within a range. <br />
     *   <br />
     *   0 = Utils.clamp(-1,0,128) <br />
     *   0 = Utils.clamp(0,0,128)<br />
     *   1 = Utils.clamp(1,0,128) <br />
     *   128 = Utils.clamp(128,0,128) <br />
     *   128 = Utils.clamp(129,0,128) <br />
     * </p>
     * @param value
     * @param startRange
     * @param endRange
     * @return clamped value
     */
    public static float clamp(float value, float startRange, float endRange) {
        return value < startRange ? startRange : value > endRange ? endRange : value;
    }

    /**
	 * Convenience method to create a Bitmap given a Context's drawable resource ID.
	 */
	public static Bitmap makeBitmapFromResourceId(int id) {
		InputStream is = Registry.getContext().getResources().openRawResource(id);

		Bitmap bitmap;
		try {
		   bitmap = BitmapFactory.decodeStream(is);
		} finally {
		   try {
		      is.close();
		   } catch(IOException e) {
		      // Ignore.
		   }
		}

		return bitmap;
	}
}
