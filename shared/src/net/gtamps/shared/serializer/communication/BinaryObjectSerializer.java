package net.gtamps.shared.serializer.communication;

import java.util.HashMap;

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

	/*
	 * This array represents the "headers" of all byte wise serialized objects
	 * The indexes represent the value of the written or read byte.
	 */

	private final Class<?>[] classByte = { null, // 0 - reserved
			Integer.class, // 1
			Float.class, // 2
			Long.class, // 3
			Boolean.class, // 4
			String.class, // 5
			AbstractSendable.class, // 6
			NewSendable.class, // 7
			DataMap.class, // 8
			ListNode.class, // 9
			Value.class // 10
	};

	private HashMap<Class<?>, Byte> classByteLookup = null;

	/*
	 * the init() method creates the reverse lookup table, so that classes can
	 * be resolved to bytes for serialization. Must be called before any action
	 * Occurs
	 */
	private void init() {
		if (classByteLookup == null) {
			classByteLookup = new HashMap<Class<?>, Byte>();
			for (int i = 0; i < classByte.length; i++) {
				classByteLookup.put(classByte[i], (byte) i);
			}
		}
	}

	private void clearBuffer() {
		int i;
		for (i = 0; i < buf.length; i++) {
			buf[i] = 0;
		}
	}

	// remove me, I'm just mocking the way.
	ListNode<NewSendable> mockList = new ListNode<NewSendable>();

	@Override
	public byte[] serializeMessage(final Message m) {
		init();
		for (final NewSendable s : mockList) {
			BinaryConverter.writeIntToBytes(s.id, buf, ps);
			BinaryConverter.writeStringToBytes(s.sessionId, buf, ps);
			serialzeAbstractSendable(s, buf, ps);
		}
		return buf;
	}

	@Override
	public Message deserializeMessage(final byte[] bytes) {
		ArrayPointer pd = new ArrayPointer();
		init();
		final Message m = new Message();
		//TODO read over message id; why is there no setter?
		BinaryConverter.readIntFromBytes(bytes,pd);
		m.setSessionId(BinaryConverter.readStringFromBytes(bytes, pd));
		while(pd.pos()<bytes.length){
			deserializeAbstractSendable(m,bytes,pd);
		}
		return m;
	}
	
	private void deserializeAbstractSendable(Message m, byte[] bytes, ArrayPointer pd) {
		//TODO !!!!!!!!!!!!!!!!
		Byte b = BinaryConverter.readByteFromBytes(bytes, pd);
		if(classByte[b] == NewSendable.class){
			deserializeSendable(m, bytes, pd);
		} else if(classByte[b] == ListNode.class){
			deserializeListNode(m, bytes, pd);
		} else if(classByte[b] == DataMap.class){
			deserializeDataMap(m, bytes, pd);
		} else if(classByte[b] == Value.class){
			//deserializeValue( ??? , bytes, pd);
		}
	}

	
	private void serialzeAbstractSendable(AbstractSendable<?> s, byte[] buf, ArrayPointer ps) {
		init();
		if (s.getClass() == NewSendable.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(NewSendable.class), buf, ps);
			NewSendable ns = (NewSendable) s;
			serialzeSendable(ns, buf, ps);
		} else if (s.getClass() == ListNode.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(ListNode.class), buf, ps);
			serializeListNode((ListNode<? extends AbstractSendable<?>>) s, buf, ps);
		} else if (s.getClass() == DataMap.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(DataMap.class), buf, ps);
			serializeDataMap((DataMap) s, buf, ps);
		} else if (s.getClass() == Value.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Value.class), buf, ps);
			serializeValue((Value<?>) s, buf, ps);
		} else {
			throw new SendableSerializationException(s.getClass().getCanonicalName()
					+ " can not be Serialized by BinaryObjectSerializer!");
		}
	}
	
	private void deserializeSendable(Message m, byte[] bytes, ArrayPointer pd) {
		// TODO Auto-generated method stub
		
	}
	
	private void serialzeSendable(NewSendable ns, byte[] buf, ArrayPointer ps) {
		init();
		if (ns.data.getClass() == ListNode.class) {
			serializeListNode((ListNode<? extends AbstractSendable<?>>) ns.data, buf, ps);
		} else if (ns.data.getClass() == DataMap.class) {
			serializeDataMap((DataMap) ns.data, buf, ps);
		} else if (ns.data.getClass() == Value.class) {
			serializeValue((Value<?>) ns.data, buf, ps);
		}
	}

	private void serializeValue(Value<?> data, byte[] buf, ArrayPointer ps) {
		init();
		if (data.get().getClass() == Integer.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Integer.class), buf, ps);
			BinaryConverter.writeIntToBytes((Integer) data.get(), buf, ps);
		} else if (data.get().getClass() == Float.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Float.class), buf, ps);
			BinaryConverter.writeFloatToBytes((Float) data.get(), buf, ps);
		} else if (data.get().getClass() == Long.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Long.class), buf, ps);
			BinaryConverter.writeLongToBytes((Long) data.get(), buf, ps);
		} else if (data.get().getClass() == Boolean.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Boolean.class), buf, ps);
			BinaryConverter.writeBooleanToBytes((Boolean) data.get(), buf, ps);
		} else if (data.get().getClass() == String.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(String.class), buf, ps);
			BinaryConverter.writeStringToBytes((String) data.get(), buf, ps);
		} else {
			throw new SendableSerializationException(data.get().getClass().getCanonicalName()
					+ " can not be Serialized by BinaryObjectSerializer!");
		}
	}

	private Value deserializeValue(AbstractSendable<?> putHere, byte[] buf, ArrayPointer p) {
		// TODO doesn't do anything right now: where should I put the read
		// primitives?

		Class<?> valueClass = classByte[BinaryConverter.readByteFromBytes(buf, p)];
		if (valueClass == Integer.class) {
			BinaryConverter.readIntFromBytes(buf, p);
		}
		if (valueClass == Long.class) {
			BinaryConverter.readLongFromBytes(buf, p);
		}
		if (valueClass == Float.class) {
			BinaryConverter.readFloatFromBytes(buf, p);
		}
		if (valueClass == Byte.class) {
			BinaryConverter.readByteFromBytes(buf, p);
		}
		if (valueClass == Boolean.class) {
			BinaryConverter.readBooleanFromBytes(buf, p);
		}
		if (valueClass == String.class) {
			BinaryConverter.readStringFromBytes(buf, p);
		}
		return null;
	}
	
	private void deserializeDataMap(Message m, byte[] bytes, ArrayPointer pd) {
		// TODO Auto-generated method stub
		
	}

	private void serializeDataMap(DataMap data, byte[] buf2, ArrayPointer ps2) {
		init();
		//write header
		BinaryConverter.writeByteToBytes(classByteLookup.get(DataMap.class), buf, ps2);
		for (MapEntry<?> me : data) {
			// TODO describe each byte stream with a little header
			BinaryConverter.writeStringToBytes(me.key(), buf2, ps2);
			serialzeAbstractSendable(me.value(), buf2, ps2);
		}
	}
	
	private void deserializeListNode(Message m, byte[] bytes, ArrayPointer pd) {
		// TODO Auto-generated method stub
		
	}

	private void serializeListNode(ListNode<? extends AbstractSendable<?>> l, byte[] buf, ArrayPointer p) {
		init();
		//write header
		BinaryConverter.writeByteToBytes(classByteLookup.get(ListNode.class), buf, p);
		l.resetIterator();
		for (AbstractSendable<?> as : l) {
			serialzeAbstractSendable(as, buf, p);
		}
	}



}
