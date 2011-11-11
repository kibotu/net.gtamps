package net.gtamps.shared.serializer.communication.data;

import net.gtamps.shared.SharedObject;

public class StringData extends SharedObject implements ISendableData {

    private static final long serialVersionUID = 7553010143644035579L;

    public final String value;

    public StringData(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "StringData{" +
                "value='" + value + '\'' +
                '}';
    }
}
