package net.gtamps.android.renderer.graph.scene.primitives;

import net.gtamps.android.renderer.graph.ProcessingState;
import net.gtamps.android.renderer.graph.SceneNode;
import org.jetbrains.annotations.NotNull;

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

    @Override
    protected void onResumeInternal(ProcessingState state) {
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
