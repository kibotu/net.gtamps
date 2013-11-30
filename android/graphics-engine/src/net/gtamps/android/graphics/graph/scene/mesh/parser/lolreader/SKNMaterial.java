package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

/**
 * Abstraction for a material used in .skn files.
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 09:58
 */
public class SKNMaterial {

    public String name;
    public static final int MATERIAL_NAME_SIZE = 64; // in bytes

    public int startVertex;
    public int numVertices;
    public int startIndex;
    public int numIndices;

    public SKNMaterial() {
        name = "";
        startVertex = numVertices = startIndex = numIndices = 0;
    }
}
