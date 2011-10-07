package net.gtamps.shared;

public interface IDirty {
    public void onDirty();
    public boolean isDirty();
	public void setDirtyFlag();
	public void clearDirtyFlag();
}
