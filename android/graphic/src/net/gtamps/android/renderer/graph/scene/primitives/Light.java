package net.gtamps.android.renderer.graph.scene.primitives;

import net.gtamps.android.renderer.RenderCapabilities;
import net.gtamps.android.renderer.graph.ProcessingState;
import net.gtamps.android.renderer.graph.RenderableNode;
import net.gtamps.android.renderer.mesh.Mesh;
import net.gtamps.android.renderer.utils.OpenGLUtils;
import net.gtamps.android.renderer.utils.Utils;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static javax.microedition.khronos.opengles.GL11.*;

/**
 * Lightning source based on @see <a href="http://glprogramming.com/red/chapter05.html#name4">Creating Light Sources</a>
 */
public class Light extends RenderableNode {


    public enum Type {
        /**
         * directional light (x,y,z is the light direction) like the sun
         */
        DIRECTIONAL(0),
        /**
         * positional light like a fireball. Any value other than 0 treated as non-directional
         */
        POSITIONAL(1);
        private final float value;

        private Type(float value) {
            this.value = value;
        }

        public float getValue() {
            return value;
        }
    }

    /**
     * static pool for available GL_LIGHT ids
     */
    private static ArrayList<Integer> availableLightIndices;

    /**
     * Direction is a vector and should be normalized.
     */
    private FloatBuffer direction;

    /**
     * Holds the position for convenience reasons additionally with the type.
     */
    private FloatBuffer positionAndTypeBuffer;

    /**
     * Defines the Light type. OpenGL is rather boring on this one.
     */
    private Type type;

    /**
     * angle [0..180]
     */
    private float spotCutoffAngle;
    /**
     * exponent [0..128]
     */
    private float spotExponent;

    /**
     * defines how light amount reduces if model gets away from light source
     */
    private final Vector3 attenuation;

    /**
     * Holds the current GL_LIGHT id.
     */
    private int lightId;

    public Light() {
        mesh = new Mesh(0, 0);
        type = Type.DIRECTIONAL;
        material.getAmbient().setAll(128, 128, 128, 255);
        material.getDiffuse().setAll(255, 255, 255, 255);
        material.getSpecular().setAll(0, 0, 0, 255);
        material.getEmission().setAll(0, 0, 0, 255);
        direction = OpenGLUtils.makeFloatBuffer3(0, 0, -1);
        spotCutoffAngle = 180;
        spotExponent = 0;
        attenuation = Vector3.createNew(1f, 0f, 0f);
        positionAndTypeBuffer = OpenGLUtils.makeFloatBuffer4(0, 0, 0, 0);
        setPosition(0, 0, 0);
        commitPositionAndTypeBuffer();

        if (availableLightIndices == null) {
            availableLightIndices = new ArrayList<Integer>();
            for (int i = 0; i < RenderCapabilities.maxLights(); i++) {
                availableLightIndices.add(i);
            }
        }
        if (availableLightIndices.isEmpty()) {
            Logger.i(this, "GL_LIGHT resources exceeded.");
        }
        lightId = availableLightIndices.get(0);
        availableLightIndices.remove(0);
    }

    public void release() {
        availableLightIndices.add(lightId);
    }

