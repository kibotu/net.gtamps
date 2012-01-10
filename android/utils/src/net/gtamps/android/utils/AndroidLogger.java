package net.gtamps.android.utils;

import android.opengl.GLES20;
import android.opengl.GLU;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import net.gtamps.shared.Utils.ILogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum AndroidLogger implements ILogger {

    INSTANCE;

    @Override
    public void toast(@NotNull String o, String message) {
//        Message msg = handler.obtainMessage();
//        msg.arg1 = 1;
//        handler.sendMessage(msg);
//        msg.obj = message;
    }

//    private final static Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            if (msg.arg1 == 1) {
//                Toast t = Toast.makeText(Registry.getContext().getApplicationContext(), "" + msg.obj, Toast.LENGTH_LONG);
//                t.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
//                t.show();
//            }
//        }
//    };

    @Override
    public void d(@NotNull String id, @Nullable String message) {
        Log.d(id, message);
    }

    @Override
    public void v(@NotNull String id, @Nullable String message) {
        Log.v(id, message);
    }

    @Override
    public void i(@NotNull String id, @Nullable String message) {
        Log.i(id, message);
    }

    @Override
    public void w(@NotNull String id, @Nullable String message) {
        Log.w(id, message);
    }

    @Override
    public void e(@NotNull String id, @Nullable String message) {
        Log.e(id, message);
    }

    @Override
    public void save(String filename) {
    }

    @Override
    public void checkGlError(@NotNull String id, @Nullable String operation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            e(id, operation + ": glError " + error + " " + GLU.gluErrorString(error));
            throw new RuntimeException(operation + ": glError " + error + " " + GLU.gluErrorString(error));
        }
    }
}
