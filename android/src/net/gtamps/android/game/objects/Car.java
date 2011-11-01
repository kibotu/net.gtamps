package net.gtamps.android.game.objects;

import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class Car extends RenderableNode {

    @Override
    protected void renderInternal(@NotNull GL10 gl) {
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

    public enum Type {
        CAMARO(R.raw.camaro2, R.drawable.camaro),
        RIVIERA(R.raw.riviera, R.drawable.riviera),
        CHEVROLET_CORVETTE(R.raw.chevroletcorvette,R.drawable.placeholder);
        public final int rawId;
        public final int drawableId;
        private Type(final int rawId, final int drawableId) {
            this.rawId = rawId;
            this.drawableId = drawableId;
        }
    }

    public Car(Type type) {
        this(type.rawId,type.drawableId,true);
    }

    public Car(int vboResourceId, int textureResourceId, boolean generateMipMap) {
//        mesh = new Mesh();
//        setVbo(VboLoader.loadVbo(Registry.getContext(), vboResourceId));
//        setTextureId(Registry.getTextureLibrary().loadTexture(textureResourceId,generateMipMap));
//        hasMipMap = generateMipMap;
    }
}
