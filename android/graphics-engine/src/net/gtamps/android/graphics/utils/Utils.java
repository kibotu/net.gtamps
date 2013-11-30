package net.gtamps.android.graphics.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import net.gtamps.shared.Utils.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    // utility
    private Utils() {
    }

    public static void logAvailableMemory() {
        Logger.d("Available Memory", Registry.getAvailableMemory() / 1048576 + " MB");
    }

    /**
     * <p>Clamps a value to within a range. <br />
     * <br />
     * 0 = Utils.clamp(-1,0,128) <br />
     * 0 = Utils.clamp(0,0,128)<br />
     * 1 = Utils.clamp(1,0,128) <br />
     * 128 = Utils.clamp(128,0,128) <br />
     * 128 = Utils.clamp(129,0,128) <br />
     * </p>
     *
     * @param value
     * @param startRange
     * @param endRange
     * @return clamped value
     */
    public static float clamp(float value, float startRange, float endRange) {
        if (value == Float.NaN) return 0;
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
            } catch (IOException e) {
                // Ignore.
            }
        }

        return bitmap;
    }

    /**
     * Saves a bitmap into a folder.
     *
     * @param screenshot
     * @param filePath
     * @param fileName
     */
    public static void saveBitmap(@NotNull Bitmap screenshot, @NotNull String filePath, @NotNull String fileName) {
        OutputStream outStream = null;
        File dir = new File(filePath);
        dir.mkdirs();
        File output = new File(filePath,fileName);
        try {
            outStream = new FileOutputStream(output);
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            Logger.v(TAG, "Saving Screenshot [" + filePath+fileName +"]");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts 2 bytes to an integer.
     * <p/>
     * <p/>
     * Utils.log("-1 0", "0 = "+Utils.convertLength(-1,0));
     * Utils.log("0 -1", "0 = "+Utils.convertLength(0,-1));
     * Utils.log("0 0", "0 = "+Utils.convertLength(0,0));
     * Utils.log("127 127", "32639 = "+Utils.convertLength(127,127));
     * Utils.log("255 255", "65536 = "+Utils.convertLength(255,255));
     * Utils.log("256 256", "65536 = "+Utils.convertLength(256,256));
     *
     * @param high byte 0...255
     * @param low  byte 0...255
     * @return length
     */
    public static int convertLength(int high, int low) {
        high = (int) clamp(high, 0, 255);
        low = (int) clamp(low, 0, 255);
        return (high << 8) + low;
    }
}
