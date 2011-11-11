package net.gtamps.shared.Utils.math;

import net.gtamps.shared.Utils.cache.ObjectCache;
import net.gtamps.shared.Utils.cache.ObjectFactory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * 3D-Vektor
 * <p>
 * Neue Objekte werden erzeugt mittels {@link #createNew()}; Nicht länger benötigte Objekte sollten per
 * {@link #recycle(Vector3)} zurückgegeben werden.
 * </p>
 */
public final class Vector3 implements Serializable {

    public static final float DEG = (float) (Math.PI / 180f);

    /**
     * Instanz, die die Verwaltung nicht länger benötigter Instanzen übernimmt
     */
    public static final ObjectCache<Vector3> Recycling = new ObjectCache<Vector3>(new ObjectFactory<Vector3>() {
        @NotNull
        @Override
        public Vector3 createNew() {
            return new Vector3();
        }
    });

    /**
     * Erzeugt eine neue Vektor-Instanz.
     * <p>
     * <strong>Hinweis:</strong> Der Zustand der Vektors kann korrupt sein!
     * </p>
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @param z Z-Koordinate
     * @return Der neue oder aufbereitete Vektor
     * @see #Recycling
     */
    @NotNull
    public static Vector3 createNew(float x, float y, float z) {
        return Recycling.getOrCreate().set(x, y, z);
    }

    /**
     * Kopierkonstruktor. Erzeugt eine neue Vektor-Instanz.
     * <p>
     * <strong>Hinweis:</strong> Der Zustand der Vektors kann korrupt sein!
     * </p>
     *
     * @param other der zu kopierende Vector3
     * @return Der neue oder aufbereitete Vektor
     * @see #Recycling
     */
    public static Vector3 createNew(Vector3 other) {
        return Recycling.getOrCreate().set(other);
    }

    /**
     * Erzeugt eine neue Vektor-Instanz.
     * <p>
     * <strong>Hinweis:</strong> Der Zustand der Vektors kann korrupt sein!
     * </p>
     *
     * @return Der neue oder aufbereitete Vektor
     * @see #Recycling
     */
    @NotNull
    public static Vector3 createNew() {
        return Recycling.getOrCreate();
    }

    /**
     * Registriert einen Vektor für das spätere Recycling
     *
     * @param vector Der zu registrierende Vektor
     * @see #Recycling
     */
    public static void recycle(@NotNull Vector3 vector) {
        Recycling.registerElement(vector);
    }

    /**
     * Registriert diesen Vektor für das spätere Recycling
     *
     * @see #Recycling
     * @see Vector3#recycle(Vector3)
     */
    public void recycle() {
        Recycling.registerElement(this);
    }

    /**
     * Der Nullvektor {0, 0, 0}
     */
    @NotNull
    public static final Vector3 ZERO = new Vector3(0, 0, 0);

    /**
     * Der X-Vektor {1, 0, 0}
     */
    @NotNull
    public static final Vector3 XAXIS = new Vector3(1, 0, 0);

    /**
     * Der Y-Vektor {0, 1, 0}
     */
    @NotNull
    public static final Vector3 YAXIS = new Vector3(0, 1, 0);

    /**
     * Der Z-Vektor {0, 0, 1}
     */
    @NotNull
    public static final Vector3 ZAXIS = new Vector3(0, 0, 1);

    /**
     * X-Komponente des Vektors
     */
    public float x = 0.f;

    /**
     * Y-Komponente des Vektors
     */
    public float y = 0.f;

    /**
     * Z-Komponente des Vektors
     */
    public float z = 0.f;

    final static float EPSILON = 0.0001f;

    /**
     * Setzt alle Werte des Vektors
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @param z Z-Koordinate
     * @return Diese Instanz für Method chaining
     */
    @NotNull
    public Vector3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Setzt alle Werte des Vektors
     *
     * @param position Die Position
     * @return Diese Instanz für Method chaining
     */
    @NotNull
    public Vector3 set(@NotNull final Vector3 position) {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        return this;
    }

    /**
     * Erzeugt einen neuen, leeren Vektor
     */
    private Vector3() {
    }

    /**
     * Erzeugt einen neuen Vektor der Dimensionen {x, y, z}
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @param z Z-Koordinate
     * @see #createNew()
     */
    private Vector3(float x, float y, float z) {
        set(x, y, z);
    }

    /**
     * Ermittelt die Länge des Vektors
     *
     * @return Die Länge des Vektors
     * @see Vector3#getLengthSquared
     */
    public float getLength() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Ermittelt die quadrierte Länge des Vektors
     * Diese Operation ist schneller als getLength()
     *
     * @return Die quadrierte Länge des Vektors
     * @see Vector3#getLength
     */
    public float getLengthSquared() {
        return (float) (x * x + y * y + z * z);
    }

    /**
     * Berechnet die Distanz zwischen zwei Vektoren
     *
     * @param from Der Vektor, zu dem die Distanz berechnet werden soll
     * @return Die Distanz zwischen den Vektoren
     * @see Vector3#getDistanceSquared
     */
    public float getDistance(Vector3 from) {
        float dx = from.x - x;
        float dy = from.y - y;
        float dz = from.z - z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public float getDistance(float x, float y, int z) {
        float dx = x - this.x;
        float dy = y - this.y;
        float dz = z - this.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Berechnet die quadratische Distanz zwischen zwei Vektoren
     * Diese Operation ist schneller als <code>getDistanceSquared()</code>
     *
     * @param from Der Vektor, zu dem die Distanz berechnet werden soll
     * @return Die quadrierte Distanz
     * @see Vector3#getDistance
     */
    public float getDistanceSquared(Vector3 from) {
        float dx = from.x - x;
        float dy = from.y - y;
        float dz = from.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Bezieht eine normalisierte Kopie dieses Vektors
     *
     * @return Der normalisierte Vektor (Kopie!)
     */
    @NotNull
    public Vector3 getNormalized() {
        float invLength = 1.0f / getLength();
        return createNew().set(x * invLength, y * invLength, z * invLength);
    }

    /**
     * Normalisiert diesen Vektor
     */
    public void normalize() {
        float invLength = 1.0f / getLength();
        x *= invLength;
        y *= invLength;
        z *= invLength;
    }

    /**
     * Addiert einen Vektor auf diesen Vektor und liefert das Ergebnis zurück.
     * Dieser Vektor wird nicht modifiziert.
     *
     * @param b Der zu addierende Vektor
     * @return Die Summe (Kopie!)
     * @see Vector3#addInPlace(Vector3)
     */
    public Vector3 add(@NotNull Vector3 b) {
        return createNew().set(x + b.x, y + b.y, z + b.z);
    }

    /**
     * Addiert einen Vektor auf diesen Vektor
     *
     * @param b Der zu addierende Vektor
     * @see Vector3#add(Vector3)
     */
    @NotNull
    public Vector3 addInPlace(@NotNull Vector3 b) {
        x += b.x;
        y += b.y;
        z += b.z;
        return this;
    }

    /**
     * Addiert einen Vektor auf diesen Vektor
     *
     * @param x Der zu addierende X-Wert
     * @param y Der zu addierende Y-Wert
     * @param z Der zu addierende Z-Wert
     * @see Vector3#add(Vector3)
     */
    @NotNull
    public Vector3 addInPlace(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    /**
     * Subtrahiert einen Vektor von diesem Vektor und liefert das Ergebnis zurück.
     * Dieser Vektor wird nicht modifiziert.
     *
     * @param b Der zu subtrahierende Vektor
     * @return Die Differenz (Kopie!)
     * @see Vector3#subInPlace(Vector3)
     */
    public Vector3 sub(@NotNull Vector3 b) {
        return createNew().set(x - b.x, y - b.y, z - b.z);
    }

    /**
     * Subtrahiert einen Vektor von diesem Vektor
     *
     * @param b Der zu subtrahierende Vektor
     * @see Vector3#sub(Vector3)
     */
    @NotNull
    public Vector3 subInPlace(@NotNull Vector3 b) {
        x -= b.x;
        y -= b.y;
        z -= b.z;
        return this;
    }

    /**
     * Skaliert einen Vektor und liefert das Ergebnis zurück.
     * Dieser Vektor wird nicht modifiziert.
     *
     * @param f Skalierungsfaktor
     * @return Der skalierte Vektor (Kopie!)
     * @see Vector3#mulInPlace(float)
     */
    @NotNull
    public Vector3 mul(float f) {
        return createNew().set(x * f, y * f, z * f);
    }

    /**
     * Skaliert einen Vektor
     *
     * @param f Skalierungsfaktor
     * @see Vector3#mul(float)
     */
    @NotNull
    public Vector3 mulInPlace(float f) {
        x *= f;
        y *= f;
        z *= f;
        return this;
    }

    /**
     * Berechnet das Punktprodukt zweier Vektoren
     *
     * @param b Der zweite Vektor
     * @return Punktprodukt der Vektoren
     */
    public float dot(@NotNull Vector3 b) {
        return x * b.x + y * b.y + z * b.z;
    }

    /**
     * Berechnet das Kreuzprodukt zweier Vektoren
     *
     * @param b Der zweite Vektor
     * @return Kreuzprodukt beider Vektoren (Kopie!)
     * @see Vector3#crossInPlace(Vector3)
     */
    @NotNull
    public Vector3 cross(@NotNull Vector3 b) {
        float nx = y * b.z - z * b.y;
        float ny = z * b.x - x * b.z;
        float nz = x * b.y - y * b.x;
        return createNew().set(nx, ny, nz);
    }

    /**
     * Berechnet das Kreuzprodukt zweier Vektoren
     *
     * @param b Der zweite Vektor
     * @see Vector3#cross(Vector3)
     */
    @NotNull
    public Vector3 crossInPlace(@NotNull Vector3 b) {
        float nx = y * b.z - z * b.y;
        float ny = z * b.x - x * b.z;
        z = x * b.y - y * b.x;
        this.y = ny;
        this.x = nx;
        return this;
    }

    /**
     * Stringify
     *
     * @return Der String
     */
    @Override
    public String toString() {
        return "{" + x + "; " + y + "; " + z + "}";
    }

    /**
     * Liefert eine invertierte Kopie dieses Vektors
     *
     * @return Die invertierte Kopie
     */
    @NotNull
    public Vector3 getInverted() {
        return createNew().set(-x, -y, -z);
    }

    /**
     * Invertiert diesen Vektor
     */
    public void invert() {
        x = -x;
        y = -y;
        z = -z;
    }

    /**
     * Multipliziert die Werte des Vectors mit einem bestimmten Faktor, ohne einen neuen Vektor generierne zu müssen
     */
    public void multiplyByFactor(float factor) {
        x *= factor;
        y *= factor;
        z *= factor;
    }


    /**
     * Erzeugt eine identische Kopie
     *
     * @return Die Kopie
     */
    @NotNull
    @Override
    public Vector3 clone() {
        return createNew().set(x, y, z);
    }

    /**
     * Vergleicht dieses Objekt mit einem anderen. TODO: Delta!?
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3)) return false;

        Vector3 vector3 = (Vector3) o;

        if (Math.abs(vector3.x - x) > EPSILON) return false;
        if (Math.abs(vector3.y - y) > EPSILON) return false;
        if (Math.abs(vector3.z - z) > EPSILON) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }

    /**
     * Überprüft, ob der Vektor leer ist
     *
     * @return true, wenn der Vektor leer ist
     */
    public boolean isEmpty() {
        return equals(Vector3.ZERO);
    }

    /**
     * Computes the angle between two vectors.
     * <p/>
     * u * v = |u|*|v|*cos(angel)
     * angle = acos( (u*v) / (|u|*|v|) )
     *
     * @param b
     * @return angle in degrees
     */
    @Deprecated
    public float angleInBetween(Vector3 b) {
//        float angle = (float) Math.acos(dot(b)/((getLength()*b.getLength()))) / DEG;
        return (float) (Math.atan2(x, y) - Math.atan2(b.x, b.y)) / DEG;
    }

    /**
     * Computes the angle between two vectors.
     *
     * @param a
     * @param b
     * @return angle in degrees
     */
    public static float angleIntBetween(Vector3 a, Vector3 b) {
        return a.angleInBetween(b);
    }

    public void rotateX(float angle) {
        float cosRY = (float) Math.cos(angle);
        float sinRY = (float) Math.sin(angle);

        Vector3 temp = Vector3.createNew(x, y, z);

        y = (temp.y * cosRY) - (temp.z * sinRY);
        z = (temp.y * sinRY) + (temp.z * cosRY);

        temp.recycle();
    }

    public void rotateY(float angle) {
        float cosRY = (float) Math.cos(angle);
        float sinRY = (float) Math.sin(angle);

        Vector3 temp = Vector3.createNew(x, y, z);

        x = (temp.x * cosRY) + (temp.z * sinRY);
        z = (temp.x * -sinRY) + (temp.z * cosRY);

        temp.recycle();
    }

    public void rotateZ(float angle) {
        float cosRY = (float) Math.cos(angle);
        float sinRY = (float) Math.sin(angle);

        Vector3 temp = Vector3.createNew(x, y, z);

        x = (temp.x * cosRY) - (temp.y * sinRY);
        y = (temp.x * sinRY) + (temp.y * cosRY);

        temp.recycle();
    }
}
