/**
 * Represents a shader object
 */

package net.gtamps.android.core.renderer.shader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.opengl.GLES20;
import net.gtamps.shared.Utils.Logger;

import static javax.microedition.khronos.opengles.GL10.GL_FLAT;
import static javax.microedition.khronos.opengles.GL10.GL_SMOOTH;

public class Shader {

    public enum Native {
        FLAT(GL_FLAT),
        SMOOTH(GL_SMOOTH);

        private final int value;
        private Native (int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    /**
     * program/vertex/fragment handles
     */
	private int program, vertexShaderHandlerID, pixelShaderHandlerID;

    /**
     * actual shader strings
     */
	private String vertexS, fragmentS;

    /**
     * use textures
     */
	private boolean hasTextures;

    /**
     * texture amount
     */
	private int numTextures;

    /**
     * Constructs shader by string.
     *
     * @param vertexS
     * @param fragmentS
     * @param hasTextures
     * @param numTextures
     */
	public Shader(String vertexS, String fragmentS, boolean hasTextures, int numTextures) {
		setup(vertexS, fragmentS, hasTextures, numTextures);
	}

    /**
     * Constructs shader by resource id.
     *
     * @param vID
     * @param fID
     * @param context
     * @param hasTextures
     * @param numTextures
     */
	public Shader(int vID, int fID, Context context, boolean hasTextures, int numTextures) {
		StringBuffer vs = new StringBuffer();
		StringBuffer fs = new StringBuffer();

		// read the files
		try {
			// Read the file from the resource
			//Log.d("loadFile", "Trying to read vs");
			// Read VS first
			InputStream inputStream = context.getResources().openRawResource(vID);
			// setup Bufferedreader
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

			String read = in.readLine();
			while (read != null) {
				vs.append(read + "\n");
				read = in.readLine();
			}

			vs.deleteCharAt(vs.length() - 1);

			// Now read FS
			inputStream = context.getResources().openRawResource(fID);
			// setup Bufferedreader
			in = new BufferedReader(new InputStreamReader(inputStream));

			read = in.readLine();
			while (read != null) {
				fs.append(read + "\n");
				read = in.readLine();
			}

			fs.deleteCharAt(fs.length() - 1);
		} catch (Exception e) {
			Logger.d("ERROR-readingShader", "Could not read shader: " + e.getLocalizedMessage());
		}


		// Setup everything
		setup(vs.toString(), fs.toString(), hasTextures, numTextures);
	}

	/** 
	 * Sets up everything
     *
	 * @param vs the vertex shader
	 * @param fs the fragment shader 
	 */
	private void setup(String vs, String fs, boolean hasTextures, int numTextures) {
		this.vertexS = vs;
		this.fragmentS = fs;

		// create the program
		int create = createProgram();

		// texture variables
		this.hasTextures = hasTextures;
		this.numTextures = numTextures;
	}

	/**
	 * Creates a shader program.
     *
	 * @param vertexSource
	 * @param fragmentSource
	 * @return returns 1 if creation successful, 0 if not
	 */
	private int createProgram() {
		// Vertex shader
		vertexShaderHandlerID = loadShader(GLES20.GL_VERTEX_SHADER, vertexS);
		if (vertexShaderHandlerID == 0) {
			return 0;
		}

		// pixel shader
		pixelShaderHandlerID = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentS);
		if (pixelShaderHandlerID == 0) {
			return 0;
		}

		// Create the program
		program = GLES20.glCreateProgram();
		if (program == 0) {
            Logger.d("CreateProgram", "Could not create program");
        }

        GLES20.glAttachShader(program, vertexShaderHandlerID);
        //checkGlError("glAttachShader VS " + this.toString());
        GLES20.glAttachShader(program, pixelShaderHandlerID);
        //checkGlError("glAttachShader PS");
        GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            Logger.e("Shader", "Could not link program: ");
            Logger.e("Shader", GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program);
            program = 0;
            return 0;
        }

		return 1;
	}

	/**
	 * Loads a shader (either vertex or pixel) given the source
     *
	 * @param shaderType VERTEX or PIXEL
	 * @param source The string data representing the shader code
	 * @return handle for shader
	 */
	private int loadShader(int shaderType, String source) {
		int shader = GLES20.glCreateShader(shaderType);
		if (shader == 0) return shader;

        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Logger.e("Shader", "Could not compile shader " + shaderType + ":");
            Logger.e("Shader", GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }
		return shader;
	}

	/**
	 * Error for OpenGL
	 * @param op
	 */
	private void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Logger.e("Shader", op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}

	public int getProgram() {
		return program;
	}

	public void setProgram(int program) {
		this.program = program;
	}

	public int getVertexShaderHandlerID() {
		return vertexShaderHandlerID;
	}

	public void setVertexShaderHandlerID(int shader) {
		vertexShaderHandlerID = shader;
	}

	public int getPixelShaderHandlerID() {
		return pixelShaderHandlerID;
	}

	public void setPixelShaderHandlerID(int shader) {
		pixelShaderHandlerID = shader;
	}

	public String getVertexS() {
		return vertexS;
	}

	public void setVertexS(String vertexs) {
		vertexS = vertexs;
	}

	public String getFragmentS() {
		return fragmentS;
	}

	public void setFragmentS(String fragments) {
		fragmentS = fragments;
	}
}
