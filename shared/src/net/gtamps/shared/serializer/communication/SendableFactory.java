package net.gtamps.shared.serializer.communication;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.gtamps.shared.Utils.cache.TypableObjectCacheFactory;
import net.gtamps.shared.serializer.communication.data.AbstractSendableData;
import net.gtamps.shared.serializer.communication.data.DataMap;
import net.gtamps.shared.serializer.communication.data.ListNode;
import net.gtamps.shared.serializer.communication.data.MapEntry;
import net.gtamps.shared.serializer.communication.data.Value;

public final class SendableFactory {

	private final SendableProvider sendableProvider;

	public SendableFactory(final TypableObjectCacheFactory cacheFactory) {
		this.sendableProvider = new SendableProvider(cacheFactory);
	}

	public MessageBuilder createMessageBuilder() {
		return new MessageBuilder(this);
	}

	public NewMessage createMessage() {
		return sendableProvider.getMessage();
	}

	public ListNode<NewSendable> wrapSendable(final NewSendable content) {
		return sendableProvider.getListNode(content);
	}

	public NewSendable createSessionRequest() {
		return createSendable(SendableType.SESSION); 
	}

	public NewSendable createLoginRequest(final String username, final String password) {
		final DataMap authData = new DataMap();
		final MapEntry<Value<String>> nameEntry = sendableProvider.getMapEntry(StringConstants.AUTH_USERNAME, sendableProvider.getValue(username));
		final MapEntry<Value<String>> passEntry = sendableProvider.getMapEntry(StringConstants.AUTH_PASSWORD, sendableProvider.getValue(password));
		authData.add(nameEntry).add(passEntry);

		return createSendable(SendableType.LOGIN).setData(authData);
	}

	public NewSendable createRegisterRequest(final String username, final String password) {
		final DataMap authData = new DataMap();
		final MapEntry<Value<String>> nameEntry = sendableProvider.getMapEntry(StringConstants.AUTH_USERNAME, sendableProvider.getValue(username));
		final MapEntry<Value<String>> passEntry = sendableProvider.getMapEntry(StringConstants.AUTH_PASSWORD, sendableProvider.getValue(password));
		authData.add(nameEntry).add(passEntry);

		return createSendable(SendableType.REGISTER).setData(authData);
	}

	public NewSendable createJoinRequest() {
		return createJoinRequest(null);
	}

	public NewSendable createJoinRequest(final String gameId) {
		return createSendable(SendableType.JOIN)
				.setData(sendableProvider.getValue(gameId));
	}

	public NewSendable createLeaveRequest() {
		return createSendable(SendableType.LEAVE);
	}

	public NewSendable createGetMapDataRequest() {
		return createSendable(SendableType.GETMAPDATA);
	}

	public NewSendable createGetPlayerRequest() {
		return createSendable(SendableType.GETPLAYER);
	}

	public NewSendable createGetUpdateRequest(final long revId) {
		final DataMap data = new DataMap();
		final MapEntry<Value<Long>> revEntry = sendableProvider.getMapEntry(StringConstants.UPDATE_REVISION, sendableProvider.getValue(revId));
		data.add(revEntry);

		return createSendable(SendableType.GETUPDATE).setData(data);
	}

	public NewSendable createAccelerateCommand(final float value) {
		return createSendable(SendableType.ACTION_ACCELERATE)
				.setData(sendableProvider.getValue(value));
	}

	public NewSendable createDecelerateCommand(final float value) {
		return createSendable(SendableType.ACTION_DECELERATE)
				.setData(sendableProvider.getValue(value));
	}

	public NewSendable createRightCommand(final float value) {
		return createSendable(SendableType.ACTION_RIGHT)
				.setData(sendableProvider.getValue(value));
	}

	public NewSendable createLeftCommand(final float value) {
		return createSendable(SendableType.ACTION_LEFT)
				.setData(sendableProvider.getValue(value));
	}

	public NewSendable createEnterExitCommand() {
		return createSendable(SendableType.ACTION_ENTEREXIT)
				;
	}

	public NewSendable createShootCommand() {
		return createSendable(SendableType.ACTION_SHOOT)
				;
	}

	public NewSendable createHandbrakeCommand() {
		return createSendable(SendableType.ACTION_HANDBRAKE)
				;
	}

	public NewSendable createResponseOK(final NewSendable request, final Object data) {
		return createResponse("OK", request, data);
	}

	public NewSendable createResponseNeed(final NewSendable request, final String message) {
		return createResponse("NEED", request, message);
	}

	public NewSendable createResponseBad(final NewSendable request, final String message) {
		return createResponse("BAD", request, message);
	}

	public NewSendable createResponseError(final NewSendable request, final String message) {
		return createResponse("ERROR", request, message);
	}

	private NewSendable createResponse(final String responseCode, final NewSendable request, final Object data) {
		final SendableType type = findResponseType(responseCode, request);
		return createSendable(type)
				.setId(request.id)
				.setData(createSendableData(data))
				;
	}

	private SendableType findResponseType(final String responseCode, final NewSendable request) throws IllegalArgumentException {
		try {
			return SendableType.valueOf(request.type.name() + "_" + responseCode);
		} catch (final IllegalArgumentException e) {
			final String message = "unable to find corresponding SendableType for type, responseCode: "
					+ request.type + "," +  responseCode;
			throw new IllegalArgumentException(message, e);
		}
	}

	private NewSendable createSendable(final SendableType type) {
		return sendableProvider.getSendable()
				.setType(type)
				;
	}

	private AbstractSendableData<?> createSendableData(final Object o) throws IllegalArgumentException {
		AbstractSendableData<?> data;
		try {
			if (o instanceof Map) {
				data = createDataMap((Map<String, Object>) o);
			} else if (o instanceof List) {
				data = this.createList((List<AbstractSendableData>) o);
			} else {
				data = createValue(o);
			}
		} catch (final RuntimeException e) {
			throw new IllegalArgumentException("error creating data for Sendable", e);
		}
		return data;
	}

	private DataMap createDataMap(final Map<String, Object> map) {
		final DataMap dataMap = sendableProvider.getDataMap();
		for (final Entry<String, Object> entry : map.entrySet()) {
			dataMap.add(sendableProvider.getMapEntry(
					entry.getKey(),
					(AbstractSendableData<?>) createSendableData(entry.getValue())
					));
		}
		return dataMap;
	}

	private <T extends AbstractSendableData<T>> ListNode<T> createList(final List<T> list) {
		final int length = list.size();
		if (length == 0) {
			return null;
		}
		final ListNode<T> dataList = (ListNode<T>) createSendableData(list.get(0));
		for (int i = 1; i < length; i++) {
			dataList.append((ListNode<T>) createSendableData(list.get(0)));
		}
		return dataList;
	}

	private Value<?> createValue(final Object o) {
		return sendableProvider.getValue(o);
	}

}
