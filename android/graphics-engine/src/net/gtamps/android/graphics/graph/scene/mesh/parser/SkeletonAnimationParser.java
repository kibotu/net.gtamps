package net.gtamps.android.graphics.graph.scene.mesh.parser;

import android.content.res.Resources;
import net.gtamps.android.graphics.graph.scene.animation.skeleton.AnimatedSkeletonObject3D;
import net.gtamps.android.graphics.graph.scene.animation.skeleton.Bone;
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

    public static void loadAnimation(String resourceID, AnimatedSkeletonObject3D object3D) {
        String packageID = "";
        if (resourceID.indexOf(":") > -1) packageID = resourceID.split(":")[0];
        Logger.v(TAG, "Reading in binary file named : " + packageID + ":" + resourceID);
        final Resources resources = Registry.getContext().getResources();

        RiotAnimation riotAnimation = null;

        try {
            DataInputStream input = null;
            try {
                input = new DataInputStream(new BufferedInputStream(resources.openRawResource(resources.getIdentifier(resourceID, null, packageID))));
                riotAnimation = new RiotAnimation();

                // check length
                int minLength = 28;
                int length = input.available();
//                Logger.v(this, "length=" + length);
                if (length < minLength) Logger.e(TAG, "File is empty: " + length + " < " + minLength);

                // magic
                String magic = readString(input, 8);
//                Logger.v(this, "magic=" + magic);
                riotAnimation.setMagic(magic);

                // version
                int version = readInt(input);
//                Logger.v(this, "version=" + version);
                riotAnimation.setVersion(version);

                if (version == 3) {

                    // get designer ID
                    int designerId = readInt(input);
//                    Logger.v(this, "designerId=" + designerId);

                    // get numBones
                    int numBones = readInt(input);
//                    Logger.v(this, "numBones=" + numBones);

                    // get numFrames
                    int numFrames = readInt(input);
//                    Logger.v(this, "numFrames=" + numFrames);

                    // get fps (algorithm seen during the reversing)
                    float fps;
                    float ffps = readFloat(input);
                    if (ffps < 0.0f)
                        fps = ffps + 4294967296.0f;
                    else
                        fps = ffps;
//                    Logger.v(this, "ffps=" + ffps + "|fps="+fps);
                    riotAnimation.setFps(30); // TODO check the result against multiple animations

                    // check minimum length
                    minLength += numBones * numFrames * kSizeInFile + numBones * kHeaderSize;
                    if (length < minLength) Logger.e(TAG, "Unexpected end of file: " + length + " < " + minLength);
                    riotAnimation.setNumBones(numBones);
                    riotAnimation.setNumFrames(numFrames);

                    // get bones with frames
                    for (int i = 0; i < numBones; ++i) {
                        Bone bone = new Bone();

//                        Logger.i(this, readString(input, kNameLen) + " flag: " +readInt(input));

                        for (int j = 0; j < numFrames; ++j) {

                            BoneKeyFrame keyFrame = new BoneKeyFrame();
                            keyFrame.rot[0] = readFloat(input);
                            keyFrame.rot[1] = readFloat(input);
                            keyFrame.rot[2] = readFloat(input);
                            keyFrame.rot[3] = readFloat(input);
                            keyFrame.x = readFloat(input);
                            keyFrame.y = readFloat(input);
                            keyFrame.z = readFloat(input);
//                            Logger.i(this, j + " " + keyFrame);

                        }
//                        data_.bones.push_back(bone);
                    }

//                    data_.switchHand();

                } else if (version == 4) {

//                    // get data size
//                    int dataSize = readInt(input);
//                    Logger.v(this, "dataSize=" + dataSize);
//
//                    // check length
//                    minLength = 12 + dataSize;
//                    if (length < minLength)if (length < minLength) Logger.e(this, "Unexpected end of file: " + length + " < " + minLength);
//
//                    // get magic
//                    int magic2 = readInt(input);
//                    Logger.v(this, "magic=" + magic2);
////                    if(magic2 != 0xBE0794D3) Logger.e(this, "v4, magic is wrong!");
//
//                    // 2 bytes unused
//                    input.skip(8);
//
//                    // get bones count
//                    int numBones = readInt(input);
//                    Logger.v(this, "numBones=" + numBones);


                    // TODO


                }
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

    public static ByteBuffer getByteBuffer(@NotNull final DataInputStream input, final boolean useLittleEndian, final int length) throws IOException {
        byte[] result = new byte[length];
        input.read(result, 0, length);
        ByteBuffer bb = ByteBuffer.wrap(result);
        if (useLittleEndian) bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb;
    }
}
