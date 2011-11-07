package net.gtamps.shared.serializer.communication.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.gtamps.shared.SharedObject;

public class SharedMap<K extends Object, V extends Object> extends SharedObject {
	
	public static transient final String[] generics = {"map"}; 
	
	public final Map<K, V> map = new HashMap<K,V>();


}
