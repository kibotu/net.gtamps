package net.gtamps.android.graphics.graph.scene.mesh.parser;

/**
 * User: Jan Rabe
 * Date: 26/11/12
 * Time: 22:25
 */
public class SknVertex {

//    public static final int kSizeInFile = 0x50; // 80
    public static final int kSizeInFile = 0x34;

    public float x;
    public float y;
    public float z;
    public int sknIndices1;
    public int sknIndices2;
    public int sknIndices3;
    public int sknIndices4;
    public float weightsX;
    public float weightsY;
    public float weightsZ;
    public float weightsW;
    public float normalX;
    public float normalY;
    public float normalZ;
    public float u;
    public float v;
//    public int [] sklIndices;
//    public int uvIndex;
//    public int dupeDataIndex;

    public SknVertex() {
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("SknVertex");
        sb.append("{x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", z=").append(z);
        sb.append(", sknIndices1=").append(sknIndices1);
        sb.append(", sknIndices2=").append(sknIndices2);
        sb.append(", sknIndices3=").append(sknIndices3);
        sb.append(", sknIndices4=").append(sknIndices4);
        sb.append(", weightsX=").append(weightsX);
        sb.append(", weightsY=").append(weightsY);
        sb.append(", weightsZ=").append(weightsZ);
        sb.append(", weightsW=").append(weightsW);
        sb.append(", normalX=").append(normalX);
        sb.append(", normalY=").append(normalY);
        sb.append(", normalZ=").append(normalZ);
        sb.append(", u=").append(u);
        sb.append(", v=").append(v);
        sb.append('}');
        return sb.toString();
    }
}
