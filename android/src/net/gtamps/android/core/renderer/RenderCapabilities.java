package net.gtamps.android.core.renderer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import java.nio.IntBuffer;

import static javax.microedition.khronos.opengles.GL10.*;

/**
 * Simple static class holding values representing various capabilities of
 * hardware's concrete OpenGL capabilities that are relevant to
 * supported features.
 */
public class RenderCapabilities {

    private static final String TAG = RenderCapabilities.class.getSimpleName();

    private static float openGLVersion;
    private static boolean isGL10Only;
    private static boolean supportsOpenGLES;
    private static int maxTextureUnits;
    private static int maxTextureSize;
    private static int aliasedPointSizeMin;
    private static int aliasedPointSizeMax;
    private static int smoothPointSizeMin;
    private static int smoothPointSizeMax;
    private static int aliasedLineSizeMin;
    private static int aliasedLineSizeMax;
    private static int smoothLineSizeMin;
    private static int smoothLineSizeMax;
    private static int maxLights;


    public static float openGlVersion() {
        return openGLVersion;
    }

    public static boolean isGl10Only() {
        return isGL10Only;
    }

    public static boolean isSupportsOpenGLES() {
        return supportsOpenGLES;
    }

    public static int maxTextureUnits() {
        return maxTextureUnits;
    }

    public static int getMaxTextureSize() {
        return maxTextureSize;
    }

    public static int aliasedPointSizeMin() {
        return aliasedPointSizeMin;
    }

    public static int aliasedPointSizeMax() {
        return aliasedPointSizeMax;
    }

    public static int smoothPointSizeMin() {
        return smoothPointSizeMin;
    }

    public static int smoothPointSizeMax() {
        return smoothPointSizeMax;
    }

    public static int aliasedLineSizeMin() {
        return aliasedLineSizeMin;
    }

    public static int aliasedLineSizeMax() {
        return aliasedLineSizeMax;
    }

    public static int smoothLineSizeMin() {
        return smoothLineSizeMin;
    }

    public static int smoothLineSizeMax() {
        return smoothLineSizeMax;
    }

    public static int maxLights() {
        return maxLights;
    }

    /**
     * Called by GLRenderer.onSurfaceCreate()
     */
    public static void setRenderCaps(GL10 gl) /* package-private*/ {
        IntBuffer i;

        // OpenGL ES version
        if (gl instanceof GL11) {
            openGLVersion = 1.1f;
        } else {
            openGLVersion = 1.0f;
        }

        // Max texture units
        if (openGLVersion <= 1.1) {
            i = IntBuffer.allocate(1);
            gl.glGetIntegerv(GL_MAX_TEXTURE_UNITS, i);
            maxTextureUnits = i.get(0);
        }

        // Max texture size
        i = IntBuffer.allocate(1);
        gl.glGetIntegerv(GL_MAX_TEXTURE_SIZE, i);
        maxTextureSize = i.get(0);

        // Aliased point size range
        if (openGLVersion <= 1.1) {
            i = IntBuffer.allocate(2);
            gl.glGetIntegerv(GL_ALIASED_POINT_SIZE_RANGE, i);
            aliasedPointSizeMin = i.get(0);
            aliasedPointSizeMax = i.get(1);
        }

        // Smooth point size range
        if (openGLVersion <= 1.1) {
            i = IntBuffer.allocate(2);
            gl.glGetIntegerv(GL_SMOOTH_POINT_SIZE_RANGE, i);
            smoothPointSizeMin = i.get(0);
            smoothPointSizeMax = i.get(1);
        }

        // Aliased line width range
        if (openGLVersion <= 1.1) {
            i = IntBuffer.allocate(2);
            gl.glGetIntegerv(GL_ALIASED_LINE_WIDTH_RANGE, i);
            aliasedLineSizeMin = i.get(0);
            aliasedLineSizeMax = i.get(1);
        }

        // Smooth line width range
        if (openGLVersion <= 1.1) {
            i = IntBuffer.allocate(2);
            gl.glGetIntegerv(GL_SMOOTH_LINE_WIDTH_RANGE, i);
            smoothLineSizeMin = i.get(0);
            smoothLineSizeMax = i.get(1);
        }

        // Max lights
        if (openGLVersion <= 1.1) {
            i = IntBuffer.allocate(1);
            gl.glGetIntegerv(GL_MAX_LIGHTS, i);
            maxLights = i.get(0);
        }

        Logger.i(TAG, "RenderCapabilities - openGLVersion: " + openGLVersion + " (" + (supportsOpenGLES ? "With " : "Without ") + "OpenGLES20 support.)");
        if (openGLVersion <= 1.1) Logger.i(TAG, "RenderCapabilities - maxTextureUnits: " + maxTextureUnits);
        Logger.i(TAG, "RenderCapabilities - maxTextureSize: " + maxTextureSize);
        if (openGLVersion <= 1.1) Logger.i(TAG, "RenderCapabilities - maxLights: " + maxLights);
    }

    /**
     * Detects if OpenGL ES 2.0 exists
     *
     * @return <code>true</code> if it does
     */
    public static boolean detectOpenGLES20(final Context context) {
        ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return supportsOpenGLES = (info.reqGlEsVersion >= 0x20000);
    }

    public static boolean supportsGLES20() {
        return supportsOpenGLES;
    }
}
