package net.gtamps.android.game.primitives;

import net.gtamps.android.R;
import net.gtamps.android.Registry;
import net.gtamps.android.core.graph.PureVboNode;
import net.gtamps.android.core.graph.SceneNode;
import net.gtamps.android.core.utils.VboLoader;

public class City extends PureVboNode implements IObject3d{

    public City() {
        this(R.raw.city, R.drawable.placeholder, false);
    }

    public City(int vboResourceId, int textureResourceId, boolean generateMipMap) {
        setVbo(VboLoader.loadVbo(Registry.getContext(), vboResourceId));
//        setTextureId(Registry.getTextureLibrary().loadTexture(textureResourceId,generateMipMap));
        hasMipMap = generateMipMap;
    }

    @Override
    public SceneNode getNode() {
        return this;
    }
}
