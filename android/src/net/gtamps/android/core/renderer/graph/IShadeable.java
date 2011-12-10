package net.gtamps.android.core.renderer.graph;

import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public interface IShadeable {
    public void shade(@NotNull ProcessingState state);
}
