package net.gtamps.android.core.input;

import net.gtamps.shared.communication.SendableType;

public abstract interface InputEventListener {
	public void onSendableRetrieve(SendableType sendableType);
}
