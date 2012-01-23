package net.gtamps.shared.serializer.communication;

import java.util.HashMap;

import net.gtamps.shared.serializer.communication.data.AbstractSendableData;
import net.gtamps.shared.serializer.communication.data.DataMap;
import net.gtamps.shared.serializer.communication.data.ListNode;
import net.gtamps.shared.serializer.communication.data.MapEntry;
import net.gtamps.shared.serializer.communication.data.Value;
import net.gtamps.shared.serializer.helper.ArrayPointer;
import net.gtamps.shared.serializer.helper.BinaryConverter;
import net.gtamps.shared.serializer.helper.SerializedMessage;

/**
 * This class
 * 
 * @author tom
 * 
 */
public class BinaryObjectSerializer implements ISerializer {

	private final int BUFFER_SIZE = 655360;
	private final byte[] buf = new byte[BUFFER_SIZE];

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
			Value.class, // 10
			Byte.class, // 11
			DataMapEOF.class, // 12
			ListNodeEOF.class, // 13
			ListNodeHeader.class, // 14
			MapEntry.class, // 15
			Null.class, // 16
			ListNode.EmptyListNode.class,
			Const.class
	};

	private HashMap<Class<?>, Byte> classByteLookup = null;


	public BinaryObjectSerializer() {
		init();
	}

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

	public void clearBuffer() {
		int i;
		for (i = 0; i < buf.length; i++) {
			buf[i] = 0;
		}
	}

	@Override
	public SerializedMessage serializeAndPackNewMessage(final NewMessage m) {
		clearBuffer();
		final ArrayPointer ps = new ArrayPointer();
		ps.set(0);

		serializeListNode(m.sendables, buf, ps);

		BinaryConverter.writeStringToBytes(m.getSessionId(), buf, ps);
		//		Logger.e(this, "Actual Message size "+ps.pos()+" Bytes.");

		serializedMessage.message = buf;
		serializedMessage.length = ps.pos();

		return serializedMessage;
	}

	@Override
	public byte[] serializeNewMessage(final NewMessage m) {
		return serializeAndPackNewMessage(m).message;
	}

	@Override
	public NewMessage deserializeNewMessage(final byte[] bytes) {
		final ArrayPointer pd = new ArrayPointer();
		pd.set(0);
		final NewMessage m = new NewMessage();

		m.sendables = (ListNode<NewSendable>) deserializeListNode(bytes, pd);

		m.setSessionId(BinaryConverter.readStringFromBytes(bytes, pd));

		return m;
	}

	protected AbstractSendable deserializeAbstractSendable(final byte[] bytes, final ArrayPointer pd) {
		final Byte b = BinaryConverter.readByteFromBytes(bytes, pd);
		if (classByte[b] == NewSendable.class) {
			return deserializeSendable(bytes, pd);
		} else if (classByte[b] == ListNode.class) {
			return deserializeListNode(bytes, pd);
		} else if (classByte[b] == DataMap.class) {
			return deserializeDataMap(bytes, pd);
		} else if (classByte[b] == Value.class) {
			return deserializeValue(bytes, pd);
		} else if (classByte[b] == ListNode.EmptyListNode.class) {
			return ListNode.emptyList();
		} else {
			throw new SendableSerializationException("Can't resolve to a valid class with byte header " + b
					+ " in this context!");
		}
	}

	protected void serialzeAbstractSendable(final AbstractSendable<?> s, final byte[] bytes, final ArrayPointer ps) {
		if (s.getClass() == NewSendable.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(NewSendable.class), bytes, ps);
			final NewSendable ns = (NewSendable) s;
			serialzeSendable(ns, bytes, ps);
		} else if (s.getClass() == ListNode.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(ListNode.class), bytes, ps);
			serializeListNode((ListNode<? extends AbstractSendable<?>>) s, bytes, ps);
		} else if (s.getClass() == ListNode.EmptyListNode.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(ListNode.EmptyListNode.class), bytes, ps);
		} else if (s.getClass() == DataMap.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(DataMap.class), bytes, ps);
			serializeDataMap((DataMap) s, bytes, ps);
		} else if (s.getClass() == Value.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Value.class), bytes, ps);
			serializeValue((Value<?>) s, bytes, ps);
		} else {
			throw new SendableSerializationException(s.getClass().getCanonicalName()
					+ " can not be Serialized by BinaryObjectSerializer!");
		}
	}

	protected NewSendable deserializeSendable(final byte[] bytes, final ArrayPointer pd) {
		// read sendable header
		Byte b = BinaryConverter.readByteFromBytes(bytes, pd);
		// TODO Enum should be send in a more intelligent manner...
		final NewSendable ns = new NewSendable(SendableType.valueOf(BinaryConverter.readStringFromBytes(bytes, pd)));
		// read session id
		ns.sessionId = BinaryConverter.readStringFromBytes(bytes, pd);
		// read sendable id
		ns.id = BinaryConverter.readIntFromBytes(bytes, pd);
		// read data class header
		b = BinaryConverter.readByteFromBytes(bytes, pd);
		if (classByte[b] == Null.class) {
			ns.data = null;
			return ns;
		} else if (classByte[b] == ListNode.class) {
			ns.data = deserializeListNode(bytes, pd);
			return ns;
		} else if (classByte[b] == DataMap.class) {
			ns.data = deserializeDataMap(bytes, pd);
			return ns;
		} else if (classByte[b] == Value.class) {
			ns.data = deserializeValue(bytes, pd);
			return ns;
		} else {
			throw new SendableSerializationException("Can't deserialize this mess. (the bad byte was " + b + ")");
		}
	}

	protected void serialzeSendable(final NewSendable ns, final byte[] bytes, final ArrayPointer ps) {
		// write sendable header
		BinaryConverter.writeByteToBytes(classByteLookup.get(NewSendable.class), bytes, ps);
		// TODO Enum should be send in a more intelligent manner...
		BinaryConverter.writeStringToBytes(ns.type.name().toString(), bytes, ps);
		// write session id
		BinaryConverter.writeStringToBytes(ns.sessionId, bytes, ps);
		// write sendable id
		BinaryConverter.writeIntToBytes(ns.id, bytes, ps);
		if(ns.data == null){
			BinaryConverter.writeByteToBytes(classByteLookup.get(Null.class), bytes, ps);
		} else if (ns.data.getClass() == ListNode.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(ListNode.class), bytes, ps);
			serializeListNode((ListNode<? extends AbstractSendable<?>>) ns.data, bytes, ps);
		} else if (ns.data.getClass() == DataMap.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(DataMap.class), bytes, ps);
			serializeDataMap((DataMap) ns.data, bytes, ps);
		} else if (ns.data.getClass() == Value.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Value.class), bytes, ps);
			serializeValue((Value<?>) ns.data, bytes, ps);
		}
	}

	//preallocate
	byte translated = -1;
	protected void serializeValue(final Value<?> data, final byte[] bytes, final ArrayPointer ps) {
		if (data.get() == null) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Null.class), bytes, ps);
		} else if (data.get().getClass() == Integer.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Integer.class), bytes, ps);
			BinaryConverter.writeIntToBytes((Integer) data.get(), bytes, ps);
		} else if (data.get().getClass() == Float.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Float.class), bytes, ps);
			BinaryConverter.writeFloatToBytes((Float) data.get(), bytes, ps);
		} else if (data.get().getClass() == Long.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Long.class), bytes, ps);
			BinaryConverter.writeLongToBytes((Long) data.get(), bytes, ps);
		} else if (data.get().getClass() == Boolean.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Boolean.class), bytes, ps);
			BinaryConverter.writeBooleanToBytes((Boolean) data.get(), bytes, ps);
		} else if (data.get().getClass() == String.class) {
			translated = Translator.lookup((String) data.get());
			if(translated>-1){
				BinaryConverter.writeByteToBytes(classByteLookup.get(Const.class), bytes, ps);
				BinaryConverter.writeByteToBytes(translated, bytes, ps);
			} else {
				BinaryConverter.writeByteToBytes(classByteLookup.get(String.class), bytes, ps);
				BinaryConverter.writeStringToBytes((String) data.get(), bytes, ps);
			}
		} else if (data.get().getClass() == Byte.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Byte.class), bytes, ps);
			BinaryConverter.writeByteToBytes((Byte) data.get(), bytes, ps);
		} else {
			throw new SendableSerializationException(data.get().getClass().getCanonicalName()
					+ " can not be Serialized by BinaryObjectSerializer!");
		}
	}

	protected Value<?> deserializeValue(final byte[] bytes, final ArrayPointer p) {

		// FIXME do not allocate new Values!

		final Class<?> valueClass = classByte[BinaryConverter.readByteFromBytes(bytes, p)];
		if(valueClass == Null.class){
			final Value<Object> v = new Value<Object>();
			v.set(null);
			return v;
		} else if (valueClass == Integer.class) {
			final Value<Integer> v = new Value<Integer>();
			v.set(BinaryConverter.readIntFromBytes(bytes, p));
			return v;
		} else if (valueClass == Long.class) {
			final Value<Long> v = new Value<Long>();
			v.set(BinaryConverter.readLongFromBytes(bytes, p));
			return v;
		} else if (valueClass == Float.class) {
			final Value<Float> v = new Value<Float>();
			v.set(BinaryConverter.readFloatFromBytes(bytes, p));
			return v;
		} else if (valueClass == Byte.class) {
			final Value<Byte> v = new Value<Byte>();
			v.set(BinaryConverter.readByteFromBytes(bytes, p));
			return v;
		} else if (valueClass == Boolean.class) {
			final Value<Boolean> v = new Value<Boolean>();
			v.set(BinaryConverter.readBooleanFromBytes(bytes, p));
			return v;
		} else if (valueClass == String.class) {
			final Value<String> v = new Value<String>();
			v.set(BinaryConverter.readStringFromBytes(bytes, p));
			return v;
		} else if (valueClass == Const.class) {
			final Value<String> v = new Value<String>();
			v.set(Translator.lookup(BinaryConverter.readByteFromBytes(bytes, p)));
			return v;
		} else {
			throw new SendableSerializationException(valueClass + " cant be deserialized!");
		}
	}

	protected DataMap deserializeDataMap(final byte[] bytes, final ArrayPointer pd) {

		// read data map header
		final Byte dmhead = BinaryConverter.readByteFromBytes(bytes, pd);

		final DataMap dm = new DataMap();
		Byte b = BinaryConverter.readByteFromBytes(bytes, pd);
		while (classByte[b] == MapEntry.class) {
			final MapEntry<AbstractSendableData<?>> me = new MapEntry<AbstractSendableData<?>>();
			me.setKey(BinaryConverter.readStringFromBytes(bytes, pd));
			me.setValue((AbstractSendableData<?>) deserializeAbstractSendable(bytes, pd));
			dm.add(me);
			b = BinaryConverter.readByteFromBytes(bytes, pd);
		}
		if (classByte[b] != DataMapEOF.class) {
			throw new SendableSerializationException("DataMap ended unexpectedly with byte " + b + " at position "
					+ pd.pos());
		}
		return dm;
	}

	protected void serializeDataMap(final DataMap data, final byte[] bytes, final ArrayPointer ps2) {
		// write data map header
		BinaryConverter.writeByteToBytes(classByteLookup.get(DataMap.class), bytes, ps2);

		// FIXME remove iterator: use counter instead to avoid Iterator
		// allocations
		for (final MapEntry<? extends AbstractSendableData<?>> me : data) {
			// write header per mapEntry
			BinaryConverter.writeByteToBytes(classByteLookup.get(MapEntry.class), bytes, ps2);
			BinaryConverter.writeStringToBytes(me.key(), bytes, ps2);
			serialzeAbstractSendable(me.value(), bytes, ps2);
		}
		BinaryConverter.writeByteToBytes(classByteLookup.get(DataMapEOF.class), bytes, ps2);
	}

	protected ListNode<? extends AbstractSendable<?>> deserializeListNode(final byte[] bytes, final ArrayPointer pd) {
		// TODO avoid allocations
		Byte b = BinaryConverter.readByteFromBytes(bytes, pd);
		// System.out.println(classByte[b]);
		final Byte classB = BinaryConverter.readByteFromBytes(bytes, pd);
		// System.out.println(classByte[classB]);

		final SendableCacheFactory scf = new SendableCacheFactory();
		final SendableProvider sp = new SendableProvider(scf);

		// read first header
		b = BinaryConverter.readByteFromBytes(bytes, pd);
		ListNode rootNode = sp.getListNode(deserializeAbstractSendable(bytes, pd));

		b = BinaryConverter.readByteFromBytes(bytes, pd);
		while (classByte[b] == ListNodeHeader.class) {
			rootNode = rootNode.append(sp.getListNode(deserializeAbstractSendable(bytes, pd)));
			b = BinaryConverter.readByteFromBytes(bytes, pd);
		}

		if (classByte[b] != ListNodeEOF.class) {
			throw new SendableSerializationException("ListNode ended unexpectedly with byte " + b
					+ "! That's disgusting.");
		}
		return rootNode;
	}

	protected void serializeListNode(final ListNode<? extends AbstractSendable<?>> l, final byte[] bytes,
			final ArrayPointer p) {
		// write header
		BinaryConverter.writeByteToBytes(classByteLookup.get(ListNode.class), bytes, p);
		BinaryConverter.writeByteToBytes(classByteLookup.get(l.value().getClass()), bytes, p);

		l.resetIterator();
		for (final AbstractSendable<?> as : l) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(ListNodeHeader.class), bytes, p);
			serialzeAbstractSendable(as, bytes, p);
		}
		BinaryConverter.writeByteToBytes(classByteLookup.get(ListNodeEOF.class), bytes, p);
	}

	public class DataMapEOF {
		// Fake class for Binary Object Serializer
	}

	public class ListNodeEOF {
		// Fake class for serialization
	}

	public class ListNodeHeader {

	}

	public class Null{

	}
	
	public class Const{
		
	}

}