    @Override
    public void render(GL11 gl) {

        GL11 gl11 = (GL11) gl;

        if (isVisible()) {
            gl11.glEnable(GL_LIGHTING);
            gl11.glEnable(GL_LIGHT0 + lightId);
            gl11.glEnable(GL_NORMALIZE);
            gl11.glEnable(GL_RESCALE_NORMAL);

            gl11.glLightfv(GL_LIGHT0 + lightId, GL_POSITION, positionAndTypeBuffer);
            gl11.glLightfv(GL_LIGHT0 + lightId, GL_AMBIENT, material.getAmbient().asBuffer());
            gl11.glLightfv(GL_LIGHT0 + lightId, GL_DIFFUSE, material.getDiffuse().asBuffer());
            gl11.glLightfv(GL_LIGHT0 + lightId, GL_SPECULAR, material.getSpecular().asBuffer());
            gl11.glLightfv(GL_LIGHT0 + lightId, GL_SPOT_DIRECTION, direction);
            gl11.glLightf(GL11.GL_LIGHT0 + lightId, GL11.GL_SPOT_CUTOFF, spotCutoffAngle);
            gl11.glLightf(GL11.GL_LIGHT0 + lightId, GL11.GL_SPOT_EXPONENT, spotExponent);
//            Utils.log(this, ""+diffuse.getColorBuffer().get(0) + " "+diffuse.getColorBuffer().get(1)+ " "+diffuse.getColorBuffer().get(2) + " "+diffuse.getColorBuffer().get(3));

            gl11.glLightf(GL11.GL_LIGHT0 + lightId, GL11.GL_CONSTANT_ATTENUATION, attenuation.x);
            gl11.glLightf(GL11.GL_LIGHT0 + lightId, GL11.GL_LINEAR_ATTENUATION, attenuation.y);
            gl11.glLightf(GL11.GL_LIGHT0 + lightId, GL11.GL_QUADRATIC_ATTENUATION, attenuation.z);
            commitPositionAndTypeBuffer();
        } else {
            gl.glDisable(GL_LIGHTING);
        }
    }

    @Override
    protected void renderInternal(@NotNull GL10 gl) {

    }

    @Override
    public RenderableNode getStatic() {
        return this;
    }

    @Override
    protected void setOptions() {

    }

    @Override
    public void setPosition(float x, float y, float z) {
        super.setPosition(x, y, z);
    }

    @Override
    protected void updateInternal(float deltat) {

    }

    @Override
    protected void cleanupInternal(@NotNull ProcessingState state) {

    }

    @Override
    protected void setupInternal(@NotNull ProcessingState state) {
    }

    /**
     * 0 = no attenuation towards edges of spotlight. Max is 128.
     * Default is 0, matching OpenGL's default value.
     */
    public void setSpotExponent(float value) {
        spotExponent = Utils.clamp(value, 0, 128);
    }

    /**
     * GL_POSITION takes 4 arguments, the first 3 being x/y/z position,
     * and the 4th being what we're calling 'type' (positional or directional)
     */
    public void commitPositionAndTypeBuffer() {
        OpenGLUtils.addFloat4PositionZero(positionAndTypeBuffer, getPosition().x, getPosition().y, getPosition().z, type.getValue());
    }

    public void setDirection(float x, float y, float z) {
        OpenGLUtils.addFloat3PositionZero(direction, x, y, z);
    }

    /**
     * No cutoff angle (ie, no spotlight effect)
     * (represented internally with a value of 180)
     */
    public void setSpotCutoffAngle(float angle) {
        if (angle == 180) {
            setSpotCutoffAngleNone();
        } else {
            spotCutoffAngle = Utils.clamp(angle, 0, 90);
        }
    }

    /**
     * No cutoff angle (ie, no spotlight effect)
     * (represented internally with a value of 180)
     */
    public void setSpotCutoffAngleNone() {
        spotCutoffAngle = 180;
    }

    /**
     * Defaults are 1,0,0 (resulting in no attenuation over distance),
     * which match OpenGL default values.
     */
    public void setAttenuation(float constant, float linear, float quadratic) {
        attenuation.set(constant, linear, quadratic);
    }

    public void setType(Type type) {
        this.type = type;
        commitPositionAndTypeBuffer();
    }

    @Override
    public void onDirty() {
    }


    public FloatBuffer getDirection() {
        return direction;
    }

    public static ArrayList<Integer> getAvailableLightIndices() {
        return availableLightIndices;
    }

    public FloatBuffer getPositionAndTypeBuffer() {
        return positionAndTypeBuffer;
    }

    public Type getType() {
        return type;
    }

    public float getSpotCutoffAngle() {
        return spotCutoffAngle;
    }

    public float getSpotExponent() {
        return spotExponent;
    }

    public Vector3 getAttenuation() {
        return attenuation;
    }

    public int getLightId() {
        return lightId;
    }
}
