package net.gtamps.android.graphics.graph.scene.mesh.parser;

/**
 * User: Jan Rabe
 * Date: 26/11/12
 * Time: 22:05
 */
public class SknMaterial {

    public static final int kSizeInFile = 0x50; // 80
    public static final int kNameLen = 0x40; // 64

    public SknMaterial(String name, int startVertex, int numVertices, int startIndex, int numIndices) {
        this.name = name;
        this.startVertex = startVertex;
        this.numVertices = numVertices;
        this.startIndex = startIndex;
        this.numIndices = numIndices;
    }

    public String name;
    public int startVertex;
    public int numVertices;
    public int startIndex;
    public int numIndices;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("SknMaterial");
        sb.append("{name='").append(name).append('\'');
        sb.append(", startVertex=").append(startVertex);
        sb.append(", numVertices=").append(numVertices);
        sb.append(", startIndex=").append(startIndex);
        sb.append(", numIndices=").append(numIndices);
        sb.append('}');
        return sb.toString();
    }
}
