package net.gtamps.android.renderer;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import net.gtamps.android.renderer.shader.Shader;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Color4;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;

public class GLES20Renderer extends BasicRenderer {

    public GLES20Renderer(IRenderAction renderAction) {
        super(renderAction);
    }

    @Override
    public void draw(GL10 unusedGL) {
        if (!RenderCapabilities.supportsGLES20()) return;

        int program = Shader.Type.PHONG.shader.getProgram();

        // unbound last shader
        GLES20.glUseProgram(program);
        Logger.checkGlError(this, "glUseProgram");

        // draw scenes
        for (int i = 0; i < renderAction.getScenes().size(); i++) {
            renderAction.getScenes().get(i).getScene().update(getDelta());
            renderAction.getScenes().get(i).getScene().process(glState);
        }
    }

    @Override
    public int allocTexture(Bitmap texture, boolean generateMipMap) {
        int bitmapFormat = texture.getConfig() == Bitmap.Config.ARGB_8888 ? GLES20.GL_RGBA : GLES20.GL_RGB;
        int textureId = newTextureID();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapFormat, texture, 0);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, generateMipMap ? GLES20.GL_NEAREST_MIPMAP_NEAREST : GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        Logger.i(this, "[w:" + texture.getWidth() + "|h:" + texture.getHeight() + "|id:" + textureId + "|mimap=" + generateMipMap + "] Bitmap atlas successfully allocated.");
        texture.recycle();
        return textureId;
    }

    /**
     * Create a mipmapped 2D texture image
     *
     * @return
     */
    private int createMipMappedTexture2D() {

        // Texture object handle
        int[] textureId = new int[1];
        int width = 256;
        int height = 256;
        int level;
        byte[] pixels;
        byte[] prevImage;
        byte[] newImage;

        pixels = genCheckImage(width, height, 8);

        // Generate a texture object
        GLES20.glGenTextures(1, textureId, 0);

        // Bind the texture object
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

        // Load mipmap level 0
        ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(width * height * 3);
        pixelBuffer.put(pixels).position(0);

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, width, height, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);

        level = 1;
        prevImage = pixels;

        while (width > 1 && height > 1) {
            int newWidth,
                    newHeight;

            newWidth = width / 2;
            if (newWidth <= 0)
                newWidth = 1;

            newHeight = height / 2;
            if (newHeight <= 0)
                newHeight = 1;

            // Generate the next mipmap level
            newImage = genMipMap2D(prevImage, width, height, newWidth, newHeight);

            // Load the mipmap level
            pixelBuffer = ByteBuffer.allocateDirect(newWidth * newHeight * 3);
            pixelBuffer.put(newImage).position(0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, level, GLES20.GL_RGB, newWidth, newHeight, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);

            // Set the previous image for the next iteration
            prevImage = newImage;
            level++;

            // Half the width and height
            width = newWidth;
            height = newHeight;
        }


        // Set the filtering mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST_MIPMAP_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        return textureId[0];

    }

    /**
     * From an RGB8 source image, generate the next level mipmap
     *
     * @param src
     * @param srcWidth
     * @param srcHeight
     * @param dstWidth
     * @param dstHeight
     * @return
     */
    private byte[] genMipMap2D(byte[] src, int srcWidth, int srcHeight, int dstWidth, int dstHeight) {
        int x,
                y;
        int texelSize = 3;

        byte[] dst = new byte[texelSize * (dstWidth) * (dstHeight)];

        for (y = 0; y < dstHeight; y++) {
            for (x = 0; x < dstWidth; x++) {
                int[] srcIndex = new int[4];
                float r = 0.0f,
                        g = 0.0f,
                        b = 0.0f;
                int sample;

                // Compute the offsets for 2x2 grid of pixels in previous
                // image to perform box filter
                srcIndex[0] =
                        (((y * 2) * srcWidth) + (x * 2)) * texelSize;
                srcIndex[1] =
                        (((y * 2) * srcWidth) + (x * 2 + 1)) * texelSize;
                srcIndex[2] =
                        ((((y * 2) + 1) * srcWidth) + (x * 2)) * texelSize;
                srcIndex[3] =
                        ((((y * 2) + 1) * srcWidth) + (x * 2 + 1)) * texelSize;

                // Sum all pixels
                for (sample = 0; sample < 4; sample++) {
                    r += src[srcIndex[sample]];
                    g += src[srcIndex[sample] + 1];
                    b += src[srcIndex[sample] + 2];
                }

                // Average results
                r /= 4.0;
                g /= 4.0;
                b /= 4.0;

                // Store resulting pixels
                dst[(y * (dstWidth) + x) * texelSize] = (byte) (r);
                dst[(y * (dstWidth) + x) * texelSize + 1] = (byte) (g);
                dst[(y * (dstWidth) + x) * texelSize + 2] = (byte) (b);
            }
        }
        return dst;
    }

    /**
     * Generate an RGB8 checkerboard image
     *
     * @param width
     * @param height
     * @param checkSize
     * @return texture
     */
    private byte[] genCheckImage(int width, int height, int checkSize) {
        int x,
                y;
        byte[] pixels = new byte[width * height * 3];


        for (y = 0; y < height; y++)
            for (x = 0; x < width; x++) {
                byte rColor = 0;
                byte bColor = 0;

                if ((x / checkSize) % 2 == 0) {
                    rColor = (byte) (127 * ((y / checkSize) % 2));
                    bColor = (byte) (127 * (1 - ((y / checkSize) % 2)));
                } else {
                    bColor = (byte) (127 * ((y / checkSize) % 2));
                    rColor = (byte) (127 * (1 - ((y / checkSize) % 2)));
                }

                pixels[(y * height + x) * 3] = rColor;
                pixels[(y * height + x) * 3 + 1] = 0;
                pixels[(y * height + x) * 3 + 2] = bColor;
            }

        return pixels;
    }

    @Override
    public int newTextureID() {
        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        return textureIds[0];
    }

    @Override
    public void deleteTexture(int... textureIds) {
        GLES20.glDeleteTextures(textureIds.length, textureIds, 0);
    }

    @Override
    public void clearScreen(Color4 bgcolor) {
        GLES20.glClearColor(bgcolor.r,bgcolor.g,bgcolor.b,bgcolor.a);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_STENCIL_BUFFER_BIT);
    }

    @Override
    public void reset() {
        Shader.load();

        //GLES20.glEnable   ( GLES20.GL_DEPTH_TEST );
        GLES20.glClearDepthf(1.0f);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthMask(true);

        // cull backface
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
    }
}
