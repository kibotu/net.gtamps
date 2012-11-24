package net.gtamps.android.graphics.graph.scene.mesh.parser;

import android.content.res.Resources;
import net.gtamps.android.graphics.graph.scene.animation.skeleton.AnimatedSkeletonObject3D;
import net.gtamps.android.graphics.graph.scene.animation.skeleton.BoneKeyFrame;
import net.gtamps.android.graphics.graph.scene.animation.skeleton.RiotAnimation;
import net.gtamps.android.graphics.utils.Registry;
import net.gtamps.shared.Utils.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * User: Jan Rabe
 * Date: 24/11/12
 * Time: 12:05
 */
public class SkeletonAnimationParser {

    private static final String TAG = SkeletonAnimationParser.class.getSimpleName();

    public final static int kSizeInFile = 0x1C; // 28
    public final static int kHeaderSize = 0x24; // 36
    public final static int kNameLen = 0x20;    // 32

    // utility class
    private SkeletonAnimationParser() {
    }

    public static void loadAnimation(String resourceID, @NotNull AnimatedSkeletonObject3D object3D) {
        String packageID = "";
        if (resourceID.indexOf(":") > -1) packageID = resourceID.split(":")[0];
        Logger.v(TAG, "Reading in binary file named : " + packageID + ":" + resourceID);
        final Resources resources = Registry.getContext().getResources();

        RiotAnimation riotAnimation = null;

        try {
            DataInputStream input = null;
            try {
                input = new DataInputStream(new BufferedInputStream(resources.openRawResource(resources.getIdentifier(resourceID, null, packageID))));

                // check length
                int minLength = 28;
                int length = input.available();
                Logger.v(TAG, "length=" + length);
                if (length < minLength) Logger.e(TAG, "File is empty: " + length + " < " + minLength);

                // magic
                String magic = readString(input, 8);
                Logger.v(TAG, "magic=" + magic);

                // version
                int version = readInt(input);
                Logger.v(TAG, "version=" + version);

                if (version == 3) {

                    // get designer ID
                    int designerId = readInt(input);
                    Logger.v(TAG, "designerId=" + designerId);

                    // get numBones
                    int numBones = readInt(input);
                    Logger.v(TAG, "numBones=" + numBones);

                    // get numFrames
                    int numFrames = readInt(input);
                    Logger.v(TAG, "numFrames=" + numFrames);

                    // get fps (algorithm seen during the reversing)
                    float fps;
                    float ffps = readFloat(input);
                    if (ffps < 0.0f)
                        fps = ffps + 4294967296.0f;
                    else
                        fps = ffps;
                    Logger.v(TAG, "ffps=" + ffps + "|fps="+fps);

                    // check minimum length
                    minLength += numBones * numFrames * kSizeInFile + numBones * kHeaderSize;
                    if (length < minLength) Logger.e(TAG, "Unexpected end of file: " + length + " < " + minLength);

                    riotAnimation = new RiotAnimation(numBones,numFrames, 30, version);

                    // get bones with frames
                    for (int i = 0; i < numBones; ++i) {
                        String boneName = readString(input, kNameLen);
                        int rootBone = readInt(input); // 2 = root
//                        Logger.i(this, readString(input, kNameLen) + " flag: " +readInt(input));
                        for (int j = 0; j < numFrames; ++j) {
                            final BoneKeyFrame boneKeyFrame = new BoneKeyFrame();
                            boneKeyFrame.rot[0] = readFloat(input);
                            boneKeyFrame.rot[1] = readFloat(input);
                            boneKeyFrame.rot[2] = readFloat(input);
                            boneKeyFrame.rot[3] = readFloat(input);
                            boneKeyFrame.x = readFloat(input);
                            boneKeyFrame.y = readFloat(input);
                            boneKeyFrame.z = readFloat(input);
//                            Logger.i(this, j + " " + keyFrame);
                            riotAnimation.addFrame(boneName, boneKeyFrame);
                        }
//                        data_.bones.push_back(bone);
                    }

//                    data_.switchHand();

                } else if (version == 4) {

                    Logger.v(TAG, "anm is of version 4, this support is in beta test.");

                    // get data size
                    int dataSize = readInt(input);
                    Logger.v(TAG, "dataSize=" + dataSize);

                    // check against length
                    minLength = 12 + dataSize;
                    if (length < minLength) Logger.e(TAG, "Unexpected end of file: " + length + " < " + minLength);

                    // magic
                    int magic2 = readInt(input);
                    Logger.v(TAG, "magic2=" + magic2);
                    if (magic2 != 0xBE0794D3)  Logger.e(TAG, "v4, magic is wrong! " + magic2);

                    // 2 bytes unused
                    input.skip(8);

                    // get numBones
                    int numBones = readInt(input);
                    Logger.v(TAG, "numBones=" + numBones);

                    // get numFrames
                    int numFrames = readInt(input);
                    Logger.v(TAG, "numFrames=" + numFrames);

                    // get fps (algorithm seen during the reversing)
                    float fps;
                    float ffps = readFloat(input);
                    if (ffps < 1.0f)
                        fps = 1.0f / ffps;
                    else
                        fps = ffps;
                    Logger.v(TAG, "ffps=" + ffps + "|fps="+fps);

                    // 3 bytes unused
                    input.skip(12);

                    // get positionsOffset
                    int positionsOffset = readInt(input);
                    Logger.v(TAG, "positionsOffset=" + positionsOffset);

                    // get quaternionsOffset
                    int quaternionsOffset = readInt(input);
                    Logger.v(TAG, "quaternionsOffset=" + quaternionsOffset);

                    // get framesOffset
                    int framesOffset = readInt(input);
                    Logger.v(TAG, "framesOffset=" + framesOffset);

                    int numPos = (quaternionsOffset - positionsOffset) / 12;
                    int numQuat = (framesOffset - quaternionsOffset) / 16;
                    Logger.v(TAG, "numPos=" + numPos + "|numQuat=" + numQuat);

                    // 3 bytes unused
                    input.skip(12);

                    // get positions TODO read more efficiently
                    float [][] pos = new float[numPos][3];
                    for(int i = 0; i < numPos; ++i) {
                        pos[i][0] = readFloat(input);
                        pos[i][1] = readFloat(input);
                        pos[i][2] = readFloat(input);
                    }

                    // get quaternions TODO read more efficiently
                    float [][] quat = new float[numQuat][4];
                    for(int i = 0; i < numQuat; ++i) {
                        quat[i][0] = readFloat(input);
                        quat[i][1] = readFloat(input);
                        quat[i][2] = readFloat(input);
                        quat[i][3] = readFloat(input);
                    }

                    // get bones with frames
                    for (int i = 0; i < numFrames; ++i) {
                        for (int j = 0; j < numBones; ++j) {

                            // get name hash
                            int nameHash = readInt(input);
//                            Logger.v(TAG, "nameHash=" + nameHash);

                            // get posId
                            short posId = readShort(input);
//                            Logger.v(TAG, "posId=" + posId);

                            // pos id from unit pos, useless for us
                            input.skip(2);

                            // get quatId
                            short quatId = readShort(input);
//                            Logger.v(TAG, "quatId=" + quatId);

                            // 0
                            input.skip(2);

//                            if (i == 0) data_.bones.at(j).name_hash = name_hash;

                            BoneKeyFrame boneKeyFrame = new BoneKeyFrame();
                            boneKeyFrame.nameHash = nameHash;
                            boneKeyFrame.rot[0] = quat[quatId][0];
                            boneKeyFrame.rot[1] = quat[quatId][1];
                            boneKeyFrame.rot[2] = quat[quatId][2];
                            boneKeyFrame.rot[3] = quat[quatId][3];
                            boneKeyFrame.x = pos[posId][0];
                            boneKeyFrame.y = pos[posId][1];
                            boneKeyFrame.z = pos[posId][2];
                        }
                    }
//                    data_.switchHand();

                }

                object3D.addSkeletonAnimation(resourceID, riotAnimation);

            } finally {
                Logger.v(TAG, "Closing input stream.");
                input.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.e(TAG, "File not found.");
            Logger.printException(TAG, ex);
        } catch (IOException ex) {
            Logger.printException(TAG, ex);
        }
    }

    public static void loadBones(String resourceID, AnimatedSkeletonObject3D object3D) {
        String packageID = "";
        if (resourceID.indexOf(":") > -1) packageID = resourceID.split(":")[0];
        Logger.v(TAG, "Reading in binary file named : " + packageID + ":" + resourceID);
        final Resources resources = Registry.getContext().getResources();

        try {
            DataInputStream input = null;
            try {
                input = new DataInputStream(new BufferedInputStream(resources.openRawResource(resources.getIdentifier(resourceID, null, packageID))));

            } finally {
                Logger.v(TAG, "Closing input stream.");
                input.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.e(TAG, "File not found.");
            Logger.printException(TAG, ex);
        } catch (IOException ex) {
            Logger.printException(TAG, ex);
        }
    }

    public static String readString(@NotNull final DataInputStream input, final int length) throws IOException {
        final byte[] bytes = new byte[length];
        input.read(bytes, 0, length);
        return new String(bytes).trim();
    }

    private static float readFloat(DataInputStream input) throws IOException {
        return getByteBuffer(input, true, 4).getFloat();
    }

    public static int readInt(final DataInputStream input) throws IOException {
        return getByteBuffer(input, true, 4).getInt();
    }

    public static short readShort(DataInputStream input) throws IOException {
        return getByteBuffer(input,true,2).getShort();
    }

    public static ByteBuffer getByteBuffer(@NotNull final DataInputStream input, final boolean useLittleEndian, final int length) throws IOException {
        byte[] result = new byte[length];
        input.read(result, 0, length);
        ByteBuffer bb = ByteBuffer.wrap(result);
        if (useLittleEndian) bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb;
    }
}
