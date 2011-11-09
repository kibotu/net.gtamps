package net.gtamps.shared;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.lang.model.type.TypeVariable;

import org.jetbrains.annotations.Nullable;


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

	/** the name of the package SharedObject is part of */
	public static transient final String SHARED_PACKAGE_NAME;

	/**
	 * classes that do not extend SharedObject but are known to be shared.
	 * <p/>
	 * They must be <tt>final</tt>, as well as the declared types of all 
	 * declared fields not marked <tt>transient</tt>.
	 */
	public static transient final Class<?>[] OTHER_INTRANSIENT_MEMBER_CLASSES = {
	// Arrays and primitives should be taken care of
//		boolean.class, byte.class, char.class, short.class, int.class, long.class,
//		float.class, double.class, 
		Boolean.class, Byte.class, Character.class, Short.class,
		Integer.class, Long.class, Float.class, Double.class, String.class,
		Class.class,
//		boolean[].class, byte[].class, char[].class, short[].class, int[].class, 
//		long[].class, float[].class, double[].class, 
//		Boolean[].class, Byte[].class, Character[].class, Short[].class,
//		Integer[].class, Long[].class, Float[].class, Double[].class, String[].class,
//		SharedObject[].class,
	}; 
	
	/** types allowed for public final members */
	public static transient final Class<?>[] ALLOWED_IF_FINAL_MEMBER = {
		java.util.List.class, java.util.Map.class, java.util.ArrayList.class,
		java.util.HashMap.class
	};

	/** class that were checked and found to be okay */
	private static transient final Set<Class<?>> checked;

	static {
		String fullName = SharedObject.class.getCanonicalName();
		SHARED_PACKAGE_NAME = fullName.substring(0, fullName.lastIndexOf('.')+1);
		checked = new HashSet<Class<?>>();
		checked.add(java.lang.String.class);
		selfTest();
	}
	
	public static boolean isShareable(Object o) {
		return isShared(o.getClass());
	}
	
	// TODO reconsider
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
				throw new IllegalArgumentException("class in OTHER_"
						+ "INTRANSIENT_MEMBER_CLASSES must be final: " 
						+ c.getSimpleName());
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
	 * If assertions are enabled, this constructor makes sure that every 
	 * class extending SharedObject follows the sharing rules. 
	 * See rules in {@link SharedObject}.
	 * 
	 * @throws ClassCastException	if sharing rules are broken
	 * 
	 */
	public SharedObject() throws ClassCastException {
		assert isShareable();
	}
	
	/**
	 * You can use this method for runtime checks.
	 * 
	 * @return
	 * @throws ClassCastException
	 */
	public final boolean isShareable() throws ClassCastException {
		Stack<CheckedItem> checkStack = new Stack<CheckedItem>(
				new CheckedItem(this.getClass(), this));
		if (!isShareable(checkStack)) {
			//TODO stack is not correct at this point
			StringBuilder msg =  new StringBuilder();
			catMsgFromStack(checkStack, msg);
			msg.insert(0, ", but is not shared: ");
			msg.insert(0, SharedObject.class.getCanonicalName());
			msg.insert(0, "class extends ");
			throw new ClassCastException(msg.toString());
		}
		return true;
	}
	
	// TODO swap toCheck (make local) and checking (make argument)
	private boolean isShareable(Stack<CheckedItem> toCheck) {
		Stack<Class<?>> checking = new Stack<Class<?>>();
		while (!toCheck.empty()) {
			CheckedItem item = toCheck.pop();
			Class<?> type = item.type;
			if (!(checked.contains(type) || checking.contains(type))) {
				checking.push(type);
				// TODO swap
				if (!(isAllowed(item) || isShared(type))) {
					toCheck.push(item);
					return false;
				}
				FilterableCollection<Field> fields = FilterableList.fromArray(type.getDeclaredFields());
				fields = fields.removeAll(or.applyTo(isTransient, isShared, isEnumSelfReference, isPrimitive));
				for (Field field : fields) {
					type = field.getType();
					//TODO do this where? getValue()...?
					//type = type.isArray() ? type.getComponentType() : type;
					toCheck.push(new CheckedItem(field, null));
					if (!isPublicFinal(type.getModifiers())) {
						if (!isPublicFinal(field.getModifiers())) {
							return false;
						} else {
							CheckedItem chk = toCheck.pop();
							Object value = getValue(field, item.instance);
							// getValueType()
							// if ValueType != null: continue
							if (value == null) {
								continue;
							}
							toCheck.push(chk.changeType(value.getClass(), value));
						}
					}
				}
			}
		}
		checked.addAll(checking); //use filter
		return true;
	}
	
