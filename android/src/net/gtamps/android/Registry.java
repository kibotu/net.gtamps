package net.gtamps.android;

import android.app.ActivityManager;
import android.content.Context;
import net.gtamps.android.core.renderer.Renderer;
import net.gtamps.android.core.renderer.TextureLibrary;

import java.util.ArrayList;
import java.util.HashMap;

public class Registry {

    // static class
    private Registry() {
    }

    public static ActivityManager activityManager;
    public static ActivityManager.MemoryInfo memoryInfo;
    private static Context context;
    private static TextureLibrary textureLibrary;

     public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Registry.context = context;

        // get available memory
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        memoryInfo = new ActivityManager.MemoryInfo();
    }


    public static TextureLibrary getTextureLibrary() {
        return textureLibrary;
    }

    public static void setTextureLibrary(TextureLibrary textureLibrary) {
        Registry.textureLibrary = textureLibrary;
    }

    public static long getAvailableMemory() {
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }
}
