package net.gtamps.shared.communication;

public class RevisionData implements ISendableData {

    public final long revisionId;

    public RevisionData(long revisionId) {
        this.revisionId = revisionId;
    }

    @Override
    public String toString() {
        return "Revision{" +
                "revisionId=" + revisionId +
                '}';
    }
}
