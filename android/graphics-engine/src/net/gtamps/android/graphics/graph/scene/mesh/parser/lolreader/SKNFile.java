package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores content of a .skn File
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 09:52
 */
public class SKNFile {

    // File Header Data
    public int magic;  // check if it is correct file
    public int version;
    public int numObjects;

    // Some versions have material header information.
    public int numMaterialHeaders;
    public List<SKNMaterial> materialHeaders;

    // Actual model information.
    public int numIndices;
    public int numVertices;
    public List<Integer> indices;
    public List<SKNVertex> vertices;

    // Contained in version two.
    public List<Integer> endTab; // check if it is really end of file content

    public SKNFile() {
        magic = version = numObjects = 0;

        numMaterialHeaders = 0;
        materialHeaders = new ArrayList<SKNMaterial>(1);

        numIndices = numVertices = 0;
        indices = new ArrayList<Integer>();
        vertices = new ArrayList<SKNVertex>();

        endTab = new ArrayList<Integer>();
    }
}
