package net.gtamps.android.graphics.graph.scene.animation;

/**
 * User: Jan Rabe
 * Date: 22/11/12
 * Time: 14:23
 */
final public class Interpolation {

    // utility class
    private Interpolation() {
    }

    private final static float[] temp = new float[3];

    public static float[] getLinearInterpolatedPoint(float x0, float y0, float z0, float x1, float y1, float z1, float percent) {
        temp[0] = x0 + ( x1 - x0 ) * percent;
        temp[1] = y0 + ( y1 - y0 ) * percent;
        temp[2] = z0 + ( z1 - z0 ) * percent;
        return temp;
    }
}
