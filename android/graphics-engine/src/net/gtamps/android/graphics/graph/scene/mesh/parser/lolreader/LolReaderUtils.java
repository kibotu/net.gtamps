package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

import net.gtamps.android.graphics.graph.scene.animation.rigged.GLAnimation;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.Vector3BufferManager;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.WeightManager;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Matrix4;
import net.gtamps.shared.Utils.math.Quaternion;
import net.gtamps.shared.Utils.math.Vector3;

import java.io.IOException;
import java.util.List;

import static net.gtamps.shared.Utils.math.Matrix4.M41;
import static net.gtamps.shared.Utils.math.Matrix4.M42;
import static net.gtamps.shared.Utils.math.Matrix4.M43;

/**
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 12:58
 */
public class LolReaderUtils {

    public static final int BYTESIZE_OF_FLOAT = 4;
    private static final String TAG = LolReaderUtils.class.getSimpleName();

    // utility
    private LolReaderUtils() {
    }

    public static String removeBoneNamePadding(String s) {
        int position = s.indexOf('\0');
        if (position >= 0) {
            s = s.replace("\0", "");
        }

        return s;
    }

    public static void readSklBoneNames(final BinaryReader file, int length, List<SKLBone> bones) throws IOException {
        String[] temp = file.readString(length).split("\0");
        int counter = 0;
        for (int i = 0; i < temp.length; ++i) {
            // remove single letters
            if (temp[i].length() > 1) {
                // remove weird signs in front of bone name
                bones.get(counter).name = normalizeSklBoneName(temp[i]);
                ++counter;
            }
        }
    }

    public static String normalizeSklBoneName(String s) {
        int i;
        for (i = 0; i < s.length(); ++i) {
            // bone names start with upper case letter, but can have underscore sign as 2nd letter
            if (Character.isUpperCase(s.charAt(i)) && (Character.isLetter(s.charAt(i + 1)) || s.charAt(i + 1) == '_')) {
                break;
            }
        }
        return s.substring(i);
    }

    public static float[] lookUpVector(int id, List<Float> vectors) {
        float[] result = new float[3];

        int startingPosition = id * result.length;
        for (int i = 0; i < result.length; ++i) {
            result[i] = vectors.get(startingPosition + i);
        }

        return result;
    }

    public static float[] lookUpQuaternion(int id, List<Float> quaternions) {
        float[] result = new float[4];

        int startingPosition = id * result.length;
        for (int i = 0; i < result.length; ++i) {
            result[i] = quaternions.get(startingPosition + i);
        }

        return result;
    }



    /**
     * Marry Bones with Vector
     *
     * @param w weights
     * @param i influences
     * @param m bone transformation matrices
     * @param t target vector (like position or normal)
     * @param f flag for position or directional vector
     */
    public static void marryBonesWithVector(float[] w, int[] i, Matrix4[] m, Vector3 t, float f) {

        Vector3 influence1 = getSingleBoneInfluence(m[i[0]],t,f).mulInPlace(w[0]);
        Vector3 influence2 = getSingleBoneInfluence(m[i[1]],t,f).mulInPlace(w[1]);
        Vector3 influence3 = getSingleBoneInfluence(m[i[2]],t,f).mulInPlace(w[2]);
        Vector3 influence4 = getSingleBoneInfluence(m[i[3]],t,f).mulInPlace(w[3]);

        t.addInPlace(influence1).addInPlace(influence2).addInPlace(influence3).addInPlace(influence4);

        influence1.recycle();
        influence2.recycle();
        influence3.recycle();
        influence4.recycle();
    }

    /**
     * computes single bone influence
     *
     * @param m current bone transformation Matrix
     * @param t position vector
     * @param f flag: 1 position, 0 direction
     * @return Vector3 single bone influence
     */
    private static Vector3 getSingleBoneInfluence(Matrix4 m, Vector3 t, float f) {
        float [] pI = new float[4];
        pI[0] = m.values[0]  * t.x + m.values[1]  * t.y + m.values[2]  * t.z + m.values[3]  * f;
        pI[1] = m.values[4]  * t.x + m.values[5]  * t.y + m.values[6]  * t.z + m.values[7]  * f;
        pI[2] = m.values[8]  * t.x + m.values[9]  * t.y + m.values[10] * t.z + m.values[11] * f;
//        pI[3] = m.values[12] * t.x + m.values[13] * t.y + m.values[14] * t.z + m.values[15] * f;
        return Vector3.createNew(pI[0],pI[1],pI[2]);
    }

