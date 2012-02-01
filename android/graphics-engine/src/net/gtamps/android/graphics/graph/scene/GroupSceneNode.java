package net.gtamps.android.graphics.graph.scene;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 23:13
 */
public class GroupSceneNode extends SceneNode {

    private ArrayList<SceneNode> children;

    public GroupSceneNode() {
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        for (int i = 0; i < size(); ++i) {
            children.get(i).onDrawFrame(gl10);
        }
        onDrawFrameInternal(gl10);
    }

    @Override
    protected void onTransformation(GL10 gl10) {
        super.onTransformation(gl10);

        for (int i = 0; i < size(); ++i) {
            children.get(i).onTransformation(gl10);
        }
        onTransformationInternal(gl10);
    }

    @Override
    public void onCreate(GL10 gl10) {
        for (int i = 0; i < size(); ++i) {
            children.get(i).onCreate(gl10);
        }
        onCreateInternal(gl10);
    }

    public void onResume(GL10 gl10) {
        for (int i = 0; i < size(); ++i) {
            children.get(i).onResume(gl10);
        }
        onResumeInternal(gl10);
    }

    public void add(SceneNode child) {
        if (children == null) children = new ArrayList<SceneNode>();
        children.add(child);
        child.setParent(this);
    }

    public void remove(SceneNode child) {
        if (children == null) return;
        children.remove(child);
        child.setParent(null);
    }

    public SceneNode getChild(int index) {
        return index > size() ? null : children.get(index);
    }

    public int size() {
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
    protected void onTransformationInternal(GL10 gl10) {
        // do nothing
    }
}
