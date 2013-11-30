package net.gtamps.android.graphics.graph.scene.animation.rigged;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores information required for skeletal animation in OpenGL.
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 14:03
 */
public class GLAnimation {
    public int playbackFPS;
    public float timePerFrame;

    public int numberOfBones;
    public int numberOfFrames;

    public ArrayList<GLBone> bones;

    public GLAnimation() {
        playbackFPS = 0;
        numberOfBones = 0;
        numberOfFrames = 0;
        bones = new ArrayList<GLBone>();
    }
}
