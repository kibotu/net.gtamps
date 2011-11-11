package net.gtamps.shared.Utils.math;

import org.jetbrains.annotations.NotNull;

/**
 * Ein Quader
 */
public final class Box implements IBoundsTestable {

    /**
     * Die Position der Box
     */
    @NotNull
    private Vector3 _position = Vector3.createNew();

    /**
     * Die Ausdehnung der Box
     */
    @NotNull
    private Vector3 _extent = Vector3.createNew();


    /**
     * Setzt die Größe des Quaders
     *
     * @param width  Breite
     * @param height Höhe
     * @param depth  Tiefe
     * @see #setExtent(float, float, float)
     */
    public void setSize(float width, float height, float depth) {
        setExtent(width, height, depth);
    }

    /**
     * Setzt die Größe des Quaders
     *
     * @param width  Breite
     * @param height Höhe
     * @param depth  Tiefe
     */
    public void setExtent(float width, float height, float depth) {
        assert width > 0 && height > 0 && depth > 0;
        _extent.set(width, height, depth);
    }

    /**
     * Setzt die Breite der Box
     *
     * @param width Die Breite
     */
    public void setWidth(float width) {
        _extent.x = width;
    }

    /**
     * Setzt die Höhe der Box
     *
     * @param height Die Höhe
     */
    public void setHeight(float height) {
        _extent.y = height;
    }

    /**
     * Setzt die Tiefe der Box
     *
     * @param depth Die Tiefe
     */
    public void setDepth(float depth) {
        _extent.z = depth;
    }

    /**
     * Bezieht die Breite der Box
     *
     * @return Die Breite
     */
    public float getWidth() {
        return _extent.x;
    }

    /**
     * Bezieht die Höhe der Box
     *
     * @return Die Höhe
     */
    public float getHeight() {
        return _extent.y;
    }

    /**
     * Bezieht die Tiefe der Box
     *
     * @return Die Tiefe
     */
    public float getDepth() {
        return _extent.z;
    }

    /**
     * Setzt die Ausdehnung der Box
     *
     * @param extent Die Ausdehnung
     * @return Diese Instanz für method chaining
     * @see #getExtent()
     */
    @NotNull
    public Box setExtent(@NotNull Vector3 extent) {
        _extent.set(extent);
        return this;
    }

    /**
     * Bezieht die Ausdehnung der Box
     *
     * @return Die Ausdehnung des Quaders
     * @see #setExtent(Vector3)
     */
    @NotNull
    public Vector3 getExtent() {
        return _extent;
    }

    /**
     * Setzt die Position der Box
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @param z Z-Koordinate
     * @return Diese Instanz für method chaining
     */
    @NotNull
    public Box setPosition(float x, float y, float z) {
        _position.set(x, y, z);
        return this;
    }

    /**
     * Setzt die Position der Box
     *
     * @param position Die Position
     * @return Diese Instanz für method chaining
     */
    @NotNull
    public Box setPosition(@NotNull Vector3 position) {
        _position.set(position);
        return this;
    }

    /**
     * Bezieht die Position der Box
     *
     * @return Die Position des Quaders
     */
    @NotNull
    public Vector3 getPosition() {
        return _position;
    }

    /**
     * Bezieht die X-Koordinate der Box
     *
     * @return X-Koordinate
     */
    public float getX() {
        return _position.x;
    }

    /**
     * Bezieht die Y-Koordinate der Box
     *
     * @return Y-Koordinate
     */
    public float getY() {
        return _position.y;
    }

    /**
     * Bezieht die Z-Koordinate der Box
     *
     * @return Z-Koordinate
     */
    public float getZ() {
        return _position.z;
    }

    /**
     * Erzeugt eine Box der Größe 1x1x1
     */
    public Box() {
    }

    /**
     * Erzeugt eine Box der Größe SxSxS
     *
     * @param size Breite, Höhe und Tiefe der Box
     */
    public Box(float size) {
        assert size > 0;
        setSize(size, size, size);
    }

    /**
     * Erzeugt eine Box der Größe WxHxD
     *
     * @param width  Breite des Quaders
     * @param height Höhe des Quaders
     * @param depth  Tiefe des Quaders
     */
    public Box(float width, float height, float depth) {
        assert width > 0 && height > 0 && depth > 0;
        setSize(width, height, depth);
    }

    /**
     * Überprüft, ob sich ein Punkt innerhalb der Box befindet
     *
     * @param point Die zu testende Position
     * @return <code>true</code>, wenn sich der Punkt innerhalb des Quaders befindet
     */
    public boolean containsPoint(@NotNull Vector3 point) {
        float w2 = _extent.x * 0.5f;
        if (point.x < _position.x - w2) return false;
        if (point.x > _position.x + w2) return false;

        float h2 = _extent.y * 0.5f;
        if (point.y < _position.y - h2) return false;
        if (point.y > _position.y + h2) return false;

        float d2 = _extent.z * 0.5f;
        return point.z >= _position.z - d2 && point.z <= _position.z + d2;
    }

    /**
     * Erzeugt eine optimale Bounding Box aus einer Punktwolke
     *
     * @param points Die Punkte
     * @return Die Bounding Sphere
     * @see Box#createFrom(float, Vector3...)
     */
    @NotNull
    public Box createFrom(@NotNull Vector3... points) {
        return createFrom(0.01f, points);
    }

    /**
     * Erzeugt eine optimale Bounding Box aus einer Punktwolke
     *
     * @param minSize Die minimale Größe
     * @param points  Die Punkte
     * @return Die Bounding Sphere
     * @see Box#createFrom(Vector3...)
     */
    @NotNull
    public Box createFrom(float minSize, @NotNull Vector3... points) {
        assert minSize > 0;
        assert points.length > 0;

        // Minimal- und Maximalwerte finden
        float xmin = points[0].x;
        float xmax = points[0].x;
        float ymin = points[0].y;
        float ymax = points[0].y;
        float zmin = points[0].z;
        float zmax = points[0].z;

        // Mittelwert der Vektoren bilden, um Position zu finden
        Vector3 mean = Vector3.createNew().set(xmin, ymin, zmin);
        for (int i = 1; i < points.length; ++i) {
            xmax = Math.max(xmax, points[i].x);
            xmin = Math.min(xmin, points[i].x);
            ymax = Math.max(ymax, points[i].y);
            ymin = Math.min(ymin, points[i].y);
            zmax = Math.max(zmax, points[i].z);
            zmin = Math.min(zmin, points[i].z);

            // aufaddieren
            mean.addInPlace(points[i]);
        }
        mean.mulInPlace(1.0f / points.length);

        // Dimensionen berechnen
        float width = Math.abs(xmax - xmin);
        float height = Math.abs(ymax - ymin);
        float depth = Math.abs(zmax - zmin);

        // Position berechnen
        float posX = mean.x;
        float posY = mean.y;
        float posZ = mean.z;
        Vector3.recycle(mean);

        // Voilà
        return new Box(width, height, depth).setPosition(posX, posY, posZ); // TODO: Cache
    }

    /**
     * Bezieht einen gecachten Vektor, der den Minimalpunkt der Box repräsentiert
     *
     * @return Minimaler Punkt (cached)
     */
    @NotNull
    public Vector3 getMinPoint() {
        return Vector3.createNew(_position).subInPlace(_extent);
    }

    /**
     * Bezieht einen gecachten Vektor, der den Maximalpunkt der Box repräsentiert
     *
     * @return Maximaler Punkt (cached)
     */
    @NotNull
    public Vector3 getMaxPoint() {
        return Vector3.createNew(_position).addInPlace(_extent);
    }
}
