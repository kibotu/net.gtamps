package net.gtamps.android.gtandroid.core.input.event;

import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.ISendableData;

public abstract interface InputEventListener {
    public void onSendableRetrieve(SendableType sendableType, ISendableData sendableData);
}
