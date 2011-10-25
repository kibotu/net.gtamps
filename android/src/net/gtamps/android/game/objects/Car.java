package net.gtamps.android.game.objects;

import net.gtamps.android.R;
import net.gtamps.android.core.Registry;
import net.gtamps.android.core.renderer.graph.PureVboNode;
import net.gtamps.android.core.renderer.graph.SceneNode;
import net.gtamps.android.core.utils.VboLoader;

public class Car extends PureVboNode implements IObject3d {

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
        setVbo(VboLoader.loadVbo(Registry.getContext(), vboResourceId));
        setTextureId(Registry.getTextureLibrary().loadTexture(textureResourceId,generateMipMap));
        hasMipMap = generateMipMap;
    }

    @Override
    public SceneNode getNode() {
        return this;
    }
}
