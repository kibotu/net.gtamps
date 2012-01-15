package net.gtamps.shared.serializer.communication.data;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.gtamps.shared.serializer.communication.AbstractSendable;

public class ListNode<T extends AbstractSendable<T>> extends AbstractSendableData<ListNode<T>> implements Iterable<T>{

	private static final long serialVersionUID = 408802690961330006L;
	private static final String errorIteratorNotResetMsg = "iterator is not ininitial state; call resetIterator() first.";

	@SuppressWarnings("unchecked")
	public static final <T extends AbstractSendable<T>> ListNode<T> emptyList() {
		return (ListNode<T>) EmptyListNode.INSTANCE;
	}

	protected ListNode<T> next;
	private T value;
	private transient ListNode<T> iteratorNextElement;
	private transient final Iterator<T> iterator = new Iterator<T>() {
		@Override
		public boolean hasNext() {
			return !(iteratorNextElement.isEmpty());
		}
		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			final T value = iteratorNextElement.value;
			iteratorNextElement = iteratorNextElement.next;
			return value;
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	};

	public ListNode() {
		super();
		this.value = null;
		this.next = emptyList();
		resetIterator();
	}

	ListNode(final T value) {
		this();
		set(value);
	}

	/**
	 * is this the empty list?
	 * 
	 * @return <code>true</code> if this is the empty list
	 */
	public boolean isEmpty() {
		return false;
	}

	/**
	 * append a new list node to this one and return the resulting list
	 */
	public ListNode<T> append(final ListNode<T> newNode) {
		next = next.append(newNode);
		return this;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ListNode<?> other = (ListNode<?>) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		if (next == null) {
			if (other.next != null) {
				return false;
			}
		} else if (!next.equals(other.next)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((next == null) ? 0 : next.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "ListNode [" + (value == null ? "null" : value.toString()) + "] -> " + (next == null ? "null" : next.toString()) + " ";
	}

	@Override
	public Iterator<T> iterator() throws IllegalStateException {
		ensureIteratorReset();
		return iterator;
	}

	public void resetIterator() {
		iteratorNextElement = this;
	}

	/**
	 * @param value	not <code>null</code>
	 * @return
	 */
	public ListNode<T> set(final T value) {
		this.value = value;
		return this;
	}

	public T value() throws IllegalStateException {
		return this.value;
	}

	ListNode<T> next() {
		return next;
	}

	void unlink() {
		iteratorNextElement = null;
		value = null;
		next.unlink();
		next = null;
	}

	private void ensureIteratorReset() {
		if (iteratorNextElement != this) {
			throw new IllegalStateException(errorIteratorNotResetMsg);
		}
	}

	@Override
	protected void initHook() {
		resetIterator();
	}

	@Override
	protected void recycleHook() {
		if (!next.isEmpty()) {
			next.recycle();
		}
		unlink();
	}

	private static class EmptyListNode<T extends AbstractSendable<T>> extends ListNode<T> {

		@SuppressWarnings("rawtypes")
		public static final EmptyListNode<?> INSTANCE = new EmptyListNode();

		private static final long serialVersionUID = -115606057102526274L;

		private EmptyListNode() {
			this.next = null;
		}

		@Override
		void unlink() {
			// do nothing
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public ListNode<T> append(final ListNode<T> newNode) {
			assert newNode.next.isEmpty();
			return newNode;
		}

		@Override
		public ListNode<T> set(final T value) {
			throw new UnsupportedOperationException("the empty list has no value that could be set");
		}

		@Override
		public T value() {
			throw new UnsupportedOperationException("the empty list has no value");
		}

		@Override
		public String toString() {
			return "EmptyListNode";
		}
	}

}
