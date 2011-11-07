package net.gtamps.android.core.input;

import android.util.Log;
import net.gtamps.shared.serializer.communication.SendableType;

public class DebugInputEventListener implements InputEventListener{

	private String TAG = this.getClass().getSimpleName();

	@Override
	public void onSendableRetrieve(SendableType sendableType) {
		Log.d(TAG , "received: "+sendableType.toString());	
	}

}
