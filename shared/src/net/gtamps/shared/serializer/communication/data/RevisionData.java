package net.gtamps.shared.serializer.communication.data;

import net.gtamps.shared.SharedObject;

public class RevisionData extends SharedObject implements ISendableData {

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
