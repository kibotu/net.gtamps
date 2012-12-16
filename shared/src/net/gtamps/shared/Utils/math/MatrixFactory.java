package net.gtamps.shared.Utils.math;

import net.gtamps.shared.Utils.cache.annotations.ReturnsCachedValue;
import org.jetbrains.annotations.NotNull;

import static net.gtamps.shared.Utils.math.Matrix4.*;

/**
 * Factory für {@link Matrix4}
 */
public class MatrixFactory {

//                  transposed matrix
//    |    M11        M21       M31        M41  |
//    |                                         |
//    |    M12        M22       M32        M42  |
//    |                                         |
//    |    M13        M23       M33        M43  |
//    |                                         |
//    |    M14        M24       M34        M44  |

    public static final int M11 = 0;
    public static final int M12 = 4;
    public static final int M13 = 8;
    public static final int M14 = 12;

    public static final int M21 = 1;
    public static final int M22 = 5;
    public static final int M23 = 9;
    public static final int M24 = 13;

    public static final int M31 = 2;
    public static final int M32 = 6;
    public static final int M33 = 10;
    public static final int M34 = 14;

    public static final int M41 = 3;
    public static final int M42 = 7;
    public static final int M43 = 11;
    public static final int M44 = 15;

    /**
     * Versteckter Konstruktor
     */
    private MatrixFactory() {
    }

