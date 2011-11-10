package net.gtamps.android.core.renderer.graph.scene.primitives;

import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class StaticRenderableNode extends RenderableNode {

    public StaticRenderableNode() {
    }

    @Override
    protected void setupInternal(@NotNull ProcessingState state) {
    }

    @Override
    protected void renderInternal(@NotNull GL10 gl) {
    }

    @Override
    protected void updateInternal(float deltat) {
    }

    @Override
    protected void setOptions() {
    }

    @Override
    public void onDirty() {
    }

    @Override
    protected void cleanupInternal(@NotNull ProcessingState state) {
    }
}
