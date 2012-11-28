package net.gtamps.android.graphics.graph.scene.mesh.parser;

/**
 * User: Jan Rabe
 * Date: 26/11/12
 * Time: 22:25
 */
public class SknVertex {

//    public static final int kSizeInFile = 0x50; // 80
    public static final int kSizeInFile = 0x34;

    public SknVertex() {
        weights = new float[4];
        normal = new float[3];
        sklIndices = new int[4];
    }

    public float x;
    public float y;
    public float z;
    public String sknIndices;
    public float [] weights;
    public float [] normal;
    public float u;
    public float v;
    public int [] sklIndices;
    public int uvIndex;
    public int dupeDataIndex;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("SknVertex");
        sb.append("{x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", z=").append(z);
        sb.append(", sknIndices='").append(sknIndices).append('\'');
        sb.append(", weights=").append(weights == null ? "null" : "");
        for (int i = 0; weights != null && i < weights.length; ++i)
            sb.append(i == 0 ? "" : ", ").append(weights[i]);
        sb.append(", normal=").append(normal == null ? "null" : "");
        for (int i = 0; normal != null && i < normal.length; ++i)
            sb.append(i == 0 ? "" : ", ").append(normal[i]);
        sb.append(", u=").append(u);
        sb.append(", v=").append(v);
        sb.append(", sklIndices=").append(sklIndices == null ? "null" : "");
        for (int i = 0; sklIndices != null && i < sklIndices.length; ++i)
            sb.append(i == 0 ? "" : ", ").append(sklIndices[i]);
        sb.append(", uvIndex=").append(uvIndex);
        sb.append(", dupeDataIndex=").append(dupeDataIndex);
        sb.append('}');
        return sb.toString();
    }
}
