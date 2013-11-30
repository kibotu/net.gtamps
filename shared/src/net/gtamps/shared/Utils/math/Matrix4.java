package net.gtamps.shared.Utils.math;

import net.gtamps.shared.Utils.cache.IObjectCache;
import net.gtamps.shared.Utils.cache.ObjectFactory;
import net.gtamps.shared.Utils.cache.ThreadLocalObjectCache;
import net.gtamps.shared.Utils.cache.annotations.ReturnsCachedValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.lang.Math.sqrt;

/**
 * 4D-Matrix.
 * <p>
 * Neue Objekte werden erzeugt mittels {@link #createNew()}; Nicht länger benötigte Objekte sollten per
 * {@link #recycle(Matrix4)} zurückgegeben werden.
 * </p>
 *
 * @see MatrixFactory
 */
public final class Matrix4 {

    /**
     * Instanz, die die Verwaltung nicht länger benötigter Instanzen übernimmt
     */
    public static final IObjectCache<Matrix4> Cache = new ThreadLocalObjectCache<Matrix4>(new ObjectFactory<Matrix4>() {
        @NotNull
        @Override
        public Matrix4 createNew() {
            return new Matrix4();
        }
    });

    public static final int SIZE = 16;

    /**
     * Erzeugt eine neue Matrix-Instanz und initialisiert sie auf die Einheitsmatrix
     *
     * @return Die neue oder aufbereitete Matrix
     * @see #Cache
     * @see #createNew(boolean)
     * @see #createNew(float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float)
     */
    @NotNull
    public static Matrix4 createNew() {
        return Cache.getOrCreate().toUnit();
    }

    /**
     * Erzeugt eine neue Matrix-Instanz.
     *
     * @param makeUnit Gibt an, ob eine Einheitsmatrix erstellt werden soll. Ist dieser Wert false, bleibt die Matrix uninitialisiert.
     * @return Die neue oder aufbereitete Matrix
     * @see #Cache
     * @see #createNew()
     * @see #createNew(float, float, float, float, float, float, float, float, float, float, float, float, float, float, float, float)
     */
    @NotNull
    public static Matrix4 createNew(boolean makeUnit) {
        return makeUnit ? Cache.getOrCreate().toUnit() : Cache.getOrCreate();
    }

    /**
     * Erzeugt eine neue Matrix-Instanz.
     * <p>
     * <strong>Hinweis:</strong> Der Zustand der Matrix kann korrupt sein!
     * </p>
     *
     * @param m11 Zeile 1, Spalte 1
     * @param m12 Zeile 1, Spalte 2
     * @param m13 Zeile 1, Spalte 3
     * @param m14 Zeile 1, Spalte 4
     * @param m21 Zeile 2, Spalte 1
     * @param m22 Zeile 2, Spalte 2
     * @param m23 Zeile 2, Spalte 3
     * @param m24 Zeile 2, Spalte 4
     * @param m31 Zeile 3, Spalte 1
     * @param m32 Zeile 3, Spalte 2
     * @param m33 Zeile 3, Spalte 3
     * @param m34 Zeile 3, Spalte 4
     * @param m41 Zeile 4, Spalte 1
     * @param m42 Zeile 4, Spalte 2
     * @param m43 Zeile 4, Spalte 3
     * @param m44 Zeile 4, Spalte 4
     * @return Die neue oder aufbereitete Matrix
     * @see #Cache
     */
    @NotNull
    public static Matrix4 createNew(float m11, float m12, float m13, float m14,
                                    float m21, float m22, float m23, float m24,
                                    float m31, float m32, float m33, float m34,
                                    float m41, float m42, float m43, float m44) {
        return Cache.getOrCreate().set(
                m11, m12, m13, m14,
                m21, m22, m23, m24,
                m31, m32, m33, m34,
                m41, m42, m43, m44);
    }

    /**
     * Registriert eine Matrix für das spätere Cache
     *
     * @param matrix Die zu registrierende Matrix
     * @see #Cache
     * @see Matrix4#recycle()
     */
    public static void recycle(@NotNull Matrix4 matrix) {
        Cache.registerElement(matrix);
    }

    /**
     * Registriert diese Matrix für das spätere Cache
     *
     * @see #Cache
     * @see Matrix4#recycle(Matrix4)
     */
    public void recycle() {
        Cache.registerElement(this);
    }

    /**
     * Die Elemente
     */
    @NotNull
    public final float[] values = new float[16];

    /**
     * Die Einheitsmatrix
     */
    @NotNull
    public static final Matrix4 UNIT = new Matrix4(
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f);

    /**
     * Magische Matrix
     */
    @NotNull
    public static final Matrix4 MAGIC = new Matrix4(
            16, 2, 3, 13,
            5, 11, 10, 8,
            9, 7, 6, 12,
            4, 14, 15, 1);

    /**
     * Zeile 1, Spalte 1
     */
    public static final int M11 = 0;
    /**
     * Zeile 1, Spalte 2
     */
    public static final int M12 = 1;
    /**
     * Zeile 1, Spalte 3
     */
    public static final int M13 = 2;
    /**
     * Zeile 1, Spalte 4
     */
    public static final int M14 = 3;

    /**
     * Zeile 2, Spalte 1
     */
    public static final int M21 = 4;
    /**
     * Zeile 2, Spalte 2
     */
    public static final int M22 = 5;
    /**
     * Zeile 2, Spalte 3
     */
    public static final int M23 = 6;
    /**
     * Zeile 2, Spalte 4
     */
    public static final int M24 = 7;

    /**
     * Zeile 3, Spalte 1
     */
    public static final int M31 = 8;
    /**
     * Zeile 3, Spalte 2
     */
    public static final int M32 = 9;
    /**
     * Zeile 3, Spalte 3
     */
    public static final int M33 = 10;
    /**
     * Zeile 3, Spalte 4
     */
    public static final int M34 = 11;

    /**
     * Zeile 4, Spalte 1
     */
    public static final int M41 = 12;
    /**
     * Zeile 4, Spalte 2
     */
    public static final int M42 = 13;
    /**
     * Zeile 4, Spalte 3
     */
    public static final int M43 = 14;
    /**
     * Zeile 4, Spalte 4
     */
    public static final int M44 = 15;

    /**
     * Erzeugt eine neue, leere Matrix
     */
    private Matrix4() {
    }

    /**
     * Errzeugt eine neue Matrix aus ihren Komponenten
     *
     * @param m11 Zeile 1, Spalte 1
     * @param m12 Zeile 1, Spalte 2
     * @param m13 Zeile 1, Spalte 3
     * @param m14 Zeile 1, Spalte 4
     * @param m21 Zeile 2, Spalte 1
     * @param m22 Zeile 2, Spalte 2
     * @param m23 Zeile 2, Spalte 3
     * @param m24 Zeile 2, Spalte 4
     * @param m31 Zeile 3, Spalte 1
     * @param m32 Zeile 3, Spalte 2
     * @param m33 Zeile 3, Spalte 3
     * @param m34 Zeile 3, Spalte 4
     * @param m41 Zeile 4, Spalte 1
     * @param m42 Zeile 4, Spalte 2
     * @param m43 Zeile 4, Spalte 3
     * @param m44 Zeile 4, Spalte 4
     */
    public Matrix4(float m11, float m12, float m13, float m14,
                   float m21, float m22, float m23, float m24,
                   float m31, float m32, float m33, float m34,
                   float m41, float m42, float m43, float m44
    ) {
        // Array füllen
        values[0] = m11;
        values[1] = m12;
        values[2] = m13;
        values[3] = m14;
        values[4] = m21;
        values[5] = m22;
        values[6] = m23;
        values[7] = m24;
        values[8] = m31;
        values[9] = m32;
        values[10] = m33;
        values[11] = m34;
        values[12] = m41;
        values[13] = m42;
        values[14] = m43;
        values[15] = m44;
    }

