package net.gtamps.shared.serializer.communication;


import net.gtamps.shared.Utils.cache.IObjectCache;
import net.gtamps.shared.Utils.cache.ObjectFactory;
import net.gtamps.shared.Utils.cache.ThreadLocalObjectCache;
import net.gtamps.shared.Utils.cache.TypableObjectCacheFactory;
import net.gtamps.shared.serializer.communication.data.DataMap;
import net.gtamps.shared.serializer.communication.data.ListNode;
import net.gtamps.shared.serializer.communication.data.MapEntry;
import net.gtamps.shared.serializer.communication.data.Value;

/**
 * A factory that produces {@link IObjectCache caches} for {@link AbstractSendable sendable} objects.
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 * 
 * @see IObjectCache
 * @see AbstractSendable
 *
 */
public class SendableCacheFactory implements TypableObjectCacheFactory {

	@Override
	public <T> IObjectCache<T> createObjectCache(final Class<T> type) {
		ObjectFactory<T> factory = null;
		if (type == NewMessage.class) {
			factory = (ObjectFactory<T>) new MessageFactory();
		} else if (type == NewSendable.class) {
			factory = (ObjectFactory<T>) new SendableFactory();
		} else if (type == DataMap.class) {
			factory = (ObjectFactory<T>) new DataMapFactory();
		} else if (type == MapEntry.class) {
			factory = (ObjectFactory<T>) new MapEntryFactory();
		} else if (type == ListNode.class) {
			factory = (ObjectFactory<T>) new ListNodeFactory();
		} else if (type == Value.class) {
			factory = (ObjectFactory<T>) new ValueFactory();
		} else {
			throw new IllegalArgumentException("unsupported type: " + type.getCanonicalName());
		}
		return new ThreadLocalObjectCache<T>(factory);
	}

	private static class MessageFactory implements ObjectFactory<NewMessage> {
		@Override
		public NewMessage createNew() {
			return new NewMessage();
		}
	}

	private static class SendableFactory implements ObjectFactory<NewSendable> {
		@Override
		public NewSendable createNew() {
			return new NewSendable();
		}
	}

	private static class DataMapFactory implements ObjectFactory<DataMap> {
		@Override
		public DataMap createNew() {
			return new DataMap();
		}
	}

	private static class MapEntryFactory implements ObjectFactory<MapEntry> {
		@Override
		public MapEntry<?> createNew() {
			return new MapEntry();
		}
	}

	private static class ListNodeFactory implements ObjectFactory<ListNode> {
		@Override
		public ListNode createNew() {
			return new ListNode();
		}
	}

	private static class ValueFactory implements ObjectFactory<Value> {
		@Override
		public Value createNew() {
			return new Value();
		}
	}

}
