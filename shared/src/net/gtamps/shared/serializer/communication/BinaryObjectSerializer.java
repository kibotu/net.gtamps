package net.gtamps.shared.serializer.communication;

import java.nio.ByteBuffer;

import net.gtamps.shared.serializer.communication.data.DataMap;
import net.gtamps.shared.serializer.communication.data.ListNode;
import net.gtamps.shared.serializer.communication.data.MapEntry;
import net.gtamps.shared.serializer.communication.data.Value;
import net.gtamps.shared.serializer.helper.ArrayPointer;
import net.gtamps.shared.serializer.helper.BinaryConverter;

public class BinaryObjectSerializer implements ISerializer {

	private final int BUFFER_SIZE = 2048;
	private byte[] buf = new byte[BUFFER_SIZE];
	private ArrayPointer ps = new ArrayPointer();

	int i;

	private void clearBuffer() {
		for (i = 0; i < buf.length; i++) {
			buf[i] = 0;
		}
	}

	ListNode<NewSendable> mockList = new ListNode<NewSendable>();

	@Override
	public byte[] serializeMessage(final Message m) {
		for (final NewSendable s : mockList) {
			// 1.st byte is type
			BinaryConverter.intToBytes(s.id, buf, ps);
			// 2nd - 5th byte is sessionid (int)
			BinaryConverter.stringToByte(s.sessionId, buf, ps);

			serialzeAbstractSendable(s, buf, ps);
		}
		return buf;
	}

	private void serialzeAbstractSendable(AbstractSendable<?> s, byte[] buf, ArrayPointer ps) {
		if (s.getClass() == NewSendable.class) {
			NewSendable ns = (NewSendable) s;
			serialzeSendable(ns, buf, ps);
		} else if (s.getClass() == ListNode.class) {
			serializeListNode((ListNode<? extends AbstractSendable<?>>) s, buf, ps);
		} else if (s.getClass() == DataMap.class) {
			serializeDataMap((DataMap) s, buf, ps);
		} else if (s.getClass() == Value.class) {
			serializeValue((Value<?>) s, buf, ps);
		} else {
			throw new SendableSerializationException(s.getClass().getCanonicalName()
					+ " can not be Serialized by BinaryObjectSerializer!");
		}
	}

	private void serialzeSendable(NewSendable ns, byte[] buf, ArrayPointer ps) {
		if (ns.data.getClass() == ListNode.class) {
			serializeListNode((ListNode<? extends AbstractSendable<?>>) ns.data, buf, ps);
		} else if (ns.data.getClass() == DataMap.class) {
			serializeDataMap((DataMap) ns.data, buf, ps);
		} else if (ns.data.getClass() == Value.class) {
			serializeValue((Value<?>) ns.data, buf, ps);
		}
	}

	private void serializeValue(Value<?> data, byte[] buf, ArrayPointer ps) {
		// TODO describe each byte stream with a little header
		if (data.get().getClass() == Integer.class) {
			BinaryConverter.intToBytes((Integer) data.get(), buf, ps);
		} else if (data.get().getClass() == Float.class) {
			BinaryConverter.floatToBytes((Float) data.get(), buf, ps);
		} else if (data.get().getClass() == Long.class) {
			BinaryConverter.longToBytes((Long) data.get(), buf, ps);
		} else if (data.get().getClass() == Boolean.class) {
			BinaryConverter.booleanToByte((Boolean) data.get(), buf, ps);
		} else if (data.get().getClass() == String.class) {
			BinaryConverter.stringToByte((String) data.get(), buf, ps);
		} else {
			throw new SendableSerializationException(data.get().getClass().getCanonicalName()
					+ " can not be Serialized by BinaryObjectSerializer!");
		}
	}

	private void serializeDataMap(DataMap data, byte[] buf2, ArrayPointer ps2) {
		for(MapEntry<?> me : data){
			// TODO describe each byte stream with a little header
			BinaryConverter.stringToByte(me.key(), buf2, ps2);
			serialzeAbstractSendable(me.value(), buf2, ps2);
		}
	}

	private void serializeListNode(ListNode<? extends AbstractSendable<?>> l, byte[] buf, ArrayPointer p) {
		l.resetIterator();

		for (AbstractSendable<?> as : l) {
			serialzeAbstractSendable(as, buf, p);
		}
	}

	ArrayPointer pd = new ArrayPointer();

	@Override
	public Message deserializeMessage(final byte[] bytes) {
		final Message m = new Message();
		while (pd.pos() < bytes.length) {

		}
		return m;
	}

}