    /**
     * Bezieht das Element am angegebenen Feldindex
     *
     * @param index Der Feldindex (0..14)
     * @return Der Wert
     */
    public final float getAt(int index) {
        assert index >= 0 && index <= 15;
        return values[index];
    }

    /**
     * Bezieht den Wert an der angegebenen Zeile und SPalte
     *
     * @param row    Die Zeile (nullbasiert!)
     * @param column Die Spalte (nullbasiert!)
     * @return Der Wert
     */
    public final float getAt(int row, int column) {
        assert row >= 0 && row < 4 && column >= 0 && column < 4;
        return values[row * 4 + column];
    }

    /**
     * Setzt den Wert an der angegeben Position
     *
     * @param index Der Feldindex (0..15)
     * @param value Der Wert
     */
    public final void setAt(int index, float value) {
        assert index >= 0 && index <= 15;
        values[index] = value;
    }

    /**
     * Setzt den Wert an der angegeben Zeile und Spalte
     *
     * @param row    Die Zeile (nullbasiert!)
     * @param column Die Spalte (nullbasiert!)
     * @param value  Der Wert
     */
    public final void setAt(int row, int column, float value) {
        assert row >= 0 && row < 4 && column >= 0 && column < 4;
        values[row * 4 + column] = value;
    }

    /**
     * Setzt die Matrix aus ihren Einzelkomponenten
     *
     * @param m11 Zeile 1, Spalte 1
     * @param m12 Zeile 1, Spalte 2
     * @param m13 Zeile 1, Spalte 3
     * @param m14 Zeile 1, Spalte 4
     * @param m21 Zeile 2, Spalte 1
     * @param m22 Zeile 2, Spalte 2
     * @param m23 Zeile 2, Spalte 3
     * @param m24 Zeile 2, Spalte 4
     * @param m31 Zeile 3, Spalte 1
     * @param m32 Zeile 3, Spalte 2
     * @param m33 Zeile 3, Spalte 3
     * @param m34 Zeile 3, Spalte 4
     * @param m41 Zeile 4, Spalte 1
     * @param m42 Zeile 4, Spalte 2
     * @param m43 Zeile 4, Spalte 3
     * @param m44 Zeile 4, Spalte 4
     * @return Dieselbe Instanz für method chaining
     */
    public final Matrix4 set(float m11, float m12, float m13, float m14,
                             float m21, float m22, float m23, float m24,
                             float m31, float m32, float m33, float m34,
                             float m41, float m42, float m43, float m44
    ) {
        values[0] = m11;
        values[1] = m12;
        values[2] = m13;
        values[3] = m14;
        values[4] = m21;
        values[5] = m22;
        values[6] = m23;
        values[7] = m24;
        values[8] = m31;
        values[9] = m32;
        values[10] = m33;
        values[11] = m34;
        values[12] = m41;
        values[13] = m42;
        values[14] = m43;
        values[15] = m44;

        return this;
    }

    /**
     * Setzt die Matrix aus ihren Einzelkomponenten
     *
     * @param other Die Matrix, deren Werte übernommen werden sollen
     * @return Dieselbe Instanz für method chaining
     */
    public final Matrix4 set(@NotNull Matrix4 other) {
        values[0] = other.values[0];
        values[1] = other.values[1];
        values[2] = other.values[2];
        values[3] = other.values[3];
        values[4] = other.values[4];
        values[5] = other.values[5];
        values[6] = other.values[6];
        values[7] = other.values[7];
        values[8] = other.values[8];
        values[9] = other.values[9];
        values[10] = other.values[10];
        values[11] = other.values[11];
        values[12] = other.values[12];
        values[13] = other.values[13];
        values[14] = other.values[14];
        values[15] = other.values[15];

        return this;
    }

    /**
     * Wandelt die Matrix in die Einheitsmatrix um. (Alias für {@link #toUnit()}
     *
     * @return Dieselbe Instanz für method chaining
     */
    public final Matrix4 toIdentity() {
        return toUnit();
    }

    /**
     * Wandelt die Matrix in die Einheitsmatrix um
     *
     * @return Dieselbe Instanz für method chaining
     */
    public final Matrix4 toUnit() {
        values[0] = 1.0f;
        values[1] = 0.0f;
        values[2] = 0.0f;
        values[3] = 0.0f;
        values[4] = 0.0f;
        values[5] = 1.0f;
        values[6] = 0.0f;
        values[7] = 0.0f;
        values[8] = 0.0f;
        values[9] = 0.0f;
        values[10] = 1.0f;
        values[11] = 0.0f;
        values[12] = 0.0f;
        values[13] = 0.0f;
        values[14] = 0.0f;
        values[15] = 1.0f;
        return this;
    }

    /**
     * Multipliziert die Matrix mit einem Faktor und liefert das Ergebnis
     *
     * @param f Der Faktor
     * @return Das Ergebnis (Kopie!)
     * @see Matrix4#mulInPlace(float)
     */
    @NotNull
    @ReturnsCachedValue
    public final Matrix4 mul(final float f) {
        return createNew().set(f * values[0], f * values[1], f * values[2], f * values[3], f * values[4], f * values[5], f * values[6], f * values[7], f * values[8], f * values[9], f * values[10], f * values[11], f * values[12], f * values[13], f * values[14], f * values[15]);
    }

    /**
     * Multipliziert die Matrix mit einem Faktor
     *
     * @param f Der Faktor
     * @see Matrix4#mul(float)
     */
    @NotNull
    public final Matrix4 mulInPlace(float f) {
        values[0] *= f;
        values[1] *= f;
        values[2] *= f;
        values[3] *= f;
        values[4] *= f;
        values[5] *= f;
        values[6] *= f;
        values[7] *= f;
        values[8] *= f;
        values[9] *= f;
        values[10] *= f;
        values[11] *= f;
        values[12] *= f;
        values[13] *= f;
        values[14] *= f;
        values[15] *= f;
        return this;
    }

    /**
     * Multipliziert eine Matrix mit einer zweiten
     *
     * @param b Die zweite Matrix
     * @return Das Ergebnis
     * @see #mulInPlace(Matrix4)
     */
    @NotNull
    @ReturnsCachedValue
    public final Matrix4 mul(@NotNull final Matrix4 b) {
        return createNew().set(

                values[M11] * b.values[M11] + values[M12] * b.values[M21] + values[M13] * b.values[M31] + values[M14] * b.values[M41],
                values[M11] * b.values[M12] + values[M12] * b.values[M22] + values[M13] * b.values[M32] + values[M14] * b.values[M42],
                values[M11] * b.values[M13] + values[M12] * b.values[M23] + values[M13] * b.values[M33] + values[M14] * b.values[M43],
                values[M11] * b.values[M14] + values[M12] * b.values[M24] + values[M13] * b.values[M34] + values[M14] * b.values[M44],

                values[M21] * b.values[M11] + values[M22] * b.values[M21] + values[M23] * b.values[M31] + values[M24] * b.values[M41],
                values[M21] * b.values[M12] + values[M22] * b.values[M22] + values[M23] * b.values[M32] + values[M24] * b.values[M42],
                values[M21] * b.values[M13] + values[M22] * b.values[M23] + values[M23] * b.values[M33] + values[M24] * b.values[M43],
                values[M21] * b.values[M14] + values[M22] * b.values[M24] + values[M23] * b.values[M34] + values[M24] * b.values[M44],

                values[M31] * b.values[M11] + values[M32] * b.values[M21] + values[M33] * b.values[M31] + values[M34] * b.values[M41],
                values[M31] * b.values[M12] + values[M32] * b.values[M22] + values[M33] * b.values[M32] + values[M34] * b.values[M42],
                values[M31] * b.values[M13] + values[M32] * b.values[M23] + values[M33] * b.values[M33] + values[M34] * b.values[M43],
                values[M31] * b.values[M14] + values[M32] * b.values[M24] + values[M33] * b.values[M34] + values[M34] * b.values[M44],

                values[M41] * b.values[M11] + values[M42] * b.values[M21] + values[M43] * b.values[M31] + values[M44] * b.values[M41],
                values[M41] * b.values[M12] + values[M42] * b.values[M22] + values[M43] * b.values[M32] + values[M44] * b.values[M42],
                values[M41] * b.values[M13] + values[M42] * b.values[M23] + values[M43] * b.values[M33] + values[M44] * b.values[M43],
                values[M41] * b.values[M14] + values[M42] * b.values[M24] + values[M43] * b.values[M34] + values[M44] * b.values[M44]);
    }

