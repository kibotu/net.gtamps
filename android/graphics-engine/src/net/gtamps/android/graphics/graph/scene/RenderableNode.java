package net.gtamps.android.graphics.graph.scene;

import net.gtamps.android.graphics.graph.scene.mesh.Material;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.texture.TextureSample;
import net.gtamps.android.graphics.renderer.RenderState;
import net.gtamps.android.graphics.utils.Registry;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:00
 */
public abstract class RenderableNode extends GroupSceneNode {

    protected Material material = Material.DEFAULT;

    protected RenderState renderState = new RenderState();

    protected final ArrayList<TextureSample> textureSamples = new ArrayList<TextureSample>(8);

    @Override
    public void onDrawFrame(GL10 gl10) {
        if (!isFrozen()) super.onDrawFrame(gl10);
        if (!isVisible()) return;

        Registry.getRenderer().draw(this);
    }

    public abstract Mesh getMesh();

    @Override
    public abstract void onCreateInternal(GL10 gl10);

    @Override
    protected abstract void onDrawFrameInternal(GL10 gl10);

    @Override
    public void onResumeInternal(GL10 gl10) {
        getMesh().onResume();
    }

    @Override
    protected abstract void onTransformationInternal(GL10 gl10);

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public RenderState getRenderState() {
        return renderState;
    }

    public void addTexture(TextureSample textureSample) {
        textureSamples.add(textureSample);
    }

    public void removeTexture(TextureSample textureSample) {
        textureSamples.remove(textureSample);
    }

    public ArrayList<TextureSample> getTextureSamples() {
        return textureSamples;
    }
}
