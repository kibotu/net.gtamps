package net.gtamps.android.graphics.graph.scene.primitives;

import net.gtamps.android.graphics.graph.RootNode;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.renderer.Shader;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.MathUtils;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe
 * Date: 14/12/12
 * Time: 15:42
 */
public class Skybox extends RootNode {

    Plane near;
    Plane far;
    Plane top;
    Plane bottom;
    Plane left;
    Plane right;

    public enum Face {
        NEAR_POSITIVE_Z,
        FAR_NEGATIVE_Z,
        TOP_POSITIVE_Y,
        BOTTOM_NEGATIVE_Y,
        LEFT_NEGATIVE_X,
        RIGHT_POSITIVE_X;
    }

    public Skybox() {
        build();
    }

    private void build() {
        near = new Plane();
        far = new Plane();
        top = new Plane();
        bottom = new Plane();
        left = new Plane();
        right = new Plane();

        final float halfSize = 0.5f;
        final float e = 1.01f;

        near.getPosition(true).z = halfSize;
        near.getRotation(true).y = MathUtils.deg2Rad(180);
        near.getRenderState().setShader(Shader.Type.SKYBOX);
        near.setScaling(e,e,e);

        left.getRotation(true).y = MathUtils.deg2Rad(-90);
        left.getPosition(true).x = halfSize;
        left.getRenderState().setShader(Shader.Type.SKYBOX);
        left.setScaling(e,e,e);

        far.getPosition(true).z = -halfSize;
        far.getRenderState().setShader(Shader.Type.SKYBOX);
        far.setScaling(e,e,e);

        right.getRotation(true).y = MathUtils.deg2Rad(90);
        right.getPosition(true).x = -halfSize;
        right.getRenderState().setShader(Shader.Type.SKYBOX);
        right.setScaling(e,e,e);

        top.getRotation(true).x = MathUtils.deg2Rad(90);
        top.getRotation(true).y = MathUtils.deg2Rad(180);
        top.getPosition(true).y = halfSize;
        top.getRenderState().setShader(Shader.Type.SKYBOX);
        top.setScaling(e,e,e);

        bottom.getRotation(true).x = MathUtils.deg2Rad(-90);
        bottom.getRotation(true).y = MathUtils.deg2Rad(180);
        bottom.getPosition(true).y = -halfSize;
        bottom.getRenderState().setShader(Shader.Type.SKYBOX);
        bottom.setScaling(e,e,e);

        add(near);
        add(far);
        add(top);
        add(bottom);
        add(left);
        add(right);
    }

    public void addTexture(@NotNull Texture texture, @NotNull Face face) {
        switch (face) {
            case NEAR_POSITIVE_Z:
                near.addTexture(texture);
                break;
            case FAR_NEGATIVE_Z:
                far.addTexture(texture);
                break;
            case TOP_POSITIVE_Y:
                top.addTexture(texture);
                break;
            case BOTTOM_NEGATIVE_Y:
                bottom.addTexture(texture);
                break;
            case LEFT_NEGATIVE_X:
                left.addTexture(texture);
                break;
            case RIGHT_POSITIVE_X:
                right.addTexture(texture);
                break;
            default:
                Logger.e(this, "this error can't ever happen.");
        }
    }

    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
    }
}
