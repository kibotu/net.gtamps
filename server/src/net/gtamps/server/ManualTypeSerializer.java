package net.gtamps.server;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.serializer.communication.AbstractSendable;
import net.gtamps.shared.serializer.communication.ISerializer;
import net.gtamps.shared.serializer.communication.MessageDeserializationException;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.NewSendable;
import net.gtamps.shared.serializer.communication.SendableCacheFactory;
import net.gtamps.shared.serializer.communication.SendableProvider;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.AbstractSendableData;
import net.gtamps.shared.serializer.communication.data.DataMap;
import net.gtamps.shared.serializer.communication.data.ListNode;
import net.gtamps.shared.serializer.communication.data.MapEntry;
import net.gtamps.shared.serializer.communication.data.Value;

import org.jetbrains.annotations.NotNull;


/**
 * $message 	= M $sessionId [($sendable , )*]
 * $sessionId 	= _String_
 * $sendable 	= SEND $sendId $sendType ($sendData)?
 * $sendId 		= _Integer_
 * $sendType 	= _SendableType.name()_
 * $sendData 	= $message | $sendable | $list | $datamap | $mapentry | $value
 * $list		= [ ($sendData , )* ]
 * $datamap		= { ($mapentry ; )* }
 * $mapentry	= _String_ : $sendData
 * $value		= _String_ | _Integer_ | _Long_ | _Float_ | _Boolean_
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 */
public class ManualTypeSerializer implements ISerializer {

	private static final String NULL_VALUE_TOKEN = "__NULL__";

	private static final LogType TAG = LogType.SERVER;

	static final  String DELIMITER = " ";

	static final String MESSAGE_START_TOKEN = "M";
	static final String SENDABLE_START_TOKEN = "SEND";

	static final String LIST_SEPARATOR_TOKEN = ",";
	static final String LIST_END_TOKEN = "]";
	static final String LIST_START_TOKEN = "[";

	static final String DATAMAP_END_TOKEN = "}";
	static final String DATAMAP_START_TOKEN = "{";

	static final String MAPENTRY_SEPARATOR_TOKEN = ";";
	static final String MAPENTRY_DEFINITION_TOKEN = ":";

	// TODO inject
	private final SendableProvider sendableProvider = new SendableProvider(new SendableCacheFactory());

	@Override
	public byte[] serializeNewMessage(@NotNull final NewMessage message) {
		GUILogger.getInstance().log(TAG, "serializing message: " + message);
		final StringBuilder bld = new StringBuilder();
		serializeAbstractSendable(bld, message);
		return bld.toString().getBytes();
	}

	private void addToken(final StringBuilder bld, final String token) {
		bld.append(token + DELIMITER);
	}

	private <T extends AbstractSendable<T>>void serializeAbstractSendable(final StringBuilder bld, final AbstractSendable<T> sdb) {
		if (sdb instanceof NewMessage) {
			serializeMessage(bld, (NewMessage) sdb);
		} else if (sdb instanceof NewSendable) {
			serializeSendable(bld, (NewSendable) sdb);
		} else if (sdb instanceof Value) {
			serializeValue(bld, (Value<?>) sdb);
		} else if (sdb instanceof ListNode) {
			serializeList(bld, (ListNode<?>) sdb);
		} else if (sdb instanceof DataMap) {
			serializeDataMap(bld, (DataMap) sdb);
		} else if (sdb instanceof MapEntry) {
			serializeMapEntry(bld, (MapEntry<?>) sdb);
		} else {
			throw new IllegalArgumentException("unknown type: " + sdb.getClass().getCanonicalName());
		}
	}

	private void serializeMessage(final StringBuilder bld, final NewMessage message) {
		addToken(bld, MESSAGE_START_TOKEN);
		final String sessId = message.getSessionId();
		addToken(bld, (sessId == null || sessId.length() == 0) ? "" : sessId);
		serializeAbstractSendable(bld, message.sendables);
	}

	private void serializeSendable(final StringBuilder bld, final NewSendable s) {
		addToken(bld, SENDABLE_START_TOKEN);
		addToken(bld, Integer.toString(s.id));
		addToken(bld, s.type.name());
		if (s.data != null) {
			serializeAbstractSendable(bld, s.data);
		}
	}

	private <T extends AbstractSendable<T>>void serializeList(final StringBuilder bld, final ListNode<T> sdb) {
		addToken(bld, LIST_START_TOKEN);
		sdb.resetIterator();
		for (final T element: sdb) {
			serializeAbstractSendable(bld, element);
			addToken(bld, LIST_SEPARATOR_TOKEN);
		}
		addToken(bld, LIST_END_TOKEN);
	}

	private void serializeDataMap(final StringBuilder bld, final DataMap sdb) {
		addToken(bld, DATAMAP_START_TOKEN);
		sdb.resetIterator();
		for (final MapEntry<?> entry: sdb) {
			serializeMapEntry(bld, entry);
			addToken(bld, MAPENTRY_SEPARATOR_TOKEN);
		}
		addToken(bld, DATAMAP_END_TOKEN);
	}

