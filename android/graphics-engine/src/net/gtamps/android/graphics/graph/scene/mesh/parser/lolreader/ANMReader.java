package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

import net.gtamps.shared.Utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Abrstraction to read .anm files.
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 10:14
 */
public class ANMReader {

    private static final String TAG = ANMReader.class.getSimpleName();

    private ANMReader() {
    }

    public static ANMFile readBinary(String resourceID) {
        ANMFile result = new ANMFile();

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

    private static boolean readData(BinaryReader file, ANMFile data) {
        boolean result = true;

        try {
            // File Header Information.
            data.id = file.readString(ANMFile.ID_SIZE);
            data.version = file.readInt();

            // Version 0, 1, 2, 3 Code
            if (data.version == 0 ||
                    data.version == 1 ||
                    data.version == 2 ||
                    data.version == 3) {
                //
                // Header information specific to these versions.
                //

                data.magic = file.readInt();
                data.numberOfBones = file.readInt();
                data.numberOfFrames = file.readInt();
                data.playbackFPS = file.readInt();

                // Read in all the bones
                for (int i = 0; i < data.numberOfBones; ++i) {
                    ANMBone bone = new ANMBone();
                    bone.name = file.readString(ANMBone.BONE_NAME_LENGTH);
                    bone.name = LolReaderUtils.removeBoneNamePadding(bone.name);
//                    bone.name = bone.name.toLowerCase();

                    // Unknown
                    file.readInt();

                    // For each bone, read in its value at each frame in the animation.
                    for (int j = 0; j < data.numberOfFrames; ++j) {
                        ANMFrame frame = new ANMFrame();

                        // Read in the frame's quaternion.
                        frame.orientation[0] = file.readFloat(); // x
                        frame.orientation[1] = file.readFloat(); // y
                        frame.orientation[2] = file.readFloat(); // z
                        frame.orientation[3] = file.readFloat(); // w

                        // Read in the frame's position.
                        frame.position[0] = file.readFloat(); // x
                        frame.position[1] = file.readFloat(); // y
                        frame.position[2] = file.readFloat(); // z

                        bone.frames.add(frame);
                    }

                    data.bones.add(bone);
                }
            }
            // Version 4 Code
            else if (data.version == 4) {
                //
                // Based on the reverse engineering work of Hossein Ahmadi.
                //
                // In this version, position vectors and orientation quaternions are
                // stored separately in sorted, keyed blocks.  The assumption is Riot
                // is removing duplicate vectors and quaternions by using an indexing scheme
                // to look up values.
                //
                // So, after the header, there are three data sections: a vector section, a quaternion
                // section, and a look up section.  The number of vectors and quaternions
                // may not match the expected value based on the number of frames and bones.  However,
                // the number of look ups should match this value and can be used to create the animation.

                //
                // Header information specific to version 4.
                //

                data.magic = file.readInt();

                // Not sure what any of these mean.
                float unknown = file.readFloat();
                unknown = file.readFloat();
                unknown = file.readFloat();

                data.numberOfBones = file.readInt();
                data.numberOfFrames = file.readInt();

                // Time per frame is stored in this file type.  Need to invert it into FPS.
                data.playbackFPS = (int) Math.round(1.0f / file.readFloat());

                // These are offsets to specific data sections in the file.
                int unknownOffset = file.readInt();
                unknownOffset = file.readInt();
                unknownOffset = file.readInt();

                int positionOffset = file.readInt();
                int orientationOffset = file.readInt();
                int indexOffset = file.readInt();

                // These last three values are confusing.
                // They aren't a vector and they throw off the offset values
                // by 12 bytes. Just ignore them and keep reading.
                unknownOffset = file.readInt();
                unknownOffset = file.readInt();
                unknownOffset = file.readInt();

                //
                // Vector section.
                //

                List<Float> positions = new ArrayList<Float>();
                int numberOfPositions = (orientationOffset - positionOffset) / LolReaderUtils.BYTESIZE_OF_FLOAT;
                for (int i = 0; i < numberOfPositions; ++i) {
                    positions.add(file.readFloat());
                }

                //
                // Quaternion section.
                //

                List<Float> orientations = new ArrayList<Float>();
                int numberOfOrientations = (indexOffset - orientationOffset) / LolReaderUtils.BYTESIZE_OF_FLOAT;
                for (int i = 0; i < numberOfOrientations; ++i) {
                    orientations.add(file.readFloat());
                }

                //
                // Offset section.
                //
                // Note: Unlike versions 0-3, data in this version is
                // Frame 1:
                //      Bone 1:
                //      Bone 2:
                // ...
                // Frame 2:
                //      Bone 1:
                // ...
                //

                HashMap<Integer, ANMBone> boneMap = new HashMap<Integer, ANMBone>();
                for (int i = 0; i < data.numberOfBones; ++i) {
                    //
                    // The first frame is a special case since we are allocating bones
                    // as we read them in.
                    //

                    // Read in the offset data.
                    int boneID = file.readInt();
                    int positionID = file.readShort();
                    int unknownIndex = file.readShort(); // Unknown.
                    int orientationID = file.readShort();
                    unknownIndex = file.readShort(); // Unknown. Seems to always be zero.

                    // Allocate the bone.
                    ANMBone bone = new ANMBone();
                    bone.id = boneID;

                    // Allocate all the frames for the bone.
                    for (int j = 0; j < data.numberOfFrames; ++j) {
                        bone.frames.add(new ANMFrame());
                    }

                    // Retrieve the data for the first frame.
                    ANMFrame frame = bone.frames.get(0);
                    frame.position = LolReaderUtils.lookUpVector(positionID, positions);
                    frame.orientation = LolReaderUtils.lookUpQuaternion(orientationID, orientations);

                    // Store the bone in the dictionary by bone ID.
                    boneMap.put(boneID, bone); // TODO check against possibly overwriting bone at index
                }

                int currentFrame = 1;
                int currentBone = 0;

                int numberOfLookUps = (data.numberOfFrames - 1) * data.numberOfBones;
                for (int i = 0; i < numberOfLookUps; ++i) {
                    //
                    // Normal case for all frames after the first.
                    //

                    // Read in the offset data.

                    int boneID = file.readInt();
                    int positionID = file.readShort();
                    int unknownIndex = file.readShort(); // Unknown.
                    int orientationID = file.readShort();
                    unknownIndex = file.readShort(); // Unknown. Seems to always be zero.

                    // Retrieve the bone from the dictionary.
                    // Note: The bones appear to be in the same order in every frame.  So, a dictionary
                    // isn't exactly needed and you could probably get away with a list.  However, this way
                    // feels safer just in case something ends up being out of order.
                    ANMBone bone = boneMap.get(boneID);
                    ANMFrame frame = bone.frames.get(currentFrame);
                    frame.position = LolReaderUtils.lookUpVector(positionID, positions);
                    frame.orientation = LolReaderUtils.lookUpQuaternion(orientationID, orientations);

                    // This loop is slightly ambiguous.
                    //
                    // The problem is previous .anm versions contain data like:
                    // foreach bone
                    //      foreach frame
                    //
                    // However, this version contains data like:
                    // foreach frame
                    //      foreach bone
                    //
                    // So, reading one version is going to be a little goofy.
                    currentBone++;
                    if (currentBone >= data.numberOfBones) {
                        currentBone = 0;
                        currentFrame++;
                    }
                }

                // Finally, we need to move all the data from the dictionary into the ANMFile.
                for (ANMBone bone : boneMap.values()) {
                    data.bones.add(bone);
                }

                // Currently returning false for this version.  We can not render this version correctly yet.
                // So, we need to tell the viewer not to try and load it.
                result = false;
            }
            // Unknown version
            else {
                Logger.e(TAG, "Unknown anm version: " + data.version);
                result = false;
            }
        } catch (Exception e) {
            Logger.e(TAG, "Anm reading error.");
            Logger.printException(TAG, e);
            result = false;
        }

        Logger.v(TAG, "File ID: " + data.id);
//        Logger.v(TAG, "Magic: " + data.magic);
        Logger.v(TAG, "Version: " + data.version);
        Logger.v(TAG, "Number of Bones: " + data.numberOfBones);
        Logger.v(TAG, "Number of Frames: " + data.numberOfFrames);
        Logger.v(TAG, "Playback FPS: " + data.playbackFPS);

        return result;
    }
}