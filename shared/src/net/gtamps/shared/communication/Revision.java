package net.gtamps.shared.communication;

public class Revision implements ISendableData {

    public final long revisionId;

    public Revision(long revisionId) {
        this.revisionId = revisionId;
    }

    @Override
    public String toString() {
        return "Revision{" +
                "revisionId=" + revisionId +
                '}';
    }
}
