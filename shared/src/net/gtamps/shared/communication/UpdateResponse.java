package net.gtamps.shared.communication;

public class UpdateResponse implements ISendableData {

    public long revId;

    public UpdateResponse(long revId) {
        this.revId = revId;
    }
}
