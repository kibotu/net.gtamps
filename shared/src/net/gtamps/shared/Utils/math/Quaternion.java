package net.gtamps.shared.Utils.math;

import net.gtamps.shared.Utils.math.Vector3;

/**
 * User: Jan Rabe
 * Date: 24/11/12
 * Time: 14:53
 */
public class Quaternion {

    public float x;
    public float y;
    public float z;
    public float w;

    public Quaternion() {
    }

    public Vector3 asEulerAngles() {
        return Vector3.eulerAngles(this);
    }
}
