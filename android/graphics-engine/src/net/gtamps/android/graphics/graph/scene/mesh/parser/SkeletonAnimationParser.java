package net.gtamps.android.graphics.graph.scene.mesh.parser;

import android.content.res.Resources;
import net.gtamps.android.graphics.graph.scene.animation.skeleton.*;
import net.gtamps.android.graphics.utils.Registry;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.MathUtils;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import static net.gtamps.android.graphics.graph.scene.mesh.parser.AParser.*;

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
    public final static String DELIMETER = "\0";
    private static final int DIRECTX_TO_OPENGL = -1;
//    private static final int DIRECTX_TO_OPENGL = 1;

    // utility class
    private SkeletonAnimationParser() {
    }

    /**
     * This function converts the handedness of the DirectX style input data
     * into the handedness OpenGL expects.
     * So, vector inputs have their Z value negated and quaternion inputs have their
     * Z and W values negated.
     *
     * @param resourceID
     * @param object3D
     */
    public static void loadAnm(String resourceID, @NotNull AnimatedSkeletonObject3D object3D) {

        if(object3D.getBones() == null) {
            Logger.e(TAG, "Skeleton missing! Please load skl before animations.");
            return;
        }

        final long startTime = Calendar.getInstance().getTimeInMillis();
        String packageID = "";
        if (resourceID.contains(":")) packageID = resourceID.split(":")[0];
        final Resources resources = Registry.getContext().getResources();

        RiotAnimation riotAnimation = null;

        try {
            DataInputStream input = null;
            try {
                input = new DataInputStream(new BufferedInputStream(resources.openRawResource(resources.getIdentifier(resourceID, null, packageID))));

                int readBytes = 0;

                // check length
                int minLength = 28;
                int length = input.available();
//                Logger.v(TAG, "length=" + length);
                if (length < minLength) Logger.e(TAG, "File is empty: " + length + " < " + minLength);

                // magic
                String magic = readString(input,8);
                readBytes += 8;
//                Logger.v(TAG, "magic=" + magic);

                // version
                int version = readInt(input);
                readBytes += 4;
//                Logger.v(TAG, "version=" + version);

                if (version == 3) {

                    // get designer ID
                    int designerId = readInt(input);
                    readBytes += 4;
//                    Logger.v(TAG, "designerId=" + designerId);

                    // get numBones
                    int numBones = readInt(input);
                    readBytes += 4;
//                    Logger.v(TAG, "numBones=" + numBones);

                    // get numFrames
                    int numFrames = readInt(input);
                    readBytes += 4;
//                    Logger.v(TAG, "numFrames=" + numFrames);

                    // get fps (algorithm seen during the reversing)
                    float fps;
                    float ffps = readFloat(input);
                    readBytes += 4;
                    if (ffps < 0.0f)
                        fps = ffps + 4294967296.0f;
                    else
                        fps = ffps;
//                    Logger.v(TAG, "ffps=" + ffps + "|fps="+fps);

                    // check minimum length
                    minLength += numBones * numFrames * kSizeInFile + numBones * kHeaderSize;
                    if (length < minLength) Logger.e(TAG, "Unexpected end of file: " + length + " < " + minLength);

                    riotAnimation = new RiotAnimation(numBones, numFrames, 30, version);

                    // get bones with frames
                    for (int i = 0; i < numBones; ++i) {
                        String boneName = readString(input,kNameLen);
                        readBytes += kNameLen;
                        int rootBone = readInt(input); // 2 = root
                        readBytes += 4;
//                        Logger.i(TAG, boneName + " flag: " +rootBone);
                        for (int j = 0; j < numFrames; ++j) {
                            final BoneKeyFrame boneKeyFrame = new BoneKeyFrame();
                            boneKeyFrame.rotation.set(readFloat(input),readFloat(input),DIRECTX_TO_OPENGL*readFloat(input),DIRECTX_TO_OPENGL*readFloat(input));
                            boneKeyFrame.position.set(readFloat(input),readFloat(input),DIRECTX_TO_OPENGL*readFloat(input));
                            readBytes += 7 * 4;
//                            Logger.i(this, j + " " + keyFrame);
                            riotAnimation.addFrame(object3D.getBone(boneName), boneKeyFrame);
                        }
                    }

//                    data_.switchHand();

                } else if (version == 4) {

                    Logger.v(TAG, "anm is of version 4, this support is in beta test.");

                    // get data size
                    int dataSize = readInt(input);
                    readBytes += 4;
//                    Logger.v(TAG, "dataSize=" + dataSize);

                    // check against length
                    minLength = 12 + dataSize;
                    if (length < minLength) Logger.e(TAG, "Unexpected end of file: " + length + " < " + minLength);

                    // magic
                    int magic2 = readInt(input);
                    readBytes += 4;
//                    Logger.v(TAG, "magic2=" + magic2);
                    if (magic2 != 0xBE0794D3) Logger.e(TAG, "v4, magic is wrong! " + magic2);

                    // 2 bytes unused
                    input.skip(8);
                    readBytes += 8;

                    // get numBones
                    int numBones = readInt(input);
                    readBytes += 4;
//                    Logger.v(TAG, "numBones=" + numBones);

                    // get numFrames
                    int numFrames = readInt(input);
                    readBytes += 4;
//                    Logger.v(TAG, "numFrames=" + numFrames);

                    // get fps (algorithm seen during the reversing)
                    float fps;
                    float ffps = readFloat(input);
                    readBytes += 4;
                    if (ffps < 1.0f)
                        fps = 1.0f / ffps;
                    else
                        fps = ffps;
//                    Logger.v(TAG, "ffps=" + ffps + "|fps="+fps);

                    // 3 bytes unused
                    input.skip(12);
                    readBytes += 12;

                    // get positionsOffset
                    int positionsOffset = readInt(input);
                    readBytes += 4;
//                    Logger.v(TAG, "positionsOffset=" + positionsOffset);

                    // get quaternionsOffset
                    int quaternionsOffset = readInt(input);
                    readBytes += 4;
//                    Logger.v(TAG, "quaternionsOffset=" + quaternionsOffset);

                    // get framesOffset
                    int framesOffset = readInt(input);
                    readBytes += 4;
//                    Logger.v(TAG, "framesOffset=" + framesOffset);

                    int numPos = (quaternionsOffset - positionsOffset) / 12;
                    int numQuat = (framesOffset - quaternionsOffset) / 16;
//                    Logger.v(TAG, "numPos=" + numPos + "|numQuat=" + numQuat);

                    // 3 bytes unused
                    input.skip(12);
                    readBytes += 12;

                    // get positions TODO read more efficiently
                    float[][] pos = new float[numPos][3];
                    for (int i = 0; i < numPos; ++i) {
                        pos[i][0] = readFloat(input);
                        pos[i][1] = readFloat(input);
                        pos[i][2] = DIRECTX_TO_OPENGL*readFloat(input);
                        readBytes += 3 * 4;
                    }

                    // get quaternions TODO read more efficiently
                    float[][] quat = new float[numQuat][4];
                    for (int i = 0; i < numQuat; ++i) {
                        quat[i][0] = readFloat(input);
                        quat[i][1] = readFloat(input);
                        quat[i][2] = DIRECTX_TO_OPENGL*readFloat(input);
                        quat[i][3] = DIRECTX_TO_OPENGL*readFloat(input);
                        readBytes += 4 * 4;
                    }

                    riotAnimation = new RiotAnimation(numBones, numFrames, 30, version);

                    // get bones with frames
                    for (int i = 0; i < numFrames; ++i) {
                        for (int j = 0; j < numBones; ++j) {

                            // get name hash
                            int nameHash = readInt(input);
                            readBytes += 4;
//                            Logger.v(TAG, "nameHash=" + nameHash);

                            // get posId
                            int posId = readShort(input);
                            readBytes += 2;
//                            Logger.v(TAG, "posId=" + posId);

                            // pos id from unit pos, useless for us
                            input.skip(2);
                            readBytes += 2;

                            // get quatId
                            int quatId = readShort(input);
                            readBytes += 2;
//                            Logger.v(TAG, "quatId=" + quatId);

                            // 0
                            input.skip(2);
                            readBytes += 2;

//                            if (i == 0) data_.bones.at(j).name_hash = name_hash;

                            final BoneKeyFrame boneKeyFrame = new BoneKeyFrame();
                            boneKeyFrame.nameHash = nameHash;
                            boneKeyFrame.rotation.set(quat[quatId][0],quat[quatId][1],DIRECTX_TO_OPENGL*quat[quatId][2],DIRECTX_TO_OPENGL*quat[quatId][3]);
                            boneKeyFrame.position.set(pos[posId][0],pos[posId][1],DIRECTX_TO_OPENGL*pos[posId][2]);

                            riotAnimation.addFrame(object3D.getBone(nameHash), boneKeyFrame);
                        }
                    }
//                    data_.switchHand();

                }

                object3D.addSkeletonAnimation(resourceID, riotAnimation);
                long endTime = Calendar.getInstance().getTimeInMillis();
                Logger.i(TAG, "[" + resourceID + "|" + readBytes + "/" + length + "bytes] Successfully loaded in " + (endTime - startTime) + "ms.");

            } finally {
//                Logger.v(TAG, "Closing input stream.");
                input.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.e(TAG, "File not found.");
            Logger.printException(TAG, ex);
        } catch (IOException ex) {
            Logger.printException(TAG, ex);
        }
    }

    private static final boolean SHOW_READ_BYTES = false;

    public static void loadSkl(String resourceID, @NotNull AnimatedSkeletonObject3D object3D) {
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
                int minLength = 20;
                int length = input.available();
                if (length < minLength) Logger.e(TAG, "File is empty: " + length + " < " + minLength);


                // get raw bone header
                RawBoneHeader rawBoneHeader = readRawBoneHeader(input);
                readBytes += RawBoneHeader.BYTELENGTH;
//                Logger.v(TAG, rawBoneHeader);

                // skip
                input.skip(rawBoneHeader.header_size - readBytes);
                if (SHOW_READ_BYTES) Logger.v(TAG, "skipping " + (rawBoneHeader.header_size - readBytes) + " bytes for more future header information.");
                readBytes += rawBoneHeader.header_size - readBytes;
                if (SHOW_READ_BYTES) Logger.v(TAG, readBytes + " bytes read");

                // get raw skl bone
                RawSklBone rawSklBone[] = new RawSklBone[rawBoneHeader.nbSklBones];
                for (int i = 0; i < rawBoneHeader.nbSklBones; ++i) {
                    rawSklBone[i] = readRawSklBone(input);
                    readBytes += RawSklBone.BYTELENGTH + 32;
                }

//                Logger.v(TAG, rawSklBone[0]);
//                Logger.v(TAG, rawSklBone[rawBoneHeader.nbSklBones-1]);

                // skip
                input.skip(rawBoneHeader.size_after_array2 - readBytes);
                if (SHOW_READ_BYTES)
                    Logger.v(TAG, "skipping " + (rawBoneHeader.size_after_array2 - readBytes) + " bytes");
                readBytes += rawBoneHeader.size_after_array2 - readBytes;
                if (SHOW_READ_BYTES) Logger.v(TAG, readBytes + " bytes read");

                // get animated indices
                int numIndices = rawBoneHeader.num_bones_foranim;
                int[] indices = new int[numIndices];
                for (int i = 0; i < numIndices; ++i) {
                    indices[i] = readShort(input);
//                    Logger.v(TAG, i + " " +indices[i]);
                }
                readBytes += numIndices * 2;
                if (SHOW_READ_BYTES) Logger.v(TAG, readBytes + " bytes read");
                object3D.setBoneAnimationIndices(indices);

                // skip
                input.skip(rawBoneHeader.size_after_array4 - readBytes);
                if (SHOW_READ_BYTES)
                    Logger.v(TAG, "skipping " + (rawBoneHeader.size_after_array4 - readBytes) + " bytes");
                readBytes += rawBoneHeader.size_after_array4 - readBytes;
                if (SHOW_READ_BYTES) Logger.v(TAG, readBytes + " bytes read");

                // names
                String[] boneNames = readSklBoneNames(input, ((rawBoneHeader.size - rawBoneHeader.size_after_array4) & 0xFFFFFFFC), rawBoneHeader.nbSklBones);
                readBytes += ((rawBoneHeader.size - rawBoneHeader.size_after_array4) & 0xFFFFFFFC);
//                for(int i = 0; i < boneNames.length; ++i) {
//                    Logger.i(TAG, i + " " +boneNames[i] +"\n");
//                }

                // get bones
                for (int i = 0; i < rawBoneHeader.nbSklBones; ++i) {
                    Bone bone = new Bone();
                    bone.position.set(rawSklBone[i].tx, rawSklBone[i].ty, DIRECTX_TO_OPENGL*rawSklBone[i].tz);
                    bone.rotation.set(rawSklBone[i].q1, rawSklBone[i].q2, DIRECTX_TO_OPENGL*rawSklBone[i].q3, DIRECTX_TO_OPENGL*rawSklBone[i].q4);
                    bone.ct.set(rawSklBone[i].ctx, rawSklBone[i].cty, rawSklBone[i].ctz);
                    bone.name = boneNames[i];
                    bone.id = rawSklBone[i].id;
                    bone.parentID = rawSklBone[i].parent_id;
                    bone.nameHash = rawSklBone[i].namehash;
                    object3D.addBone(bone);
//                    if(bone.name.equalsIgnoreCase("L_Hand")) {
//                        Vector3 r = Vector3.createNew().setEulerAnglesFromQuaternion(bone.rotation);
//                        Logger.i(TAG, "bName=" + bone.name + "|rot={" + MathUtils.rad2Deg(r.x) + "; " + MathUtils.rad2Deg(r.y) + "; " + MathUtils.rad2Deg(r.z) +"|pos="+bone.position);
//                    }
                }

                object3D.transformChildBonesAlongParents(0);

                long endTime = Calendar.getInstance().getTimeInMillis();
                Logger.i(TAG, "[" + resourceID + "|fLength=" + length + "bytes|read=" + readBytes + "/" + rawBoneHeader.size + "bytes] Successfully loaded in " + (endTime - startTime) + "ms.");

            } finally {
//                Logger.v(TAG, "Closing input stream.");
                input.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.e(TAG, "File not found.");
            Logger.printException(TAG, ex);
        } catch (IOException ex) {
            Logger.printException(TAG, ex);
        }
    }

    private static String[] readSklBoneNames(final DataInputStream input, int length, int bones) throws IOException {
        String[] temp = readString(input,length).split(DELIMETER);
        String[] boneNames = new String[bones];
        int counter = 0;
        for (int i = 0; i < temp.length; ++i) {
            // remove single letters
            if (temp[i].length() > 1) {
                // remove weird signs in front of bone name
                boneNames[counter] = normalizeSklBoneName(temp[i]);
                ++counter;
            }
        }
        return boneNames;
    }

    private static String normalizeSklBoneName(String s) {
        int i;
        for (i = 0; i < s.length(); ++i) {
            // bone names start with upper case letter, but can have underscore sign as 2nd letter
            if (Character.isUpperCase(s.charAt(i)) && (Character.isLetter(s.charAt(i + 1)) || s.charAt(i + 1) == '_')) {
                break;
            }
        }
        return s.substring(i);
    }

    private static RawSklBone readRawSklBone(final DataInputStream input) throws IOException {
        RawSklBone rawSklBone = new RawSklBone();
        rawSklBone.uk = readShort(input);           // read 2 bytes     | 2
        rawSklBone.id = readShort(input);           // read 2 bytes     | 4
        rawSklBone.parent_id = readShort(input);    // read 2 bytes     | 6
        rawSklBone.uk2 = readShort(input);          // read 2 bytes     | 8
        rawSklBone.namehash = readInt(input);       // read 4 bytes     | 12
        rawSklBone.unused = readFloat(input);       // read 4 bytes     | 16
        rawSklBone.tx = readFloat(input);           // read 4 bytes     | 20
        rawSklBone.ty = readFloat(input);           // read 4 bytes     | 24
        rawSklBone.tz = readFloat(input);           // read 4 bytes     | 28
        rawSklBone.unused1 = readFloat(input);      // read 4 bytes     | 32
        rawSklBone.unused2 = readFloat(input);      // read 4 bytes     | 36
        rawSklBone.unused3 = readFloat(input);      // read 4 bytes     | 40
        rawSklBone.q1 = readFloat(input);           // read 4 bytes     | 44
        rawSklBone.q2 = readFloat(input);           // read 4 bytes     | 48
        rawSklBone.q3 = readFloat(input);           // read 4 bytes     | 52
        rawSklBone.q4 = readFloat(input);           // read 4 bytes     | 56
        rawSklBone.ctx = readFloat(input);          // read 4 bytes     | 60
        rawSklBone.cty = readFloat(input);          // read 4 bytes     | 64
        rawSklBone.ctz = readFloat(input);          // read 4 bytes     | 68
        input.skip(32);
        return rawSklBone;                          // totally read bytes 68
    }

    private static RawBoneHeader readRawBoneHeader(final DataInputStream input) throws IOException {
        RawBoneHeader rawH = new RawBoneHeader();
        rawH.size = readInt(input);                     // read 4 bytes     | 4
        rawH.magic = readInt(input);                    // read 4 bytes     | 8
        rawH.uk = readInt(input);                       // read 4 bytes     | 12
        rawH.uk2 = readShort(input);                    // read 2 bytes     | 14
        rawH.nbSklBones = readShort(input);             // read 2 bytes     | 16
        rawH.num_bones_foranim = readInt(input);        // read 4 bytes     | 20
        rawH.header_size = readInt(input); // 0x40      // read 4 bytes     | 24
        rawH.size_after_array1 = readInt(input);        // read 4 bytes     | 28
        rawH.size_after_array2 = readInt(input);        // read 4 bytes     | 32
        rawH.size_after_array3 = readInt(input);        // read 4 bytes     | 36
        rawH.size_after_array3_ = readInt(input);       // read 4 bytes     | 40
        rawH.size_after_array4 = readInt(input);        // read 4 bytes     | 44
        return rawH;                                    // totally read bytes 44
    }
}
