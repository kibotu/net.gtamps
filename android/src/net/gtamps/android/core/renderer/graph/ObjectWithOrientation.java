package net.gtamps.android.core.renderer.graph;

import net.gtamps.shared.Utils.math.AxisAlignedBox;
import net.gtamps.shared.Utils.math.Matrix4;
import net.gtamps.shared.Utils.math.MatrixFactory;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;

// TODO: ObjectWithDimension ableiten, das eine Ausdehnung besitzt!

/**
 * Orientierungsunterstützung für ein Objekt.
 * <p/>
 * Diese Klasse übernimmt die Berechnung der Position, Rotation und Skalierung
 * eines Objektes und überprüft dabei die Notwendigkeit einer Neuberechnung
 * der Orientierungsmatrix.
 *
 * @author sunside
 */
public abstract class ObjectWithOrientation implements ISpatialObject {

    /**
     * Gibt an, ob das Objekt eingefroren ist
     */
    private boolean frozen = false;

    /**
     * Die Orientierung des Objektes
     */
    @NotNull
    protected Matrix4 orientation = Matrix4.createNew();

    /**
     * Gibt an, ob der Zustand dieses Objektes verändert wurde
     */
    private boolean isDirty = true;

    /**
     * Die Position
     */
    @NotNull
    protected final Vector3 position = Vector3.createNew(0.f, 0.f, 0.f);

    /**
     * Die Skalierung
     */
    @NotNull
    protected final Vector3 scaling = Vector3.createNew(1.f, 1.f, 1.f);

    /**
     * Die Rotation (Roll, Pitch, Yaw)
     */
    @NotNull
    protected final Vector3 rotation = Vector3.createNew(0.f, 0.f, 0.f);

    /**
     * Die Dimensionen des Objektdingens in Einheiten
     */
    @NotNull
    protected final Vector3 dimension = Vector3.createNew(1f, 1f, 1f);

    /**
     * Die achsenorientierte Bounding Box des Elementes
     */
    @NotNull
    protected final AxisAlignedBox aabb = AxisAlignedBox.createNew();

    /**
     * Bezieht die Bounding Box des Elementes
     *
     * @return Die Bounding Box
     * @see #getAABB()
     * @see #updateBoundingBox()
     */
    @NotNull
    public AxisAlignedBox getBoundingBox() {
        return getAABB();
    }

    /**
     * Bezieht die Bounding Box des Elementes
     *
     * @return Die Bounding Box
     */
    @NotNull
    public AxisAlignedBox getAABB() {
        return aabb;
    }

    /**
     * Aktualisiert die Bounding Box des Elementes.
     * Abgeleitete Klassen können diese Methode überladen, um
     * spezifische Aktualisierungslogik zu implementieren.
     *
     * @return Die Bounding Box
     */
    @NotNull
    public void updateBoundingBox() {
        // TODO: OBB aus Werten erzeugen
        AxisAlignedBox obb = AxisAlignedBox.createNew();
        obb.setCenter(getPosition());

        // TODO: OBB in AABB umwandeln
    }

    /**
     * Gibt an, ob das Objekt eingefroren ist
     *
     * @return <code>true</code>, wenn das Objekt eingefroren ist
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Gibt an, ob das Objekt eingefroren ist
     *
     * @param frozen <code>true</code>, wenn das Objekt eingefroren werden soll
     */
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * Ermittelt, ob die Orientierung neuberechnet werden muss oder nicht
     *
     * @return <code>true</code>, wenn der Orientierungszustand als dirty markiert ist
     */
    public boolean isOrientationDirty() {
        return !frozen && isDirty;
    }

    /**
     * Setzt das Dirty-Flag und erzwingt somit eine Neuberechnung der Orientierung.
     */
    public void forceOrientationDirty() {
        isDirty = true;
    }

    /**
     * Erzeugt eine neues Instanz des Objektes
     */
    public ObjectWithOrientation() {
        orientation.toUnit();
    }


    /**
     * Bewegt das Objekt
     *
     * @param x Die X-Komponente
     * @param y Die Y-Komponente
     * @param z Die Z-Komponente
     */
    public void move(float x, float y, float z) {
        position.addInPlace(x, y, z);
        isDirty = true;
    }

    /**
     * Bewegt das Objekt
     *
     * @param position Die Position
     */
    public void move(@NotNull Vector3 position) {
        move(position.x, position.y, position.z);
    }

