package net.gtamps.android.core.renderer.graph;

import org.jetbrains.annotations.Nullable;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * State, der während der Verarbeitung der Knoten verwendet wird
 */
public class ProcessingState {

    /**
     * Die OpenGL-Instanz
     */
    @Nullable
    private GL10 gl;

    /**
     * Setzt den State zurück
     */
    public void reset() {
        gl = null;
    }

    /**
     * Liefert die OpenGL-Instanz
     *
     * @return
     */
    @Nullable
    public GL10 getGl() {
        return gl;
    }

    /**
     * Liefert die OpenGL-Instanz als GL11-Version
     *
     * @return
     */
    @Nullable
    public GL11 getGl11() {
        return (GL11) gl;
    }

    /**
     * Setzt die OpenGL-Instanz
     *
     * @param gl Die OpenGL-Instanz
     */
    public void setGl(@Nullable GL10 gl) {
        this.gl = gl;
    }

}
