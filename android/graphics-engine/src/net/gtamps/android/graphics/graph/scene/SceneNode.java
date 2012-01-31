package net.gtamps.android.graphics.graph.scene;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 19:59
 */
public abstract class SceneNode extends ObjectWithOrientation {
    public abstract void onDrawFrame(GL10 gl10);
}