//	private boolean fieldsAreShareable(Stack<CheckedItem> toCheck) {
//		return false;
//	}
	
	/**
	 * builds an error message from the checking stack
	 * @param stack		the checking stack, after {@link #isShareable(Stack)} failed
	 * @param msg		the error message will be inserted at index <tt>0</tt>
	 */
	private void catMsgFromStack(Stack<CheckedItem> stack, StringBuilder msg) {
		int indentSize = 2;
		char[] fullIndentChars = new char[indentSize * stack.size()];
		Arrays.fill(fullIndentChars, ' ');
		String fullIndent = new String(fullIndentChars);
		while (!stack.empty()) {
			CheckedItem chk = stack.pop();
			msg.insert(0, chk.msg);
			msg.insert(0, fullIndent.substring(0, indentSize * stack.size()));
			msg.insert(0, "\n");
		}
	}
	
	
	/**
	 * a quick stack implementation, courtesy of www.java2s.com
	 * @see <a href="http://www.java2s.com/Code/Java/Collections-Data-Structure/extendsArrayListTtocreateStack.htm">www.java2s.com</a>
	 * @param <T>
	 */
	@SuppressWarnings("serial")
	private class Stack<T> extends ArrayList<T> {
		public Stack() {
			this(20);
		}
		
		public Stack(int initialCap) {
			super(initialCap);
		}
		
		public Stack(T firstElement) {
			super(20);
			this.push(firstElement);
		}
		
	    public void push(T value) {
	        add(value);
	    }

	    public T pop() {
	        return remove(size() - 1);
	    }

	    public boolean empty() {
	        return size() == 0;
	    }

		public T peek() {
	        return get(size() - 1);
	    }
	    
	    @Override
	    public String toString() {
	    	StringBuilder str = new StringBuilder();
	    	for (int i = size() - 1; i >= 0; i--) {
	    		str.append(get(i).toString() + " ");
	    	}
	    	str.insert(0, "stk: ");
	    	return str.toString();
	    }
		
	}
	
	
	
	////////
	//
	// STATIC PRIVATE
	//
	////////

	private static transient final Predicate<Field> isCheckedGeneric = new Predicate<Field>() {
		@Override
		public boolean isTrueFor(Field x) { 
			return x.getAnnotation(CheckedGeneric.class) != null; 
		}
		public String toString() {
			return "p(x) := isCheckedGeneric(Field)";
		}
	}; 

	private static transient final Predicate<Field> isTransient = new Predicate<Field>() {
		@Override
		public boolean isTrueFor(Field x) { 
			return Modifier.isTransient(x.getModifiers());
		}
		public String toString() {
			return "p(x) := isTransient(Field)";
		}
	};
	
	private static transient final Predicate<Field> isShared = new Predicate<Field>() {
		@Override
		public boolean isTrueFor(Field x) {
//			return !SharedObject.class.isAssignableFrom(x.getType());
			return isShared(x.getType());
		}
		public String toString() {
			return "p(x) := isShared(Field)";
		}
	};
	
	private static transient final Predicate<Field> isEnumSelfReference = new Predicate<Field>() {
		@Override
		public boolean isTrueFor(Field x) {
			Class<?> type = x.getType();
			return (type.isEnum() && type.equals(x.getDeclaringClass()));
		}
		public String toString() {
			return "p(x) := isEnumSelfReference(Field)";
		}
	};
	
	private static transient final Predicate<Field> isPrimitive = new Predicate<Field>() {
		@Override
		public boolean isTrueFor(Field x) {
			Class<?> type = x.getType();
			return type.isPrimitive();
		}
		public String toString() {
			return "p(x) := isPrimitive(Field)";
		}
	};
	
	@SuppressWarnings("rawtypes")
	private static transient final MightyMorphinPredicate not = new MightyMorphinPredicate() {
		@Override
		public Predicate applyTo(final Predicate... subjects) {
			assert subjects.length == 1 : "expects exactly one argument";
			return new Predicate() {
				public boolean isTrueFor(Object x) {
					return !subjects[0].isTrueFor(x);
				}
			};
		}
	};
	
	@SuppressWarnings("rawtypes")
	private static transient final MightyMorphinPredicate or = new MightyMorphinPredicate() {
		@Override
		public Predicate applyTo(final Predicate... subjects) {
			if (subjects.length < 1) {
				throw new IllegalArgumentException("must give at least one argument");
			}
			return new Predicate() {
				public boolean isTrueFor(Object x) {
					for (Predicate p : subjects) {
						if (p.isTrueFor(x)) {
							return true;
						}
					}
					return false;
				}
			};
		}
	};

	private static boolean isAllowed(CheckedItem chk) {
		for (Class<?> c : OTHER_INTRANSIENT_MEMBER_CLASSES) {
			if (c.isAssignableFrom(chk.type)) {
				return true;
			}
		}
		if (isPublicFinal(chk.modifiers)) {
			for (Class<?> c : ALLOWED_IF_FINAL_MEMBER) {
				if (c.isAssignableFrom(chk.type)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean isShared(Class<?> type) {
		boolean result =  (SharedObject.class.isAssignableFrom(type)
							|| type.isInterface() || type.isEnum())
			&& type.getCanonicalName().startsWith(SHARED_PACKAGE_NAME);
		return result;
	}
	
	private static boolean isPublicFinal(int modifiers) {
		return Modifier.isPublic(modifiers) && Modifier.isFinal(modifiers);
	}
	
	private static Object getValue(Field field, Object instance) {
		try {
			//return field.get(this);
			return field.get(instance);
		} catch (NullPointerException e) {
			// nyahaha
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// http://stackoverflow.com/questions/122105/java-what-is-the-best-way-to-filter-a-collection
	private interface Predicate<T> {
		boolean isTrueFor(T x);
	}
	
	
	private interface MightyMorphinPredicate<T> {
		Predicate<T> applyTo(Predicate<T>... subjects);
	}
	
	private interface FilterableCollection<T> extends Collection<T> {
		public FilterableCollection<T> retainAll(Predicate<T> p);
		public FilterableCollection<T> removeAll(Predicate<T> p);
		public boolean trueForAll(Predicate<T> p);
		public boolean trueForOne(Predicate<T> p);
	}
	
	private static class FilterableList<T> extends ArrayList<T> implements FilterableCollection<T> {
		
		public static <A> FilterableList<A> fromArray(A[] a) {
			FilterableList<A> list = new FilterableList<A>();
			list.ensureCapacity(a.length);
			list.addAll(Arrays.asList(a));
			return list;
		}

		@Override
		public FilterableList<T> retainAll(Predicate<T> p) {
			removeAll(not.applyTo(p));
			return this;
		}

		@Override
		public FilterableList<T> removeAll(Predicate<T> p) {
			int i = 0;
			while (i < size()) {
				if (p.isTrueFor(get(i))) {
					remove(i);
				} else {
					i++;
				}
			}
			return this;
		}

		@Override
		public boolean trueForAll(Predicate<T> p) {
			return !trueForOne(not.applyTo(p));
		}

		@Override
		public boolean trueForOne(Predicate<T> p) {
			int size = size();
			for (int i = 0; i < size; i++) {
				if (p.isTrueFor(get(i))) {
					return true;
				}
			}
			return false;
		}
		
	}
	
	/**
	 * groups relevant data for a shareability check
	 * 
	 * @author til, tom, jan
	 */
	private class CheckedItem {
		public final Class<?> type;
		@Nullable 
		public final Object instance;
		public final int modifiers;
		public final String msg;
		
		public CheckedItem(Class<?> clazz, Object instance) {
			this(clazz, instance, clazz.getModifiers(), clazz.getSimpleName());
		}
		
		public CheckedItem(Field field, Object value) {
			this(field.getType(), value, field.getType().getModifiers(), 
					field.getType().getSimpleName() + ' ' + field.getName() + ':');
		}
		
		public CheckedItem(Class<?> type, Object instance, int modifier, String msg) {
			assert type != null;
			assert msg != null;
			this.type = type;
			this.instance = instance;
			this.modifiers = modifier;
			this.msg = msg;
		}
		
		public CheckedItem changeType(Class<?> newType, Object newInstance) {
			assert newType != null;
			return new CheckedItem(newType, newInstance, this.modifiers, this.msg);
		}
		
		@Override
		public String toString() {
			//return String.format("chk[%s (%s), %d] -> %s", type.getSimpleName(), instance.toString(), modifiers, msg); 
			return String.format("[%s]", type.getSimpleName()); 
		}
		
	}
	
}
