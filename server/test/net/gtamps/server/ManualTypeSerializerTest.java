package net.gtamps.server;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.NewSendable;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.DataMap;
import net.gtamps.shared.serializer.communication.data.ListNode;
import net.gtamps.shared.serializer.communication.data.MapEntry;
import net.gtamps.shared.serializer.communication.data.Value;

import org.junit.Before;
import org.junit.Test;

public class ManualTypeSerializerTest {

	private static final String BOOLVAL_KEY = "bool";
	private static final String INTVAL_KEY = "int";
	private static final String LONGVAL_KEY = "long";
	private static final String STRINGLIST_KEY = "stringlist";
	private static final int SENDABLE_ID = 1;
	private static final SendableType SENDABLE_TYPE = SendableType.SESSION;
	private static final String MESSAGE_SESSION_ID = "1234";
	private static final String[] STRING_LIST = {"a", "b"};

	ManualTypeSerializer serializer;
	NewMessage testMessage = createTestMessageData();
	byte[] testBytes = createTestMessageBytes();

	@Before
	public final void before() {
		serializer = new ManualTypeSerializer();
	}

	@Test
	public final void testSerializeNewMessage() {
		final byte[] bytes = serializer.serializeNewMessage(testMessage);
		System.out.println(new String(testBytes));
		System.out.println(new String(bytes));
		assertArrayEquals(testBytes, bytes);
	}

	@Test
	public final void testDeserializeNewMessage() {
		assertEquals(testMessage, serializer.deserializeNewMessage(testBytes));
	}

	private NewMessage createTestMessageData() {
		final NewMessage msg = new NewMessage();
		msg.setSessionId(MESSAGE_SESSION_ID);
		final NewSendable sdb = new NewSendable();
		sdb.id = SENDABLE_ID;
		sdb.type = SENDABLE_TYPE;
		sdb.data = new DataMap();

		ListNode<Value<String>> stringlist = ListNode.emptyList();
		stringlist = stringlist.append(new ListNode<Value<String>>(new Value<String>(STRING_LIST[0])));
		stringlist = stringlist.append(new ListNode<Value<String>>(new Value<String>(STRING_LIST[1])));

		final MapEntry<ListNode<Value<String>>> listentry = new MapEntry<ListNode<Value<String>>>(STRINGLIST_KEY, stringlist);
		final MapEntry<Value<Long>> longentry = new MapEntry<Value<Long>>(LONGVAL_KEY, new Value<Long>(Long.MAX_VALUE));
		final MapEntry<Value<Integer>> intentry = new MapEntry<Value<Integer>>(INTVAL_KEY, new Value<Integer>(1));
		final MapEntry<Value<Boolean>> boolentry = new MapEntry<Value<Boolean>>(BOOLVAL_KEY, new Value<Boolean>(true));

		sdb.data.asMap().add(listentry).add(longentry).add(intentry).add(boolentry);

		final ListNode<NewSendable> sendables = new ListNode<NewSendable>(sdb);
		msg.sendables = sendables;
		return msg;

	}

	private byte[] createTestMessageBytes() {
		final StringBuilder sb = new StringBuilder();
		addToken(sb, ManualTypeSerializer.MESSAGE_START_TOKEN);
		addToken(sb, MESSAGE_SESSION_ID);
		addToken(sb, ManualTypeSerializer.LIST_START_TOKEN);
		addToken(sb, ManualTypeSerializer.SENDABLE_START_TOKEN);
		addToken(sb, ""+SENDABLE_ID);
		addToken(sb, SENDABLE_TYPE.name());
		addToken(sb, ManualTypeSerializer.DATAMAP_START_TOKEN);

		addToken(sb, STRINGLIST_KEY);
		addToken(sb, ManualTypeSerializer.MAPENTRY_DEFINITION_TOKEN);
		addToken(sb, ManualTypeSerializer.LIST_START_TOKEN);
		addToken(sb, STRING_LIST[0]);
		addToken(sb, ManualTypeSerializer.LIST_SEPARATOR_TOKEN);
		addToken(sb, STRING_LIST[1]);
		addToken(sb, ManualTypeSerializer.LIST_SEPARATOR_TOKEN);
		addToken(sb, ManualTypeSerializer.LIST_END_TOKEN);
		addToken(sb, ManualTypeSerializer.MAPENTRY_SEPARATOR_TOKEN);

		addToken(sb, LONGVAL_KEY);
		addToken(sb, ManualTypeSerializer.MAPENTRY_DEFINITION_TOKEN);
		addToken(sb, Long.MAX_VALUE+"");
		addToken(sb, ManualTypeSerializer.MAPENTRY_SEPARATOR_TOKEN);

		addToken(sb, INTVAL_KEY);
		addToken(sb, ManualTypeSerializer.MAPENTRY_DEFINITION_TOKEN);
		addToken(sb, 1+"");
		addToken(sb, ManualTypeSerializer.MAPENTRY_SEPARATOR_TOKEN);

		addToken(sb, BOOLVAL_KEY);
		addToken(sb, ManualTypeSerializer.MAPENTRY_DEFINITION_TOKEN);
		addToken(sb, Boolean.TRUE+"");
		addToken(sb, ManualTypeSerializer.MAPENTRY_SEPARATOR_TOKEN);

		addToken(sb, ManualTypeSerializer.DATAMAP_END_TOKEN);
		addToken(sb, ManualTypeSerializer.LIST_SEPARATOR_TOKEN);
		addToken(sb, ManualTypeSerializer.LIST_END_TOKEN);

		return sb.toString().getBytes();
	}


	private void addToken(final StringBuilder sb, final String token) {
		sb.append(token + ManualTypeSerializer.DELIMITER);
	}

}
