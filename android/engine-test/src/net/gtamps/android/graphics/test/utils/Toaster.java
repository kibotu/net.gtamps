package net.gtamps.android.graphics.test.utils;

import android.app.Activity;
import android.widget.Toast;
import net.gtamps.android.graphics.utils.Registry;

/**
 * User: Jan Rabe
 * Date: 22/11/12
 * Time: 19:54
 */
final public class Toaster {

    /**
     * Toast message in ui thread even from gl thread.
     *
     * @param message
     */
    final public static void toast(final Activity activity, final String message, final int length) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity.getApplicationContext(), message, length).show();
            }
        });
    }

    final public static void toast(final String message) {
        toast(((Activity) Registry.getContext()), message, Toast.LENGTH_SHORT);
    }
}
