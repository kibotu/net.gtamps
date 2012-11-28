package net.gtamps.android.graphics.graph.scene.mesh.parser;

import android.content.res.Resources;
import android.graphics.Color;
import net.gtamps.android.graphics.graph.scene.mesh.Material;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.Vertex;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.FaceManager;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.UvBufferManager;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.Vector3BufferManager;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.VertexManager;
import net.gtamps.android.graphics.graph.scene.primitives.Cube;
import net.gtamps.android.graphics.graph.scene.primitives.Object3D;
import net.gtamps.android.graphics.utils.Registry;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

/**
 * User: Jan Rabe
 * Date: 26/11/12
 * Time: 20:18
 */
public class SknParser extends AParser {

    public static final String TAG = SknParser.class.getSimpleName();
    private SknMaterial[] sknMaterials;
    private SknVertex [] sknVertices;
    private FaceManager faceManager;

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
                long endTime = Calendar.getInstance().getTimeInMillis();

                // check length
                int minLength = 8;
                int readBytes = 0;
                int length = input.available();
//                Logger.v(TAG, "length=" + length);
                if (length < minLength) Logger.e(TAG, "File is empty: " + length + " < " + minLength);

                // check magic
                int magic = readInt(input);
                readBytes += 4;
                if (magic != 0x00112233) Logger.e(TAG, "Skn magic is wrong!");
//                Logger.v(TAG, "magic=" + magic);

                // get version
                int version = readShort(input);
                readBytes += 2;
//                Logger.v(TAG, "version=" + version);
                if (version > 2) Logger.e(TAG, "skn type not supported");

                // get num obj
                int numObjects = readShort(input);
                readBytes += 2;
//                Logger.v(TAG, "numObjects=" + numObjects);
                if (numObjects != 1) Logger.e(TAG, " more than 1 or no objects in the file.");

                // get materials
                if (version == 1 || version == 2) {
                    minLength += 4;
                    if (length < minLength) Logger.e(TAG, "unexpected end of file");

                    int numMaterials = readInt(input);
                    readBytes += 4;
//                    Logger.v(TAG, "numMaterials=" + numMaterials);

                    minLength += SknMaterial.kSizeInFile * numMaterials;
                    if (length < minLength) Logger.e(TAG, "unexpected end of file");

                    sknMaterials = new SknMaterial[numMaterials];
                    for (int i = 0; i < numMaterials; ++i) {
                        SknMaterial material = readSknMaterial(input);
                        sknMaterials[i] = material;
                        readBytes += SknMaterial.kSizeInFile;
//                        Logger.v(TAG, material);
                    }
                }

                // check minimum length
                minLength += 8;
                if (length < minLength) Logger.e(TAG, "unexpected end of file");

                // get numIndices
                int numIndices = readInt(input);
                readBytes += 4;
//                Logger.v(TAG, "numIndices="+numIndices);

                // get numIndices
                int numVertices = readInt(input);
                readBytes += 4;
//                Logger.v(TAG, "numVertices="+numVertices);

                // sanity check
                if (numIndices % 3 != 0) Logger.v(TAG, "numIndices % 3 != 0 ...");

                // check minimum length
                minLength += 2 * numIndices + SknVertex.kSizeInFile * numVertices;
                if (length < minLength) Logger.e(TAG, "unexpected end of file: " + length + "<" + minLength);

