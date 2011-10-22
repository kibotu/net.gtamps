package net.gtamps.android;

import android.app.ActivityManager;
import android.content.Context;
import net.gtamps.android.core.renderer.Renderer;
import net.gtamps.android.core.renderer.TextureLibrary;
import org.jetbrains.annotations.NotNull;

public class Registry {

    // static class
    private Registry() {
    }

    public static ActivityManager activityManager;
    public static ActivityManager.MemoryInfo memoryInfo;
    private static Context context;
    private static TextureLibrary textureLibrary;
    private static Renderer renderer;

     public static Context getContext() {
        return context;
    }

    public static void setContext(@NotNull Context context) {
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

    public static void setRenderer(@NotNull Renderer renderer) {
        Registry.renderer = renderer;
    }

    public static Renderer getRenderer() {
        return renderer;
    }
}
