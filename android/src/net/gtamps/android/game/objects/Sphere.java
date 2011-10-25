package net.gtamps.android.game.objects;

import net.gtamps.android.R;
import net.gtamps.android.core.Registry;
import net.gtamps.android.core.renderer.graph.PureVboNode;
import net.gtamps.android.core.renderer.graph.SceneNode;
import net.gtamps.android.core.utils.VboLoader;

public class Sphere extends PureVboNode implements IObject3d{

    public Sphere() {
        setVbo(VboLoader.loadVbo(Registry.getContext(), R.raw.sphere));
    }

    @Override
    public SceneNode getNode() {
        return this;
    }
}
