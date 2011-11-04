package net.gtamps.android.core.renderer.graph.scene;

import net.gtamps.android.core.renderer.graph.IProcessable;
import net.gtamps.android.core.renderer.graph.IUpdatableLogic;
import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.SceneNode;
import net.gtamps.android.core.renderer.graph.primitives.Camera;
import net.gtamps.android.core.renderer.graph.primitives.NullNode;
import net.gtamps.shared.Utils.math.Color4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.microedition.khronos.opengles.GL10;

/**
 * Der Szenengraph
 */
public class SceneGraph implements IUpdatableLogic, IProcessable {

    /**
     * Der Wurzelknoten
     */
    @NotNull
    private NullNode rootNode = new NullNode();

    /**
     * Die aktive Kamera
     */
    @Nullable
    private Camera activeCamera;

    /**
     * Defines the background color of the scenes.
     */
    @NotNull
    private static Color4 background = new Color4(0, 0, 0, 0);
    private static boolean isDirty = true;

    /**
     * Bezieht die aktive Kamera
     *
     * @return
     */
    @Nullable
    public final Camera getActiveCamera() {
        return activeCamera;
    }

    /**
     * Setzt die aktive Kamera
     *
     * @param camera Die Kamera
     */
    public final void setActiveCamera(@Nullable Camera camera) {
        activeCamera = camera;
    }

    /**
     * Der Processing-State
     */
    @NotNull
    private final ProcessingState state = new ProcessingState();

    /**
     * Rendert den Graphen (Spezialform von {@link #process})
     *
     * @param gl Die OpenGL-Instanz
     * @see #process(ProcessingState)
     */
    public void render(@NotNull GL10 gl) {
        state.reset();
        state.setGl(gl);
        process(state);
        if (isDirty) {
            state.getGl().glClearColor(background.r, background.g, background.b, background.a);
            isDirty = false;
        }
    }

    /**
     * Verarbeitet den Knoten und alle Kindknoten
     *
     * @param state Die State-Referenz
     */
    @Override
    public void process(@NotNull ProcessingState state) {
        if (activeCamera != null) {
            activeCamera.setVisible(true);
            activeCamera.process(state);
            activeCamera.setVisible(false);
        }
        rootNode.process(state);
    }

    public Color4 getBackground() {
        isDirty = true;
        return background;
    }

    /**
     * Ermittelt die Sichtbarkeit des Knotens
     *
     * @return <code>true</code>, wenn der Knoten sichtbar ist
     */
    @Override
    public boolean isVisible() {
        return rootNode.isVisible();
    }

    /**
     * Sets visibility.
     *
     * @param isVisible
     */
    public void setVisible(boolean isVisible) {
        rootNode.setVisible(isVisible);
    }

    /**
     * Aktualisiert die aktive Kamera.
     *
     * @param deltat Zeitdifferenz zum vorherigen Frame
     */
    private void updateActiveCamera(float deltat) {
        if (activeCamera != null) activeCamera.update(deltat);
    }

    /**
     * Aktualisiert den Knoten und alle Kind- und Elternknoten.
     * Es wird ein volles Update ausgeführt.
     *
     * @param deltat Zeitdifferenz zum vorherigen Frame
     */
    @Override
    public void update(float deltat) {
        // Kamera aktualisieren
        updateActiveCamera(deltat);

        // Reguläre Aktualisierung durchführen
        rootNode.update(deltat);
    }

    /**
     * Aktualisiert den Knoten und alle Kind- und Elternknoten.
     * Es wird ein volles Update ausgeführt.
     *
     * @param deltat       Zeitdifferenz zum vorherigen Frame
     * @param positionOnly Legt fest, ob nur ein Positionsupdate durchgeführt werden soll (z.B. für Kameraupdates)
     */
    @Override
    public void update(float deltat, boolean positionOnly) {
        // Kamera aktualisieren
        updateActiveCamera(deltat);

        // Reguläre Aktualisierung durchführen
        rootNode.update(deltat, positionOnly);
    }

    /**
     * Initialisiert den Graphen
     *
     * @param state Die State-Referenz
     */
    public final void setup(@NotNull ProcessingState state) {
        rootNode.setup(state);
    }

    /**
     * Fügt der Hierarchie ein Kind hinzu
     *
     * @param child Der hinzuzufügende Kindknoten
     */
    public final void add(@NotNull SceneNode child) {
        rootNode.add(child);
    }

    /**
     * Entfernt ein Kind aus der Hierarchie
     *
     * @param child Der zu entfernende Kindknoten
     * @return Gibt an, ob das Kind entfernt wurde
     */
    public final boolean remove(@NotNull SceneNode child) {
        return rootNode.remove(child);
    }

    /**
     * Bezieht einen Kindknoten
     *
     * @param childIndex Der Index des Kindknotens
     * @return Der Kindknoten
     */
    @Nullable
    public final SceneNode get(int childIndex) {
        return rootNode.get(childIndex);
    }

    /**
     * Liefert die Anzahl der direkten Kindknoten
     *
     * @return Anzahl der direkten Kindknoten
     */
    public int getChildCount() {
        return rootNode.getChildCount();
    }

    public void setBackground(Color4 color) {
        this.background = color;
    }
}