    /**
     * Computes bone transformation matrices
     *
     * @param transforms
     * @param animation
     * @param currentFrameTime
     * @param currentFrame
     */
    public static void computeBoneTransformation(Matrix4[] transforms, GLAnimation animation, float currentFrameTime, int currentFrame) {

        //
        // Normal Case
        //
        // Interpolate between the current and next frames to calculate
        // the transform to send to the renderer.
        //

        float blend = currentFrameTime / animation.timePerFrame;

        int nextFrame = (currentFrame + 1) % animation.numberOfFrames;
        for (int i = 0; i < animation.bones.size(); ++i) {
            // Get the current frame's transform.
            Matrix4 current = animation.bones.get(i).frames.get(currentFrame);

            //
            // The interpolation code is unstable.  It creates alot of animation anomalies.
            //
            // TODO: Fix it.
            //

            // Break it down into a vector and quaternion.
            Vector3 currentPosition = Vector3.createNew();
            currentPosition.x = current.values[M41];
            currentPosition.y = current.values[M42];
            currentPosition.z = current.values[M43];

            Quaternion currentOrientation = Matrix4.createQuatFromMatrix(current);

            // Get the next frame's transform.
            Matrix4 next = animation.bones.get(i).frames.get(nextFrame);

            // Break it down into a vector and quaternion.
            Vector3 nextPosition = Vector3.createNew();
            nextPosition.x = next.values[M41];
            nextPosition.y = next.values[M42];
            nextPosition.z = next.values[M43];

            Quaternion nextOrientation = Matrix4.createQuatFromMatrix(next);

            // Interpolate the frame data.
            currentPosition.lerpInPlace(nextPosition, blend);
            currentOrientation.slerpInPlace(nextOrientation, blend);

            // Rebuild a transform.
//            Matrix4 finalTransform = Matrix4.rotate(currentOrientation);
//            finalTransform.values[M41] = currentPosition.x;
//            finalTransform.values[M42] = currentPosition.y;
//            finalTransform.values[M43] = currentPosition.z;

            Matrix4 finalTransform = Matrix4.rotate(nextOrientation);
            finalTransform.values[M41] = nextPosition.x;
            finalTransform.values[M42] = nextPosition.y;
            finalTransform.values[M43] = nextPosition.z;

            transforms[i] = finalTransform;
        }
    }

    private static final Vector3 tempPosition = Vector3.createNew();
    private static final Vector3 tempNormal = Vector3.createNew();
    private static final float[] tempWeight = new float[4];
    private static final int[] tempInfluences = new int[4];

    public static void updateMesh(Mesh curM, Mesh nextM, Matrix4 [] transM) {

        Vector3BufferManager orgV = curM.vertices.getVertices();
        Vector3BufferManager orgN = curM.vertices.getNormals();
        WeightManager orgW = curM.vertices.getWeights();

        Vector3BufferManager curV = nextM.vertices.getVertices();
        Vector3BufferManager curN = nextM.vertices.getNormals();
        WeightManager curW = nextM.vertices.getWeights();

        for (int i = 0; i < curV.size(); ++i) {

            // current position
            tempPosition.set(orgV.getPropertyX(i), orgV.getPropertyY(i), orgV.getPropertyZ(i));
            tempNormal.set(orgN.getPropertyX(i), orgN.getPropertyY(i), orgN.getPropertyZ(i));

            // current weight
            tempWeight[0] = orgW.getPropertyWeightX(i);
            tempWeight[1] = orgW.getPropertyWeightY(i);
            tempWeight[2] = orgW.getPropertyWeightZ(i);
            tempWeight[3] = orgW.getPropertyWeightW(i);

            // current weight
            tempInfluences[0] = orgW.getPropertyInfluence1(i);
            tempInfluences[1] = orgW.getPropertyInfluence2(i);
            tempInfluences[2] = orgW.getPropertyInfluence3(i);
            tempInfluences[3] = orgW.getPropertyInfluence4(i);

            LolReaderUtils.marryBonesWithVector(tempWeight, tempInfluences, transM, tempPosition, 1);
//            LolReaderUtils.marryBonesWithVector(tempWeight, tempInfluences, boneTransformations, tempNormal, 0);

            curV.overwrite(i, tempPosition.x, tempPosition.y, tempPosition.z);
            curN.overwrite(i, tempNormal.x, tempNormal.y, tempNormal.z);
        }
        curM.update();
    }
}
