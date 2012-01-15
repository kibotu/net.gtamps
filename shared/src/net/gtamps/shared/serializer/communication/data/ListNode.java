package net.gtamps.shared.serializer.communication.data;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.gtamps.shared.Utils.validate.Validate;
import net.gtamps.shared.serializer.communication.AbstractSendable;

public final class ListNode<T extends AbstractSendable<T>> extends AbstractSendableData<ListNode<T>> implements Iterable<T>{
	@SuppressWarnings("rawtypes")
	private static final ListNode<?> EMPTY = new ListNode();
	private static final long serialVersionUID = 408802690961330006L;
	private static final String errorIteratorNotResetMsg = "iterator is not ininitial state; call resetIterator() first.";

	@SuppressWarnings("unchecked")
	public static final <T extends AbstractSendable<T>> ListNode<T> emptyList() {
		return (ListNode<T>) EMPTY;
	}

	//	private transient final IObjectCache<ListNode<T>> cache;

	private T value;
	private ListNode<T> next;
	private transient ListNode<T> iteratorNextElement;
	private transient final Iterator<T> iterator = new Iterator<T>() {
		@Override
		public boolean hasNext() {
			return !(iteratorNextElement == null || iteratorNextElement.isEmpty());
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
		this(null);
	}

	ListNode(final T value) {
		super();
		set(value);
	}

	public boolean isEmpty() {
		return this.equals(emptyList());
	}

	public ListNode<T> append(final ListNode<T> newNode) throws IllegalStateException {
		if (next == null || next.isEmpty()) {
			newNode.next = emptyList();
			next = newNode;
		} else {
			next.append(newNode);
		}
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
		if (next == null) {
			if (other.next != null) {
				return false;
			}
		} else if (!next.equals(other.next)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
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
		Validate.notNull(value);
		this.value = value;
		return this;
	}

	public T value() throws IllegalStateException {
		return this.value;
	}

	//	@Override
	//	void reset() {
	//		resetIterator();
	//		unlink();
	//	}

	ListNode<T> next() {
		return next;
	}

	void unlink() {
		iteratorNextElement = null;
		value = null;
		if (next != null) {
			next.unlink();
			next = null;
		}
	}

	private void ensureIteratorReset() {
		if (iteratorNextElement != this) {
			throw new IllegalStateException(errorIteratorNotResetMsg);
		}

	}

	//	@Override
	//	void recycle(final IObjectCache<? extends AbstractSendableData> cache) {
	//		// TODO Auto-generated method stub
	//
	//	}

	@Override
	protected void initHook() {
		resetIterator();
	}

	@Override
	protected void recycleHook() {
		if (next != null) {
			next.recycle();
		}
		unlink();
	}

}
