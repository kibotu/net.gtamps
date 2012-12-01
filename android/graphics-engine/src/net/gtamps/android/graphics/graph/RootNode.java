package net.gtamps.android.graphics.graph;

import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 23:13
 */
public class RootNode extends SceneNode {

    private ArrayList<SceneNode> children;

    public RootNode() {
    }

    @Override
    public void onDrawFrame(GL10 gl10) {

//        Logger.i(this, "drawing uID=" + uID + (hasParent() ? "|parentID= " +parentID.uID : ""));

        // transform
        onTransformation(gl10);

        // render
        for (int i = 0; i < size(); ++i) {
            final SceneNode node = children.get(i);
            // reset parentID relationship if child has multiple parents
            node.setParent(this);
            // draw child
            node.onDrawFrame(gl10);
        }
        onDrawFrameInternal(gl10);
    }

    @Override
    final public void onCreate(GL10 gl10) {
        for (int i = 0; i < size(); ++i) {
            children.get(i).onCreate(gl10);
        }
        onCreateInternal(gl10);
    }

    final public void onResume(GL10 gl10) {
        for (int i = 0; i < size(); ++i) {
            children.get(i).onResume(gl10);
        }
        onResumeInternal(gl10);
    }

    final public void add(SceneNode child) {
        if (children == null) children = new ArrayList<SceneNode>();
        children.add(child);
        child.setParent(this);
    }

    final public void remove(SceneNode child) {
        if (children == null) return;
        children.remove(child);
        child.setParent(null);
    }

    final public SceneNode getChild(int index) {
        return index > size() ? null : children.get(index);
    }

    final public int size() {
        return children == null ? 0 : children.size();
    }

    @Override
    public void onCreateInternal(GL10 gl10) {
        // do nothing
    }

    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
        // do nothing
    }

    @Override
    public void onResumeInternal(GL10 gl10) {
        // do nothing
    }

    @Override
    protected void onTransformationInternal(GL10 gl10, boolean isDirty) {
        for (int i = 0; i < size(); ++i) {
            if (isDirty) children.get(i).forceCombinedOrientationDirty();
            children.get(i).onTransformation(gl10);
        }
    }
}
