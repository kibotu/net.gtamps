package net.gtamps.android.game.objects;

import net.gtamps.android.core.renderer.graph.PureVboNode;
import net.gtamps.android.core.renderer.graph.SceneNode;
import net.gtamps.android.core.renderer.mesh.Mesh;
import net.gtamps.android.core.renderer.mesh.buffermanager.FaceManager;
import net.gtamps.android.core.renderer.mesh.buffermanager.Vbo;
import net.gtamps.android.core.renderer.mesh.texture.TextureManager;

import java.util.ArrayList;

public class ParsedObject extends PureVboNode implements IObject3d {

    public String name;

    public Mesh vertices;
	public TextureManager textures;
	public FaceManager faces;

    public ArrayList<ParsedObject> children;

    public ParsedObject(int maxVertices, int maxFaces) {
        vertices = new Mesh(maxVertices, true,true,true);
		faces = new FaceManager(maxFaces);
		textures = new TextureManager();
        children = new ArrayList<ParsedObject>();
    }

    public void updateVbo() {
        ParsedObject o = children.get(0);
        setVbo(new Vbo(o.vertices.getVertices().getBuffer(), o.faces.getBuffer(), o.vertices.getNormals().getBuffer(),o.vertices.getColors().getFloatBuffer(),o.vertices.getUvs().getFloatBuffer()));
    }

    @Override
    public SceneNode getNode() {
        return this;
    }
}
