package net.gtamps.android.graphics.graph.scene.animation.skeleton;

/**
 * User: Jan Rabe
 * Date: 25/11/12
 * Time: 15:16
 */
public class RawBoneHeader {
    public int size;
    public int magic;
    public int uk;
    public short uk2;
    public short nbSklBones;
    public int num_bones_foranim;
    public int header_size; // 0x40
    public int size_after_array1;
    public int size_after_array2;
    public int size_after_array3;
    public int size_after_array3_; // duped ..
    public int size_after_array4;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("RawBoneHeader");
        sb.append("{size=").append(size);
        sb.append(", magic=").append(magic);
        sb.append(", uk=").append(uk);
        sb.append(", uk2=").append(uk2);
        sb.append(", nbSklBones=").append(nbSklBones);
        sb.append(", num_bones_foranim=").append(num_bones_foranim);
        sb.append(", header_size=").append(header_size);
        sb.append(", size_after_array1=").append(size_after_array1);
        sb.append(", size_after_array2=").append(size_after_array2);
        sb.append(", size_after_array3=").append(size_after_array3);
        sb.append(", size_after_array3_=").append(size_after_array3_);
        sb.append(", size_after_array4=").append(size_after_array4);
        sb.append('}');
        return sb.toString();
    }
}