    /**
     * Multipliziert diese Matrix mit einer zweiten
     *
     * @param b Die zweite Matrix
     * @return Diese Instanz für Method Chaining
     * @see #mul(Matrix4)
     */
    @NotNull
    @ReturnsCachedValue
    public final Matrix4 mulInPlace(@NotNull final Matrix4 b) {
        return set(

                values[M11] * b.values[M11] + values[M12] * b.values[M21] + values[M13] * b.values[M31] + values[M14] * b.values[M41],
                values[M11] * b.values[M12] + values[M12] * b.values[M22] + values[M13] * b.values[M32] + values[M14] * b.values[M42],
                values[M11] * b.values[M13] + values[M12] * b.values[M23] + values[M13] * b.values[M33] + values[M14] * b.values[M43],
                values[M11] * b.values[M14] + values[M12] * b.values[M24] + values[M13] * b.values[M34] + values[M14] * b.values[M44],

                values[M21] * b.values[M11] + values[M22] * b.values[M21] + values[M23] * b.values[M31] + values[M24] * b.values[M41],
                values[M21] * b.values[M12] + values[M22] * b.values[M22] + values[M23] * b.values[M32] + values[M24] * b.values[M42],
                values[M21] * b.values[M13] + values[M22] * b.values[M23] + values[M23] * b.values[M33] + values[M24] * b.values[M43],
                values[M21] * b.values[M14] + values[M22] * b.values[M24] + values[M23] * b.values[M34] + values[M24] * b.values[M44],

                values[M31] * b.values[M11] + values[M32] * b.values[M21] + values[M33] * b.values[M31] + values[M34] * b.values[M41],
                values[M31] * b.values[M12] + values[M32] * b.values[M22] + values[M33] * b.values[M32] + values[M34] * b.values[M42],
                values[M31] * b.values[M13] + values[M32] * b.values[M23] + values[M33] * b.values[M33] + values[M34] * b.values[M43],
                values[M31] * b.values[M14] + values[M32] * b.values[M24] + values[M33] * b.values[M34] + values[M34] * b.values[M44],

                values[M41] * b.values[M11] + values[M42] * b.values[M21] + values[M43] * b.values[M31] + values[M44] * b.values[M41],
                values[M41] * b.values[M12] + values[M42] * b.values[M22] + values[M43] * b.values[M32] + values[M44] * b.values[M42],
                values[M41] * b.values[M13] + values[M42] * b.values[M23] + values[M43] * b.values[M33] + values[M44] * b.values[M43],
                values[M41] * b.values[M14] + values[M42] * b.values[M24] + values[M43] * b.values[M34] + values[M44] * b.values[M44]);
    }

