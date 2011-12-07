package net.gtamps.android.core.renderer;

import android.opengl.GLES20;
import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.scene.primitives.Camera;
import net.gtamps.android.core.renderer.graph.scene.primitives.Cube;
import net.gtamps.android.core.renderer.shader.Shader;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;
import java.util.HashMap;

public class ShaderRenderer extends BasicRenderer {

    private HashMap<String, Shader> shaderMap;

    public ShaderRenderer(BasicRenderActivity.IRenderActivity renderActivity) {
        super(renderActivity);
        shaderMap = new HashMap<String, Shader>(3);
    }

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
    
    Cube cube = new Cube();
    Camera camera = new Camera(0, 0, 30, 0, 0, -1, 0, 1, 0);

    @Override
    public void draw(GL10 unusedGL) {

        int program = shaderMap.get("min").getProgram();

        camera.render(glState.getGl11());

        // use program
        GLES20.glUseProgram(program);

        // send to the shader
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "uMVPMatrix"), 1, false,camera.getCombinedTransformation().values , 0);

        cube.render(glState.getGl11());

//        // rotate the light?
//        if (lightRotate) {
//            angle += 0.000005f;
//            if (angle >= 6.2)
//                angle = 0.0f;
//
//            // rotate light about y-axis
//            float newPosX = (float)(Math.cos(angle) * lightPos[0] - Math.sin(angle) * lightPos[2]);
//            float newPosZ = (float)(Math.sin(angle) * lightPos[0] + Math.cos(angle) * lightPos[2]);
//            lightPos[0] = newPosX; lightPos[2] = newPosZ;
//        }


    }

	public static void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Logger.e("GLERROR", op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}

    private void loadShader() {
//        shaderMap.put("phong",new Shader(R.raw.phong_vs,R.raw.phong_ps, Registry.getContext().getApplicationContext(), false, 0));
//        shaderMap.put("gouraud",new Shader(R.raw.gouraud_vs,R.raw.gouraud_ps, Registry.getContext().getApplicationContext(), false, 0));
//        shaderMap.put("normal",new Shader(R.raw.normalmap_vs,R.raw.normalmap_ps, Registry.getContext().getApplicationContext(), false, 0));
        shaderMap.put("min",new Shader(R.raw.min_vs,R.raw.min_ps, Registry.getContext().getApplicationContext(), false, 0));
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

        cube.setup(glState);
        cube.setShader(shaderMap.get("min"));
    }

    @Override
    public void onSurfaceChanged(GL10 unusedGL, int width, int height) {
        super.onSurfaceChanged(unusedGL,width,height);
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
//		Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 0.5f, 10);
    }
}
