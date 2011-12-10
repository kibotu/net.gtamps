package net.gtamps.android.core.renderer.graph.scene.primitives;

import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.SceneNode;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

/**
 * SceneNode, der nicht selbst rendert, sondern lediglich für Organisationszwecke verwendet wird
 */
public final class NullNode extends SceneNode {
    /**
     * Leere Implementierung der Aktualisierungslogik.
     *
     * @param deltat Zeitdifferenz zum vorherigen Frame
     */
    @Override
    protected void updateInternal(float deltat) {
    }

    @Override
    protected void shadeInternal(@NotNull ProcessingState state) {
    }

    @Override
    protected void afterProcess(@NotNull ProcessingState state) {
    }

    /**
     * Leere Implementierung des Verarbeitungsvorganges
     *
     * @param state Die State-Referenz
     */
    @Override
    protected void processInternal(@NotNull ProcessingState state) {
    }

    /**
     * leere Implementierung des Bereinigungsvorganges
     *
     * @param state Die State-Referenz
     */
    @Override
    protected void cleanupInternal(@NotNull ProcessingState state) {
    }

    /**
     * Spezifische Implementierung des Setupvorganges
     *
     * @param state Die State-Referenz
     */
    @Override
    protected void setupInternal(@NotNull ProcessingState state) {
    }

    @Override
    protected void setOptions() {
    }
}
