package net.gtamps.shared.serializer.communication.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.gtamps.shared.SharedObject;

/**
 * A wrapper for a list that can be shared.
 * 
 * @author til, tom, jan
 *
 * @param <T>	extends SharedObject
 */
public class SharedList<T extends SharedObject> extends SharedObject implements ISendableData {

	public final List<T> list = new ArrayList<T>(); 
	
}
