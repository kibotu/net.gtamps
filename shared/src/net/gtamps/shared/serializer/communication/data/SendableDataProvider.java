package net.gtamps.shared.serializer.communication.data;

public class SendableDataProvider {

	//	private final Map<Class<?>, IObjectCache<?>> caches = new HashMap<Class<?>, IObjectCache<?>>();
	//
	//
	//	public SendableDataProvider() {
	//	}
	//
	//	@ReturnsCachedValue
	//	public <T> Value<T> getValue() {
	//		final Value<T> value = getInitializedDataObject(Value.class);
	//		return value;
	//	}
	//
	//	@ReturnsCachedValue
	//	public <T> Value<T> getValue(final T content) {
	//		final Value<T> value = getInitializedDataObject(Value.class);
	//		value.set(content);
	//		return value;
	//	}
	//
	//	@ReturnsCachedValue
	//	public <T extends AbstractSendableData<T>> ListNode<T> getListNode() {
	//		final ListNode<T> node = getInitializedDataObject(ListNode.class);
	//		return node;
	//	}
	//
	//	@ReturnsCachedValue
	//	public <T extends AbstractSendableData<T>> ListNode<T> getListNode(final T content) {
	//		final ListNode<T> node = getInitializedDataObject(ListNode.class);
	//		node.set(content);
	//		return node;
	//	}
	//
	//	@ReturnsCachedValue
	//	public <T extends AbstractSendableData<?>> MapEntry<T> getMapEntry() {
	//		final MapEntry<T> entry = getInitializedDataObject(MapEntry.class);
	//		return entry;
	//	}
	//
	//	@ReturnsCachedValue
	//	public <T extends AbstractSendableData<?>> MapEntry<T> getMapEntry(final String key, final T value) {
	//		final MapEntry<T> entry = getInitializedDataObject(MapEntry.class);
	//		entry.set(key, value);
	//		return entry;
	//	}
	//
	//	@ReturnsCachedValue
	//	public DataMap getDataMap() {
	//		return getInitializedDataObject(DataMap.class);
	//	}
	//
	//	//	public void recycle(final Value<?> value) {
	//	//		acquireCache(Value.class).registerElement(value);
	//	//	}
	//	//
	//	//	public void recycle(final ListNode<?> node) {
	//	//		acquireCache(ListNode.class).registerElement(node);
	//	//	}
	//	//
	//	//	public void recycle(final MapEntry<?> mapEntry) {
	//	//		acquireCache(MapEntry.class).registerElement(mapEntry);
	//	//	}
	//	//
	//	//	public void recycle(final DataMap map) {
	//	//		acquireCache(DataMap.class).registerElement(map);
	//	//	}
	//
	//	@ReturnsCachedValue
	//	private <T extends AbstractSendableData<T>> T getInitializedDataObject(final Class<T> type) {
	//		final IObjectCache<T> cache = acquireCache(type); 
	//		final T data = cache.getOrCreate();
	//		data.setCache(cache);
	//		data.init();
	//		return data;
	//	}
	//
	//	private <T extends AbstractSendableData<T>> IObjectCache<T> acquireCache(final Class<T> cachedType) {
	//		IObjectCache<T> cache = (IObjectCache<T>) caches.get(cachedType);
	//		if (cache == null) {
	//			cache = createCache(cachedType);
	//		}
	//		return cache;
	//	}
	//
	//	private <T extends AbstractSendableData<T>> IObjectCache<T> createCache(final Class<T> cachedType) {
	//		final IObjectCache<T> cache = new ThreadLocalObjectCache<T>(getObjectFactoryForType(cachedType));
	//		caches.put(cachedType, cache);
	//		return cache;
	//	}
	//
	//	@SuppressWarnings({ "rawtypes", "unchecked" })
	//	private static final <T extends AbstractSendableData> ObjectFactory<T> getObjectFactoryForType(final Class<T> type) {
	//		return new ObjectFactory() {
	//
	//			@Override
	//			public Object createNew() {
	//				try {
	//					return type.getDeclaredConstructor().newInstance();
	//				} catch (final IllegalArgumentException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				} catch (final SecurityException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				} catch (final InstantiationException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				} catch (final IllegalAccessException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				} catch (final InvocationTargetException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				} catch (final NoSuchMethodException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				}
	//				return null;
	//			}
	//		};
	//	}

}
