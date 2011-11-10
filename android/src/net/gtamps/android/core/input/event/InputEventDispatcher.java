package net.gtamps.android.core.input.event;

import java.util.LinkedList;

import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.ISendableData;

public class InputEventDispatcher {
	
	LinkedList<InputEventListener> listener = new LinkedList<InputEventListener>();

	public void dispatch(SendableType sendableType, ISendableData data) {
		for(InputEventListener iel : listener){
			//TODO parameter
			iel.onSendableRetrieve(sendableType, data);
		}
	}
	
	public void addInputEventListener(InputEventListener iel){
		listener.add(iel);
	}
	public void removeInputEventListener(InputEventListener iel){
		listener.remove(iel);
	}

}