    /**
     * Erzeugt eine rechtshändige Matrix zur Rotation um die X-Achse
     * <p/>
     * <h3>RH-/LH-System</h3>
     * <a href="http://www.cprogramming.com/tutorial/3d/rotationMatrices.html">Rotations in Three Dimensions</a>
     * <p/>
     * <h3>Layout</h3>
     * <pre>
     *    1,   0,    0, 0
     *    0, cos,  sin, 0
     *    0, -sin, cos, 0
     *    0,    0,   0, 1
     * </pre>
     *
     * @param theta Der Winkel in radians
     * @return Die Rotationsmatrix
     * @see MatrixFactory#getRotationEulerRPY(float, float, float)
     * @see MatrixFactory#getRotationEulerZXZ(float, float, float)
     * @see MatrixFactory#getRotationEulerZYZ(float, float, float)
     * @see MatrixFactory#getRotationY(float)
     * @see MatrixFactory#getRotationZ(float)
     * @see MatrixFactory#getRotationAxisAngle(Vector3, float)
     * @see MatrixFactory#getProgressiveRotation(float, float, float)
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getRotationX(final float theta) {
        return getRotationX(FloatMath.cos(theta), FloatMath.sin(theta));
    }

    /**
     * Erzeugt eine rechtshändige Matrix zur Rotation um die X-Achse
     * <p/>
     * <h3>RH-/LH-System</h3>
     * <a href="http://www.cprogramming.com/tutorial/3d/rotationMatrices.html">Rotations in Three Dimensions</a>
     * <p/>
     * <h3>Layout</h3>
     * <pre>
     *    1,   0,    0, 0
     *    0, cos,  sin, 0
     *    0, -sin, cos, 0
     *    0,    0,   0, 1
     * </pre>
     *
     * @param cosTheta Der Kosinus des Winkels
     * @param sinTheta Der Sinus des Winkels
     * @return Die Rotationsmatrix
     * @see MatrixFactory#getRotationX(float)
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getRotationX(final float cosTheta, final float sinTheta) {
        return createNew().set(
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, cosTheta, sinTheta, 0.0f,
                0.0f, -sinTheta, cosTheta, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Erzeugt eine rechtshändige Matrix zur Rotation um die Y-Achse
     * <p/>
     * <h3>RH-/LH-System</h3>
     * <a href="http://www.cprogramming.com/tutorial/3d/rotationMatrices.html">Rotations in Three Dimensions</a>
     * <p/>
     * <h3>Layout</h3>
     * <pre>
     *  cos, 0, -sin, 0
     *    0, 1,    0, 0
     *  sin, 0,  cos, 0
     *    0, 0,    0, 1
     * </pre>
     *
     * @param theta Der Winkel in radians
     * @return Die Rotationsmatrix
     * @see MatrixFactory#getRotationEulerRPY(float, float, float)
     * @see MatrixFactory#getRotationEulerZXZ(float, float, float)
     * @see MatrixFactory#getRotationEulerZYZ(float, float, float)
     * @see MatrixFactory#getRotationX(float)
     * @see MatrixFactory#getRotationZ(float)
     * @see MatrixFactory#getRotationAxisAngle(Vector3, float)
     * @see MatrixFactory#getProgressiveRotation(float, float, float)
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getRotationY(final float theta) {
        return getRotationY(FloatMath.cos(theta), FloatMath.sin(theta));
    }

    /**
     * Erzeugt eine rechtshändige Matrix zur Rotation um die Y-Achse
     * <p/>
     * <h3>RH-/LH-System</h3>
     * <a href="http://www.cprogramming.com/tutorial/3d/rotationMatrices.html">Rotations in Three Dimensions</a>
     * <p/>
     * <h3>Layout</h3>
     * <pre>
     *  cos, 0, -sin, 0
     *    0, 1,    0, 0
     *  sin, 0,  cos, 0
     *    0, 0,    0, 1
     * </pre>
     *
     * @param cosTheta Der Kosinus des Winkels
     * @param sinTheta Der Sinus des Winkels
     * @return Die Rotationsmatrix
     * @see MatrixFactory#getRotationY(float)
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getRotationY(final float cosTheta, final float sinTheta) {
        return createNew().set(
                cosTheta, 0.0f, -sinTheta, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                sinTheta, 0.0f, cosTheta, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Erzeugt eine rechtshändige Matrix zur Rotation um die Z-Achse
     * <p/>
     * <h3>RH-/LH-System</h3>
     * <a href="http://www.cprogramming.com/tutorial/3d/rotationMatrices.html">Rotations in Three Dimensions</a>
     * <p/>
     * <h3>Layout</h3>
     * <pre>
     *  cos, sin, 0, 0
     * -sin, cos, 0, 0
     *    0,   0, 1, 0
     *    0,   0, 0, 1
     * </pre>
     *
     * @param theta Der Winkel in radians
     * @return Die Rotationsmatrix
     * @see MatrixFactory#getRotationEulerRPY(float, float, float)
     * @see MatrixFactory#getRotationEulerZXZ(float, float, float)
     * @see MatrixFactory#getRotationEulerZYZ(float, float, float)
     * @see MatrixFactory#getRotationX(float)
     * @see MatrixFactory#getRotationY(float)
     * @see MatrixFactory#getRotationAxisAngle(Vector3, float)
     * @see MatrixFactory#getProgressiveRotation(float, float, float)
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getRotationZ(final float theta) {
        return getRotationZ(FloatMath.cos(theta), FloatMath.sin(theta));
    }

    /**
     * Erzeugt eine rechtshändige Matrix zur Rotation um die Z-Achse
     * <p/>
     * <h3>RH-/LH-System</h3>
     * <a href="http://www.cprogramming.com/tutorial/3d/rotationMatrices.html">Rotations in Three Dimensions</a>
     * <p/>
     * <h3>Layout</h3>
     * <pre>
     *  cos, sin, 0, 0
     * -sin, cos, 0, 0
     *    0,   0, 1, 0
     *    0,   0, 0, 1
     * </pre>
     *
     * @param cosTheta Der Kosinus des Winkels
     * @param sinTheta Der Sinus des Winkels
     * @return Die Rotationsmatrix
     * @see MatrixFactory#getRotationZ(float)
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getRotationZ(final float cosTheta, final float sinTheta) {
        return createNew().set(
                cosTheta, sinTheta, 0.0f, 0.0f,
                -sinTheta, cosTheta, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Erzeugt eine Matrix zur Rotation um eine Achse
     *
     * @param axis  Die Achse
     * @param theta Der Winkel in radians
     * @return Die Rotationsmatrix
     * @see MatrixFactory#getRotationEulerRPY(float, float, float)
     * @see MatrixFactory#getRotationEulerZXZ(float, float, float)
     * @see MatrixFactory#getRotationEulerZYZ(float, float, float)
     * @see MatrixFactory#getRotationX(float)
     * @see MatrixFactory#getRotationY(float)
     * @see MatrixFactory#getRotationZ(float)
     * @see MatrixFactory#getProgressiveRotation(float, float, float)
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getRotationAxisAngle(@NotNull Vector3 axis, float theta) {
        float cos = (float) Math.cos(theta);
        float sin = (float) Math.sin(theta);
        return getRotationAxisAngle(axis, cos, sin);
    }

    /**
     * Erzeugt eine Matrix zur Rotation um eine Achse
     *
     * @param axis     Die Achse
     * @param cosTheta Der Kosinus des Winkels
     * @param sinTheta Der Sinus des Winkels
     * @return Die Rotationsmatrix
     * @see MatrixFactory#getRotationAxisAngle(Vector3, float)
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getRotationAxisAngle(@NotNull Vector3 axis, float cosTheta, float sinTheta) {
        // pre-calculate squared
        float xx = axis.x * axis.x;
        float yy = axis.y * axis.y;
        float zz = axis.z * axis.z;

        // pre-calculate axis combinations
        float xy = axis.x * axis.y;
        float xz = axis.x * axis.z;
        float yz = axis.y * axis.z;

        // pre-calculate axes and angle functions
        float xsin = axis.x * sinTheta;
        float ysin = axis.y * sinTheta;
        float zsin = axis.z * sinTheta;
        float xcos = axis.x * cosTheta;
        float ycos = axis.y * cosTheta;

        /*
          return new Matrix4D(
              xx * (1 - cos) + cos, xy * (1 - cos) + zsin, xz * (1 - cos) - ysin, 0.0d,
              xy * (1 - cos) - zsin, yy * (1 - cos) + cos, yz * (1 - cos) + xsin, 0.0d,
              xz * (1 - cos) + ysin, yz * (1 - cos) + xsin, zz * (1 - cos) + cos, 0.0d,
              0.0d, 0.0d, 0.0d, 1.0d);
          */

