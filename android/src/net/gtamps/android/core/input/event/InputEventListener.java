package net.gtamps.android.core.input.event;

import net.gtamps.shared.serializer.communication.SendableType;

public abstract interface InputEventListener {
	public void onSendableRetrieve(SendableType sendableType);
}
