package net.gtamps.android.core.renderer.graph.scene.primitives;

import net.gtamps.android.core.renderer.Registry;
import net.gtamps.android.core.renderer.RenderCapabilities;
import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.mesh.Mesh;
import net.gtamps.android.core.renderer.mesh.parser.IParser;
import net.gtamps.android.core.renderer.mesh.parser.Parser;
import net.gtamps.shared.Config;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class ParsedObject extends RenderableNode {

    public String name;

    public ParsedObject() {
    }

    public ParsedObject(int maxVertices, int maxFaces) {
        mesh = new Mesh(maxVertices, maxFaces);
    }

    public ParsedObject(ParsedObject other) {
        mesh = new Mesh(other.mesh);
        this.dimension.set(other.dimension);
        this.scaling.set(other.scaling);
    }

    public static ParsedObject parseObject(String objname, int textureResourceId, boolean generateMipMap) {
        IParser objParser = Parser.createParser(Parser.Type.OBJ, Config.PACKAGE_NAME + objname, generateMipMap);
        objParser.parse();
        ParsedObject parsedObject = objParser.getParsedObject();
        if(RenderCapabilities.supportsGLES20()) {
            ((ParsedObject) parsedObject.get(0)).setTextureId(textureResourceId);
        } else {
            ((ParsedObject) parsedObject.get(0)).setTextureId(Registry.getTextureLibrary().loadTexture(textureResourceId, generateMipMap));
        }

        return parsedObject;
    }

    @Override
    protected void renderInternal(@NotNull GL10 gl) {
    }

    @Override
    public RenderableNode getStatic() {
        return new ParsedObject(this);
    }

    @Override
    public void onDirty() {
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

    @Override
    protected void setOptions() {
    }
}
