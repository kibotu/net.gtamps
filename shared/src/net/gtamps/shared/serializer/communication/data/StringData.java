package net.gtamps.shared.serializer.communication.data;

import net.gtamps.shared.SharedObject;

public class StringData extends SharedObject implements ISendableData {

    public final String value;

    public StringData(String value) {
        this.value = value;
    }
}
