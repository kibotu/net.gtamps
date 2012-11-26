package net.gtamps.android.graphics.graph.scene.mesh.parser;

import android.content.res.Resources;
import net.gtamps.android.graphics.graph.scene.mesh.Material;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.primitives.Cube;
import net.gtamps.android.graphics.graph.scene.primitives.Object3D;
import net.gtamps.android.graphics.utils.Registry;
import net.gtamps.shared.Utils.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * User: Jan Rabe
 * Date: 26/11/12
 * Time: 20:18
 */
public class SknParser extends AParser {

    public static final String TAG = SknParser.class.getSimpleName();
    private SknMaterial[] sknMaterials;
    private ArrayList<Short> indicesList;
    private SknVertex [] sknVertices;

    public SknParser(String resourceID, Boolean generateMipMap) {
        super(resourceID, generateMipMap);
    }

    @Override
    public void parse() {
        final long startTime = Calendar.getInstance().getTimeInMillis();
        String packageID = "";
        if (resourceID.contains(":")) packageID = resourceID.split(":")[0];
        final Resources resources = Registry.getContext().getResources();

        try {
            DataInputStream input = null;
            try {
                input = new DataInputStream(new BufferedInputStream(resources.openRawResource(resources.getIdentifier(resourceID, null, packageID))));

                int readBytes = 0;

                // check length
                int minLength = 8;
                int length = input.available();
                Logger.v(TAG, "length=" + length);
                if (length < minLength) Logger.e(TAG, "File is empty: " + length + " < " + minLength);

                // check magic
                int magic = readInt(input);
                readBytes += 4;
                if (magic != 0x00112233) Logger.e(TAG, "Skn magic is wrong!");

                // get version
                short version = readShort(input);
                readBytes += 2;
                Logger.v(TAG, "version=" + version);
                if (version > 2) Logger.e(TAG, "skn type not supported");

                // get num obj
                short numObjects = readShort(input);
                readBytes += 2;
                Logger.v(TAG, "numObjects=" + numObjects);
                if (numObjects != 1) Logger.e(TAG, " more than 1 or no objects in the file.");

                // get materials
                if (version == 1 || version == 2) {
                    minLength += 4;
                    if (length < minLength) Logger.e(TAG, "unexpected end of file");

                    // get num materials
                    int numMaterials = readInt(input);
                    readBytes += 4;
                    Logger.v(TAG, "numMaterials=" + numMaterials);

                    minLength += SknMaterial.kSizeInFile * numMaterials;
                    if (length < minLength) Logger.e(TAG, "unexpected end of file");

                    // get materials
                    sknMaterials = new SknMaterial[numMaterials];
                    for (int i = 0; i < numMaterials; ++i) {
                        sknMaterials[i] = new SknMaterial(readString(input,SknMaterial.kNameLen),readInt(input),readInt(input),readInt(input),readInt(input));
                        readBytes += SknMaterial.kNameLen + 4 * 4;
                        Logger.i(TAG, sknMaterials[i]);
                    }
                }

                // check minimum length
                minLength += 8;
                if (length < minLength) Logger.e(TAG, "unexpected end of file");

                // get numIndices
                int numIndices = readInt(input);
                readBytes += 4;
                Logger.v(TAG, "numIndices=" + numIndices);

                // get numVertices
                int numVertices = readInt(input);
                readBytes += 4;
                Logger.v(TAG, "numVertices=" + numVertices);

                if (numIndices % 3 != 0) Logger.v(TAG, "num_indices % 3 != 0 ...");

                // check minimum length
                minLength += 2 * numIndices + SknVertex.kSizeInFile * numVertices;
                if (length < minLength) Logger.v(TAG, "weird warning: unexpected end of file " + length + " < " + minLength );

                // get indices
                indicesList = new ArrayList<Short>(numIndices);
                int numTriangles = numIndices / 3;
                for (int i = 0; i < numTriangles; ++i) {
                    short [] indices = new short[3];
                    indices[0] = readShort(input);
                    indices[1] = readShort(input);
                    indices[2] = readShort(input);
                    readBytes += 3 * 2;

                    // check if that can build a triangle
                    if (indices[0] == indices[1] ||
                            indices[0] == indices[2] ||
                            indices[1] == indices[2] ||
                            indices[0] < 0 ||
                            indices[0] >= numVertices ||
                            indices[1] < 0 ||
                            indices[1] >= numVertices ||
                            indices[2] < 0 ||
                            indices[2] >= numVertices) {
                        Logger.e(TAG, "input mesh has a badly built triangle, removing it...");
                        numIndices -= 3;
                    } else {
                        indicesList.add(indices[0]);
                        indicesList.add(indices[1]);
                        indicesList.add(indices[2]);
                    }
                }

                sknVertices = new SknVertex[numVertices];
                // get vertices
                for (int i = 0; i < numVertices; i++) {
                    sknVertices[i] = readSknVertex(input);
                    readBytes += SknVertex.kSizeInFile;
                }

                // get endtab
                int [] endTab = new int[3];
                if (version == 2) {
                    endTab[0] = readInt(input);
                    endTab[1] = readInt(input);
                    endTab[2] = readInt(input);
                    readBytes += 4 * 3;
                    Logger.v(TAG, "endtab=[" + endTab[0] + "|"  + endTab[1] + "|" + endTab[2] + "]" );
                }

//                data_.switchHand();

                long endTime = Calendar.getInstance().getTimeInMillis();
                Logger.i(TAG, "[" + resourceID + "|"+readBytes+"/"+length+"bytes] Successfully loaded in " + (endTime - startTime) + "ms.");

            } finally {
//                Logger.v(TAG, "Closing input stream.");
                if (input != null) {
                    input.close();
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.e(TAG, "File not found.");
            Logger.printException(TAG, ex);
        } catch (IOException ex) {
            Logger.printException(TAG, ex);
        }
    }

    private SknVertex readSknVertex(@NotNull DataInputStream input) throws IOException {
        SknVertex sknVertex = new SknVertex();
        sknVertex.x = readFloat(input);                 // read 4 bytes     | 4
        sknVertex.y = readFloat(input);                 // read 4 bytes     | 8
        sknVertex.z = readFloat(input);                 // read 4 bytes     | 12
        sknVertex.sknIndices[0] = readChar(input);      // read 2 bytes     | 14
        sknVertex.sknIndices[1] = readChar(input);      // read 2 bytes     | 16
        sknVertex.sknIndices[2] = readChar(input);      // read 2 bytes     | 18
        sknVertex.sknIndices[3] = readChar(input);      // read 2 bytes     | 20
        sknVertex.weights[0] = readFloat(input);        // read 4 bytes     | 24
        sknVertex.weights[1] = readFloat(input);        // read 4 bytes     | 28
        sknVertex.weights[2] = readFloat(input);        // read 4 bytes     | 32
        sknVertex.weights[3] = readFloat(input);        // read 4 bytes     | 36
        sknVertex.normal[0] = readFloat(input);         // read 4 bytes     | 40
        sknVertex.normal[1] = readFloat(input);         // read 4 bytes     | 44
        sknVertex.normal[2] = readFloat(input);         // read 4 bytes     | 48
        sknVertex.u = readFloat(input);                 // read 4 bytes     | 52
        sknVertex.v = readFloat(input);                 // read 4 bytes     | 56
        sknVertex.sklIndices[0] = readInt(input);       // read 4 bytes     | 60
        sknVertex.sklIndices[1] = readInt(input);       // read 4 bytes     | 64
        sknVertex.sklIndices[2] = readInt(input);       // read 4 bytes     | 68
        sknVertex.sklIndices[3] = readInt(input);       // read 4 bytes     | 72
        sknVertex.uvIndex = readInt(input);             // read 4 bytes     | 76
        sknVertex.dupeDataIndex = readInt(input);       // read 4 bytes     | 80
        return sknVertex;                               // totally read bytes 80
    }

    @Override
    public void getParsedObject(@NotNull Object3D object3D) {
        Cube cube = new Cube();
        cube.onCreateInternal(Registry.getRenderer().getGl10());
        object3D.setMesh(cube.getMesh());
    }
}
