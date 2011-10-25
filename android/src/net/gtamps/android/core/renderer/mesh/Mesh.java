package net.gtamps.android.core.renderer.mesh;

import net.gtamps.android.core.math.Color4;
import net.gtamps.android.core.renderer.mesh.buffermanager.Color4BufferManager;
import net.gtamps.android.core.renderer.mesh.buffermanager.UvBufferManager;
import net.gtamps.android.core.renderer.mesh.buffermanager.Vector3BufferManager;
import net.gtamps.shared.math.Vector3;

public class Mesh {

	private Vector3BufferManager vertices;
	private UvBufferManager uvs;
	private Vector3BufferManager normals;
	private Color4BufferManager colors;

	private boolean hasUvs;
	private boolean hasNormals;
	private boolean hasColors;

	public Mesh(int maxElements) {
		this(maxElements,true,true,true);
	}

	public Mesh(int maxElements, Boolean useUvs, Boolean useNormals, Boolean useColors) {
		vertices = new Vector3BufferManager(maxElements);

		hasUvs = useUvs;
		hasNormals = useNormals;
		hasColors = useColors;

		if (hasUvs) uvs = new UvBufferManager(maxElements);
		if (hasNormals) normals = new Vector3BufferManager(maxElements);
		if (hasColors) colors = new Color4BufferManager(maxElements);
	}

	public Mesh(Vector3BufferManager points, UvBufferManager uvs, Vector3BufferManager normals, Color4BufferManager colors){
		vertices = points;
		this.uvs = uvs;
		this.normals = normals;
		this.colors = colors;

		hasUvs = uvs != null && uvs.size() > 0;
		hasNormals = normals != null && normals.size() > 0;
		hasColors = colors != null && colors.size() > 0;
	}

	public int size() {
		return vertices.size();
	}

	public int capacity() {
		return vertices.capacity();
	}

	public boolean hasUvs() {
		return hasUvs;
	}

	public boolean hasNormals()	{
		return hasNormals;
	}

	public boolean hasColors()	{
		return hasColors;
	}


	/**
	 * Use this to populate an Object3d's vertex data.
	 * Return's newly added vertex's index
	 *
	 *  	If hasUvs, hasNormals, or hasColors was set to false,
	 * 		their corresponding arguments are just ignored.
	 */
	public short addVertex(
		float pointX, float pointY, float pointZ,
		float textureU, float textureV,
		float normalX, float normalY, float normalZ,
		short colorR, short colorG, short colorB, short colorA)
	{
		vertices.add(pointX, pointY, pointZ);

		if (hasUvs) uvs.add(textureU, textureV);
		if (hasNormals) normals.add(normalX, normalY, normalZ);
		if (hasColors) colors.add(colorR, colorG, colorB, colorA);

		return (short)(vertices.size()-1);
	}

	/**
	 * More structured-looking way of adding a vertex (but potentially wasteful).
	 * The VO's taken in as arguments are only used to read the values they hold
	 * (no references are kept to them).
	 * Return's newly added vertex's index
	 *
	 * 		If hasUvs, hasNormals, or hasColors was set to false,
	 * 		their corresponding arguments are just ignored.
	 */
	public short addVertex(Vector3 point, Uv textureUv, Vector3 normal, Color4 color) {
		vertices.add(point);

		if (hasUvs) uvs.add(textureUv);
		if (hasNormals) normals.add(normal);
		if (hasColors) colors.add(color);

		return (short)(vertices.size()-1);
	}

	public void overwriteVerts(float[] newVerts) {
		vertices.overwrite(newVerts);
	}

	public void overwriteNormals(float[] newNormals) {
		normals.overwrite(newNormals);
	}

	public Vector3BufferManager getVertices() {
		return vertices;
	}

	/**
	 * List of texture coordinates
	 */
	public UvBufferManager getUvs() {
		return uvs;
	}

	/**
	 * List of normal values
	 */
	public Vector3BufferManager getNormals() {
		return normals;
	}

	/**
	 * List of color values
	 */
	public Color4BufferManager getColors()	{
		return colors;
	}

	public Mesh clone() {
		return new Mesh(vertices.clone(), uvs.clone(), normals.clone(), colors.clone());
	}
}