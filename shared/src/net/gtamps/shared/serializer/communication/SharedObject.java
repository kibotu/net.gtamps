package net.gtamps.shared.serializer.communication;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;


/**
 * Represents an object to be shared between server and client.
 * <p/>
 * On construction, it will assert that it is indeed shareable. To be
 * shareable, the object must meet the following conditions: 
 * 
 * <ul>
 * <li>it can be cast as SharedObject,</li>
 * <li>it is declared in the same package as SharedObject or one of its sub-packages,</li>
 * <li>all intransient fields meet the same conditions or are objects of one of the
 * {@link #OTHER_INTRANSIENT_MEMBER_CLASSES}.</li>
 * </ul>
 * 
 * 
 * @author til
 *
 */
public class SharedObject {
	
	//////
	// STATIC
	//////
	
	// add only final classes
	public static transient final Class<?>[] OTHER_INTRANSIENT_MEMBER_CLASSES = new Class<?>[] {
		boolean.class, byte.class, char.class, short.class, int.class, long.class,
		float.class, double.class,	Boolean.class, Byte.class, Character.class, Short.class,
		Integer.class, Long.class,	Float.class, Double.class, String.class,
		Class.class,
		boolean[].class, byte[].class, char[].class, short[].class, int[].class, long[].class,
		float[].class, double[].class,	Boolean[].class, Byte[].class, Character[].class, Short[].class,
		Integer[].class, Long[].class,	Float[].class, Double[].class, String[].class,
		
	}; 

	public static transient final String SHARED_PACKAGE_NAME;
	static {
		String fullName = SharedObject.class.getCanonicalName();
		SHARED_PACKAGE_NAME = fullName.substring(0, fullName.lastIndexOf('.')+1);
		selfTest();
	}
	
	 //TODO use 
	private static transient final Set<Class<?>> checked = new HashSet<Class<?>>();
	
	 //TODO check OTHER_INTRANSIENT_MEMBER_CLASSES for finality
	private static void selfTest() throws IllegalArgumentException {
		
	}
	
	
	//////
	// INSTANCE
	//////

	
	public SharedObject()   {
		assert isShareable() : String.format("object class is not in shared package: %s", 
				this.getClass().getCanonicalName());
		// use static set, don't use assertions
		//TODO find unchecked exception to throw
	}
	
	public final boolean isShareable() {
		boolean ok =  isShareable(this.getClass(), 0);
		return ok;
	}
	
	//TODO build message, throw exception on violation	
	private boolean isShareable(Class<?> classToCheck, int recursionLevel) {
		Class<? extends Object> runtimeClass = classToCheck;
// is it a java.lang.String? -> exception. ^^ 
		if (String.class.equals(runtimeClass)) {
			return true;
		}
		boolean isAllowedClass = isAnAllowedClass(runtimeClass); 
		String runtimeClassName = runtimeClass.getCanonicalName();
// not a SharedObject in shared package or otherwise allowed?
		if (!(SharedObject.class.isAssignableFrom(runtimeClass)
				&& runtimeClassName.startsWith(SHARED_PACKAGE_NAME))
			&& !isAllowedClass) { 
			return false;
		}
// all non-transient fields shareable?
		for (Field field : runtimeClass.getDeclaredFields() ) {
			if ( Modifier.isTransient(field.getModifiers() )) {
				continue;
			}
			Class<?> fieldType = field.getType();
			if (runtimeClass.isAssignableFrom(fieldType)) {
				continue;
			}
			if (!isShareable(fieldType, recursionLevel + 1)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isAnAllowedClass(Class<?> checkMe) {
		for (Class<?> c : OTHER_INTRANSIENT_MEMBER_CLASSES) {
			if (c.equals(checkMe)) {
				return true;
			}
		}
		return false;
	}
	
}
