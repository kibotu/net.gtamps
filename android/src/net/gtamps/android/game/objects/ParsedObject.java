package net.gtamps.android.game.objects;

import net.gtamps.android.core.graph.PureVboNode;
import net.gtamps.android.core.graph.SceneNode;
import net.gtamps.android.core.renderer.Vbo;
import net.gtamps.android.core.utils.Utils;
import net.gtamps.android.core.utils.parser.FaceManager;
import net.gtamps.android.core.utils.parser.TextureManager;
import net.gtamps.android.core.utils.parser.VertexManager;

import java.util.ArrayList;

public class ParsedObject extends PureVboNode implements IObject3d {

    private static final String TAG = ParsedObject.class.getSimpleName();

    public String name;

    public VertexManager vertices;
	public TextureManager textures;
	public FaceManager faces;

    public ArrayList<ParsedObject> children;

    public ParsedObject(int maxVertices, int maxFaces) {
        vertices = new VertexManager(maxVertices, true,true,true);
		faces = new FaceManager(maxFaces);
		textures = new TextureManager();
        children = new ArrayList<ParsedObject>();

        if(children.size() > 1) {
            ParsedObject o = children.get(0);
            Utils.log(TAG, "Obj[vertices="+o.vertices.getVertices().size()+"|normals="+o.vertices.getNormals().size()+"|faces="+o.faces.size()+"|colors="+o.vertices.getColors().size()+"uvs="+o.vertices.getUvs().size()+"]");
            setVbo(new Vbo(o.vertices.getVertices().getBuffer(), o.faces.getBuffer(), o.vertices.getNormals().getBuffer(),null,o.vertices.getUvs().getFloatBuffer()));
            hasMipMap = true;
            Utils.log(TAG, "Obj[vertices="+o.vertices.getVertices().getBuffer().capacity()+"|normals="+o.vertices.getNormals().getBuffer().capacity()+"|faces="+o.faces.getBuffer().capacity()+"|colors="+o.vertices.getColors().getFloatBuffer().capacity()+"uvs="+o.vertices.getUvs().capacity()+"]");
        }
    }

    @Override
    public SceneNode getNode() {
        return this;
    }
}
