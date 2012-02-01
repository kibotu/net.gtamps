package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.scene.SceneNode;
import net.gtamps.android.graphics.renderer.Shader;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Vector3;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 19:59
 */
public class Light extends SceneNode {

    private float[] color = {0.5f, 0.5f, 0.5f, 1f};
    private Vector3 direction = Vector3.createNew();

    public Light(float positionX, float positionY, float positionZ, float directionX, float directionY, float directionZ) {
        setPosition(positionX,positionY,positionZ);
        direction.set(directionX,directionY,directionZ);
    }

    @Override
    public void onCreate(GL10 gl10) {
    }

    @Override
    protected void onCreateInternal(GL10 gl10) {
    }

    @Override
    protected void onTransformationInternal(GL10 gl10) {
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        int program = Shader.Type.PHONG.shader.getProgram();
        // unbound last shader
        glUseProgram(program);
        Logger.checkGlError(this, "glUseProgram");
        glUniform3fv(glGetUniformLocation(program, "lightPosition"), 1, getPosition().asArray(), 0);
        Logger.checkGlError(this, "lightPosition");
        glUniform4fv(glGetUniformLocation(program, "lightColor"), 1, color, 0);
        Logger.checkGlError(this, "lightColor");
    }

    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
    }

    @Override
    public void onResume(GL10 gl10) {
    }

    @Override
    protected void onResumeInternal(GL10 gl10) {
    }
}
