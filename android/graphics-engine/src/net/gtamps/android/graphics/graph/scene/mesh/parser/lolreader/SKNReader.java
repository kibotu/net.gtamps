package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

import net.gtamps.shared.Utils.Logger;

import java.io.IOException;

/**
 * Abstraction to read .skn files.
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 10:14
 */
public class SKNReader {

    private static final String TAG = SKNReader.class.getSimpleName();

    // utility
    private SKNReader() {
    }

    public static SKNFile readBinary(String resourceID) {
        SKNFile result = new SKNFile();

        try {
            BinaryReader file = new BinaryReader(resourceID);
            readData(file, result);
            file.close();
        } catch (IOException e) {
            Logger.e(TAG, "Unable to open binary reader.");
            Logger.printException(TAG, e);
        }
        return result;
    }

    private static boolean readData(BinaryReader file, SKNFile data) {
        boolean result = true;

        try {
            // File Header Information.
            data.magic = file.readInt();
            data.version = file.readShort();
            data.numObjects = file.readShort();

            if (data.version == 1 || data.version == 2) {
                // Contains material headers.
                data.numMaterialHeaders = file.readInt();
                for (int i = 0; i < data.numMaterialHeaders; ++i) {
                    // Read in the headers.
                    SKNMaterial header = new SKNMaterial();

                    header.name = file.readString(SKNMaterial.MATERIAL_NAME_SIZE);
                    header.startVertex = file.readInt();
                    header.numVertices = file.readInt();
                    header.startIndex = file.readInt();
                    header.numIndices = file.readInt();

                    data.materialHeaders.add(header);
                }

                // Read in model data.
                data.numIndices = file.readInt();
                data.numVertices = file.readInt();

                for (int i = 0; i < data.numIndices; ++i) {
                    data.indices.add(file.readShort());
                }

                for (int i = 0; i < data.numVertices; ++i) {
                    SKNVertex vertex = new SKNVertex();

                    vertex.position[0] = file.readFloat(); // x
                    vertex.position[1] = file.readFloat(); // y
                    vertex.position[2] = file.readFloat(); // z

                    for (int j = 0; j < SKNVertex.BONE_INDEX_SIZE; ++j) {
                        int bone = (int) file.readChar();
                        vertex.boneIndex[j] = bone;
                    }

                    vertex.weights[0] = file.readFloat();
                    vertex.weights[1] = file.readFloat();
                    vertex.weights[2] = file.readFloat();
                    vertex.weights[3] = file.readFloat();

                    vertex.normal[0] = file.readFloat(); // x
                    vertex.normal[1] = file.readFloat(); // y
                    vertex.normal[2] = file.readFloat(); // z

                    vertex.uv[0] = file.readFloat(); // u
                    vertex.uv[1] = file.readFloat(); // v

                    data.vertices.add(vertex);
                }

                // Data exclusive to version two.
                if (data.version == 2) {
                    data.endTab.add(file.readInt());
                    data.endTab.add(file.readInt());
                    data.endTab.add(file.readInt());
                }
            }
            // Unknown Version
            else {
                Logger.e(TAG, "Unknown skn version: " + data.version);
                result = false;
            }
        } catch (IOException e) {
            Logger.e(TAG, "Skn reading error.");
            Logger.printException(TAG, e);
            result = false;
        }

//        Logger.v(TAG, "Magic: " + data.magic);
        Logger.v(TAG, "Version: " + data.version);
        Logger.v(TAG, "Number of Objects: " + data.numObjects);
        Logger.v(TAG, "Number of Material Headers: " + data.numMaterialHeaders);
        Logger.v(TAG, "Number of Vertices: " + data.numVertices);
        Logger.v(TAG, "Number of Indices: " + data.numIndices);

        return result;
    }
}
