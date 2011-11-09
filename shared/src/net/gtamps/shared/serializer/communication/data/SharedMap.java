package net.gtamps.shared.serializer.communication.data;

import java.util.HashMap;
import java.util.Map;
import net.gtamps.shared.CheckedShareable;
import net.gtamps.shared.SharedObject;

public class SharedMap<K extends Object, V extends Object> extends SharedObject {
	
	public SharedMap() {
		
	}
	
	public final Map<K, V> map = new HashMap<K,V>();


}
