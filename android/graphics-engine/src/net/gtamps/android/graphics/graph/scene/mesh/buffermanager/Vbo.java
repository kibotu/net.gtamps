package net.gtamps.android.graphics.graph.scene.mesh.buffermanager;

import net.gtamps.android.graphics.utils.Registry;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 01/02/12
 * Time: 12:58
 */
public class Vbo {

    public int vertexBufferID;
    public int normalBufferID;
    public int colorBufferID;
    public int uvBufferID;
    public int indexBufferID;
    public boolean isAllocated;

    public Vbo(int[] buffer, boolean isAllocated) {
        this(buffer[0], buffer[1], buffer[2], buffer[3], buffer[4], isAllocated);
    }

    public Vbo(int vertexBufferID, int normalBufferID, int colorBufferID, int uvBufferID, int indexBufferID, boolean isAllocated) {
        this.vertexBufferID = vertexBufferID;
        this.normalBufferID = normalBufferID;
        this.colorBufferID = colorBufferID;
        this.uvBufferID = uvBufferID;
        this.indexBufferID = indexBufferID;
        this.isAllocated = isAllocated;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("[vertexID=").append(vertexBufferID);
        sb.append("|normalID=").append(normalBufferID);
        sb.append("|colorID=").append(colorBufferID);
        sb.append("|uvID=").append(uvBufferID);
        sb.append("|indexID=").append(indexBufferID);
        sb.append("|allocated=").append(isAllocated);
        sb.append(']');
        return sb.toString();
    }
}
