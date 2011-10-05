package net.gtamps.android.game.primitives;

import net.gtamps.android.R;
import net.gtamps.android.Registry;
import net.gtamps.android.core.graph.PureVboNode;
import net.gtamps.android.core.graph.SceneNode;
import net.gtamps.android.core.utils.VboLoader;

public class Car extends PureVboNode implements IObject3d {

    public Car() {
        this(R.raw.camaro2,R.drawable.camaro,true);
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
