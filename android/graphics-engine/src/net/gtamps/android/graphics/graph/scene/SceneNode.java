package net.gtamps.android.graphics.graph.scene;

import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Matrix4;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 19:59
 */
public abstract class SceneNode extends ObjectWithOrientation {

    protected SceneNode parent;
    protected boolean combinedTransformationDirty = true;
    protected Matrix4 combinedTransformation = Matrix4.UNIT;

    public SceneNode(@NotNull SceneNode parent) {
        this.parent = parent;
    }

    public SceneNode() {
    }

    public void setParent(SceneNode parent) {
        this.parent = parent;
    }

    public SceneNode getParent() {
        return parent;
    }

    public abstract void onCreate(GL10 gl10);

    protected abstract void onCreateInternal(GL10 gl10);

    protected void onTransformation(GL10 gl10) {

        // Orientierung neu berechnen
        if (updateOrientation()) {
            // Orientierungsmatrix hat sich geändert, combined transformation als dirty markieren
            combinedTransformationDirty = true;
        }

        // Wenn combined transformation als dirty markiert, neu berechnen
        if (combinedTransformationDirty) {
            updateCombinedTransformation();
            combinedTransformationDirty = false;
        }
    }

    protected abstract void onTransformationInternal(GL10 gl10);

    /**
     * Aktualisiert die kombinierte Transformationsmatrix
     */
    protected final void updateCombinedTransformation() {
        // Elternknoten existiert, also "obere" Orientierung mit dieser verheiraten
        if (parent != null) {
            combinedTransformation = parent.getCombinedTransformation().mul(orientation);
        }
    }

    public Matrix4 getCombinedTransformation() {
        return combinedTransformation;
    }

    public abstract void onDrawFrame(GL10 gl10);

    protected abstract void onDrawFrameInternal(GL10 gl10);

    public abstract void onResume(GL10 gl10);

    protected abstract void onResumeInternal(GL10 gl10);
}