    /**
     * Erzeugt eine Kopie dieser Matrix
     *
     * @return Die Kopie
     */
    @NotNull
    @ReturnsCachedValue
    public final Matrix4 clone() {
        return createNew().set(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], values[12], values[13], values[14], values[15]);
    }

    /**
     * Bezieht die Determinante der Matrix
     *
     * @return Die Determinante
     * @see Matrix4#getSubDeterminant(int, int)
     */
    public final float getDeterminant() {
        /*
          return
              Cell[0, 0]*GetSubDeterminant(0, 0) -
              Cell[0, 1]*GetSubDeterminant(0, 1) +
              Cell[0, 2]*GetSubDeterminant(0, 2) -
              Cell[0, 3]*GetSubDeterminant(0, 3);
          */

        //double value1 = Cell[0, 0]*(Cell[1, 1]*Cell[2, 2]*Cell[3, 3] +
        //                            Cell[1, 2]*Cell[2, 3]*Cell[3, 1] +
        //                            Cell[1, 3]*Cell[2, 1]*Cell[3, 2] -
        //                            Cell[1, 3]*Cell[2, 2]*Cell[3, 1] -
        //                            Cell[1, 1]*Cell[2, 3]*Cell[3, 2] -
        //                            Cell[1, 2]*Cell[2, 1]*Cell[3, 3]);

        //double value2 = Cell[0, 1]*(Cell[1, 0]*Cell[2, 2]*Cell[3, 3] +
        //                            Cell[1, 2]*Cell[2, 3]*Cell[3, 0] +
        //                            Cell[1, 3]*Cell[2, 0]*Cell[3, 2] -
        //                            Cell[1, 3]*Cell[2, 2]*Cell[3, 0] -
        //                            Cell[1, 0]*Cell[2, 3]*Cell[3, 2] -
        //                            Cell[1, 2]*Cell[2, 0]*Cell[3, 3]);

        //double value3 = Cell[0, 2]*(Cell[1, 0]*Cell[2, 1]*Cell[3, 3] +
        //                            Cell[1, 1]*Cell[2, 3]*Cell[3, 0] +
        //                            Cell[1, 3]*Cell[2, 0]*Cell[3, 1] -
        //                            Cell[1, 3]*Cell[2, 1]*Cell[3, 0] -
        //                            Cell[1, 0]*Cell[2, 3]*Cell[3, 1] -
        //                            Cell[1, 1]*Cell[2, 0]*Cell[3, 3]);

        //double value4 = Cell[0, 3]*(Cell[1, 0]*Cell[2, 1]*Cell[3, 2] +
        //                            Cell[1, 1]*Cell[2, 2]*Cell[3, 0] +
        //                            Cell[1, 2]*Cell[2, 0]*Cell[3, 1] -
        //                            Cell[1, 2]*Cell[2, 1]*Cell[3, 0] -
        //                            Cell[1, 0]*Cell[2, 2]*Cell[3, 1] -
        //                            Cell[1, 1]*Cell[2, 0]*Cell[3, 2]);

        float c1122 = getAt(1, 1) * getAt(2, 2);
        float c1223 = getAt(1, 2) * getAt(2, 3);
        float c1233 = getAt(1, 2) * getAt(3, 3);
        float c1332 = getAt(1, 3) * getAt(3, 2);
        float c1322 = getAt(1, 3) * getAt(2, 2);
        float c1123 = getAt(1, 1) * getAt(2, 3);
        float c1021 = getAt(1, 0) * getAt(2, 1);
        float c1022 = getAt(1, 0) * getAt(2, 2);
        float c1023 = getAt(1, 0) * getAt(2, 3);
        float c1120 = getAt(1, 1) * getAt(2, 0);
        float c2031 = getAt(2, 0) * getAt(3, 1);
        float c2130 = getAt(2, 1) * getAt(3, 0);

        float value1 = getAt(0, 0) * (c1122 * getAt(3, 3) +
                c1223 * getAt(3, 1) +
                c1332 * getAt(2, 1) -
                c1322 * getAt(3, 1) -
                c1123 * getAt(3, 2) -
                c1233 * getAt(2, 1));

        float value2 = getAt(0, 1) * (c1022 * getAt(3, 3) +
                c1223 * getAt(3, 0) +
                c1332 * getAt(2, 0) -
                c1322 * getAt(3, 0) -
                c1023 * getAt(3, 2) -
                c1233 * getAt(2, 0));

        float value3 = getAt(0, 2) * (c1021 * getAt(3, 3) +
                c1123 * getAt(3, 0) +
                c2031 * getAt(1, 3) -
                c2130 * getAt(1, 3) -
                c1023 * getAt(3, 1) -
                c1120 * getAt(3, 3));

        float value4 = getAt(0, 3) * (c1021 * getAt(3, 2) +
                c1122 * getAt(3, 0) +
                c2031 * getAt(1, 2) -
                c2130 * getAt(1, 2) -
                c1022 * getAt(3, 1) -
                c1120 * getAt(3, 2));

        return value1 - value2 + value3 - value4;
    }

    /**
     * Bezieht die 3x3 Unterdeterminante der Matrix durch Ignorieren bestimmter Zeilen und Spalten
     *
     * @param row    Die zu überspringende Zeile
     * @param column Die zu überspringende Spalte
     * @return Die Unterdeterminante
     * @see Matrix4#getDeterminant()
     */
    public final float getSubDeterminant(int row, int column) {
        assert row >= 0 && row < 4;
        assert column >= 0 && column < 4;

        // get target row indices
        int row0 = 0;
        int row1 = 1;
        int row2 = 2;

        // get target column indices
        int col0 = 0;
        int col1 = 1;
        int col2 = 2;

        // adjust for skipped rows
        if (row == 0) {
            ++row0;
            ++row1;
            ++row2;
        } else if (row == 1) {
            ++row1;
            ++row2;
        } else if (row == 2) {
            ++row2;
        }

        // adjust for skipped columns
        if (column == 0) {
            ++col0;
            ++col1;
            ++col2;
        } else if (column == 1) {
            ++col1;
            ++col2;
        } else if (column == 2) {
            ++col2;
        }

        float r0c0 = getAt(row0, col0); // TODO: Order by row or column access when it is clear what is needed!
        float r1c1 = getAt(row1, col1);
        float r0c1 = getAt(row0, col1);
        float r1c0 = getAt(row1, col0);
        float r2c2 = getAt(row2, col2);
        float r2c1 = getAt(row2, col1);
        float r0c2 = getAt(row0, col2);
        float r1c2 = getAt(row1, col2);
        float r2c0 = getAt(row2, col0);

        // Regel von Sarrus
        // gem. Mathematische Formelsammlung, 9. Auflage, Papula, S. 201
        return r0c0 * r1c1 * r2c2 +
                r0c1 * r1c2 * r2c0 +
                r0c2 * r1c0 * r2c1 -
                r0c2 * r1c1 * r2c0 -
                r0c0 * r1c2 * r2c1 -
                r0c1 * r1c0 * r2c2;
    }

    /**
     * Bezieht die Adjunkte
     *
     * @return Die Adjunkte
     */
    @NotNull
    @ReturnsCachedValue
    public final Matrix4 getAdjoint() {
        /*
          return new Matrix4D(
              +GetSubDeterminant(0, 0), -GetSubDeterminant(0, 1), +GetSubDeterminant(0, 2), -GetSubDeterminant(0, 3),
              -GetSubDeterminant(1, 0), +GetSubDeterminant(1, 1), -GetSubDeterminant(1, 2), +GetSubDeterminant(1, 3),
              +GetSubDeterminant(2, 0), -GetSubDeterminant(2, 1), +GetSubDeterminant(2, 2), -GetSubDeterminant(2, 3),
              -GetSubDeterminant(3, 0), +GetSubDeterminant(3, 1), -GetSubDeterminant(3, 2), +GetSubDeterminant(3, 3)).GetTransposed();
          */

        float c00 = getAt(0, 0);
        float c01 = getAt(0, 1);
        float c02 = getAt(0, 2);
        float c03 = getAt(0, 3);

        float c10 = getAt(1, 0);
        float c11 = getAt(1, 1);
        float c12 = getAt(1, 2);
        float c13 = getAt(1, 3);

        float c20 = getAt(2, 0);
        float c21 = getAt(2, 1);
        float c22 = getAt(2, 2);
        float c23 = getAt(2, 3);

        float c30 = getAt(3, 0);
        float c31 = getAt(3, 1);
        float c32 = getAt(3, 2);
        float c33 = getAt(3, 3);

        // Erster Block
        float c1122 = c11 * c22;
        float c1223 = c12 * c23;
        float c1321 = c13 * c21;
        float c1322 = c13 * c22;
        float c1123 = c11 * c23;
        float c1221 = c12 * c21;

        // Determinant row 0, cell 0
        float m11 = c1122 * c33 +
                c1223 * c31 +
                c1321 * c32 -
                c1322 * c31 -
                c1123 * c32 -
                c1221 * c33;

        // Zweiter Block
        float c2233 = c22 * c33;
        float c1320 = c13 * c20;
        float c2332 = c23 * c32;
        float c1220 = c12 * c20;

        // Determinant row 0, cell 1
        float m12 = c2233 * c10 +
                c1223 * c30 +
                c1320 * c32 -
                c1322 * c30 -
                c2332 * c10 -
                c1220 * c33;

        // Dritter Block
        float c2133 = c21 * c33;
        float c2331 = c23 * c31;
        float c2033 = c20 * c33;

        // Determinant row 0, cell 2
        float m13 = c2133 * c10 +
                c1123 * c30 +
                c1320 * c31 -
                c1321 * c30 -
                c2331 * c10 -
                c2033 * c11;

        // Vierter Block
        float c2231 = c22 * c31;
        float c2032 = c20 * c32;
        float c2132 = c21 * c32;

        // Determinant row 0, cell 3
        float m14 = c2132 * c10 +
                c1122 * c30 +
                c1220 * c31 -
                c1221 * c30 -
                c2231 * c10 -
                c2032 * c11;

        // Determinant row 1, cell 0
        float m21 = c2233 * c01 +
                c2331 * c02 +
                c2132 * c03 -
                c2231 * c03 -
                c2332 * c01 -
                c2133 * c02;

        // Fünfter Block
        float c1233 = c12 * c33;
        float c1133 = c11 * c33;
        float c1332 = c13 * c32;
        float c1231 = c12 * c31;
        float c1132 = c11 * c32;
        float c1331 = c13 * c31;

        // Determinant row 2, cell 0
        float m31 = c1233 * c01 +
                c1331 * c02 +
                c1132 * c03 -
                c1231 * c03 -
                c1332 * c01 -
                c1133 * c02;

        // Sechster Block
        float c1032 = c10 * c32;
        float c1330 = c13 * c30;
        float c1230 = c12 * c30;
        float c1033 = c10 * c33;

        // Determinant row 2, cell 1
        float m32 = c1233 * c00 +
                c1330 * c02 +
                c1032 * c03 -
                c1230 * c03 -
                c1332 * c00 -
                c1033 * c02;

        // Siebter Block
        float c1130 = c11 * c30;
        float c1031 = c10 * c31;

        // Determinant row 2, cell 2
        float m33 = c1133 * c00 +
                c1330 * c01 +
                c1031 * c03 -
                c1130 * c03 -
                c1331 * c00 -
                c1033 * c01;

        // Determinant row 2, cell 3
        float m34 = c1132 * c00 +
                c1230 * c01 +
                c1031 * c02 -
                c1130 * c02 -
                c1231 * c00 -
                c1032 * c01;

        // Achter Block
        float c1022 = c10 * c22;
        float c1023 = c10 * c23;

        // Determinant row 3, cell 1
        float m42 = c1223 * c00 +
                c1320 * c02 +
                c1022 * c03 -
                c1220 * c03 -
                c1322 * c00 -
                c1023 * c02;

        // Neunter Block
        float m43, m44;
        {
            float c1021 = c10 * c21;
            float c1120 = c11 * c20;

            // Determinant row 3, cell 2
            m43 = c1123 * c00 +
                    c1320 * c01 +
                    c1021 * c03 -
                    c1120 * c03 -
                    c1321 * c00 -
                    c1023 * c01;

            // Determinant row 3, cell 3
            m44 = c1122 * c00 +
                    c1220 * c01 +
                    c1021 * c02 -
                    c1120 * c02 -
                    c1221 * c00 -
                    c1022 * c01;
        }

        // Zehnter Block
        float c2230 = c22 * c30;
        float c2330 = c23 * c30;

        // Determinant row 1, cell 1
        float m22 = c2233 * c00 +
                c2330 * c02 +
                c2032 * c03 -
                c2230 * c03 -
                c2332 * c00 -
                c2033 * c02;

        // Elfter Block
        float m24, m23;
        {
            float c2031 = c20 * c31;
            float c2130 = c21 * c30;

            // Determinant row 1, cell 2
            m23 = c2133 * c00 +
                    c2330 * c01 +
                    c2031 * c03 -
                    c2130 * c03 -
                    c2331 * c00 -
                    c2033 * c01;

            // Determinant row 1, cell 3
            m24 = c2132 * c00 +
                    c2230 * c01 +
                    c2031 * c02 -
                    c2130 * c02 -
                    c2231 * c00 -
                    c2032 * c01;
        }

        // Determinant row 3, cell 0
        float m41 = c1223 * c01 +
                c1321 * c02 +
                c1122 * c03 -
                c1221 * c03 -
                c1322 * c01 -
                c1123 * c02;

        //return new Matrix4D(
        //    +m11, -m12, +m13, -m14,
        //    -m21, +m22, -m23, +m24,
        //    +m31, -m32, +m33, -m34,
        //    -m41, +m42, -m43, +m44).GetTransposed();

        // Directly transpose the matrix by swapping the field indices
        return createNew().set(
                +m11, -m21, +m31, -m41,
                -m12, +m22, -m32, +m42,
                +m13, -m23, +m33, -m43,
                -m14, +m24, -m34, +m44);
    }

    /**
     * Invertiert die Matrix
     *
     * @return Die invertierte Matrix
     * @throws MatrixException Matrix ist nicht invertierbar
     * @see Matrix4#getInvertedNoThrow()
     */
    @NotNull
    public final Matrix4 getInverted() throws MatrixException {
        float invDeterminant = 1.0f / getDeterminant();
        if (Float.isInfinite(invDeterminant)) throw new MatrixException("Matrix cannot be inverted.");
        return getAdjoint().mul(invDeterminant);
    }

    /**
     * Invertiert die Matrix
     *
     * @return Die invertierte Matrix oder <code>null</code>, wenn die Matrix nicht invertierbar ist
     * @see Matrix4#getInverted()
     */
    @Nullable
    @ReturnsCachedValue
    public final Matrix4 getInvertedNoThrow() {
        float invDeterminant = 1.0f / getDeterminant();
        if (Float.isInfinite(invDeterminant)) return null;
        return getAdjoint().mulInPlace(invDeterminant);
    }

    /**
     * Liefert eine transponierte Kopie dieser Matrix
     *
     * @return Die transponierte Kopie
     */
    @NotNull
    @ReturnsCachedValue
    public Matrix4 getTransposed() {
        return Matrix4.createNew(
                values[M11], values[M21], values[M31], values[M41],
                values[M12], values[M22], values[M32], values[M42],
                values[M13], values[M23], values[M33], values[M43],
                values[M14], values[M24], values[M34], values[M44]
        );
    }

    /**
     * Transponiert diese Matrix
     *
     * @return Dieselbe Instanz für Method chaining
     */
    @NotNull
    public Matrix4 transposeInPlace() {
        set(values[M11], values[M21], values[M31], values[M41],
                values[M12], values[M22], values[M32], values[M42],
                values[M13], values[M23], values[M33], values[M43],
                values[M14], values[M24], values[M34], values[M44]);
        return this;
    }

    /**
     * Wandelt die Matrix in eine Translationsmatrix um
     * <h2>Form der Matrix</h2>
     * <pre>
     * +---------+---+
     * | 1  0  0 | 0 |
     * | 0  1  0 | 0 |
     * | 0  0  1 | 0 |
     * +---------+---+
     * |Tx Ty Tz | 1 |
     * +---------+---+
     * </pre>
     *
     * @param translation Der Translationsvektor
     * @see Matrix4#toTranslation(float, float, float)
     */
    public final void toTranslation(@NotNull final Vector3 translation) {
        values[M11] = 1f;
        values[M12] = 0f;
        values[M13] = 0f;
        values[M14] = 0f;
        values[M21] = 0f;
        values[M22] = 1f;
        values[M23] = 0f;
        values[M24] = 0f;
        values[M31] = 0f;
        values[M32] = 0f;
        values[M33] = 1f;
        values[M34] = 0f;
        values[M41] = translation.x;
        values[M42] = translation.y;
        values[M43] = translation.z;
        values[M44] = 1f;
    }

    /**
     * Wandelt die Matrix in eine Translationsmatrix um
     * *
     * <h2>Form der Matrix</h2>
     * <pre>
     * +---------+---+
     * | 1  0  0 | 0 |
     * | 0  1  0 | 0 |
     * | 0  0  1 | 0 |
     * +---------+---+
     * | x  y  z | 1 |
     * +---------+---+
     * </pre>
     *
     * @param x Die X-Komponente des Translationsvektors
     * @param y Die Y-Komponente des Translationsvektors
     * @param z Die Z-Komponente des Translationsvektors
     * @see Matrix4#toTranslation(Vector3)
     */
    public final void toTranslation(float x, float y, float z) {
        values[M11] = 1f;
        values[M12] = 0f;
        values[M13] = 0f;
        values[M14] = 0f;
        values[M21] = 0f;
        values[M22] = 1f;
        values[M23] = 0f;
        values[M24] = 0f;
        values[M31] = 0f;
        values[M32] = 0f;
        values[M33] = 1f;
        values[M34] = 0f;
        values[M41] = x;
        values[M42] = y;
        values[M43] = z;
        values[M44] = 1f;
    }

    /**
     * Wandelt die Matrix in eine Skalierungsmatrix um
     * <p/>
     * <h2>Form der Matrix</h2>
     * <pre>
     * +---------+---+
     * |Sx  0  0 | 0 |
     * | 0 Sy  0 | 0 |
     * | 0  0 Sz | 0 |
     * +---------+---+
     * | 0  0  0 | 1 |
     * +---------+---+
     * </pre>
     *
     * @param factors Die Skalierungsfaktoren
     * @see Matrix4#toScaling(float, float, float)
     * @see Matrix4#toScaling(float)
     */
    public final void toScaling(@NotNull final Vector3 factors) {
        values[M11] = factors.x;
        values[M12] = 0f;
        values[M13] = 0f;
        values[M14] = 0f;
        values[M21] = 0f;
        values[M22] = factors.y;
        values[M23] = 0f;
        values[M24] = 0f;
        values[M31] = 0f;
        values[M32] = 0f;
        values[M33] = factors.z;
        values[M34] = 0f;
        values[M41] = 0f;
        values[M42] = 0f;
        values[M43] = 0f;
        values[M44] = 1f;
    }

    /**
     * Wandelt die Matrix in eine Skalierungsmatrix um
     * <p/>
     * <h2>Form der Matrix</h2>
     * <pre>
     * +---------+---+
     * | x  0  0 | 0 |
     * | 0  y  0 | 0 |
     * | 0  0  z | 0 |
     * +---------+---+
     * | 0  0  0 | 1 |
     * +---------+---+
     * </pre>
     *
     * @param x X-Skalierungsfaktor
     * @param y Y-Skalierungsfaktor
     * @param z Z-Skalierungsfaktor
     * @see Matrix4#toScaling(Vector3)
     * @see Matrix4#toScaling(float)
     */
    public final void toScaling(float x, float y, float z) {
        values[M11] = x;
        values[M12] = 0f;
        values[M13] = 0f;
        values[M14] = 0f;
        values[M21] = 0f;
        values[M22] = y;
        values[M23] = 0f;
        values[M24] = 0f;
        values[M31] = 0f;
        values[M32] = 0f;
        values[M33] = z;
        values[M34] = 0f;
        values[M41] = 0f;
        values[M42] = 0f;
        values[M43] = 0f;
        values[M44] = 1f;
    }

    /**
     * Wandelt die Matrix in eine Skalierungsmatrix um
     * <p/>
     * <h2>Form der Matrix</h2>
     * <pre>
     * +---------+---+
     * | s  0  0 | 0 |
     * | 0  s  0 | 0 |
     * | 0  0  s | 0 |
     * +---------+---+
     * | 0  0  0 | 1 |
     * +---------+---+
     * </pre>
     *
     * @param s Der Skalierungsfaktor
     * @see Matrix4#toScaling(float)
     */
    public final void toScaling(float s) {
        values[M11] = s;
        values[M12] = 0f;
        values[M13] = 0f;
        values[M14] = 0f;
        values[M21] = 0f;
        values[M22] = s;
        values[M23] = 0f;
        values[M24] = 0f;
        values[M31] = 0f;
        values[M32] = 0f;
        values[M33] = s;
        values[M34] = 0f;
        values[M41] = 0f;
        values[M42] = 0f;
        values[M43] = 0f;
        values[M44] = 1f;
    }

    /**
     * Transformiert einen Vektor mittels dieser Matrix
     *
     * @param vector Der zu transformierende Vektor
     * @param w      Der 4-dimensionale Überhang
     * @return Der transformierte Vektor
     * @see Matrix4#transform(Vector3)
     */
    @NotNull
    public final Vector3 transform(@NotNull final Vector3 vector, float w) {
        Vector3 v = vector.clone();
        transformInPlace(v, w);
        return v;
    }

    /**
     * Transformiert einen Vektor mittels dieser Matrix
     *
     * @param vector Der zu transformierende Vektor
     * @param w      Der 4-dimensionale Überhang
     * @see Matrix4#transform(Vector3)
     */
    public final void transformInPlace(@NotNull Vector3 vector, float w) {

        if (w != 0) {
            final float x = (getAt(0, 0) * vector.x) + (getAt(1, 0) * vector.y) + (getAt(2, 0) * vector.z) + (getAt(3, 0) * w);
            final float y = (getAt(0, 1) * vector.x) + (getAt(1, 1) * vector.y) + (getAt(2, 1) * vector.z) + (getAt(3, 1) * w);
            final float z = (getAt(0, 2) * vector.x) + (getAt(1, 2) * vector.y) + (getAt(2, 2) * vector.z) + (getAt(3, 2) * w);
            final float w2 = (getAt(0, 3) * vector.x) + (getAt(1, 3) * vector.y) + (getAt(2, 3) * vector.z) + (getAt(3, 3) * w);
            final float invW = MathUtils.isZero(w2, MathUtils.DEFAULT_EPSILON) ? 1.0f : 1.0f / w2;
            vector.set(x * invW, y * invW, z * invW);
        } else {
            transformVectorInPlace(vector);
        }
    }

    /**
     * Transformiert einen Vektor mittels dieser Matrix unter der Annahme w=0.
     * <p/>
     * <h2>Besonderheiten</h2>
     * Bei dieser Form der Transformation wirken Translationen nicht auf den Vektor; Er wird also
     * lediglich in Richtung und Länge verändert.
     *
     * @param vector Der zu transformierende Vektor
     * @return Der transformierte Vektor
     * @see #transformPoint(Vector3)
     */
    @ReturnsCachedValue
    public final Vector3 transformVector(final @NotNull Vector3 vector) {
        Vector3 v = vector.clone();
        transformVectorInPlace(v);
        return v;
    }

    /**
     * Interpretiert diese Matrix als 3x3-Matrix und transformiert einen Vektor
     *
     * @param vector Der zu transformierende Vektor
     * @return Der transformierte Vektor
     * @see #transformVector(Vector3)
     */
    @ReturnsCachedValue
    public final Vector3 transform3x3(final @NotNull Vector3 vector) {
        Vector3 v = vector.clone();
        transformInPlace3x3(v);
        return v;
    }

    /**
     * Transformiert einen Vektor mittels dieser Matrix unter der Annahme w=0
     * <p/>
     * <h2>Besonderheiten</h2>
     * Bei dieser Form der Transformation wirken Translationen auf den Vektor; Er wird also
     * in Richtung, Länge und Ursprung verändert.
     *
     * @param vector Der zu transformierende Vektor
     * @return Der transformierte Vektor
     * @see #transformVector(Vector3)
     */
    @ReturnsCachedValue
    public final Vector3 transformPoint(final @NotNull Vector3 vector) {
        Vector3 v = vector.clone();
        transformPointInPlace(v);
        return v;
    }

    /**
     * Alias für {@link #transformPoint(Vector3)}
     *
     * @param vector Der zu transformierende Vektor
     * @return Der transformierte Vektor
     * @see Matrix4#transform(Vector3, float)
     */
    @ReturnsCachedValue
    public final Vector3 transform(final @NotNull Vector3 vector) {
        return transformPoint(vector);
    }

    /**
     * Transformiert einen Vektor mittels dieser Matrix unter der Annahme w=0
     *
     * @param vector Der zu transformierende Vektor
     * @see Matrix4#transform(Vector3, float)
     */
    public final void transformVectorInPlace(@NotNull Vector3 vector) {
        final float x = (getAt(0, 0) * vector.x) + (getAt(1, 0) * vector.y) + (getAt(2, 0) * vector.z);
        final float y = (getAt(0, 1) * vector.x) + (getAt(1, 1) * vector.y) + (getAt(2, 1) * vector.z);
        final float z = (getAt(0, 2) * vector.x) + (getAt(1, 2) * vector.y) + (getAt(2, 2) * vector.z);
        final float w = (getAt(0, 3) * vector.x) + (getAt(1, 3) * vector.y) + (getAt(2, 3) * vector.z);
        final float invW = MathUtils.isZero(w, MathUtils.DEFAULT_EPSILON) ? 1.0f : 1.0f / w;
        vector.set(x * invW, y * invW, z * invW);
    }

    /**
     * Transformiert einen Vektor mittels dieser Matrix unter der Annahme w=1
     *
     * @param vector Der zu transformierende Vektor
     * @see Matrix4#transform(Vector3, float)
     */
    public final void transformPointInPlace(@NotNull Vector3 vector) {
        final float x = (getAt(0, 0) * vector.x) + (getAt(1, 0) * vector.y) + (getAt(2, 0) * vector.z) + (getAt(3, 0));
        final float y = (getAt(0, 1) * vector.x) + (getAt(1, 1) * vector.y) + (getAt(2, 1) * vector.z) + (getAt(3, 1));
        final float z = (getAt(0, 2) * vector.x) + (getAt(1, 2) * vector.y) + (getAt(2, 2) * vector.z) + (getAt(3, 2));
        final float w = (getAt(0, 3) * vector.x) + (getAt(1, 3) * vector.y) + (getAt(2, 3) * vector.z) + (getAt(3, 3));
        final float invW = 1.0f / w;

        vector.set(x * invW, y * invW, z * invW);
    }

    /**
     * Interpretiert diese Matrix als 3x3-Matrix und transformiert einen Vektor.
     * </p>
     * Dieser Aufruf ähnelt {@link #transformVectorInPlace(Vector3)} bis auf die fehlende Skalierung durch
     * w-Werte.
     *
     * @param vector Der zu transformierende Vektor
     * @see Matrix4#transform(Vector3, float)
     */
    public final void transformInPlace3x3(@NotNull Vector3 vector) {
        final float x = (getAt(0, 0) * vector.x) + (getAt(1, 0) * vector.y) + (getAt(2, 0) * vector.z);
        final float y = (getAt(0, 1) * vector.x) + (getAt(1, 1) * vector.y) + (getAt(2, 1) * vector.z);
        final float z = (getAt(0, 2) * vector.x) + (getAt(1, 2) * vector.y) + (getAt(2, 2) * vector.z);
        vector.set(x, y, z);
    }

    /**
     * Ermittelt, ob diese Matrix gleich einer anderen unter Beachtung des Vorgabe-Deltawertes {@link MathUtils#DEFAULT_EPSILON} ist
     *
     * @param other Die Vergleichsmatrix
     * @return <code>true</code> wenn die Matrizen identisch sind
     */
    public final boolean equals(@NotNull Matrix4 other) {
        return equals(other, MathUtils.DEFAULT_EPSILON);
    }

    /**
     * Ermittelt, ob diese Matrix gleich einer anderen unter Beachtung des Vorgabe-Deltawertes {@link MathUtils#DEFAULT_EPSILON} ist
     *
     * @param other Die Vergleichsmatrix
     * @param delta Der zu verwendende Deltawert
     * @return <code>true</code> wenn die Matrizen identisch sind
     */
    public final boolean equals(@NotNull Matrix4 other, float delta) {
        for (int i = 15; i >= 0; --i) {
            if (!MathUtils.equals(values[i], other.values[i], delta)) return false;
        }
        return true;
    }

    /**
     * Ermittelt, ob diese Matrix gleich einer anderen unter Beachtung eines gegebenen Deltawertes ist
     *
     * @param m11 Zeile 1, Spalte 1 der Vergleichsmatrix
     * @param m12 Zeile 1, Spalte 2 der Vergleichsmatrix
     * @param m13 Zeile 1, Spalte 3 der Vergleichsmatrix
     * @param m14 Zeile 1, Spalte 4 der Vergleichsmatrix
     * @param m21 Zeile 2, Spalte 1 der Vergleichsmatrix
     * @param m22 Zeile 2, Spalte 2 der Vergleichsmatrix
     * @param m23 Zeile 2, Spalte 3 der Vergleichsmatrix
     * @param m24 Zeile 2, Spalte 4 der Vergleichsmatrix
     * @param m31 Zeile 3, Spalte 1 der Vergleichsmatrix
     * @param m32 Zeile 3, Spalte 2 der Vergleichsmatrix
     * @param m33 Zeile 3, Spalte 3 der Vergleichsmatrix
     * @param m34 Zeile 3, Spalte 4 der Vergleichsmatrix
     * @param m41 Zeile 4, Spalte 1 der Vergleichsmatrix
     * @param m42 Zeile 4, Spalte 2 der Vergleichsmatrix
     * @param m43 Zeile 4, Spalte 3 der Vergleichsmatrix
     * @param m44 Zeile 4, Spalte 4 der Vergleichsmatrix
     * @return <code>true</code> wenn die Matrizen identisch sind
     */
    public final boolean equals(float m11, float m12, float m13, float m14,
                                float m21, float m22, float m23, float m24,
                                float m31, float m32, float m33, float m34,
                                float m41, float m42, float m43, float m44) {
        return equals(m11, m12, m13, m14,
                m21, m22, m23, m24,
                m31, m32, m33, m34,
                m41, m42, m43, m44,
                MathUtils.DEFAULT_EPSILON);
    }

    /**
     * Ermittelt, ob diese Matrix gleich einer anderen unter Beachtung eines gegebenen Deltawertes ist
     *
     * @param m11   Zeile 1, Spalte 1 der Vergleichsmatrix
     * @param m12   Zeile 1, Spalte 2 der Vergleichsmatrix
     * @param m13   Zeile 1, Spalte 3 der Vergleichsmatrix
     * @param m14   Zeile 1, Spalte 4 der Vergleichsmatrix
     * @param m21   Zeile 2, Spalte 1 der Vergleichsmatrix
     * @param m22   Zeile 2, Spalte 2 der Vergleichsmatrix
     * @param m23   Zeile 2, Spalte 3 der Vergleichsmatrix
     * @param m24   Zeile 2, Spalte 4 der Vergleichsmatrix
     * @param m31   Zeile 3, Spalte 1 der Vergleichsmatrix
     * @param m32   Zeile 3, Spalte 2 der Vergleichsmatrix
     * @param m33   Zeile 3, Spalte 3 der Vergleichsmatrix
     * @param m34   Zeile 3, Spalte 4 der Vergleichsmatrix
     * @param m41   Zeile 4, Spalte 1 der Vergleichsmatrix
     * @param m42   Zeile 4, Spalte 2 der Vergleichsmatrix
     * @param m43   Zeile 4, Spalte 3 der Vergleichsmatrix
     * @param m44   Zeile 4, Spalte 4 der Vergleichsmatrix
     * @param delta Der zu verwendende Deltawert
     * @return <code>true</code> wenn die Matrizen identisch sind
     */
    public final boolean equals(float m11, float m12, float m13, float m14,
                                float m21, float m22, float m23, float m24,
                                float m31, float m32, float m33, float m34,
                                float m41, float m42, float m43, float m44,
                                float delta) {
        return MathUtils.equals(values[M11], m11, delta) &&
                MathUtils.equals(values[M12], m12, delta) &&
                MathUtils.equals(values[M13], m13, delta) &&
                MathUtils.equals(values[M14], m14, delta) &&
                MathUtils.equals(values[M21], m21, delta) &&
                MathUtils.equals(values[M22], m22, delta) &&
                MathUtils.equals(values[M23], m23, delta) &&
                MathUtils.equals(values[M24], m24, delta) &&
                MathUtils.equals(values[M31], m31, delta) &&
                MathUtils.equals(values[M32], m32, delta) &&
                MathUtils.equals(values[M33], m33, delta) &&
                MathUtils.equals(values[M34], m34, delta) &&
                MathUtils.equals(values[M41], m41, delta) &&
                MathUtils.equals(values[M42], m42, delta) &&
                MathUtils.equals(values[M43], m43, delta) &&
                MathUtils.equals(values[M44], m44, delta);
    }

    /**
     * Eigenimplementierung der Vergleichmethode
     *
     * @param o Das Vergleichsobjekt
     * @return <code>true</code>, wenn die Objekte identisch sind
     */
    @Override
    public boolean equals(Object o) {
        // TODO: Test auf 16-Elemente-Array
        if (o instanceof Matrix4) return equals((Matrix4) o, MathUtils.DEFAULT_EPSILON);
        return super.equals(o);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Matrix4\n");
        sb.append("[").append(values == null ? "null" : "");
        for (int i = 0; values != null && i < values.length; ++i)
            sb.append(i % 4 == 0 ? "\n" : "\t").append(values[i]);
        sb.append("\n]");
        return sb.toString();
    }

    public void matrixTranslate(Matrix4 matrix, float x, float y, float z) {

        // Translate slots.
        matrix.values[12] = x;
        matrix.values[13] = y;
        matrix.values[14] = z;
    }

    public void matrixScale(Matrix4 matrix, float sx, float sy, float sz) {

        // Scale slots.
        matrix.values[0] = sx;
        matrix.values[5] = sy;
        matrix.values[10] = sz;
    }

    void matrixRotateX(Matrix4 matrix, float degrees) {
        float radians = MathUtils.deg2Rad(degrees);

        // Rotate X formula.
        matrix.values[5] = (float) Math.cos(radians);
        matrix.values[6] = (float) -Math.sin(radians);
        matrix.values[9] = -matrix.values[6];
        matrix.values[10] = matrix.values[5];
    }

    void matrixRotateY(Matrix4 matrix, float degrees) {
        float radians = MathUtils.deg2Rad(degrees);

        // Rotate Y formula.
        matrix.values[0] = (float) Math.cos(radians);
        matrix.values[2] = (float) Math.sin(radians);
        matrix.values[8] = -matrix.values[2];
        matrix.values[10] = matrix.values[0];
    }

    void matrixRotateZ(Matrix4 matrix, float degrees) {
        float radians = MathUtils.deg2Rad(degrees);

        // Rotate Z formula.
        matrix.values[0] = (float) Math.cos(radians);
        matrix.values[1] = (float) Math.sin(radians);
        matrix.values[4] = -matrix.values[1];
        matrix.values[5] = matrix.values[0];
    }

    public static void setPerspectiveProjection(Matrix4 matrix, float fovy, float near, float far, float aspectRatio) {

        float size = (float) (near * Math.tan(MathUtils.deg2Rad(fovy) / 2));
        float left = -size, right = size, bottom = -size / aspectRatio, top = size / aspectRatio;

        // First Column
        matrix.values[0] = 2 * near / (right - left);
        matrix.values[1] = 0;
        matrix.values[2] = 0;
        matrix.values[3] = 0;

        // Second Column
        matrix.values[4] = 0;
        matrix.values[5] = 2 * near / (top - bottom);
        matrix.values[6] = 0;
        matrix.values[7] = 0;

        // Third Column
        matrix.values[8] = (right + left) / (right - left);
        matrix.values[9] = (top + bottom) / (top - bottom);
        matrix.values[10] = -(far + near) / (far - near);
        matrix.values[11] = -1;

        // Fourth Column
        matrix.values[12] = 0;
        matrix.values[13] = 0;
        matrix.values[14] = -(2 * far * near) / (far - near);
        matrix.values[15] = 0;
    }

    public static void setOrthographicProjection(Matrix4 matrix, float left, float right, float bottom, float top, float near, float far) {

        // First Column
        matrix.values[0] = 2 / (right - left);
        matrix.values[1] = 0;
        matrix.values[2] = 0;
        matrix.values[3] = 0;

        // Second Column
        matrix.values[4] = 0;
        matrix.values[5] = 2 / (top - bottom);
        matrix.values[6] = 0;
        matrix.values[7] = 0;

        // Third Column
        matrix.values[8] = 0;
        matrix.values[9] = 0;
        matrix.values[10] = -2 / (far - near);
        matrix.values[11] = 0;

        // Fourth Column
        matrix.values[12] = -(right + left) / (right - left);
        matrix.values[13] = -(top + bottom) / (top - bottom);
        matrix.values[14] = -(far + near) / (far - near);
        matrix.values[15] = 1;
    }

    public static void setLookAt(Matrix4 matrix, Vector3 position, Vector3 target, Vector3 up) {
        setLookAt(matrix, position.x, position.y, position.z, target.x, target.y, target.z, up.x, up.y, up.z);
    }

    private static void setLookAt(Matrix4 matrix, float posX, float posY, float posZ, float targetX, float targetY, float targetZ, float upX, float upY, float upZ) {
        float x0, x1, x2, y0, y1, y2, z0, z1, z2, len;

        if (posX == targetX && posY == targetY && posZ == targetZ) {
            matrix = Matrix4.UNIT;
        }

        z0 = posX - targetX;
        z1 = posY - targetY;
        z2 = posZ - targetZ;

        // normalize (no check needed for 0 because of early return)
        len = (float) (1 / sqrt(z0 * z0 + z1 * z1 + z2 * z2));
        z0 *= len;
        z1 *= len;
        z2 *= len;

        //vec3.normalize(vec3.cross(up, z, x));
        x0 = upY * z2 - upZ * z1;
        x1 = upZ * z0 - upX * z2;
        x2 = upX * z1 - upY * z0;
        len = (float) sqrt(x0 * x0 + x1 * x1 + x2 * x2);
        if (len == Float.NaN) {
            x0 = 0;
            x1 = 0;
            x2 = 0;
        } else {
            len = 1 / len;
            x0 *= len;
            x1 *= len;
            x2 *= len;
        }

        //vec3.normalize(vec3.cross(z, x, y));
        y0 = z1 * x2 - z2 * x1;
        y1 = z2 * x0 - z0 * x2;
        y2 = z0 * x1 - z1 * x0;

        len = (float) sqrt(y0 * y0 + y1 * y1 + y2 * y2);
        if (len == Float.NaN) {
            y0 = 0;
            y1 = 0;
            y2 = 0;
        } else {
            len = 1 / len;
            y0 *= len;
            y1 *= len;
            y2 *= len;
        }

        matrix.values[0] = x0;
        matrix.values[1] = y0;
        matrix.values[2] = z0;
        matrix.values[3] = 0;
        matrix.values[4] = x1;
        matrix.values[5] = y1;
        matrix.values[6] = z1;
        matrix.values[7] = 0;
        matrix.values[8] = x2;
        matrix.values[9] = y2;
        matrix.values[10] = z2;
        matrix.values[11] = 0;
        matrix.values[12] = -(x0 * posX + x1 * posY + x2 * posZ);
        matrix.values[13] = -(y0 * posX + y1 * posY + y2 * posZ);
        matrix.values[14] = -(z0 * posX + z1 * posY + z2 * posZ);
        matrix.values[15] = 1;
    }

    public static Quaternion createQuatFromMatrix(Matrix4 m) {
        float trace = 1 + m.values[0] + m.values[5] + m.values[10];
        float S = 0;
        float X = 0;
        float Y = 0;
        float Z = 0;
        float W = 0;

        if (trace > 0.0000001) {
            S = (float) sqrt(trace) * 2;
            X = (m.values[6] - m.values[9]) / S;
            Y = (m.values[8] - m.values[2]) / S;
            Z = (m.values[1] - m.values[4]) / S;
            W = 0.25f * S;
        } else {
            if (m.values[0] > m.values[5] && m.values[0] > m.values[10]) {
                // Column 0:
                S = (float) sqrt(1.0 + m.values[0] - m.values[5] - m.values[10]) * 2;
                X = 0.25f * S;
                Y = (m.values[1] + m.values[4]) / S;
                Z = (m.values[8] + m.values[2]) / S;
                W = (m.values[6] - m.values[9]) / S;
            } else if (m.values[5] > m.values[10]) {
                // Column 1:
                S = (float) sqrt(1.0 + m.values[5] - m.values[5] - m.values[10]) * 2;
                X = (m.values[1] + m.values[4]) / S;
                Y = 0.25f * S;
                Z = (m.values[6] + m.values[9]) / S;
                W = (m.values[8] - m.values[2]) / S;
            } else {
                // Column 2:
                S = (float) sqrt(1.0 + m.values[10] - m.values[0] - m.values[5]) * 2;
                X = (m.values[8] + m.values[2]) / S;
                Y = (m.values[6] + m.values[9]) / S;
                Z = 0.25f * S;
                W = (m.values[1] - m.values[4]) / S;
            }
        }

        return new Quaternion(X, Y, Z, W);
    }

    /**
     * Emulates a mat4 * vec4 multiplication.
     *
     * @param target in place target vector
     * @param flag   (1 positionial or 0 directional vector)
     * @return returns target vector for chaining purposes
     */
    public Vector3 mul(Vector3 target, float flag) {

        assert flag == 0 || flag == 1;

        target.x = values[0] * target.x + values[1] * target.y + values[2] * target.z + values[3] * flag;
        target.y = values[4] * target.x + values[5] * target.y + values[6] * target.z + values[7] * flag;
        target.z = values[8] * target.x + values[9] * target.y + values[10] * target.z + values[11] * flag;

        return target;
    }

    public static Matrix4 rotate(Quaternion q) {
        float[] quat = q.toAxisAngle();
        return createFromAxisAngle(quat[0],quat[1],quat[2],quat[3]);
    }

    public static Matrix4 createFromAxisAngle(float x, float y, float z, float angle) {
        Vector3 axis = Vector3.createNew(x,y,z);
        Matrix4 m = createFromAxisAngle(axis,angle);
        axis.recycle();
        return m;
    }

    public static Matrix4 createFromAxisAngle(Vector3 axis, float angle) {
        float cos = (float) Math.cos(-angle);
        float sin = (float) Math.sin(-angle);
        float t = 1.0f - cos;

        axis.normalize();

        return Matrix4.createNew(t * axis.x * axis.x + cos, t * axis.x * axis.y - sin * axis.z, t * axis.x * axis.z + sin * axis.y, 0.0f,
                t * axis.x * axis.y + sin * axis.z, t * axis.y * axis.y + cos, t * axis.y * axis.z - sin * axis.x, 0.0f,
                t * axis.x * axis.z - sin * axis.y, t * axis.y * axis.z + sin * axis.x, t * axis.z * axis.z + cos, 0.0f,
                0, 0, 0, 1);
    }
}
