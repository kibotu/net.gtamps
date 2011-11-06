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
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 */
public class SharedObject {
	
	//////
	// STATIC
	//////
	
	// add only final classes
	public static transient final Class<?>[] OTHER_INTRANSIENT_MEMBER_CLASSES = {
		boolean.class, byte.class, char.class, short.class, int.class, long.class,
		float.class, double.class, Boolean.class, Byte.class, Character.class, Short.class,
		Integer.class, Long.class, Float.class, Double.class, String.class,
		Class.class,
		boolean[].class, byte[].class, char[].class, short[].class, int[].class, long[].class,
		float[].class, double[].class, Boolean[].class, Byte[].class, Character[].class, Short[].class,
		Integer[].class, Long[].class, Float[].class, Double[].class, String[].class,
		
	}; 

	public static transient final String SHARED_PACKAGE_NAME;
	static {
		String fullName = SharedObject.class.getCanonicalName();
		SHARED_PACKAGE_NAME = fullName.substring(0, fullName.lastIndexOf('.')+1);
		selfTest();
	}
	
	 //TODO use 
//	private static transient final Map<Class<?>, Boolean> checked = new HashMap<Class<?>, Boolean>();
	private static transient final Set<Class<?>> checked = new HashSet<Class<?>>();
	
	 //TODO check OTHER_INTRANSIENT_MEMBER_CLASSES for finality
	private static void selfTest() throws IllegalArgumentException {
		
	}
	
	
	//////
	// INSTANCE
	//////

	
	public SharedObject()   {
		try {
			testShareable(this.getClass());
		} catch (InstantiationException e) {
			throw new ClassCastException(e.getLocalizedMessage());
		}
	}
	
	public final boolean isShareable() {
		try {
			testShareable(this.getClass());
		} catch (InstantiationException e) {
			return false;
		}
		return true;
	}
	
	private boolean testShareable(Class<? extends Object> classToCheck) throws InstantiationException {
		{
			if (checked.contains(classToCheck)) {
				return true;
			}
			if (java.lang.String.class.equals(classToCheck)) {
				return true;				// no harm, let it slide. ^^
			}
		}
		String className = classToCheck.getCanonicalName();
		String simpleClassName = classToCheck.getSimpleName();
		{
			if (!(SharedObject.class.isAssignableFrom(classToCheck)
					&& className.startsWith(SHARED_PACKAGE_NAME))
				&& !isAllowedClass(classToCheck)) {
				throw new InstantiationException(simpleClassName + " is not a shared class");
			}
			for (Field field : classToCheck.getDeclaredFields() ) {
				if ( Modifier.isTransient(field.getModifiers() )) {
					continue;
				}
				Class<?> fieldType = field.getType();
				if (SharedObject.class.isAssignableFrom(fieldType)) {
					continue;	// prevent mad recursion (SharedObjects take care of themselves)
				}
				try {
					testShareable(fieldType);
				} catch (InstantiationException ie) {
					String message = simpleClassName + "." + field.getName() + ":\n" + ie.getMessage();
					throw new InstantiationException(message);
				}
			}
		}
		checked.add(classToCheck);
		return true;
	}
	
	private boolean isAllowedClass(Class<?> checkMe) {
		for (Class<?> c : OTHER_INTRANSIENT_MEMBER_CLASSES) {
			if (c.equals(checkMe)) {
				return true;
			}
		}
		return false;
	}
	
}
