package net.gtamps.shared.game;

import net.gtamps.shared.Utils.cache.IObjectCache;
import net.gtamps.shared.Utils.cache.ObjectFactory;
import net.gtamps.shared.Utils.cache.ThreadLocalObjectCache;
import net.gtamps.shared.Utils.validate.Validate;

public class GameObjectCache<T extends GameObject> {


	class GameObjectFactory<U extends GameObject> implements ObjectFactory<U>{

		Class<U> classObject;

		public GameObjectFactory(final Class<U> type) {
			Validate.notNull(type);
			classObject = type;
		}

		@Override
		public U createNew() {
			try {
				return classObject.newInstance();
			} catch (final InstantiationException e) {
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	IObjectCache<T> cache;

	public GameObjectCache(final Class<T> type) {
		cache = new ThreadLocalObjectCache<T>(new GameObjectFactory<T>(type));
	}

	public IObjectCache<T> registerElement(final T element) {
		return cache.registerElement(element);
	}

	public int getCount() {
		return cache.getCount();
	}

	public boolean hasElements() {
		return cache.hasElements();
	}

	public T getOrCreate(final int uid, final String name) {
		final T element = cache.getOrCreate();
		element.setMutable(true);
		element.setUid(uid);
		element.setName(name);
		element.setMutable(false);
		return element;
	}

	//	public T getElement() {
	//		return cache.getElement();
	//	}
	//
	//	public T getElementOrNull() {
	//		return cache.getElementOrNull();
	//	}

	public void clear() {
		cache.clear();
	}

	public void compact(final boolean forceGc) {
		cache.compact(forceGc);
	}

}
