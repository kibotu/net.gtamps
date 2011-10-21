package net.gtamps.android.core.utils;

import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import net.gtamps.android.Registry;
import net.gtamps.shared.Utils.ILogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum AndroidLogger implements ILogger {

    INSTANCE;

    public static void showMsg(String message) {
        Toast t = Toast.makeText(Registry.getContext(), message, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
        t.show();
    }

    @Override
    public void d(@NotNull String id, @Nullable String message) {
        Log.d(id,message);
    }

    @Override
    public void v(@NotNull String id, @Nullable String message) {
        Log.v(id,message);
    }

    @Override
    public void i(@NotNull String id, @Nullable String message) {
        Log.i(id,message);
    }

    @Override
    public void w(@NotNull String id, @Nullable String message) {
        Log.w(id,message);
    }

    @Override
    public void e(@NotNull String id, @Nullable String message) {
        Log.e(id,message);
    }

    @Override
    public void save(String filename) {
    }
}