                // get indices
                faceManager = new FaceManager(numIndices/3);
                int num_triangles = numIndices / 3;
                for (int i = 0; i < num_triangles; ++i) {
                    int [] indices = new int[3];
                    indices[0] = readShort(input);
                    indices[1] = readShort(input);
                    indices[2] = readShort(input);
                    readBytes += 6;

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
                        faceManager.add(indices[0],indices[1],indices[2]);
                    }
                }

                sknVertices = new SknVertex[numVertices];
                // get vertices
                for (int i = 0; i < numVertices; ++i) {
                    sknVertices[i] = readSknVertex(input);
                    readBytes += SknVertex.kSizeInFile;
                    if(isEmpty(sknVertices[i])) {
                        Logger.i(TAG, i + " is empty");
                        Logger.v(TAG, sknVertices[i-1]);
                        break;
                    }
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

    private SknMaterial readSknMaterial(@NotNull final DataInputStream input) throws IOException {
        return new SknMaterial(readString(input, SknMaterial.kNameLen), readInt(input),readInt(input),readInt(input),readInt(input));
    }

    private boolean isEmpty(SknVertex sknVertex) {
        if(sknVertex.x != 0) return false;
        if(sknVertex.y != 0) return false;
        if(sknVertex.z != 0) return false;
        if(sknVertex.sknIndices == null) return false;
        if(sknVertex.weights[0] != 0) return false;
        if(sknVertex.weights[1] != 0) return false;
        if(sknVertex.weights[2]  != 0) return false;
        if(sknVertex.weights[3]  != 0) return false;
        if(sknVertex.normal[0]  != 0) return false;
        if(sknVertex.normal[1]  != 0) return false;
        if(sknVertex.normal[2] != 0) return false;
        if(sknVertex.u  != 0) return false;
        if(sknVertex.v  != 0) return false;
//        if(sknVertex.sklIndices[0] != 0) return false;
//        if(sknVertex.sklIndices[1]  != 0) return false;
//        if(sknVertex.sklIndices[2]  != 0) return false;
//        if(sknVertex.sklIndices[3]  != 0) return false;
//        if(sknVertex.uvIndex  != 0) return false;
//        if(sknVertex.dupeDataIndex  != 0) return false;
        return true;
    }

    private SknVertex readSknVertex(@NotNull DataInputStream input) throws IOException {
        SknVertex sknVertex = new SknVertex();
        sknVertex.x = readFloat(input);                 // read 4 bytes     | 4       4
        sknVertex.y = readFloat(input);                 // read 4 bytes     | 8       8
        sknVertex.z = readFloat(input);                 // read 4 bytes     | 12      12
        sknVertex.sknIndices = readString(input, 4);     // read 8 bytes     | 20     16
        sknVertex.weights[0] = readFloat(input);        // read 4 bytes     | 24      20
        sknVertex.weights[1] = readFloat(input);        // read 4 bytes     | 28      24
        sknVertex.weights[2] = readFloat(input);        // read 4 bytes     | 32      28
        sknVertex.weights[3] = readFloat(input);        // read 4 bytes     | 36      32
        sknVertex.normal[0] = readFloat(input);         // read 4 bytes     | 40      36
        sknVertex.normal[1] = readFloat(input);         // read 4 bytes     | 44      40
        sknVertex.normal[2] = readFloat(input);         // read 4 bytes     | 48      44
        sknVertex.u = readFloat(input);                 // read 4 bytes     | 52      48
        sknVertex.v = readFloat(input);                 // read 4 bytes     | 56      52
//        sknVertex.sklIndices[0] = readInt(input);       // read 4 bytes     | 60
//        sknVertex.sklIndices[1] = readInt(input);       // read 4 bytes     | 64
//        sknVertex.sklIndices[2] = readInt(input);       // read 4 bytes     | 68
//        sknVertex.sklIndices[3] = readInt(input);       // read 4 bytes     | 72
//        sknVertex.uvIndex = readInt(input);             // read 4 bytes     | 76
//        sknVertex.dupeDataIndex = readInt(input);       // read 4 bytes     | 80
        return sknVertex;                               // totally read bytes 80

//        sklIndices, uvIndex  und dupeDataIndex werden nicht gelesen.
    }

    private boolean flipU = true;
    private boolean flipV = false;

    @Override
    public void getParsedObject(@NotNull Object3D object3D) {

        final long startTime = Calendar.getInstance().getTimeInMillis();
        Logger.i(TAG, "\nStarting to build object.\n\n");

        Mesh mesh = new Mesh(faceManager.size(), sknVertices.length);

        Color4 c = Material.DEFAULT.getEmission();
        // set vertices data
        for (int i = 0; i < sknVertices.length; ++i) {
            SknVertex vtx = sknVertices[i];
            final float u = vtx.u;
            final float v = 1 - vtx.v;
            if (u > 1) Logger.e(TAG, "u out of bound (>1): " + u + " "+ i);
            if (u < 0) Logger.e(TAG, "u out of bound (<0): " + u + " "+ i);
            if (v > 1) Logger.e(TAG, "v out of bound (>1): " + v + " "+ i);
            if (v < 0) Logger.e(TAG, "v out of bound (<0): " + v + " "+ i);
            mesh.addVertex(vtx.x, vtx.y, vtx.z, vtx.normal[0], vtx.normal[1], vtx.normal[2],c.r,c.g,c.b,c.a , u * (flipU ? 1f : -1f), v  * (flipV ? 1f : -1f));
        }

        mesh.faces = faceManager;


//        Cube cube = new Cube();
//        cube.onCreateInternal(Registry.getRenderer().getGl10());
//        object3D.setMesh(cube.getMesh());

        object3D.setMesh(mesh);
        object3D.onCreateInternal(Registry.getRenderer().getGl10());

        long endTime = Calendar.getInstance().getTimeInMillis();
        Logger.i(TAG, "\nSKN Object successfully created in " + (endTime - startTime) + "ms.\n");

    }
}
