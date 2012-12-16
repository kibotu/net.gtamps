package net.gtamps.shared.serializer.communication;

import java.io.Serializable;

import net.gtamps.shared.Utils.cache.IObjectCache;
import net.gtamps.shared.serializer.communication.data.ListNode;

public abstract class AbstractSendable<Type extends AbstractSendable<Type>> implements Serializable {

	private static final String ERROR_CACHE_UNDEFINED_MSG = "'cache' must not be 'null': call setCache(IObjectCache) first";
	private static final long serialVersionUID = 7512510685123238578L;

	protected transient IObjectCache<Type> cache = null;

	protected AbstractSendable() {
	}

	@Override
	public abstract boolean equals(Object o);
	@Override
	public abstract int hashCode();

	final void setCache(final IObjectCache<Type> cache) {
		this.cache = cache;
	}

	final void init() throws IllegalStateException {
		ensureCacheDefined();
		initHook();
	}

	public void recycle() throws IllegalStateException {
		recycleHook();
		try {
			if (this != ListNode.EmptyListNode.INSTANCE) {
				cache.registerElement((Type)this);
			}
		} catch (final NullPointerException e) {
				throw new IllegalStateException(this.toString() +" "+ ERROR_CACHE_UNDEFINED_MSG, e);
		}
	}

	protected abstract void initHook();
	protected abstract void recycleHook(); 

	private void ensureCacheDefined() throws IllegalStateException {
		if (cache == null) {
			throw new IllegalStateException(ERROR_CACHE_UNDEFINED_MSG);
		}
	}

}
