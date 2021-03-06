package net.gtamps.android.renderer.graph;

import org.jetbrains.annotations.NotNull;

/**
 * Aufräumbares Element
 */
public interface ICleanable {

    /**
     * Bereinigt den Knoten und alle Kindknoten
     *
     * @param state Die State-Referenz
     */
    public void cleanup(@NotNull ProcessingState state);

}
