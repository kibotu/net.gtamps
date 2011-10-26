package net.gtamps.android.game.objects;

import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.mesh.Mesh;
import net.gtamps.android.core.renderer.mesh.buffermanager.FaceManager;
import net.gtamps.android.core.renderer.mesh.texture.TextureManager;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;

public class ParsedObject extends RenderableNode {

    public String name;

    public Mesh vertices;
	public TextureManager textures;
	public FaceManager faces;

    public ArrayList<ParsedObject> children;

    public ParsedObject(int maxVertices, int maxFaces) {
//        vertices = new Mesh(maxVertices, true,true,true);
//		faces = new FaceManager(maxFaces);
//		textures = new TextureManager();
//        children = new ArrayList<ParsedObject>();
    }

    public void updateVbo() {
//        ParsedObject o = children.get(0);
//        setVbo(new Vbo(o.vertices.getVertices().getBuffer(), o.faces.getBuffer(), o.vertices.getNormals().getBuffer(),o.vertices.getColors().getFloatBuffer(),o.vertices.getUvs().getFloatBuffer()));
    }

    @Override
    protected void renderInternal(@NotNull GL10 gl) {
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
}
