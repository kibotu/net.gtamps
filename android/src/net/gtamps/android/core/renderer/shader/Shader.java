package net.gtamps.android.core.renderer.shader;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import net.gtamps.android.R;
import net.gtamps.android.core.renderer.Registry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Shader {

    public enum Type {

        //        MIN(R.raw.min_vs, R.raw.min_ps);
        PHONG(R.raw.trambarend_phong_vs, R.raw.trambarend_phong_ps);
//        GAUROUD(R.raw.gouraud_vs, R.raw.gouraud_ps),
//        NORMAL(R.raw.normalmap_vs, R.raw.normalmap_ps);

        public final int vs;
        public final int ps;
        public Shader shader;

        private Type(final int vs, final int ps) {
            this.vs = vs;
            this.ps = ps;
            shader = null;
        }
    }

    public static void load() {
        for(Shader.Type type : Shader.Type.values()) {
            type.shader = new Shader(type, Registry.getContext().getApplicationContext(),false,0);
        }
    }

    /**
     * program/vertex/fragment handles
     */
    private int program, vertexShader, pixelShader;

    /**
     * The shaders
     */
    private String vertexS, fragmentS;

    /**
     * does it have textures?
     */
    private boolean hasTextures;
    private int numTextures;

    public Shader() {
    }

    /**
     * Takes in Strings directly
     */
    public Shader(String vertexS, String fragmentS, boolean hasTextures, int numTextures) {
        setup(vertexS, fragmentS, hasTextures, numTextures);
    }

    public Shader(Type shaderType,  Context context, boolean hasTextures, int numTextures) {
        this(shaderType.vs,shaderType.ps, context,hasTextures,numTextures);
    }

    /**
     * Takes in ids for files to be read
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
            Log.d("ERROR-readingShader", "Could not read shader: " + e.getLocalizedMessage());
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
     * @return returns 1 if creation successful, 0 if not
     */
    private int createProgram() {
        // Vertex shader
        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexS);
        if (vertexShader == 0) {
            return 0;
        }

        // pixel shader
        pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentS);
        if (pixelShader == 0) {
            return 0;
        }

        // Create the program
        program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            //checkGlError("glAttachShader VS " + this.toString());
            GLES20.glAttachShader(program, pixelShader);
            //checkGlError("glAttachShader PS");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e("Shader", "Could not link program: ");
                Log.e("Shader", GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
                return 0;
            }
        } else
            Log.d("CreateProgram", "Could not create program");

        return 1;
    }

    /**
     * Loads a shader (either vertex or pixel) given the source
     *
     * @param shaderType VERTEX or PIXEL
     * @param source     The string data representing the shader code
     * @return handle for shader
     */
    private int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e("Shader", "Could not compile shader " + shaderType + ":");
                Log.e("Shader", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    /**
     * Error for OpenGL
     *
     * @param op
     */
    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("Shader", op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    public int getProgram() {
        return program;
    }

    public void setProgram(int program) {
        this.program = program;
    }

    public int getVertexShader() {
        return vertexShader;
    }

    public void setVertexShader(int shader) {
        vertexShader = shader;
    }

    public int getPixelShader() {
        return pixelShader;
    }

    public void setPixelShader(int shader) {
        pixelShader = shader;
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
