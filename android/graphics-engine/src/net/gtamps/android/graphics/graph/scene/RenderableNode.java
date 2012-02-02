package net.gtamps.android.graphics.graph.scene;

import net.gtamps.android.graphics.graph.scene.mesh.Material;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.mesh.texture.TextureAnimation;
import net.gtamps.android.graphics.graph.scene.mesh.texture.TextureSprite;
import net.gtamps.android.graphics.renderer.RenderState;
import net.gtamps.android.graphics.utils.Registry;
import net.gtamps.shared.game.state.State;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 20:00
 */
public abstract class RenderableNode extends RootNode {

    protected Material material = Material.DEFAULT;

    protected RenderState renderState = new RenderState();

    protected final ArrayList<Texture> textures = new ArrayList<Texture>(8);

    protected TextureAnimation textureAnimation;
    protected TextureSprite textureSprite;
    protected float lastPercentage = 0;

    @Override
    final public void onDrawFrame(GL10 gl10) {
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
    final public void onResumeInternal(GL10 gl10) {
        if (getMesh() != null) {
            getMesh().invalidate();
            getMesh().allocate();
        }
        for (int i = 0; i < textures.size(); i++) {
            textures.get(i).allocate();
        }
    }

    final public Material getMaterial() {
        return material;
    }

    final public void setMaterial(Material material) {
        this.material = material;
    }

    public RenderState getRenderState() {
        return renderState;
    }

    final public void addTexture(Texture texture) {
        textures.add(texture);
    }

    final public void removeTexture(Texture texture) {
        textures.remove(texture);
    }

    final public ArrayList<Texture> getTextures() {
        return textures;
    }

    final public void addTexture(ArrayList<Texture> textures) {
        this.textures.addAll(textures);
    }

    final public void removeTexture(ArrayList<Texture> textures) {
        this.textures.removeAll(textures);
    }

    public void addTextureAnimation(TextureAnimation textureAnimation) {
        this.textureAnimation = textureAnimation;
    }

    public boolean hasTextureAnimation() {
        return textureAnimation != null;
    }

    public TextureAnimation getTextureAnimation() {
        return textureAnimation;
    }

    public TextureSprite getTextureSprite() {
        return textureSprite;
    }

    public void animate(State.Type type, float percentage) {
        if (lastPercentage == percentage)return;
        lastPercentage = percentage;
        percentage *= 100;
        percentage %= 100;
        TextureSprite [] textureSprites = textureAnimation.getAnimation(type);
        final int index = (int) (textureSprites.length / 100f * percentage);
        setImage(textureSprites[index]);
    }

    public void setImage(TextureSprite textureSprite) {
        this.textureSprite = textureSprite;
        setDimension(textureSprite.width,textureSprite.height,0);
    }
}
