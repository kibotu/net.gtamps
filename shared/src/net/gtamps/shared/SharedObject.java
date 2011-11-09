package net.gtamps.shared;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.jetbrains.annotations.Nullable;


/**
 * Represents an object to be shared between server and client.
 * <p/>
 * On construction, it will assert that it is indeed shareable. To be
 * shareable, the object must meet the following conditions: 
 * 
 * <ul>
 * <li>it is a SharedObject</li>
 * <li>it is declared in the same package as SharedObject or one of its sub-packages,</li>
 * <li>all declared non-transient fields must meet the same conditions, and:
 * 		<ul>
 * 			<li>it's type can also be primitive, Interface, Enum, SharedObject or
 * 				one of the {@link #OTHER_INTRANSIENT_MEMBER_CLASSES};</li>
 * 			<li>the declaration of its type, or the field itself, must be
 * 				<tt>public final</tt>. </li>
 * 		</ul>
 * </li>
 * </ul>
 * If a field does not meet these conditions, you can annotate it as
 * {@link CheckedShareable @CheckedShareable}. This constitutes a promise
 * that the shareability of the field's value will be checked and ensured 
 * by <em>you</em>, and SharedObject will waive all tests.
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
		Boolean.class, Byte.class, Character.class, Short.class,
		Integer.class, Long.class, Float.class, Double.class, String.class,
		Class.class,
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
		Stack<CheckItem> checkStack = new Stack<CheckItem>(
				new CheckItem(this.getClass(), this));
		if (!isShareable(checkStack, true)) {
			StringBuilder msg =  new StringBuilder();
			catMsgFromStack(checkStack, msg);
			msg.insert(0, ", but is not shared: ");
			msg.insert(0, SharedObject.class.getCanonicalName());
			msg.insert(0, "class extends ");
			throw new ClassCastException(msg.toString());
		}
		return true;
	}
	
	private boolean isShareable(Stack<CheckItem> checkStack, boolean considerFinalAppeal) {
		assert checkStack.size() == 1;
		Stack<CheckItem> todo = new Stack<CheckItem>();
		todo.push(checkStack.pop());
		while (!todo.empty()) {
			CheckItem item = todo.peek();
			if (!(checked.contains(item.type) || checkStack.contains(item))) {
				checkStack.push(item);
				if (!(isShareableItem.isTrueFor(item) 
						|| isExplicitlyAllowed.isTrueFor(item) 
						|| isOKPublicFinal.isTrueFor(item))) {
					if (!(considerFinalAppeal && finalAppeal.isTrueFor(item))) {
						return false;
					}
				}
				FilterableCollection<Field> fields = FilterableList.fromArray(item.type.getDeclaredFields());
				fields = fields.removeAll(or.applyTo(isTransient, isSharedField, isEnumSelfReference, isPrimitive));
				for (Field field : fields) {
					todo.push(new CheckItem(field, item.instance));
				}
			}
			if (item == todo.peek()) {
				checked.add(item.type);
				todo.pop();
				if (checkStack.size() != 0 && item == checkStack.peek()) {
					checkStack.pop();
				}
			}
		}
		return true;
	}
	
	/**
	 * builds an error message from the checking stack
	 * @param stack		the checking stack, after {@link #isShareable(Stack)} failed
	 * @param msg		the error message will be inserted at index <tt>0</tt>
	 */
	private void catMsgFromStack(Stack<CheckItem> stack, StringBuilder msg) {
		int indentSize = 2;
		char[] fullIndentChars = new char[indentSize * stack.size()];
		Arrays.fill(fullIndentChars, ' ');
		String fullIndent = new String(fullIndentChars);
		while (!stack.empty()) {
			CheckItem chk = stack.pop();
			msg.insert(0, chk.toString());
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
	
	private static transient final Predicate<CheckItem> finalAppeal = new Predicate<CheckItem>() {
		@Override
		public boolean isTrueFor(CheckItem x) {
			return x.checkedGeneric;
		}
		public String toString() {
			return "p(x) := finalAppeal(CheckItem)";
		}
	};
	

	private static transient final Predicate<CheckItem> isShareableItem = new Predicate<CheckItem>() {
		@Override
		public boolean isTrueFor(CheckItem x) {
			return isShared(x.type);
		}
		public String toString() {
			return "p(x) := isShared(CheckItem)";
		}
	};
	
	private static transient final Predicate<CheckItem> isExplicitlyAllowed = new Predicate<CheckItem>() {
		@Override
		public boolean isTrueFor(CheckItem x) {
			for (Class<?> c : OTHER_INTRANSIENT_MEMBER_CLASSES) {
				if (c.isAssignableFrom(x.type)) {
					return true;
				}
			}
			return false; 
		}
		public String toString() {
			return "p(x) := isExplicitlyAllowed(CheckItem)";
		}
	};
	
	private static transient final Predicate<CheckItem> isOKPublicFinal = new Predicate<CheckItem>() {
		@Override
		public boolean isTrueFor(CheckItem x) {
			if (!isPublicFinal.isTrueFor(x)) {
				return false;
			}
			for (Class<?> c : ALLOWED_IF_FINAL_MEMBER) {
				if (c.isAssignableFrom(x.type)) {
					return true;
				}
			}
			return false; 
		}
		public String toString() {
			return "p(x) := isOKPublicFinal(CheckItem)";
		}
	};
	
	private static transient final Predicate<CheckItem> isPublicFinal = new Predicate<CheckItem>() {
		@Override
		public boolean isTrueFor(CheckItem x) {
			if (Modifier.isPublic(x.classModifiers) && Modifier.isFinal(x.classModifiers)) {
				return true;
			}
			if (Modifier.isPublic(x.fieldModifiers) && Modifier.isFinal(x.fieldModifiers)) {
				return true;
			}
			return false;
		}
		public String toString() {
			return "p(x) := isPublicFinal(CheckItem)";
		}
	};

	private static transient final Predicate<Field> isCheckedGeneric = new Predicate<Field>() {
		@Override
		public boolean isTrueFor(Field x) { 
			return x.getAnnotation(CheckedShareable.class) != null; 
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
	
	private static transient final Predicate<Field> isSharedField = new Predicate<Field>() {
		@Override
		public boolean isTrueFor(Field x) {
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

	/**
	 * @return	<tt>true</tt> if <tt>type</tt> is a SharedObject, Interface
	 * 			or enum in SharedObject's package tree
	 */
	private static boolean isShared(Class<?> type) {
		boolean result =  (SharedObject.class.isAssignableFrom(type)
							|| type.isInterface() || type.isEnum())
			&& type.getCanonicalName().startsWith(SHARED_PACKAGE_NAME);
		return result;
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
		
		private static final long serialVersionUID = -9187173114053497973L;

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
	private class CheckItem {
		public final Class<?> type;
		@Nullable 
		public final boolean checkedGeneric;
		public final Object instance;
		public final int classModifiers;
		public final int fieldModifiers;
		public final String simpleTypeName;
		public final String fieldName;
		
		private Object getFieldValue(Field field, Object owner) {
			assert field == null || owner != null : "if field != null, owner must also be != null";
			Object o = null;
			if (field != null) {
				try {
					o = field.get(owner);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					System.err.println("illegal access on " + field);
//					e.printStackTrace();
				}
			}
			return o;
		}
		
		public CheckItem(Class<?> clazz, Object instance) {
			this(clazz, null, instance, null);
		}
		
		public CheckItem(Field field, Object owner) {
			this(null, field, null, owner);
		}
		
		private CheckItem(Class<?> type, Field field, Object instance, Object fieldOwner) {
			assert !(type == null && field == null) : "either type or field must be != null";
			Object fieldValue = getFieldValue(field, fieldOwner);
			if (field != null) {
				if (fieldValue != null) {
					this.type = clarifyType(fieldValue.getClass());
					this.instance = fieldValue;
				} else {
					this.type = clarifyType(field.getType());
					this.instance = null;
					
				}
				this.checkedGeneric = isCheckedGeneric.isTrueFor(field);
				String tmpName = field.getName();
				this.fieldName = tmpName.substring(tmpName.lastIndexOf('.') + 1);
				this.fieldModifiers = field.getModifiers();
			} else {
				this.type = clarifyType(type);
				this.instance = instance;
				this.checkedGeneric = false;
				this.fieldName = "";
				this.fieldModifiers = 0;
			}
			assert this.type != null;
			this.classModifiers = this.type.getModifiers();
			this.simpleTypeName = this.type.getSimpleName();
		}
		
		private Class<?> clarifyType(Class<?> type) {
			if (type.isArray()) {
				return type.getComponentType();
			}
			return type;
			
		}
		
		@Override
		public String toString() {
			String string;
			if ("".equals(fieldName)) {
				string = String.format("%s %s", Modifier.toString(classModifiers), simpleTypeName);
			} else {
				string = String.format("%s %s %s", Modifier.toString(fieldModifiers), simpleTypeName, fieldName);
			}
			return string;
		}
	}
	
}