        return createNew().set(
                xx - axis.x * xcos + cosTheta, xy - axis.y * xcos + zsin, xz - axis.y * xcos - ysin, 0.0f,
                xy - axis.y * xcos - zsin, yy - axis.y * ycos + cosTheta, yz - axis.z * ycos + xsin, 0.0f,
                xz - axis.z * xcos + ysin, yz - axis.z * ycos + xsin, zz - zz * cosTheta + cosTheta, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Erzeugt eine Matrix zur progressiven Rotation basierend auf Winkelgeschwindigkeit
     *
     * @param deltaX Winkelgeschwindigkeit in X-Richtung in radians/frame
     * @param deltaY Winkelgeschwindigkeit in Y-Richtung in radians/frame
     * @param deltaZ Winkelgeschwindigkeit in Z-Richtung in radians/frame
     * @return Die Rotationsmatrix
     * @see MatrixFactory#getRotationEulerRPY(float, float, float)
     * @see MatrixFactory#getRotationEulerZXZ(float, float, float)
     * @see MatrixFactory#getRotationEulerZYZ(float, float, float)
     * @see MatrixFactory#getRotationX(float)
     * @see MatrixFactory#getRotationY(float)
     * @see MatrixFactory#getRotationZ(float)
     * @see MatrixFactory#getRotationAxisAngle(Vector3, float)
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getProgressiveRotation(float deltaX, float deltaY, float deltaZ) {
        return createNew().set(
                0.0f, -deltaZ, deltaY, 0.0f,
                deltaZ, 0.0f, -deltaX, 0.0f,
                -deltaY, deltaX, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Erzeugt eine Matrix zur Rotation gemäß Euler-ZXZ
     *
     * @param z  Der Winkel um die Z-Achse in radians
     * @param x1 Der Winkel um die X'-Achse in radians
     * @param z2 Der Winkel um die Z''-Achse in radians
     * @return Die Rotationsmatrix
     * @see MatrixFactory#getRotationEulerRPY(float, float, float)
     * @see MatrixFactory#getRotationEulerZYZ(float, float, float)
     * @see MatrixFactory#getRotationX(float)
     * @see MatrixFactory#getRotationY(float)
     * @see MatrixFactory#getRotationZ(float)
     * @see MatrixFactory#getProgressiveRotation(float, float, float)
     * @see MatrixFactory#getRotationAxisAngle(Vector3, float)
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getRotationEulerZXZ(float z, float x1, float z2) {
        float cz = (float) Math.cos(z);
        float sz = (float) Math.sin(z);

        float cx1 = (float) Math.cos(x1);
        float sx1 = (float) Math.sin(x1);

        float cz2 = (float) Math.cos(z2);
        float sz2 = (float) Math.sin(z2);

        return createNew().set(
                cz * cz2 - sz * cx1 * sz2, -cz * sz2 - sz * cx1 * cz2, sz * sx1, 0f,
                sz * cz2 + cz * cx1 * sz2, cz * cx1 * cz2 - sz * sz2, -cz * sx1, 0f,
                sx1 * sz2, sx1 * cz2, cx1, 0f,
                0f, 0f, 0f, 1f
        );

    }

    /**
     * Erzeugt eine Matrix zur Rotation gemäß Euler-ZYZ
     *
     * @param z  Der Winkel um die Z-Achse in radians
     * @param y1 Der Winkel um die Y'-Achse in radians
     * @param z2 Der Winkel um die Z''-Achse in radians
     * @return Die Rotationsmatrix
     * @see MatrixFactory#getRotationEulerRPY(float, float, float)
     * @see MatrixFactory#getRotationEulerZXZ(float, float, float)
     * @see MatrixFactory#getRotationX(float)
     * @see MatrixFactory#getRotationY(float)
     * @see MatrixFactory#getRotationZ(float)
     * @see MatrixFactory#getProgressiveRotation(float, float, float)
     * @see MatrixFactory#getRotationAxisAngle(Vector3, float)
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getRotationEulerZYZ(float z, float y1, float z2) {
        float cz = (float) Math.cos(z);
        float sz = (float) Math.sin(z);

        float cy1 = (float) Math.cos(y1);
        float sy1 = (float) Math.sin(y1);

        float cz2 = (float) Math.cos(z2);
        float sz2 = (float) Math.sin(z2);

        return createNew().set(
                -sz * sz2 + cz * cy1 * cz2, -sz * cz2 - cz * cy1 * sz2, cz * sy1, 0f,
                cz * sz2 + sz * cy1 * cz2, cz * cz2 - sz * cy1 * sz2, sz * sy1, 0f,
                -sy1 * cz2, sy1 * sz2, cy1, 0f,
                0f, 0f, 0f, 1f
        );

    }

    /**
     * Erzeugt eine Matrix zur Rotation gemäß Euler-Roll-Pitch-Yaw
     *
     * @param rollX  Der Rollwinkel in radians
     * @param pitchY Der Nickwinkel in radians
     * @param yawZ   Der Gierwinkel in radians
     * @return Die Rotationsmatrix
     * @see MatrixFactory#getRotationEulerZXZ(float, float, float)
     * @see MatrixFactory#getRotationEulerZYZ(float, float, float)
     * @see MatrixFactory#getRotationX(float)
     * @see MatrixFactory#getRotationY(float)
     * @see MatrixFactory#getRotationZ(float)
     * @see MatrixFactory#getProgressiveRotation(float, float, float)
     * @see MatrixFactory#getRotationAxisAngle(Vector3, float)
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getRotationEulerRPY(float rollX, float pitchY, float yawZ) {
        float cr = (float) Math.cos(rollX); // Φ
        float sr = (float) Math.sin(rollX);

        float cp = (float) Math.cos(pitchY); // Θ
        float sp = (float) Math.sin(pitchY);

        float cy = (float) Math.cos(yawZ); // Ψ
        float sy = (float) Math.sin(yawZ);

        return createNew().set(
                cp * cy, cp * sy, -sp, 0f,
                sr * sp * cy - cr * sy, sr * sp * sy + cr * cy, sr * cp, 0f,
                cr * sp * cy + sr * sy, cr * sp * sy - sr * cy, cr * cp, 0f,
                0f, 0f, 0f, 1f
        );
    }

    /**
     * Bezieht eine vollständige Transformationsmatrix.
     * Die Transformationen werden in der Reihenfolge Skalierung, Rotation, Translation angewandt,
     *
     * @param scaling     Der Skalierungsvektor
     * @param rotation    Der Rotationsvektor (roll-pitch-yaw)
     * @param translation Der translationsvektor
     * @return Die Transformationsmatrix
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getTransformation(@NotNull Vector3 scaling, @NotNull Vector3 dimension, @NotNull Vector3 rotation, @NotNull Vector3 translation) {
        return setTransformation(createNew(), scaling, dimension, rotation, translation);
    }

    /**
     * Sets Transformation Matrix. ((Scale * Rotation) * Translation)
     *
     * @param target      matrix
     * @param scaling
     * @param rotation
     * @param translation
     * @return target matrix
     * @see <a href="http://db-in.com/blog/2011/04/cameras-on-opengl-es-2-x/">cameras-on-opengl-es-2-x</a>
     */
    @Deprecated
    public static Matrix4 setTransformation(@NotNull Matrix4 target, @NotNull Vector3 scaling, @NotNull Vector3 dimension, @NotNull Vector3 rotation, @NotNull Vector3 translation) {
        float crX = (float) Math.cos(rotation.x); // Φ
        float srX = (float) Math.sin(rotation.x);

        float crY = (float) Math.cos(rotation.y); // Θ
        float srY = (float) Math.sin(rotation.y);

        float crZ = (float) Math.cos(rotation.z); // Ψ
        float srZ = (float) Math.sin(rotation.z);

        float sX = scaling.x * dimension.x;
        float sY = scaling.y * dimension.y;
        float sZ = scaling.z * dimension.z;

        float tX = translation.x;
        float tY = translation.y;
        float tZ = translation.z;

//                      Matrix
//        |  M11        M12    M13      M14    |
//        |                                    |
//        |  M21        M22    M23      M24    |
//        |                                    |
//        |  M31        M32    M33      M34    |
//        |                                    |
//        |  M41        M42    M43      M44    |
//        target.values[M11] = 1;
//        target.values[M12] = 0;
//        target.values[M13] = 0;
//        target.values[M14] = 0;
//
//        target.values[M21] = 0;
//        target.values[M22] = 1;
//        target.values[M23] = 0;
//        target.values[M24] = 0;
//
//        target.values[M31] = 0;
//        target.values[M32] = 0;
//        target.values[M33] = 1;
//        target.values[M34] = 0;
//
//        target.values[M41] = 0;
//        target.values[M42] = 0;
//        target.values[M43] = 0;
//        target.values[M44] = 1;

//                      rotate x
//        |    1        0        0        0    |
//        |                                    |
//        |    0      cos(θ)   sin(θ)     0    |
//        |                                    |
//        |    0     -sin(θ)   cos(θ)     0    |
//        |                                    |
//        |    0        0        0        1    |
//        target.values[M22] = crX;
//        target.values[M23] = srX;
//        target.values[M32] = -srX;
//        target.values[M33] = crX;

//                      rotate y
//        |  cos(θ)     0    -sin(θ)      0    |
//        |                                    |
//        |    0        1        0        0    |
//        |                                    |
//        |  sin(θ)     0     cos(θ)      0    |
//        |                                    |
//        |    0        0        0        1    |
//        target.values[M11] = crY;
//        target.values[M13] = -srY;
//        target.values[M31] = srY;
//        target.values[M33] = crY;

//                    rotate z
//        |  cos(θ)  -sin(θ)     0        0    |
//        |                                    |
//        |  sin(θ)   cos(θ)     0        0    |
//        |                                    |
//        |    0        0        1        0    |
//        |                                    |
//        |    0        0        0        1    |
//        target.values[M11] = crZ;
//        target.values[M12] = -srZ;
//        target.values[M21] = srZ;
//        target.values[M22] = crZ;

//                     scale
//        |    SX       0        0        0    |
//        |                                    |
//        |    0        SY       0        0    |
//        |                                    |
//        |    0        0        SZ       0    |
//        |                                    |
//        |    0        0        0        1    |
//        target.values[M11] = sX;
//        target.values[M22] = sY;
//        target.values[M33] = sZ;

//                      translate
//        |    1        0        0        X    |
//        |                                    |
//        |    0        1        0        Y    |
//        |                                    |
//        |    0        0        1        Z    |
//        |                                    |
//        |    0        0        0        1    |
//        target.values[M14] = tX;
//        target.values[M24] = tY;
//        target.values[M34] = tZ;


        target.values[M11] = crZ * crY * sX;
        target.values[M12] = -srZ;
        target.values[M13] = -srY;
        target.values[M14] = 0;

        target.values[M21] = srZ;
        target.values[M22] = crZ * crX * sY;
        target.values[M23] = srX;
        target.values[M24] = 0;

        target.values[M31] = srY;
        target.values[M32] = -srX;
        target.values[M33] = crY * crX *sY;
        target.values[M34] = 0;

        target.values[M41] = tX;
        target.values[M42] = tY;
        target.values[M43] = tZ;
        target.values[M44] = 1;


        return target;
    }

    /**
     * Bezieht eine vollständige Transformationsmatrix.
     * Die Transformationen werden in der Reihenfolge Skalierung, Rotation, Translation angewandt,
     *
     * @param scaling     Der Skalierungsvektor
     * @param rotation    Der Rotationsvektor (roll-pitch-yaw)
     * @param translation Der translationsvektor
     * @return Die Transformationsmatrix
     */
    @NotNull
    @ReturnsCachedValue
    @Deprecated
    public static Matrix4 getTransformation(float scaling, @NotNull Vector3 rotation, @NotNull Vector3 translation) {
        float cr = (float) Math.cos(rotation.x); // Φ
        float sr = (float) Math.sin(rotation.x);

        float cp = (float) Math.cos(rotation.y); // Θ
        float sp = (float) Math.sin(rotation.y);

        float cy = (float) Math.cos(rotation.z); // Ψ
        float sy = (float) Math.sin(rotation.z);

        return createNew().set(
                scaling * cp * cy, scaling * cp * sy, scaling * (-sp), 0f,
                -scaling * (sr * sp * cy - cr * sy), scaling * (sr * sp * sy + cr * cy), scaling * sr * cp, 0f,
                scaling * (cr * sp * cy + sr * sy), scaling * (cr * sp * sy - sr * cy), scaling * cr * cp, 0f,
                translation.x, translation.y, translation.z, 1f
        );
    }

    /**
     * Bezieht eine vollständige Transformationsmatrix.
     * Die Transformationen werden in der Reihenfolge Rotation, Translation angewandt.
     *
     * @param rotation    Der Rotationsvektor (roll-pitch-yaw)
     * @param translation Der translationsvektor
     * @return Die Transformationsmatrix
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getTransformation(@NotNull Vector3 rotation, @NotNull Vector3 translation) {
        float cr = (float) Math.cos(rotation.x); // Φ
        float sr = (float) Math.sin(rotation.x);

        float cp = (float) Math.cos(rotation.y); // Θ
        float sp = (float) Math.sin(rotation.y);

        float cy = (float) Math.cos(rotation.z); // Ψ
        float sy = (float) Math.sin(rotation.z);

        return createNew().set(
                cp * cy, cp * sy, (-sp), 0f,
                -(sr * sp * cy - cr * sy), (sr * sp * sy + cr * cy), sr * cp, 0f,
                (cr * sp * cy + sr * sy), (cr * sp * sy - sr * cy), cr * cp, 0f,
                translation.x, translation.y, translation.z, 1f
        );
    }

    /**
     * Bezieht eine Translationsmatrix
     *
     * @param translation Der translationsvektor
     * @return Die Transformationsmatrix
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getTranslation(@NotNull Vector3 translation) {
        return getTranslation(translation.x, translation.y, translation.z);
    }

    /**
     * Bezieht eine Translationsmatrix
     *
     * @param x Der Translationsvektor (X-Komponente)
     * @param y Der Translationsvektor (Y-Komponente)
     * @param z Der Translationsvektor (Z-Komponente)
     * @return Die Transformationsmatrix
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getTranslation(float x, float y, float z) {
        return createNew().set(
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                x, y, z, 1f
        );
    }

    /**
     * Erzeugt eine Skalierungsmatrix
     * <p/>
     * <h3>Layout</h3>
     * <pre>
     *   fx,  0,  0, 0
     *    0, fy,  0, 0
     *    0,  0, fz, 0
     *    0,   0, 0, 1
     * </pre>
     *
     * @param factorX Die X-Skalierung
     * @param factorY Die Y-Skalierung
     * @param factorZ Die Z-Skalierung
     * @return Die Skalierungsmatrix
     */
    @NotNull
    @ReturnsCachedValue
    public static Matrix4 getScaling(final float factorX, final float factorY, final float factorZ) {
        return createNew().set(
                factorX, 0.0f, 0.0f, 0.0f,
                0.0f, factorY, 0.0f, 0.0f,
                0.0f, 0.0f, factorZ, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }

    public static Matrix4 getRotationAxisAngle(float[] xyzw) {
        return getRotationAxisAngle(Vector3.createNew(xyzw[0], xyzw[1], xyzw[2]), xyzw[3]);
    }
}
