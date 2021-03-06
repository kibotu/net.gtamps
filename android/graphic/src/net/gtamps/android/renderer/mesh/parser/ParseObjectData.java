package net.gtamps.android.renderer.mesh.parser;

import net.gtamps.android.renderer.graph.scene.primitives.ParsedObject;
import net.gtamps.android.renderer.mesh.Face;
import net.gtamps.android.renderer.mesh.Material;
import net.gtamps.android.renderer.mesh.Uv;
import net.gtamps.android.renderer.mesh.parser.AParser.BitmapAsset;
import net.gtamps.android.renderer.mesh.parser.AParser.TextureAtlas;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;

public class ParseObjectData {

    protected ArrayList<ParseObjectFace> faces;
    protected int numFaces = 0;
    protected ArrayList<Vector3> vertices;
    protected ArrayList<Uv> texCoords;
    protected ArrayList<Vector3> normals;

    public String name;

    public ParseObjectData() {
        this.vertices = new ArrayList<Vector3>();
        this.texCoords = new ArrayList<Uv>();
        this.normals = new ArrayList<Vector3>();
        this.name = "";
        faces = new ArrayList<ParseObjectFace>();
    }

    public ParseObjectData(ArrayList<Vector3> vertices, ArrayList<Uv> texCoords, ArrayList<Vector3> normals) {
        this.vertices = vertices;
        this.texCoords = texCoords;
        this.normals = normals;
        this.name = "";
        faces = new ArrayList<ParseObjectFace>();
    }

//	public AnimationObject3d getParsedObject(TextureAtlas textureAtlas, HashMap<String, Material> materialMap, KeyFrame[] frames)
//	{
//		AnimationObject3d obj = new AnimationObject3d(numFaces * 3, numFaces, frames.length);
//		obj.name(name);
//		obj.setFrames(frames);
//
//		parseObject(obj, materialMap, textureAtlas);
//
//		return obj;
//	}

    public ParsedObject getParsedObject(HashMap<String, Material> materialMap, TextureAtlas textureAtlas) {
        ParsedObject obj = new ParsedObject(numFaces * 3, numFaces);
        obj.name = name;

        parseObject(obj, materialMap, textureAtlas);

        return obj;
    }

    private void parseObject(ParsedObject obj, HashMap<String, Material> materialMap, TextureAtlas textureAtlas) {
        int numFaces = faces.size();
        int faceIndex = 0;
        boolean hasBitmaps = textureAtlas.hasBitmaps();

        for (int i = 0; i < numFaces; i++) {
            ParseObjectFace face = faces.get(i);
            BitmapAsset ba = textureAtlas.getBitmapAssetByName(face.materialKey);

            for (int j = 0; j < face.faceLength; j++) {
                Vector3 newVertex = vertices.get(face.v[j]);

                Uv newUv = face.hasuv ? texCoords.get(face.uv[j]).clone() : new Uv();
                Vector3 newNormal = face.hasn ? normals.get(face.n[j]) : Vector3.createNew();
                Material material = materialMap.get(face.materialKey);

                Color4 newColor = new Color4(255, 255, 0, 255);
                if (material != null && material.getDiffuse() != null) {
                    newColor.setAll(material.getDiffuse());
                    obj.setMaterial(material);
                }

                if (hasBitmaps && (ba != null)) {
                    newUv.u = ba.uOffset + newUv.u * ba.uScale;
                    newUv.v = ba.vOffset + ((newUv.v + 1) * ba.vScale) - 1;
                }
                obj.getMesh().addVertex(newVertex, newNormal, newColor, newUv);
            }

            if (face.faceLength == 3) {
                obj.getMesh().faces.add(new Face(faceIndex, faceIndex + 1, faceIndex + 2));
            } else if (face.faceLength == 4) {
                obj.getMesh().faces.add(new Face(faceIndex, faceIndex + 1, faceIndex + 3));
                obj.getMesh().faces.add(new Face(faceIndex + 1, faceIndex + 2, faceIndex + 3));
            }

            faceIndex += face.faceLength;
        }

        if (hasBitmaps) {
            obj.getMesh().textures.addById(textureAtlas.getId());
        }

        cleanup();
    }

    public void calculateFaceNormal(ParseObjectFace face) {

        Vector3 v1 = vertices.get(face.v[0]);
        Vector3 v2 = vertices.get(face.v[1]);
        Vector3 v3 = vertices.get(face.v[2]);

        Vector3 vector1 = v2.sub(v1);
        Vector3 vector2 = v3.sub(v1);

        Vector3 normal = Vector3.createNew();
        normal.x = (vector1.y * vector2.z) - (vector1.z * vector2.y);
        normal.y = -((vector2.z * vector1.x) - (vector2.x * vector1.z));
        normal.z = (vector1.x * vector2.y) - (vector1.y * vector2.x);

        double normFactor = Math.sqrt((normal.x * normal.x) + (normal.y * normal.y) + (normal.z * normal.z));

        normal.x /= normFactor;
        normal.y /= normFactor;
        normal.z /= normFactor;

        normals.add(normal);

        int index = normals.size() - 1;
        face.n = new int[3];
        face.n[0] = index;
        face.n[1] = index;
        face.n[2] = index;
        face.hasn = true;
    }


    protected void cleanup() {
        faces.clear();
    }
}
