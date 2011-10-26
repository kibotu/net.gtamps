package net.gtamps.android.game.objects;

import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class City extends RenderableNode {

    public City() {
        this(R.raw.city, R.drawable.placeholder, false);
    }

    @Override
    protected void renderInternal(@NotNull GL10 gl) {
    }

    public City(int vboResourceId, int textureResourceId, boolean generateMipMap) {
//        setVbo(VboLoader.loadVbo(Registry.getContext(), vboResourceId));
////        setTextureId(Registry.getTextureLibrary().loadTexture(textureResourceId,generateMipMap));
//        hasMipMap = generateMipMap;
    }

    @Override
    public void onDirty() {
    }

    @Override
    protected void updateInternal(float deltat) {
    }

    @Override
    protected void cleanupInternal(@NotNull ProcessingState state) {
    }

    @Override
    protected void setupInternal(@NotNull ProcessingState state) {
    }
}
