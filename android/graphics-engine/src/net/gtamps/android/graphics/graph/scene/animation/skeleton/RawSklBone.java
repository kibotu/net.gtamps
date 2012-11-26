package net.gtamps.android.graphics.graph.scene.animation.skeleton;

/**
 * User: Jan Rabe
 * Date: 25/11/12
 * Time: 15:17
 */
public class RawSklBone {

    public static final int BYTELENGTH = 0x44;

    public short uk;
    public short id;
    public short parent_id;
    public short uk2;
    public int namehash;
    public float unused;
    public float tx;
    public float ty;
    public float tz;
    public float unused1;
    public float unused2;
    public float unused3;
    public float q1;
    public float q2;
    public float q3;
    public float q4;
    public float ctx;
    public float cty;
    public float ctz;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("RawSklBone{\n");
        sb.append("uk=").append(uk);
        sb.append("\nid=").append(id);
        sb.append("\nparent_id=").append(parent_id);
        sb.append("\nuk2=").append(uk2);
        sb.append("\nnamehash=").append(namehash);
        sb.append("\nunused=").append(unused);
        sb.append("\ntx=").append(tx);
        sb.append("\nty=").append(ty);
        sb.append("\ntz=").append(tz);
        sb.append("\nunused1=").append(unused1);
        sb.append("\nunused2=").append(unused2);
        sb.append("\nunused3=").append(unused3);
        sb.append("\nq1=").append(q1);
        sb.append("\nq2=").append(q2);
        sb.append("\nq3=").append(q3);
        sb.append("\nq4=").append(q4);
        sb.append("\nctx=").append(ctx);
        sb.append("\ncty=").append(cty);
        sb.append("\nctz=").append(ctz);
        sb.append('}');
        return sb.toString();
    }
}
