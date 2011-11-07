package net.gtamps.shared.serializer.communication.data;

import net.gtamps.shared.SharedObject;

public class FloatData extends SharedObject implements ISendableData {
    public final float value;

    public FloatData(final float value) {
        this.value = value;
    }
}
