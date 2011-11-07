package net.gtamps.android.core.renderer;

import android.opengl.GLSurfaceView;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

public class BasicEGLConfigChooser implements GLSurfaceView.EGLConfigChooser {

    @Override
    public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
        //Querying number of configurations
        int[] num_conf = new int[1];
        egl.eglGetConfigs(display, null, 0, num_conf);  //if configuration array is null it still returns the number of configurations
        int configurations = num_conf[0];

        //Querying actual configurations
        EGLConfig[] conf = new EGLConfig[configurations];
        egl.eglGetConfigs(display, conf, configurations, num_conf);

        EGLConfig result = null;

        for (int i = 0; i < configurations; i++) {
            Logger.v(this, "Configuration #" + i);
            Logger.v(this, egl + " " + display + " " + conf[i]);
            result = better(result, conf[i], egl, display);
        }

        Logger.v(this, "Chosen EGLConfig:");
        Logger.v(this, egl + " " + display + " " + result);

        return result;
    }

    /**
     * Returns the best of the two EGLConfig passed according to depth and colours
     *
     * @param a The first candidate
     * @param b The second candidate
     * @return The chosen candidate
     */
    private EGLConfig better(EGLConfig a, EGLConfig b, EGL10 egl, EGLDisplay display) {
        if (a == null) return b;

        EGLConfig result = null;

        int[] value = new int[1];

        egl.eglGetConfigAttrib(display, a, EGL10.EGL_DEPTH_SIZE, value);
        int depthA = value[0];

        egl.eglGetConfigAttrib(display, b, EGL10.EGL_DEPTH_SIZE, value);
        int depthB = value[0];

        if (depthA > depthB)
            result = a;
        else if (depthA < depthB)
            result = b;
        else //if depthA == depthB
        {
            egl.eglGetConfigAttrib(display, a, EGL10.EGL_RED_SIZE, value);
            int redA = value[0];

            egl.eglGetConfigAttrib(display, b, EGL10.EGL_RED_SIZE, value);
            int redB = value[0];

            if (redA > redB)
                result = a;
            else if (redA < redB)
                result = b;
            else //if redA == redB
            {
                //Don't care
                result = a;
            }
        }

        return result;
    }
}
