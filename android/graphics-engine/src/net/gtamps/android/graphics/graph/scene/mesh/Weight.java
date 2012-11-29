package net.gtamps.android.graphics.graph.scene.mesh;

/**
 * User: Jan Rabe
 * Date: 28/11/12
 * Time: 09:53
 */
public class Weight {

    public float x;
    public float y;
    public float z;
    public float w;
    public int influence1;
    public int influence2;
    public int influence3;
    public int influence4;

    public Weight(float x, float y, float z, float w, int influence1, int influence2, int influence3, int influence4) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.influence1 = influence1;
        this.influence2 = influence2;
        this.influence3 = influence3;
        this.influence4 = influence4;
    }

    public Weight() {
        this(0,0,0,0,0,0,0,0);
    }

    public Weight clone() {
        return new Weight(x,y,z,w,influence1,influence2,influence3,influence4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weight)) return false;

        Weight weight = (Weight) o;

        if (influence1 != weight.influence1) return false;
        if (influence2 != weight.influence2) return false;
        if (influence3 != weight.influence3) return false;
        if (influence4 != weight.influence4) return false;
        if (Float.compare(weight.w, w) != 0) return false;
        if (Float.compare(weight.x, x) != 0) return false;
        if (Float.compare(weight.y, y) != 0) return false;
        if (Float.compare(weight.z, z) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        result = 31 * result + (w != +0.0f ? Float.floatToIntBits(w) : 0);
        result = 31 * result + influence1;
        result = 31 * result + influence2;
        result = 31 * result + influence3;
        result = 31 * result + influence4;
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Weight");
        sb.append("{x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", z=").append(z);
        sb.append(", w=").append(w);
        sb.append(", influence1=").append(influence1);
        sb.append(", influence2=").append(influence2);
        sb.append(", influence3=").append(influence3);
        sb.append(", influence4=").append(influence4);
        sb.append('}');
        return sb.toString();
    }
}
