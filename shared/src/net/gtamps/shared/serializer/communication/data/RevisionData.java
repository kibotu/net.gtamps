package net.gtamps.shared.serializer.communication.data;

import net.gtamps.shared.SharedObject;

public class RevisionData extends SharedObject implements ISendableData {

    private static final long serialVersionUID = -7603933998492251468L;

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
