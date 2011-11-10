package net.gtamps.android.core.input;

import android.util.Log;
import net.gtamps.android.core.input.event.InputEventListener;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.ISendableData;

public class DebugInputEventListener implements InputEventListener{

	private String TAG = this.getClass().getSimpleName();

	@Override
	public void onSendableRetrieve(SendableType sendableType, ISendableData sendableData) {
		Log.d(TAG , "received: "+sendableType.toString()+" data: "+sendableData.toString());	
	}

}
