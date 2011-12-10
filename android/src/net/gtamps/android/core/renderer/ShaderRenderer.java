package net.gtamps.android.core.renderer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.scene.primitives.Camera;
import net.gtamps.android.core.renderer.graph.scene.primitives.Cube;
import net.gtamps.android.core.renderer.shader.Shader;
import net.gtamps.android.core.utils.OpenGLUtils;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;
import java.util.HashMap;

public class ShaderRenderer extends BasicRenderer {

    private HashMap<String, Shader> shaderMap;

    public ShaderRenderer(BasicRenderActivity.IRenderActivity renderActivity) {
        super(renderActivity);
        shaderMap = new HashMap<String, Shader>(3);
    }

    // Modelview/Projection matrices
    private float[] mMVPMatrix = new float[16];
    private float[] mProjMatrix = new float[16];
    private float[] mScaleMatrix = new float[16];   // scaling
    private float[] mRotXMatrix = new float[16];	// rotation x
    private float[] mRotYMatrix = new float[16];	// rotation y
    private float[] mMMatrix = new float[16];		// rotation
    private float[] mVMatrix = new float[16]; 		// modelview
    private float[] normalMatrix = new float[16]; 	// modelview normal

    // textures enabled?
    private boolean enableTexture = true;
    private int[] _texIDs;

    // light parameters
    private float[] lightPos;
    private float[] lightColor;
    private float[] lightAmbient;
    private float[] lightDiffuse;

    // angle rotation for light
    float angle = 0.0f;
    boolean lightRotate = true;

    // material properties
    private float[] matAmbient;
    private float[] matDiffuse;
    private float[] matSpecular;
    private float matShininess;

    // eye pos
    private float[] eyePos = {-5.0f, 0.0f, 0.0f};

    // scaling
    float scaleX = 1.0f;
    float scaleY = 1.0f;
    float scaleZ = 1.0f;

    // rotation
    public float mAngleX = 0;
    public float mAngleY = 0;

    Cube cube = new Cube();
//    Camera camera = new Camera(0, 0, 30, 0, 0, -1, 0, 1, 0);

    @Override
    public void draw(GL10 unusedGL) {

        mAngleX++;
        mAngleY++;

        // clear screen
        GLES20.glClearColor(.0f, .0f, .0f, 1.0f);
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // unbound last shader
        GLES20.glUseProgram(GLES20.GL_NONE);
        OpenGLUtils.checkGlError("glUseProgram");

        // get current shader
        int program = shaderMap.get("phong").get_program();

        // actually use shader
        GLES20.glUseProgram(program);
        OpenGLUtils.checkGlError("glUseProgram " + program);

        // rotate the light?
        if (lightRotate) {
            angle += 0.000005f;
            if (angle >= 6.2)
                angle = 0.0f;

            // rotate light about y-axis
            float newPosX = (float)(Math.cos(angle) * lightPos[0] - Math.sin(angle) * lightPos[2]);
            float newPosZ = (float)(Math.sin(angle) * lightPos[0] + Math.cos(angle) * lightPos[2]);
            lightPos[0] = newPosX;
            lightPos[1] = 3;
            lightPos[2] = newPosZ;
        }

        // scaling
        Matrix.setIdentityM(mScaleMatrix, 0);
        Matrix.scaleM(mScaleMatrix, 0, scaleX, scaleY, scaleZ);

        // Rotation along x
        Matrix.setRotateM(mRotXMatrix, 0, mAngleY, -1.0f, 0.0f, 0.0f);
        Matrix.setRotateM(mRotYMatrix, 0, mAngleX, 0.0f, 1.0f, 0.0f);

        // Set the ModelViewProjectionMatrix
        float tempMatrix[] = new float[16];
        Matrix.multiplyMM(tempMatrix, 0, mRotYMatrix, 0, mRotXMatrix, 0);
        Matrix.multiplyMM(mMMatrix, 0, mScaleMatrix, 0, tempMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);

        // send to the shader
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "uMVPMatrix"), 1, false, mMMatrix, 0);
        OpenGLUtils.checkGlError("uMVPMatrix");

        // Create the normal modelview matrix
        // Invert + transpose of mvpmatrix
        Matrix.invertM(normalMatrix, 0, mMVPMatrix, 0);
        Matrix.transposeM(normalMatrix, 0, normalMatrix, 0);

        // send to the shader
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "normalMatrix"), 1, false, mMVPMatrix, 0);
        OpenGLUtils.checkGlError("normalMatrix");

        // lighting variables
        // send to shaders
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, "lightPos"), 1, lightPos, 0);
        OpenGLUtils.checkGlError("lightPos");
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, "lightColor"), 1, lightColor, 0);
        OpenGLUtils.checkGlError("lightColor");

        // material
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, "matAmbient"), 1, matAmbient, 0);
        OpenGLUtils.checkGlError("matAmbient");
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, "matDiffuse"), 1, matDiffuse, 0);
        OpenGLUtils.checkGlError("matDiffuse");
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, "matSpecular"), 1, matSpecular, 0);
        OpenGLUtils.checkGlError("matSpecular");
        GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "matShininess"), matShininess);
        OpenGLUtils.checkGlError("matShininess");

        // eye position
        GLES20.glUniform3fv(GLES20.glGetUniformLocation(program, "eyePos"), 1, eyePos, 0);
        OpenGLUtils.checkGlError("eyePos");

        cube.shade(glState);
    }

    private void loadShader() {
        shaderMap.put("phong",new Shader(R.raw.phong_vs,R.raw.phong_ps, Registry.getContext().getApplicationContext(), false, 0));
        shaderMap.put("gouraud",new Shader(R.raw.gouraud_vs,R.raw.gouraud_ps, Registry.getContext().getApplicationContext(), false, 0));
//        shaderMap.put("normal",new Shader(R.raw.normalmap_vs,R.raw.normalmap_ps, Registry.getContext().getApplicationContext(), false, 0));
//        shaderMap.put("min",new Shader(R.raw.min_vs,R.raw.min_ps, Registry.getContext().getApplicationContext(), false, 0));
    }

    @Override
    public void reset(GL10 unusedGL) {
        glState.setGl(unusedGL);
        loadShader();

        //GLES20.glEnable   ( GLES20.GL_DEPTH_TEST );
        GLES20.glClearDepthf(1.0f);
        GLES20.glDepthFunc( GLES20.GL_LEQUAL );
        GLES20.glDepthMask( true );

        // cull backface
        GLES20.glEnable( GLES20.GL_CULL_FACE );
        GLES20.glCullFace(GLES20.GL_BACK);

        // light variables
        float[] lightP = {30.0f, 0.0f, 10.0f, 1};
        this.lightPos = lightP;

        float[] lightC = {0.5f, 0.5f, 0.5f};
        this.lightColor = lightC;

        // material properties
        float[] mA = {1.0f, 0.5f, 0.5f, 1.0f};
        matAmbient = mA;

        float[] mD = {0.5f, 0.5f, 0.5f, 1.0f};
        matDiffuse = mD;

        float[] mS =  {1.0f, 1.0f, 1.0f, 1.0f};
        matSpecular = mS;

        matShininess = 5.0f;

        // setup cube
        addToSetupQueue(cube);
        cube.onDirty(glState.getGl());
        cube.setShader(shaderMap.get("phong"));

        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -5.0f, 0.0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        super.onSurfaceChanged(gl10,width,height);
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 0.1f, 10);
    }
}
