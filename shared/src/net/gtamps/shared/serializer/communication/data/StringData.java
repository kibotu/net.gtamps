package net.gtamps.shared.serializer.communication.data;

public class StringData implements ISendableData {
    public final String value;

    public StringData(String value) {
        this.value = value;
    }
}
