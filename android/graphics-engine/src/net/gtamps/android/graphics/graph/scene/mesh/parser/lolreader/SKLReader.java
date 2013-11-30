package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

import net.gtamps.shared.Utils.Logger;

import java.io.IOException;

/**
 * Abstraction to read .skl files.
 * <p/>
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 10:13
 */
public class SKLReader {

    private static final String TAG = SKLReader.class.getSimpleName();

    // utility
    private SKLReader() {
    }

    public static SKLFile readBinary(String resourceID) {
        SKLFile result = new SKLFile();

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

    private static boolean readData(BinaryReader file, SKLFile data) {
        boolean result = true;

        try {

            //File Header Information.
            int size = file.readInt();
            data.id = file.readString(4); // TODO get correct file id
            //data.id = file.readString(SKLFile.ID_SIZE);

            data.version = file.readInt();

            if (data.version == 1 || data.version == 2) {

                data.designerID = file.readInt();

                // Read in the bones.
                data.numBones = file.readInt();
                for (int i = 0; i < data.numBones; ++i) {
                    SKLBone bone = new SKLBone();

                    bone.name = file.readString(SKLBone.BONE_NAME_SIZE);
                    bone.name = Utils.removeBoneNamePadding(bone.name);
//                    bone.name = bone.name.toLowerCase();

                    bone.ID = i;
                    bone.parentID = file.readInt();
                    bone.scale = file.readFloat();

                    // Read in transform matrix.
                    float[] orientation = new float[SKLBone.ORIENTATION_SIZE];
                    for (int j = 0; j < SKLBone.ORIENTATION_SIZE; ++j) {
                        orientation[j] = file.readFloat();
                    }

                    bone.orientation = orientation;

                    // Position from matrix.
                    bone.position[0] = orientation[3];
                    bone.position[1] = orientation[7];
                    bone.position[2] = orientation[11];

                    data.bones.add(bone);
                }

                // Version two contains bone IDs.
                if (data.version == 2) {
                    data.numBoneIDs = file.readInt();
                    for (int i = 0; i < data.numBoneIDs; ++i) {
                        data.boneIDs.add(file.readInt());
                    }
                }
            }
            // Newest version so far.
            else if (data.version == 0) {

                // Header
                int zero = file.readShort(); // ?

                data.numBones = file.readShort();

                data.numBoneIDs = file.readInt();
                int offsetToVertexData = file.readShort(); // Should be 64.

                int unknown = file.readShort(); // ?

                int offset1 = file.readInt();
                int offsetToAnimationIndices = file.readInt();
                int offset2 = file.readInt();
                int offset3 = file.readInt();
                int offsetToStrings = file.readInt();

                // Not sure what this data represents.
                // I think it's padding incase more header data is required later.
                file.skip(20);

                for (int i = 0; i < data.numBones; ++i) {
                    SKLBone bone = new SKLBone();
                    // The old scale was always 0.1.
                    // For now, just go with it.
                    bone.scale = 0.1f;

                    unknown = file.readShort(); // ?
                    bone.ID = file.readShort();
                    bone.parentID = file.readShort();
                    unknown = file.readShort(); // ?

                    int namehash = file.readInt();

                    float twoPointOne = file.readFloat();

                    bone.position[0] = file.readFloat();
                    bone.position[1] = file.readFloat();
                    bone.position[2] = file.readFloat();

                    float one = file.readFloat(); // ? Maybe scales for X, Y, and Z
                    one = file.readFloat();
                    one = file.readFloat();

                    bone.orientation[0] = file.readFloat();
                    bone.orientation[1] = file.readFloat();
                    bone.orientation[2] = file.readFloat();
                    bone.orientation[3] = file.readFloat();

                    float ctx = file.readFloat(); // ctx
                    float cty = file.readFloat(); // cty
                    float ctz = file.readFloat(); // ctz

                    data.bones.add(bone);

                    // The rest of the bone data is unknown. Maybe padding?
                    file.skip(32);
                }

                file.skipTo(offset1);
                for (int i = 0; i < data.numBones; ++i) // Inds for version 4 animation.
                {
                    // 8 bytes
                    int sklID = file.readInt();
                    int anmID = file.readInt();

                    data.boneIDMap.put(anmID, sklID);
                }

                file.skipTo(offsetToAnimationIndices);
                for (int i = 0; i < data.numBoneIDs; ++i) // Inds for animation
                {
                    // 2 bytes
                    int boneID = file.readShort();
                    data.boneIDs.add(boneID);
                }

                file.skipTo(offsetToStrings);
                Utils.readSklBoneNames(file, size - offsetToStrings, data.bones);
            }
            // Unknown Version
            else {
                Logger.e(TAG, "Unknown skl version: " + data.version);
                result = false;
            }
        } catch (Exception e) {
            Logger.e(TAG, "Skl reading error.");
            Logger.printException(TAG, e);
            result = false;
        }

        Logger.v(TAG, "File ID: " + data.id);
        Logger.v(TAG, "Version: " + data.version);
        Logger.v(TAG, "Designer ID: " + data.designerID);
        Logger.v(TAG, "Number of Bones: " + data.numBones);
        Logger.v(TAG, "Number of Bone IDs: " + data.numBoneIDs);

        return result;
    }
}