	private void serializeValue(final StringBuilder bld, final Value<?> sdb) {
		addToken(bld, sdb.get().toString());
	}

	private void serializeMapEntry(final StringBuilder bld, final MapEntry<?> sdb) {
		addToken(bld, sdb.key());
		addToken(bld, MAPENTRY_DEFINITION_TOKEN);
		serializeAbstractSendable(bld, (AbstractSendableData<?>)sdb.value());
	}


	@Override
	public NewMessage deserializeNewMessage(@NotNull final byte[] bytes)
			throws MessageDeserializationException {
		final String msgString = new String(bytes);
		final Scanner scanner = new Scanner(msgString);
		try {
			final NewMessage msg = deserializeMessage(scanner);
			return msg;
		} catch (final InputMismatchException e) {
			throw new MessageDeserializationException(e);
		} catch (final NoSuchElementException e) {
			throw new MessageDeserializationException(e);
		}
	}

	private AbstractSendable<?> deserializeAbstractSendable(final Scanner scanner) {
		AbstractSendable<?> sendable = null;
		if (isNextToken(scanner, DATAMAP_START_TOKEN)) {
			sendable = deserializeDataMap(scanner);
		} else if (isNextToken(scanner, LIST_START_TOKEN)) {
			sendable = deserializeList(scanner);
		} else if (isNextToken(scanner, SENDABLE_START_TOKEN)) {
			sendable = deserializeSendable(scanner);
		} else if (isNextToken(scanner, MESSAGE_START_TOKEN)) {
			sendable = deserializeMessage(scanner);
		} else {
			sendable = deserializeValue(scanner);
		}
		return sendable;
	}

	private Value<?> deserializeValue(final Scanner scanner) {
		Value<?> value;
		if (scanner.hasNextLong()) {
			value = sendableProvider.getValue(scanner.nextLong());
		} else if (scanner.hasNextFloat()) {
			value = sendableProvider.getValue(scanner.nextFloat());
		} else if (scanner.hasNextBoolean()) {
			value = sendableProvider.getValue(scanner.nextBoolean());
		} else 	if (isNextToken(scanner, NULL_VALUE_TOKEN)) {
			scanner.next();
			value = null;
		} else {
			value = sendableProvider.getValue(scanner.next());
		}
		return value;
	}

	private NewMessage deserializeMessage(final Scanner scanner) {
		nextToken(scanner, MESSAGE_START_TOKEN);
		final NewMessage msg = sendableProvider.getMessage();
		final String sessionId = scanner.next();
		msg.setSessionId(sessionId);
		msg.sendables = deserializeList(scanner);
		return msg;
	}

	private NewSendable deserializeSendable(final Scanner scanner) {
		nextToken(scanner, SENDABLE_START_TOKEN);
		final NewSendable sendable = sendableProvider.getSendable();
		sendable.id = scanner.nextInt();
		sendable.type = SendableType.valueOf(scanner.next());
		sendable.data = (AbstractSendableData<?>) deserializeAbstractSendable(scanner);
		return sendable;
	}

	private DataMap deserializeDataMap(final Scanner scanner) {
		nextToken(scanner, DATAMAP_START_TOKEN);
		final DataMap map = sendableProvider.getDataMap();
		while (!isNextToken(scanner, DATAMAP_END_TOKEN)) {
			final MapEntry<?> entry = deserializeMapEntry(scanner);
			map.add(entry);
			nextToken(scanner, MAPENTRY_SEPARATOR_TOKEN);
		}
		nextToken(scanner, DATAMAP_END_TOKEN);
		return map;
	}

	private MapEntry<?> deserializeMapEntry(final Scanner scanner) {
		final String key = scanner.next();
		nextToken(scanner, MAPENTRY_DEFINITION_TOKEN);
		final AbstractSendableData<?> value = (AbstractSendableData<?>) deserializeAbstractSendable(scanner);
		return sendableProvider.getMapEntry(key, value);
	}

	private <T extends AbstractSendable<T>> ListNode<T> deserializeList(final Scanner scanner) {
		ListNode<T> list = ListNode.emptyList();
		nextToken(scanner, LIST_START_TOKEN);
		while(!isNextToken(scanner, LIST_END_TOKEN)) {
			final T content = (T) deserializeAbstractSendable(scanner);
			list = list.append(sendableProvider.getListNode(content));
			if (isNextToken(scanner, LIST_SEPARATOR_TOKEN)) {
				nextToken(scanner, LIST_SEPARATOR_TOKEN);
			}
		}
		nextToken(scanner, LIST_END_TOKEN);
		return list;
	}

	private String nextToken(final Scanner scanner, final String string) {
		return scanner.next(toPattern(string));
	}

	private boolean isNextToken(final Scanner scanner, final String string) {
		return scanner.hasNext(toPattern(string));
	}

	private Pattern toPattern(final String s) {
		return Pattern.compile(Pattern.quote(s));
	}

	private void proceedToNext(final Scanner scanner, final String pattern) {
		while (scanner.hasNext() && !scanner.hasNext(pattern)) {
			scanner.next();
		}
	}

}
