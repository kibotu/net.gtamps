package android.normalizer.model;

public class Vertex {
	
	public final Vector vertice;
	public final UV texture;
	public final Vector normal;
	public final ColorMaterial colors;
	
	public Vertex(final Vector vertice,  final UV texture, final Vector normal, final ColorMaterial colors) {
		this.vertice = vertice;
		this.normal = normal;
		this.texture = texture;
		this.colors = colors;
	}
	
	public Vertex(final Vector vertice, final Vector normal) {
		this(vertice, null, normal, null);
	}
	
	public Vertex(final Vector vertice, final Vector normal, final ColorMaterial colors) {
		this(vertice, null, normal, colors);
	}
	
	public Vertex(final Vector vertice, UV texture, final Vector normal) {
		this(vertice, texture, normal, null);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;

        Vertex vertex = (Vertex) o;

        if (!normal.equals(vertex.normal)) return false;
        if (!vertice.equals(vertex.vertice)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = vertice.hashCode();
        result = 31 * result + normal.hashCode();
        return result;
    }
}
