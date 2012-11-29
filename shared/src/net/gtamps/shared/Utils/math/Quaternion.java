package net.gtamps.shared.Utils.math;

/**
 * User: Jan Rabe
 * Date: 24/11/12
 * Time: 14:53
 */
public class Quaternion {
    public float x, y, z, w;

    /**
     * Represents a quternion
     *
     * @param x The first component of the quaternion.
     * @param y The second component of the quaternion.
     * @param z The third component of the quaternion.
     * @param w The fourth component of the quaternion.
     */
    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector3 asEulerAngles() {
        return Vector3.eulerAngles(this);
    }


    /**
     * Represents a quternion. Assumes a unit quaternion to compute the fourth component.
     *
     * @param x The first component of the quaternion.
     * @param y The second component of the quaternion.
     * @param z The third component of the quaternion.
     */
    public Quaternion(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        float t = 1.0f - x * x - y * y - z * z;

        if (t < 0.0f)
            this.w = 0.0f;
        else
            this.w = -(float) Math.sqrt(t);
    }


    /**
     * Quaternion addition.
     *
     * @param q2 The second quaternion.
     * @return The final quaternion.
     */
    public Quaternion add(Quaternion q2) {
        return new Quaternion(x + q2.x, y + q2.y, z + q2.z, w + q2.w);
    }

    /**
     * Quaternion subtraction.
     *
     * @param q2 The second quaternion.
     * @return The final quaternion.
     */
    public Quaternion sub(Quaternion q2) {
        return new Quaternion(x - q2.x, y - q2.y, z - q2.z, w - q2.w);
    }

    /**
     * Quaternion multiplication.
     *
     * @param q2 The second quaternion.
     * @return The final quaternion.
     */
    public Quaternion mul(Quaternion q2) {
        return new Quaternion(q2.w * x + q2.x * w + q2.z * y - q2.y * z,
                q2.w * y + q2.y * w + q2.x * z - q2.z * x,
                q2.w * z + q2.z * w + q2.y * x - q2.x * y,
                q2.w * w - q2.x * x - q2.y * y - q2.z * z);
    }

    /**
     * Quaternion inversion.
     *
     * @return The inverted quaternion.
     */
    public Quaternion invert() {
        return new Quaternion(-x, -y, -z, w);
    }

    /**
     * Quaternion/Vector multiplication.
     *
     * @param v The vector.
     * @return The final quaternion.
     */
    public Quaternion mul(float[] v) {
        return new Quaternion(w * v[0] + y * v[2] - z * v[1],
                w * v[1] + z * v[0] - x * v[2],
                w * v[2] + x * v[1] - y * v[0],
                -x * v[0] - y * v[1] - z * v[2]);
    }

    /**
     * Rotates a vector with this quaterion. It is expected that this quaternion is a unit quaternion.
     *
     * @param v The vector.
     * @return The rotated vector.
     */
    public float[] rotateVec(float[] v) {
        // R = Q.P.Q*
        Quaternion inv = this.invert().normalize();
        Quaternion result = this.mul(v).mul(inv);
        float[] vecNew = new float[3];
        vecNew[0] = result.x;
        vecNew[1] = result.y;
        vecNew[2] = result.z;
        return vecNew;
    }

    /**
     * Normalizes the quaternion.
     *
     * @return The normalized quaternion.
     */
    public Quaternion normalize() {
        float val = (float) Math.sqrt(w * w + x * x + y * y + z * z);
        return new Quaternion(x / val, y / val, z / val, w / val);
    }

    /**
     * Spherical linear interpolation between two quaternions.
     *
     * @param q2 The second quaternion.
     * @param f  A value from 0.0f to 1.0f. Specifies which quaternion inbetween the two quaternions should be calculated.
     * @return The interpolated quaternion.
     */
    public Quaternion slerp(Quaternion q2, float f) {
        Quaternion result;

        // check for out-of range parameter and return edge points if so
        if (f <= 0.0)
            return this;

        if (f >= 1.0)
            return q2;


        // compute dot product
        float cosOmega;
        cosOmega = x * q2.x + y * q2.y + z * q2.z + w * q2.w;

        // if dot is negative, use -this.
        result = this;

        if (cosOmega < 0.0f) {
            result.x = -result.x;
            result.y = -result.y;
            result.z = -result.z;
            result.w = -result.w;
            cosOmega = -cosOmega;
        }

        // compute interpolation fraction, checking for quaternions which are almost exactly the same
        float k0, k1;

        if (cosOmega > 0.9999f) {
            // very close - just use linear interpolation

            k0 = f;
            k1 = 1.0f - f;
        } else {
            // compute the sin of the angle using the trig identity sin^2(omega) + cos^2(omega) = 1
            float sinOmega = (float) Math.sqrt(1.0f - (cosOmega * cosOmega));

            // compute the angle from its sin and cosine
            float omega = (float) Math.atan2(sinOmega, cosOmega);

            // compute inverse of denominator, so we only have to divide once
            float invSinOmega = 1.0f / sinOmega;

            // Compute interpolation parameters
            k0 = (float) Math.sin(f * omega) * invSinOmega;
            k1 = (float) Math.sin((1.0f - f) * omega) * invSinOmega;
        }

        // interpolate and return new quaternion
        result.x = (q2.x * k0) + (result.x * k1);
        result.y = (q2.y * k0) + (result.y * k1);
        result.z = (q2.z * k0) + (result.z * k1);
        result.w = (q2.w * k0) + (result.w * k1);

        return result;
    }

    /**
     * Converts the quaterion in a 4x4 rotation matrix. It is expected that this quaternion is a unit quaternion.
     *
     * @return The matrix.
     */
    public Matrix4 toMatrix4() {
        Matrix4 matrix = Matrix4.createNew();
        float fXSq = x * x;
        float fYSq = y * y;
        float fZSq = z * z;

        // quaternion to matrix formula

        matrix.values[0] = 1.0f - 2.0f * fYSq - 2.0f * fZSq;
        matrix.values[1] = 2.0f * x * y + 2.0f * w * z;
        matrix.values[2] = 2.0f * x * z - 2.0f * w * y;
        matrix.values[3] = 0.0f;

        matrix.values[4] = 2.0f * x * y - 2.0f * w * z;
        matrix.values[5] = 1.0f - 2.0f * fXSq - 2.0f * fZSq;
        matrix.values[6] = 2.0f * y * z + 2.0f * w * x;
        matrix.values[7] = 0.0f;

        matrix.values[8] = 2.0f * x * z + 2.0f * w * y;
        matrix.values[9] = 2.0f * y * z - 2.0f * w * x;
        matrix.values[10] = 1.0f - 2.0f * fXSq - 2.0f * fYSq;
        matrix.values[11] = 0.0f;

        matrix.values[12] = matrix.values[13] = matrix.values[14] = 0.0f;        // no translation
        matrix.values[15] = 1.0f;
        return matrix;
    }

    public float[] toAxisAngle() {
        Quaternion q = this;
        if (q.w > 1.0f) q.normalize();

        float [] result = new float[4];

        result[3] = 2.0f * (float)Math.acos(q.w); // angle
        float den = (float)Math.sqrt(1.0 - q.w * q.w);
        if (den > 0.0001f) {
            result[0] = q.x / den;
            result[1] = q.y / den;
            result[2] = q.z / den;
        } else {
            // This occurs when the angle is zero.
            // Not a problem: just set an arbitrary normalized axis.
            result[0] = 1;
            result[1] = 0;
            result[2] = 0;
        }

        return result;
    }
}