package net.gtamps.android.game.primitives;

import net.gtamps.android.Registry;
import net.gtamps.android.core.graph.PureVboNode;
import net.gtamps.android.core.graph.SceneNode;
import net.gtamps.android.R;
import net.gtamps.android.core.utils.VboLoader;

public class Cube extends PureVboNode implements IObject3d {

    public Cube() {
        setVbo(VboLoader.loadVbo(Registry.getContext(), R.raw.cube));
    }

    @Override
    public SceneNode getNode() {
        return this;
    }

    public void loadTexture(int textureResourceId, boolean generateMipMap) {
        setTextureId(Registry.getTextureLibrary().loadTexture(textureResourceId, generateMipMap));
        enableMipMap(generateMipMap);
    }
}
