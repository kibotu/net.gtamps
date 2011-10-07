package net.gtamps.android.core.graph;

import net.gtamps.android.core.utils.Color4;
import net.gtamps.shared.math.Vector3;
import net.gtamps.android.core.utils.OpenGLUtils;
import net.gtamps.android.core.utils.Utils;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Lightning source based on @see <a href="http://glprogramming.com/red/chapter05.html#name4">Creating Light Sources</a>
 */
public class LightNode extends RenderableNode {

    private static final String TAG = LightNode.class.getSimpleName();

    public enum Type {
        /**
         * directional light (x,y,z is the light direction) like the sun
         */
        DIRECTIONAL (0),
        /**
         * positional light like a fireball. Any value other than 0 treated as non-directional
         */
        POSITIONAL (1);
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
     * Will be changed by renderer on create. After {@link net.gtamps.android.core.renderer.RenderCapabilities} have been checked.
     */
    public static int MAX_AMOUNT_LIGHTS = 8;

    /**
     * Light that has been reflected by other objects and hits the mesh in small amounts
     */
    public final Color4 ambient;

    /**
     * setting diffuse light color like a bulb or neon tube
     */
    public final Color4 diffuse;

    /**
     * setting specular light color like a halogen spot
     */
    public final Color4 specular;

    /**
     *  setting for the emissive light color like a sun
     */
    public final Color4 emissive;

    /**
     *  angle [0..180]
     */
    private float spotCutoffAngle;
    /**
     *  exponent [0..128]
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

    public LightNode() {
         type = Type.DIRECTIONAL;
         ambient = new Color4(128,128,128, 255);
		 diffuse = new Color4(255,255,255, 255);
		 specular = new Color4(0,0,0,255);
		 emissive = new Color4(0,0,0,255);
         direction = OpenGLUtils.makeFloatBuffer3(0, 0, -1);
		 spotCutoffAngle = 180;
		 spotExponent = 0;
         attenuation = Vector3.createNew(1f, 0f, 0f);
		 positionAndTypeBuffer = OpenGLUtils.makeFloatBuffer4(0, 0, 0, 0);
         setPosition(0,0,0);
         commitPositionAndTypeBuffer();

         if(availableLightIndices == null) {
            availableLightIndices = new ArrayList<Integer>();
            for (int i = 0; i < MAX_AMOUNT_LIGHTS; i++) {
                availableLightIndices.add(i);
            }
         }
        if (availableLightIndices.isEmpty()) {
            Utils.log(TAG, "GL_LIGHT resources exceeded.");
        }
        lightId = availableLightIndices.get(0);
        availableLightIndices.remove(0);
        Utils.log(TAG, "Light "+lightId+ " added.");
    }

    public void release() {
        availableLightIndices.add(lightId);
    }

    @Override
    protected void renderInternal(@NotNull GL10 gl) {
        if (isVisible()) {
            gl.glEnable(GL10.GL_LIGHTING);
            gl.glEnable(GL10.GL_LIGHT0 + lightId);
            gl.glEnable(GL10.GL_NORMALIZE);
            gl.glEnable(GL10.GL_RESCALE_NORMAL);

            gl.glLightfv(GL10.GL_LIGHT0 + lightId, GL10.GL_POSITION, positionAndTypeBuffer);
            gl.glLightfv(GL10.GL_LIGHT0 + lightId, GL10.GL_AMBIENT, ambient.getColorBuffer());
            gl.glLightfv(GL10.GL_LIGHT0 + lightId, GL10.GL_DIFFUSE, diffuse.getColorBuffer());
            gl.glLightfv(GL10.GL_LIGHT0 + lightId, GL10.GL_SPECULAR, specular.getColorBuffer());
            gl.glLightfv(GL10.GL_LIGHT0 + lightId, GL10.GL_EMISSION, emissive.getColorBuffer());
            gl.glLightfv(GL10.GL_LIGHT0 + lightId, GL10.GL_SPOT_DIRECTION, direction);
            gl.glLightf(GL10.GL_LIGHT0 + lightId, GL10.GL_SPOT_CUTOFF, spotCutoffAngle);
            gl.glLightf(GL10.GL_LIGHT0 + lightId, GL10.GL_SPOT_EXPONENT, spotExponent);

            gl.glLightf(GL10.GL_LIGHT0 + lightId, GL10.GL_CONSTANT_ATTENUATION, attenuation.x);
            gl.glLightf(GL10.GL_LIGHT0 + lightId, GL10.GL_LINEAR_ATTENUATION, attenuation.y);
            gl.glLightf(GL10.GL_LIGHT0 + lightId, GL10.GL_QUADRATIC_ATTENUATION, attenuation.z);
            commitPositionAndTypeBuffer();
        } else {
            gl.glDisable(GL10.GL_LIGHTING);
        }
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
		spotExponent = Utils.clamp(value,0,128);
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
        if(angle == 180) {
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
        commitPositionAndTypeBuffer();
        this.type = type;
    }
}
