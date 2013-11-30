package net.gtamps.shared.serializer.communication;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.gtamps.shared.Utils.cache.ObjectFactory;

public class ReflectionSendableFactory<T extends AbstractSendable<T>> implements ObjectFactory<T> {

	private final Class<T> type;

	public ReflectionSendableFactory(final Class<T> type) {
		this.type = type;
	}

	@Override
	public T createNew() {
		try {
			final Constructor<T> init = type.getConstructor();
			return init.newInstance();
		} catch (final NullPointerException e) {
			//
		} catch (final SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
