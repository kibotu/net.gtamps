package net.gtamps.android.core.input;

import java.util.LinkedList;

import net.gtamps.shared.serializer.communication.SendableType;

public class InputEventDispatcher {
	
	LinkedList<InputEventListener> listener = new LinkedList<InputEventListener>();

	public void dispatch(SendableType sendableType, float f) {
		for(InputEventListener iel : listener){
			//TODO parameter
			iel.onSendableRetrieve(sendableType);
		}
	}
	
	public void addInputEventListener(InputEventListener iel){
		listener.add(iel);
	}
	public void removeInputEventListener(InputEventListener iel){
		listener.remove(iel);
	}

}
