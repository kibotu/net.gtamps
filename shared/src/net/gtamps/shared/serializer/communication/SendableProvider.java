package net.gtamps.shared.serializer.communication;

import java.util.HashMap;
import java.util.Map;

import net.gtamps.shared.Utils.cache.IObjectCache;
import net.gtamps.shared.Utils.cache.TypableObjectCacheFactory;
import net.gtamps.shared.Utils.cache.annotations.ReturnsCachedValue;
import net.gtamps.shared.serializer.communication.data.*;

/**
 * A provider for Sendables, Messages and their data, giving convenient access
 * to potentially cached instances of these classes.
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 *
 */
public class SendableProvider {

	private final Map<Class<?>, IObjectCache<?>> caches = new HashMap<Class<?>, IObjectCache<?>>();
	private final TypableObjectCacheFactory cacheFactory;

	public SendableProvider(final TypableObjectCacheFactory cacheFactory) {
		if (cacheFactory == null) {
			throw new IllegalArgumentException("'cacheFactory' must not be 'null'");
		}
		this.cacheFactory = cacheFactory;
	}

	@ReturnsCachedValue
	public <T> Value<T> getValue() {
		final Value<T> value = getInitializedSendable(Value.class);
		return value;
	}

	@ReturnsCachedValue
	public <T> Value<T> getValue(final T content) {
		final Value<T> value = getInitializedSendable(Value.class);
		value.set(content);
		return value;
	}

	@ReturnsCachedValue
	public ListNode<?> getListNode() {
		final ListNode<?> node = getInitializedSendable(ListNode.class);
		return node;
	}

	@ReturnsCachedValue
	public <T extends AbstractSendable<T>> ListNode<T> getListNode(final T content) {
		final ListNode<T> node = getInitializedSendable(ListNode.class);
		node.set(content);
		return node;
	}

	@ReturnsCachedValue
	public <T extends AbstractSendableData<?>> MapEntry<T> getMapEntry() {
		final MapEntry<T> entry = getInitializedSendable(MapEntry.class);
		return entry;
	}

	@ReturnsCachedValue
	public <T extends AbstractSendableData<?>> MapEntry<T> getMapEntry(final String key, final T value) {
		final MapEntry<T> entry = getInitializedSendable(MapEntry.class);
		entry.set(key, value);
		return entry;
	}

	@ReturnsCachedValue
	public NewMessage getMessage() {
		return getInitializedSendable(NewMessage.class);
	}

	@ReturnsCachedValue
	public NewSendable getSendable() {
		return getInitializedSendable(NewSendable.class);
	}

	@ReturnsCachedValue
	public DataMap getDataMap() {
		return getInitializedSendable(DataMap.class);
	}

	@ReturnsCachedValue
	private <T extends AbstractSendable<T>> T getInitializedSendable(final Class<T> type) {
		final IObjectCache<T> cache = acquireCache(type); 
		final T data = cache.getOrCreate();
		data.setCache(cache);
		data.init();
		return data;
	}

	@SuppressWarnings("unchecked")
	private <T extends AbstractSendable<T>> IObjectCache<T> acquireCache(final Class<T> cachedType) {
		IObjectCache<?> cache = caches.get(cachedType);
		if (cache == null) {
			cache = createCache(cachedType);
		}
		return (IObjectCache<T>) cache;
	}

	private <T extends AbstractSendable<T>> IObjectCache<T> createCache(final Class<T> cachedType) {
		final IObjectCache<T> cache = cacheFactory.createObjectCache(cachedType);
		caches.put(cachedType, cache);
		return cache;
	}

}
