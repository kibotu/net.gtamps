package net.gtamps.shared;

<<<<<<< HEAD
=======
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

>>>>>>> origin/master
import net.gtamps.shared.Utils.Stack;
import net.gtamps.shared.Utils.predicate.FilteringArrayList;
import net.gtamps.shared.Utils.predicate.FilteringCollection;
import net.gtamps.shared.Utils.predicate.Predicate;
import net.gtamps.shared.Utils.predicate.PredicateModifier;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * Represents an object to be shared between server and client.
 * <p/>
 * On construction, it will assert that it is indeed shareable. To be
 * shareable, the object and, by recursion, all its declared fields
 * must meet the following conditions:
 * <p/>
 * <ul>
 * <li>it is in a package with or below SharedObject,</li>
 * <li>it is a SharedObject, a String, a primitive, primitive wrapper, or a Class,</li>
 * <li>Enums and Interfaces are okay, too, BUT:</li>
 * <li>if the type itself is not <tt>public</tt> and <tt>final</tt>,
 * the field declaring it as its type must be <tt>public final</tt> itself.</li>
 * </ul>
 * <p/>
 * If a field does not meet these conditions, you can annotate it as
 * {@link CheckedShareable @CheckedShareable}. This constitutes a promise
 * that the shareability of the field's value will be checked and ensured
 * by <em>you</em>; SharedObject will waive all tests, and if something
 * goes wrong, the fault is yours.
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


    /**
     * the name of the package SharedObject is part of
     */
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

    /**
     * class that were checked and found to be okay
     */
    private static transient final Set<Class<?>> checked;

    static {
        final String fullName = SharedObject.class.getCanonicalName();
        SHARED_PACKAGE_NAME = fullName.substring(0, fullName.lastIndexOf('.') + 1);
        checked = new HashSet<Class<?>>();
        checked.add(java.lang.String.class);
        selfTest();
    }


    /**
     * checks if all {@link OTHER_INTRANSIENT_MEMBER_CLASSES} are final
     *
     * @throws IllegalArgumentException if a non-final class is encountered
     */
    private static void selfTest() throws IllegalArgumentException {
        System.err.println("SharedObject.selfTest() is not implemented and does nothing useful.");
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
     * @throws ClassCastException if sharing rules are broken
     */
    public SharedObject() throws ClassCastException {
        assert isShareable();
    }

    /**
     * You can use this method for runtime checks. It uses the same
     * checking logic as the constructor.
     *
     * @return
     * @throws ClassCastException
     */
    public final boolean isShareable() throws ClassCastException {
        final Stack<CheckItem> checkStack = new Stack<CheckItem>(
                new CheckItem(this.getClass(), this));
        if (!isShareable(checkStack, true)) {
            final StringBuilder msg = new StringBuilder();
            catMsgFromStack(checkStack, msg);
            msg.insert(0, ", but is not shared: ");
            msg.insert(0, SharedObject.class.getCanonicalName());
            msg.insert(0, "class extends ");
            throw new ClassCastException(msg.toString());
        }
        return true;
    }


    /**
     * @param checking            the {@link CheckItem} for the root testee is expected to be the sole occupant of the stack
     * @param considerFinalAppeal let items pass the test for whom {@link #finalAppeal} returns true
     */
    private boolean isShareable(final Stack<CheckItem> checking, final boolean considerFinalAppeal) {
        assert checking.size() == 1;
        final Stack<CheckItem> todo = new Stack<CheckItem>();
        todo.push(checking.pop());
        while (!todo.empty()) {
            final CheckItem item = todo.peek();
            if (!(checked.contains(item.type) || checking.contains(item))) {
                checking.push(item);
                if (!itemIsShareable(item, true)) {
                    return false;
                }
                final Collection<Field> fields = getCheckableFields(item);
                for (final Field field : fields) {
                    todo.push(new CheckItem(field, item.instance));
                }
            }
            if (todo.topIs(item)) {            // no additional checks pushed: we're done here
                todo.pop();
                if (checking.topIs(item)) {    // did we push item on the checkStack?
                    checking.pop();
                }
                if (!item.requestsFinalAppeal) {    // don't add suspicious items to the clean list
                    checked.add(item.type);
                }
            }
        }
        return true;
    }

    /**
     * wraps all shareable criteria in one check
     *
     * @param considerFinalAppeal let items pass the test for whom {@link #finalAppeal} returns true
     */
    private boolean itemIsShareable(final CheckItem item, final boolean considerFinalAppeal) {
        if ((isPrimitive.isTrueFor(item)
                || isShareableItem.isTrueFor(item)
                || isExplicitlyAllowed.isTrueFor(item))
                && isOKPublicFinal.isTrueFor(item)) {
            return true;
        }
        if (considerFinalAppeal && finalAppeal.isTrueFor(item)) {
            return true;
        }
        return false;
    }


    private Collection<Field> getCheckableFields(final CheckItem item) {
        final FilteringCollection<Field> fields;
        fields = FilteringArrayList.fromArray(item.type.getDeclaredFields());
        fields.removeAll(isTransientField)
                .removeAll(isPrimitiveField)
                .removeAll(isSharedField)
                .removeAll(isEnumSelfReference);
        return fields;
    }

    /**
     * builds an error message from the checking stack
     *
     * @param stack the checking stack, after {@link #isShareable(Stack)} failed
     * @param msg   the error message will be inserted at index <tt>0</tt>
     */
    private void catMsgFromStack(final Stack<CheckItem> stack, final StringBuilder msg) {
        final int indentSize = 2;
        final char[] fullIndentChars = new char[indentSize * stack.size()];
        Arrays.fill(fullIndentChars, ' ');
        final String fullIndent = new String(fullIndentChars);
        while (!stack.empty()) {
            final CheckItem chk = stack.pop();
            msg.insert(0, chk.toString());
            msg.insert(0, fullIndent.substring(0, indentSize * stack.size()));
            msg.insert(0, "\n");
        }
    }

    ////////
    //
    // UTILITY STUFF
    //
    ////////

    /**
     * one last method to pass the check: @see {@link CheckItem#requestsFinalAppeal}
     */
    private static transient final Predicate<CheckItem> finalAppeal = new Predicate<CheckItem>() {
        @Override
        public boolean isTrueFor(final CheckItem x) {
            return x.requestsFinalAppeal;
        }

        @Override
        public String toString() {
            return "p(x) := finalAppeal(CheckItem)";
        }
    };


    /**
     * @see #isShared(Class)
     */
    private static transient final Predicate<CheckItem> isShareableItem = new Predicate<CheckItem>() {
        @Override
        public boolean isTrueFor(final CheckItem x) {
            return isShared(x.type);
        }

        @Override
        public String toString() {
            return "p(x) := isShared(CheckItem)";
        }
    };

    /**
     * see if CheckItem is in {@link #OTHER_INTRANSIENT_MEMBER_CLASSES}
     */
    private static transient final Predicate<CheckItem> isExplicitlyAllowed = new Predicate<CheckItem>() {
        @Override
        public boolean isTrueFor(final CheckItem x) {
            for (final Class<?> c : OTHER_INTRANSIENT_MEMBER_CLASSES) {
                if (c.isAssignableFrom(x.type)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "p(x) := isExplicitlyAllowed(CheckItem)";
        }
    };

    /**
     * if CheckItem derives from a field, see if its type or itself have 'public final' modifiers
     */
    private static transient final Predicate<CheckItem> isOKPublicFinal = new Predicate<CheckItem>() {
        @Override
        public boolean isTrueFor(final CheckItem x) {
            return (!x.fromField || isPublicFinal.isTrueFor(x));
        }

        @Override
        public String toString() {
            return "p(x) := isOKPublicFinal(CheckItem)";
        }
    };

    /**
     * is the type (class) represented by CheckItem public final?
     */
    private static transient final Predicate<CheckItem> isPublicFinalClass = new Predicate<CheckItem>() {
        @Override
        public boolean isTrueFor(final CheckItem x) {
            if (Modifier.isPublic(x.classModifiers) && Modifier.isFinal(x.classModifiers)) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "p(x) := isPublicFinal(CheckItem)";
        }
    };

    /**
     * if CheckItem derives from a field, is the field declared as 'public final'?
     */
    private static transient final Predicate<CheckItem> isPublicFinalField = new Predicate<CheckItem>() {
        @Override
        public boolean isTrueFor(final CheckItem x) {
            if (x.fromField && Modifier.isPublic(x.fieldModifiers) && Modifier.isFinal(x.fieldModifiers)) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "p(x) := isPublicFinal(CheckItem)";
        }
    };

    /**
     * {@link #isPublicFinalClass} || {@link #isPublicFinalField}
     */
    @SuppressWarnings("unchecked") // as of java 7, this warning will move to method declaration
    private static transient final Predicate<CheckItem> isPublicFinal =
            PredicateModifier.or(isPublicFinalClass, isPublicFinalField);

    /**
     * {@link Class#isPrimitive() CheckItem.type.isPrimitive}?
     */
    private static transient final Predicate<CheckItem> isPrimitive = new Predicate<CheckItem>() {
        @Override
        public boolean isTrueFor(final CheckItem x) {
            return x.type.isPrimitive();
        }

        @Override
        public String toString() {
            return "p(x) := isPrimitive(CheckItem)";
        }
    };

    /**
     * is a field annotated with {@linkplain CheckedShareable}?
     */
    private static transient final Predicate<Field> isCheckedShareable = new Predicate<Field>() {
        @Override
        public boolean isTrueFor(final Field x) {
            return x.getAnnotation(CheckedShareable.class) != null;
        }

        @Override
        public String toString() {
            return "p(x) := isCheckedGeneric(Field)";
        }
    };

    private static transient final Predicate<Field> isTransientField = new Predicate<Field>() {
        @Override
        public boolean isTrueFor(final Field x) {
            return Modifier.isTransient(x.getModifiers());
        }

        @Override
        public String toString() {
            return "p(x) := isTransient(Field)";
        }
    };

    private static transient final Predicate<Field> isPrimitiveField = new Predicate<Field>() {
        @Override
        public boolean isTrueFor(final Field x) {
            return x.getType().isPrimitive();
        }

        @Override
        public String toString() {
            return "p(x) := isPrimitive(Field)";
        }
    };

    /**
     * {@link #isShared(Class) isShared(Field.getType())}?
     */
    private static transient final Predicate<Field> isSharedField = new Predicate<Field>() {
        @Override
        public boolean isTrueFor(final Field x) {
            return isShared(x.getType());
        }

        @Override
        public String toString() {
            return "p(x) := isShared(Field)";
        }
    };

    /**
     * is the field a member of an Enum and referencing this enum?
     */
    private static transient final Predicate<Field> isEnumSelfReference = new Predicate<Field>() {
        @Override
        public boolean isTrueFor(final Field x) {
            final Class<?> type = x.getType();
            return (type.isEnum() && type.equals(x.getDeclaringClass()));
        }

        @Override
        public String toString() {
            return "p(x) := isEnumSelfReference(Field)";
        }
    };

    /**
     * @return    <tt>true</tt> if <tt>type</tt> is a SharedObject, Interface
     * or enum in SharedObject's package tree
     */
    private static boolean isShared(final Class<?> type) {
        final boolean result = (SharedObject.class.isAssignableFrom(type)
                || type.isInterface() || type.isEnum())
                && type.getCanonicalName().startsWith(SHARED_PACKAGE_NAME);
        return result;
    }

    /**
     * groups relevant data for a shareability check
     *
     * @author til, tom, jan
     */
    private class CheckItem {
        /**
         * does this item request waiving of compliance tests? (via annotation)
         */
        public final boolean requestsFinalAppeal;
        /**
         * does this item derive from the field of another item?
         */
        public final boolean fromField;
        /**
         * the type represented by this item
         */
        public final Class<?> type;
        /**
         * an instance of this item, useful for Field.get(instance)
         */
        @Nullable
        public final Object instance;
        public final int classModifiers;
        public final int fieldModifiers;
        public final String simpleTypeName;
        public final String fieldName;

        /**
         * try to get the value of a field, or return null
         */
        private Object getFieldValue(final Field field, final Object owner) {
            assert field == null || owner != null : "if field != null, owner must also be != null";
            Object o = null;
            if (field != null) {
                try {
                    o = field.get(owner);
                } catch (final IllegalAccessException e) {
                    // sshhh! non-public fields will be caught by iShared()
                    //System.err.println("illegal access on " + field);
                }
            }
            return o;
        }

        public CheckItem(final Class<?> clazz, final Object instance) {
            this(clazz, null, instance, null);
        }

        public CheckItem(final Field field, final Object owner) {
            this(null, field, null, owner);
        }

        private CheckItem(final Class<?> type, final Field field, final Object instance, final Object fieldOwner) {
            assert !(type == null && field == null) : "either type or field must be != null";
            final Object fieldValue = getFieldValue(field, fieldOwner);
            if (field != null) {
                this.fromField = true;
                if (fieldValue != null) {
                    this.type = clarifyType(fieldValue.getClass());
                    this.instance = fieldValue;
                } else {
                    this.type = clarifyType(field.getType());
                    this.instance = null;

                }
                this.requestsFinalAppeal = isCheckedShareable.isTrueFor(field);
                final String tmpName = field.getName();
                this.fieldName = tmpName.substring(tmpName.lastIndexOf('.') + 1);
                this.fieldModifiers = field.getModifiers();
            } else {
                this.fromField = false;
                this.type = clarifyType(type);
                this.instance = instance;
                this.requestsFinalAppeal = false;
                this.fieldName = "";
                this.fieldModifiers = 0;
            }
            assert this.type != null;
            this.classModifiers = this.type.getModifiers();
            this.simpleTypeName = this.type.getSimpleName();
        }

        /**
         * perform any final changes so that
         *
         * @param type
         * @return
         */
        private Class<?> clarifyType(final Class<?> type) {
            Class<?> properType = type;
            // convert Arrays
            if (type.isArray()) {
                properType = type.getComponentType();
            }
            // exchange enclosing class for local and anonynous classes
            // this might produce a type already on the checkstack (which is ok as long as they don't get added again)
            while (properType.getEnclosingClass() != null) {
                properType = properType.getEnclosingClass();
            }
            return properType;

        }

        @Override
        public String toString() {
            String string = "";
            if (fromField) {
                string += String.format("%s %s:", Modifier.toString(fieldModifiers), fieldName);
            }
            string += String.format("%s (%s)", simpleTypeName, Modifier.toString(classModifiers));
            return string;
        }
    }

}