    /**
     * Setzt die Position des Objektes
     *
     * @param x Die X-Komponente
     * @param y Die Y-Komponente
     * @param z Die Z-Komponente
     */
    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        isDirty = true;
    }

    /**
     * Setzt die Position des Objektes
     *
     * @param position Die Position
     */
    public void setPosition(@NotNull Vector3 position) {
        setPosition(position.x, position.y, position.z);
    }

    /**
     * Liefert die Ausmaße des Objektes
     *
     * @return Die Dimensionen
     */
    @NotNull
    public Vector3 getDimension() {
        return dimension;
    }

    /**
     * Liefert die Ausmaße des Objektes
     *
     * @return Die Dimensionen
     */
    public void setDimension(@NotNull Vector3 dimensionen) {
        dimension.set(dimensionen);
    }

    /**
     * Liefert die Ausmaße des Objektes
     *
     * @return Die Dimensionen
     */
    public void setDimension(float width, float height, float depth) {
        assert width > 0;
        assert height > 0;
        assert depth >= 0; // z.B. für Sprites

        dimension.set(width, height, depth);
    }

    /**
     * Bezieht die Position des Objektes.
     * Es wird eine Referenz auf das interne Vektorobjekt zurückgegeben.
     * Änderungen an dieser Referenz ändern nicht den Dirty-Zustand und werden
     * daher nicht automatisch übernommen. Das Setzen des Dirty-Zustandes kann
     * mit einem Aufruf von forceOrientationDirty() erzwungen werden.
     *
     * @return Die Position
     * @see ObjectWithOrientation#forceOrientationDirty()
     */
    @NotNull
    public Vector3 getPosition() {
        return position;
    }

    /**
     * Setzt die Skalierung des Objektes
     *
     * @param sx Die X-Skalierung
     * @param sy Die Y-Skalierung
     * @param sz Die Z-Skalierung
     */
    public void setScaling(float sx, float sy, float sz) {
        scaling.set(sx, sy, sz);
        isDirty = true;
    }

    /**
     * Setzt die Skalierung des Objektes
     *
     * @param scaling Die Skalierung
     */
    public void setScaling(Vector3 scaling) {
        setScaling(scaling.x, scaling.y, scaling.z);
    }

    /**
     * Bezieht die Skalierung des Objektes
     * Es wird eine Referenz auf das interne Vektorobjekt zurückgegeben.
     * Änderungen an dieser Referenz ändern nicht den Dirty-Zustand und werden
     * daher nicht automatisch übernommen. Das Setzen des Dirty-Zustandes kann
     * mit einem Aufruf von forceOrientationDirty() erzwungen werden.
     *
     * @return Die Skalierung
     * @see ObjectWithOrientation#forceOrientationDirty()
     */
    @NotNull
    public Vector3 getScaling() {
        return scaling;
    }

    /**
     * Setzt die Rotation des Objektes
     *
     * @param rollX  Rotation um die X-Achse in Radians
     * @param pitchY Rotation um die Y-Achse in Radians
     * @param yawZ   Rotation um die Z-Achse in Radians
     */
    public void setRotation(float rollX, float pitchY, float yawZ) {
        rotation.set(rollX, pitchY, yawZ);
        isDirty = true;
    }

    /**
     * Setzt die Rotation des Objektes
     *
     * @param rotation Die Rotation (Roll-Pitch-Yaw) in Radians
     */
    public void setRotation(Vector3 rotation) {
        setRotation(rotation.x, rotation.y, rotation.z);
    }

    /**
     * Bezieht die Rotation des Objektes
     * Es wird eine Referenz auf das interne Vektorobjekt zurückgegeben.
     * Änderungen an dieser Referenz ändern nicht den Dirty-Zustand und werden
     * daher nicht automatisch übernommen. Das Setzen des Dirty-Zustandes kann
     * mit einem Aufruf von forceOrientationDirty() erzwungen werden.
     *
     * @return Die Rotation Roll-Pitch-Yaw in Radians
     * @see ObjectWithOrientation#forceOrientationDirty()
     */
    @NotNull
    public Vector3 getRotation() {
        return rotation;
    }

    /**
     * Berechnet die Orientierung des Objektes neu, falls nötig.
     *
     * @return <code>true</code>, wenn die Orientierung neuberechnet wurde
     */
    public boolean updateOrientation() {
        if (!isDirty || frozen) return false;

        // Translationsmatrix beziehen
        //Matrix4 translation = new Matrix4();
        //translation.toTranslation(position);

        // Skalierungsmatrix beziehen
        Matrix4 scaling = Matrix4.createNew();
        scaling.toScaling(this.scaling);

        // Rotationsmatrix beziehen
        //Matrix4 rotation = Matrix4.getRotationEulerRPY(rotation.x, rotation.y, rotation.z);

        // Matrix zusammenfalten
        orientation = MatrixFactory.getTransformation(rotation, position).mul(scaling);

        // Bounding Box erzeugen
        updateBoundingBox();

        // Als nicht mehr dirty markieren
        isDirty = false;

        // Alles gut.
        Matrix4.recycle(scaling);
        return true;
    }

    /**
     * Bezieht die Orientierungsmatrix
     *
     * @return Die Orientierung
     */
    @NotNull
    public Matrix4 getOrientationMatrix() {
        return orientation;
    }

    /**
     * Setzt die Orientierungsmatrix.
     * Diese Methode umgeht die Berechnung und setzt das Dirty-Flag zurück.
     *
     * @param orientation Die neue Orientierungsmatrix
     */
    public void setOrientationMatrix(@NotNull Matrix4 orientation) {
        this.orientation = orientation;
        isDirty = false;
    }
}

