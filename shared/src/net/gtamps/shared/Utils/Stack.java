package net.gtamps.shared.Utils;

import java.util.ArrayList;
import java.util.EmptyStackException;

/**
 * a quick stack implementation, courtesy of www.java2s.com
 *
 * @param <T>
 * @see <a href="http://www.java2s.com/Code/Java/Collections-Data-Structure/extendsArrayListTtocreateStack.htm">www.java2s.com</a>
 */
public class Stack<E> extends ArrayList<E> {
    public Stack() {
        this(20);
    }

    public Stack(final int initialCap) {
        super(initialCap);
    }

    public Stack(final E firstElement) {
        super(20);
        this.push(firstElement);
    }

    public void push(final E value) {
        add(value);
    }

    public E pop() throws EmptyStackException {
        if (size() < 1) {
            throw new EmptyStackException();
        }
        return remove(size() - 1);
    }

    public boolean empty() {
        return size() == 0;
    }

    /**
     * peek & equality test without worrying about empty stack
     *
     * @param element
     * @return    <tt>true</tt> if there's an item on top of the stack with item.equals(element)
     */
    public boolean topIs(final E element) {
        if (empty() || !peek().equals(element)) {
            return false;
        }
        return true;
    }

    public E peek() throws EmptyStackException {
        if (size() < 1) {
            throw new EmptyStackException();
        }
        return get(size() - 1);
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        for (int i = size() - 1; i >= 0; i--) {
            str.append(get(i).toString() + " ");
        }
        str.insert(0, "stk: ");
        return str.toString();
    }


}
