package net.gtamps.android.graphics.graph.scene.mesh.buffermanager;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 01/02/12
 * Time: 12:58
 */
public class Vbo {

    public int vertexBufferId;
    public int normalBufferId;
    public int colorBufferId;
    public int uvBufferId;
    public int indexBufferId;
    public boolean isAllocated;

    public Vbo(int[] buffer, boolean isAllocated) {
        this(buffer[0], buffer[1], buffer[2], buffer[3], buffer[4], isAllocated);
    }

    public Vbo(int vertexBufferId, int normalBufferId, int colorBufferId, int uvBufferId, int indexBufferId, boolean isAllocated) {
        this.vertexBufferId = vertexBufferId;
        this.normalBufferId = normalBufferId;
        this.colorBufferId = colorBufferId;
        this.uvBufferId = uvBufferId;
        this.indexBufferId = indexBufferId;
        this.isAllocated = isAllocated;
    }
}
