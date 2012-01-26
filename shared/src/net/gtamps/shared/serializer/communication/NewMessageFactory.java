package net.gtamps.shared.serializer.communication;

import java.lang.reflect.InvocationTargetException;

import net.gtamps.shared.Utils.cache.IObjectCache;
import net.gtamps.shared.Utils.cache.ObjectFactory;
import net.gtamps.shared.Utils.cache.ThreadLocalObjectCache;
import net.gtamps.shared.Utils.cache.TypableObjectCacheFactory;
import net.gtamps.shared.Utils.cache.annotations.ReturnsCachedValue;
/**
 * @deprecated use {@link SendableFactory#createMessageBuilder()} instead
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 *
 */
@Deprecated
final public class NewMessageFactory {

	private static final TypableObjectCacheFactory cacheFactory = createDefaultCacheFactory();
	private static final SendableFactory sendableFactory = new SendableFactory(cacheFactory);

	private static final TypableObjectCacheFactory createDefaultCacheFactory() {
		return new TypableObjectCacheFactory() {
			@Override
			public <T> IObjectCache<T> createObjectCache(final Class<T> type) {
				return new ThreadLocalObjectCache<T>( createTypedObjectFactory(type) );
			}
		};
	}

	private static final <T> ObjectFactory<T> createTypedObjectFactory(final Class<T> type) {
		return new ObjectFactory<T>() {
			@ReturnsCachedValue
			@Override
			public T createNew() {
				try {
					return type.getConstructor().newInstance();
				} catch (final IllegalArgumentException e) {
					e.printStackTrace();
				} catch (final SecurityException e) {
					e.printStackTrace();
				} catch (final InstantiationException e) {
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				} catch (final InvocationTargetException e) {
					e.printStackTrace();
				} catch (final NoSuchMethodException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	}


	private NewMessageFactory() {
	}

	public static NewMessage createSessionRequest() {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createSessionRequest()));
		return msg;
	}

	public static NewMessage creategetTileMapRequest() {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createTileMapRequest()));
		return msg;
	}

	public static NewMessage createLoginRequest(final String username, final String password) {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createLoginRequest(username, password)));
		return msg;
	}

	public static NewMessage createRegisterRequest(final String username, final String password) {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createRegisterRequest(username, password)));
		return msg;
	}

	public static NewMessage createJoinRequest() {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createJoinRequest()));
		return msg;
	}

	public static NewMessage createLeaveRequest() {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createLeaveRequest()));
		return msg;
	}

	public static NewMessage createGetMapDataRequest() {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createGetMapDataRequest()));
		return msg;
	}

	public static NewMessage createGetPlayerRequest() {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createGetPlayerRequest()));
		return msg;
	}

	public static NewMessage createGetUpdateRequest(final long revId) {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createGetUpdateRequest(revId)));
		return msg;
	}

	public static NewMessage createAccelerateCommand(final float value) {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createAccelerateCommand(value)));
		return msg;
	}

	public static NewMessage createDecelerateCommand(final float value) {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createDecelerateCommand(value)));
		return msg;
	}

	public static NewMessage createRightCommand(final float value) {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createRightCommand(value)));
		return msg;
	}

	public static NewMessage createLeftCommand(final float value) {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createLeftCommand(value)));
		return msg;
	}

	public static NewMessage createShootCommand(){
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createShootCommand()));
		return msg;
	}

	public static NewMessage createEnterExitCommand(){
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createEnterExitCommand()));
		return msg;
	}

	public static NewMessage createJoinCommand() {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createEnterExitCommand()));
		return msg;
	}

	public static NewMessage createRegisterCommand() {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createShootCommand()));
		return msg;
	}

	public static NewMessage createLoginCommand() {
		final NewMessage msg = sendableFactory.createMessage();
		msg.addSendable(sendableFactory.wrapSendable(sendableFactory.createHandbrakeCommand()));
		return msg;
	}
}
