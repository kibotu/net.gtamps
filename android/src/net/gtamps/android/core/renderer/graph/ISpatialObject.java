package net.gtamps.android.core.renderer.graph;

import net.gtamps.shared.Utils.math.Matrix4;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;

/**
 * Interface für Orientierungsunterstützung für ein Objekt.
 * <p/>
 * Diese Klasse übernimmt die Berechnung der Position, Rotation und Skalierung
 * eines Objektes und überprüft dabei die Notwendigkeit einer Neuberechnung
 * der Orientierungsmatrix.
 *
 * @author sunside
 */
public interface ISpatialObject {
    /**
     * Ermittelt, ob die Orientierung neuberechnet werden muss oder nicht
     *
     * @return <code>true</code>, wenn der Orientierungszustand als dirty markiert ist
     */
    public boolean isOrientationDirty();

    /**
     * Setzt das Dirty-Flag und erzwingt somit eine Neuberechnung der Orientierung.
     */
    public void forceOrientationDirty();

    /**
     * Setzt die Position des Objektes
     *
     * @param x Die X-Komponente
     * @param y Die Y-Komponente
     * @param z Die Z-Komponente
     */
    public void setPosition(float x, float y, float z);

    /**
     * Setzt die Position des Objektes
     *
     * @param position Die Position
     */
    public void setPosition(@NotNull Vector3 position);

    /**
     * Bezieht die Position des Objektes.
     * Es wird eine Referenz auf das interne Vektorobjekt zurückgegeben.
     * Änderungen an dieser Referenz ändern nicht den Dirty-Zustand und werden
     * daher nicht automatisch übernommen. Das Setzen des Dirty-Zustandes kann
     * mit einem Aufruf von forceOrientationDirty() erzwungen werden.
     *
     * @return Die Position
     * @see ISpatialObject#forceOrientationDirty()
     */
    @NotNull
    public Vector3 getPosition();

    /**
     * Berechnet die Orientierung des Objektes neu, falls nötig.
     *
     * @return <code>true</code>, wenn die Orientierung neuberechnet wurde
     */
    public boolean updateOrientation();

    /**
     * Bezieht die Orientierungsmatrix
     *
     * @return Die Orientierung
     */
    @NotNull
    public Matrix4 getOrientationMatrix();
}
