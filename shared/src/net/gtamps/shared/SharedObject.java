package net.gtamps.shared;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
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
public class SharedObject implements Serializable {
	
	//////
	//
	// STATIC
	//
	//////
	
	private static final long serialVersionUID = -6150947536448498287L;

	/**
	 * classes that do not extend SharedObject but are known to be shared.
	 * <p/>
	 * They must be <tt>final</tt>, as well as the declared types of all 
	 * declared fields not marked <tt>transient</tt>.
	 *  
	 */
	public static transient final Class<?>[] OTHER_INTRANSIENT_MEMBER_CLASSES = {
		boolean.class, byte.class, char.class, short.class, int.class, long.class,
		float.class, double.class, Boolean.class, Byte.class, Character.class, Short.class,
		Integer.class, Long.class, Float.class, Double.class, String.class,
		Class.class,
		boolean[].class, byte[].class, char[].class, short[].class, int[].class, long[].class,
		float[].class, double[].class, Boolean[].class, Byte[].class, Character[].class, Short[].class,
		Integer[].class, Long[].class, Float[].class, Double[].class, String[].class,
		SharedObject[].class,
	}; 
	
	public static transient final Class<?>[] ALLOWED_IF_FINAL_MEMBER = {
		java.util.List.class, java.util.Map.class, java.util.ArrayList.class,
		java.util.HashMap.class
	};

	/**
	 * the name of the package SharedObject is part of 
	 */
	public static transient final String SHARED_PACKAGE_NAME;
	static {
		String fullName = SharedObject.class.getCanonicalName();
		SHARED_PACKAGE_NAME = fullName.substring(0, fullName.lastIndexOf('.')+1);
		checked = new HashSet<Class<?>>();
		selfTest();
	}
	
	/**
	 * if a class was already checked and found to be okay, it can be added here
	 */
	private static transient final Set<Class<?>> checked;
	
	/**
	 * checks all classes in {@link OTHER_INTRANSIENT_MEMBER_CLASSES}
	 * if they're <tt>final</tt>. this may pre-populate
	 * {@link #checked}.
	 * </p>
	 * if a non-final class is encountered, an IllegalArgumentException 
	 * is thrown.
	 * <p/>
	 * Note: this should reasonably also check every class' fields, but
	 * Java wrapper classes contain Comparators... 
	 *  
	 * @throws IllegalArgumentException		if a non-final class is encountered
	 */
	private static void selfTest() throws IllegalArgumentException {
		Queue<Class<?>> q = new LinkedList<Class<?>>();
		q.addAll(Arrays.asList(OTHER_INTRANSIENT_MEMBER_CLASSES));
		while (!q.isEmpty()) {
			Class<?> c = q.poll();
			if (checked.contains(c)) {
				continue;
			}
			if (!Modifier.isFinal(c.getModifiers())) {
				throw new IllegalArgumentException("class in OTHER_INTRANSIENT_MEMBER_CLASSES must be final: " + c.getSimpleName());
			}
			checked.add(c);
		}
	}
	
	
	//////
	//
	// INSTANCE
	//
	//////
	
	/**
	 * This constructor makes sure that every class extending SharedObject
	 * follows the sharing rules. See rules in {@link SharedObject}.
	 * 
	 * @throws ClassCastException	if sharing rules are broken
	 * 
	 */
	public SharedObject() throws ClassCastException {
		assert isShareable();
	}
	
	public final boolean isShareable() throws ClassCastException {
		StringBuilder failMessage = new StringBuilder();
		if (!isShareable(this.getClass(), failMessage)) {
			String msg = "class extends SharedObject but is not shared:\n" + failMessage.toString();
			throw new ClassCastException(msg);
		}
		return true;
	}
	
	private boolean isShareable(Class<?> classToCheck, StringBuilder failMessage) {
		if (java.lang.String.class.equals(classToCheck)) {
			return true;				// no harm, let it slide. ^^
		}
		if (checked.contains(classToCheck)) {
			return true;
		}
		String simpleClassName = classToCheck.getSimpleName();
		if (!satisfiesClassRequirements(classToCheck)) {
			failMessage.append(simpleClassName + " is not shared");
			return false;
		}
		for (Field field : classToCheck.getDeclaredFields() ) {
			String fieldName = field.getName();
			int fieldModifiers = field.getModifiers();
			if ( Modifier.isTransient(fieldModifiers) ) {
				continue;
			}
			Class<?> fieldType = field.getType();
			if (SharedObject.class.isAssignableFrom(fieldType)) {
				continue;	// prevent mad recursion (SharedObjects take care of themselves)
			}
			if (isAllowedForFinalMembers(fieldType)) {
				if ( !(Modifier.isFinal(fieldModifiers) && Modifier.isPublic(fieldModifiers)) ) {
					String msg = String.format("%s.%s: type (%s) is allowed, but field must be public final",
							simpleClassName, fieldName, fieldType.getSimpleName());
					failMessage.append(msg);
					return false;
				}
				Object value = null;
				try {
					value = field.get(this); 
				} catch (IllegalAccessException e) {
					assert false : String.format("SharedObject.isShared(class): Illegal Access on %s %s.%s",
							fieldType.getSimpleName(), simpleClassName, fieldName);
					continue;
				}
				if (value == null) {
					continue;
				}
				fieldType = value.getClass(); 
			}
			if (!isShareable(fieldType, failMessage)) {
				String msg = simpleClassName + "." + fieldName + ":\n";
				failMessage.insert(0, msg);
				return false;
			}
		}
		checked.add(classToCheck);
		return true;
	}
	
	private boolean satisfiesClassRequirements(Class<?> c) {
		if (!(SharedObject.class.isAssignableFrom(c)	
				&& c.getCanonicalName().startsWith(SHARED_PACKAGE_NAME))
			&& !isAllowedClass(c)) {
			return false;
		}
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
	
	private boolean isAllowedForFinalMembers(Class<?> checkMe) {
		for (Class<?> c : ALLOWED_IF_FINAL_MEMBER) {
			if (c.equals(checkMe)) {
				return true;
			}
		}
		return false;
	}
	
}
