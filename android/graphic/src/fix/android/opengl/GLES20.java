package fix.android.opengl;

public class GLES20 {
    static {
        System.loadLibrary("fix-GLES20");
    }

    // sole purpose of this method is to load the library during initialization
    // and not on first use; the method actually performs no actions
    native public static void init();

    native public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, int offset);

    native public static void glDrawElements(int mode, int count, int type, int offset);


    private GLES20() {
    }
}
