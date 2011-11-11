package net.gtamps.shared.Utils;

public interface IDirty {
    public void onDirty();

    public boolean isDirty();

    public void setDirtyFlag();

    public void clearDirtyFlag();
}
