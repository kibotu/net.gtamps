package net.gtamps.shared.serializer.communication.data;

import java.util.HashMap;
import java.util.Map;
import net.gtamps.shared.CheckedGeneric;
import net.gtamps.shared.SharedObject;

public class SharedMap<K extends Object, V extends Object> extends SharedObject {
	
	@CheckedGeneric
	public final Map<K, V> map = new HashMap<K,V>();


}
