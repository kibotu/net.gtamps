package net.gtamps.shared.serializer.communication;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.NoSuchElementException;
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
	}; 

	public static transient final String SHARED_PACKAGE_NAME;
	static {
		String fullName = SharedObject.class.getCanonicalName();
		SHARED_PACKAGE_NAME = fullName.substring(0, fullName.lastIndexOf('.')+1);
		selfTest();
	}
	
	 //TODO use 
	private static final Set<Class<?>> checked = new HashSet<Class<?>>();
	
	 //TODO check OTHER_INTRANSIENT_MEMBER_CLASSES for finality
	private static void selfTest() throws IllegalArgumentException {
		
	}
	
	
	//////
	// INSTANCE
	//////
	
	public SharedObject() {
		assert isShareable() : String.format("object class is not in shared package: %s", 
				this.getClass().getCanonicalName());
	}
	
	public final boolean isShareable() {
		boolean ok =  isShareable(this, this.getClass());
		return ok;
	}
	
//	private boolean isShareable(Class<?> runtimeClass) {
	private boolean isShareable(Object o, Class<?> classToCheck) {
		Class<? extends Object> runtimeClass = classToCheck;
// let through allowed classes
		for (Class<?> c : OTHER_INTRANSIENT_MEMBER_CLASSES) {
//			if (c.equals(runtimeClass)) {
			if (c.isAssignableFrom(runtimeClass)) {
				return true;
			}
		}
// not a SharedObject?
		if (!SharedObject.class.isAssignableFrom(runtimeClass)) { 
			System.out.println("not a shared object: " + runtimeClass);
			return false;
		}
// not in shared package?
		String runtimeClassName = runtimeClass.getCanonicalName();
		if (!runtimeClassName.startsWith(SHARED_PACKAGE_NAME)) {
			System.out.println("wrong package: " + runtimeClass);
			return false;
		}
// all non-transient fields shareable?
		for (Field field : runtimeClass.getDeclaredFields() ) {
			if ( Modifier.isTransient(field.getModifiers() )) {
				continue;
			}
			Object value = null;
			Class<?> fieldType = field.getType();
			try {
				value = field.get(o);
			} catch (NullPointerException e) {
				// o is null and passed all tests so far = good enough
				return true;
			} catch (IllegalArgumentException e) {
				throw new NoSuchElementException("shouldn't get here");
			} catch (IllegalAccessException e) {
				value = null;
			}
			if (runtimeClass.isAssignableFrom(fieldType)) {
				continue;
			}
//			if (value.getClass().equals(runtimeClass)) {
//					continue;
//			}
			// avoid endless recursion
//			if (runtimeClass.isAssignableFrom(value.getClass())) {
//				return true;
//			}
			if (!isShareable(value, fieldType)) {
				System.out.println("> bad member (" + field.getName() + ") in: " + runtimeClass);
				return false;
			}
		}
		return true;
	}
}
