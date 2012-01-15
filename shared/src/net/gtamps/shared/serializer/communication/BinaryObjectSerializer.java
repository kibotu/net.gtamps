package net.gtamps.shared.serializer.communication;

import java.util.HashMap;

import net.gtamps.shared.serializer.communication.data.AbstractSendableData;
import net.gtamps.shared.serializer.communication.data.DataMap;
import net.gtamps.shared.serializer.communication.data.ListNode;
import net.gtamps.shared.serializer.communication.data.MapEntry;
import net.gtamps.shared.serializer.communication.data.Value;
import net.gtamps.shared.serializer.helper.ArrayPointer;
import net.gtamps.shared.serializer.helper.BinaryConverter;

/**
 * This class 
 * @author tom
 *
 */
public class BinaryObjectSerializer implements ISerializer {

	private final int BUFFER_SIZE = 2048;
	private byte[] buf = new byte[BUFFER_SIZE];

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
			DataMap.EOF.class, //12
			ListNode.EOF.class, //13
			ListNode.Header.class //14
	};

	private HashMap<Class<?>, Byte> classByteLookup = null;

	public BinaryObjectSerializer(){
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

	//TODO
	// remove me, I'm just mocking the way.
	ListNode<NewSendable> mockList = new ListNode<NewSendable>();

	public byte[] serializeMessage(final NewMessage m) {
		ArrayPointer ps = new ArrayPointer();
		
		BinaryConverter.writeStringToBytes(m.getSessionId(), buf, ps);
		
		serializeListNode(m.sendables, buf, ps);
		
		return buf;
	}

	public NewMessage deserializeNewMessage(final byte[] bytes) {
		ArrayPointer pd = new ArrayPointer();																												
		final NewMessage m = new NewMessage();
		
		BinaryConverter.readIntFromBytes(bytes,pd);
		m.setSessionId(BinaryConverter.readStringFromBytes(bytes, pd));
		
		m.sendables = (ListNode<NewSendable>) deserializeListNode( bytes, pd);
		return m;
	}
	
	protected AbstractSendable deserializeAbstractSendable(byte[] bytes, ArrayPointer pd) {
		Byte b = BinaryConverter.readByteFromBytes(bytes, pd);
		if(classByte[b] == NewSendable.class){
			return deserializeSendable( bytes, pd);
		} else if(classByte[b] == ListNode.class){
			return deserializeListNode( bytes, pd);
		} else if(classByte[b] == DataMap.class){
			return deserializeDataMap( bytes, pd);
		} else if(classByte[b] == Value.class){
			return deserializeValue( bytes, pd);
		} else {
			throw new SendableSerializationException("Can't resolve to a valid class with byte header "+b+" in this context!");
		}
	}

	
	protected void serialzeAbstractSendable(AbstractSendable<?> s, byte[] buf, ArrayPointer ps) {
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
	
	protected NewSendable deserializeSendable( byte[] bytes, ArrayPointer pd) {
		Byte b = BinaryConverter.readByteFromBytes(bytes, pd);
		
		String typeStr = BinaryConverter.readStringFromBytes(bytes, pd);
		System.out.println(typeStr);
		//TODO Enum should be send in a more intelligent manner...
		SendableType st = SendableType.valueOf(typeStr);
		
		NewSendable ns = new NewSendable(st);
		if(classByte[b]==ListNode.class){
			ns.data = deserializeListNode(bytes, pd);
			return ns;
		} else if(classByte[b]==DataMap.class){
			ns.data = deserializeDataMap(bytes, pd);
			return ns;
		} else if(classByte[b]==Value.class){
			ns.data = deserializeValue(bytes, pd);
			return ns;
		} else {
			throw new SendableSerializationException("Can't deserialize this mess. (the bad byte was "+b+")");
		}
	}
	
	protected void serialzeSendable(NewSendable ns, byte[] buf, ArrayPointer ps) {
		BinaryConverter.writeByteToBytes(classByteLookup.get(NewSendable.class), buf, ps);

		System.out.println("serializing: "+ns.type.name());
		//TODO Enum should be send in a more intelligent manner...
		BinaryConverter.writeStringToBytes(ns.type.name(), buf, ps);
		
		if (ns.data.getClass() == ListNode.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(ListNode.class), buf, ps);
			serializeListNode((ListNode<? extends AbstractSendable<?>>) ns.data, buf, ps);
		} else if (ns.data.getClass() == DataMap.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(DataMap.class), buf, ps);
			serializeDataMap((DataMap) ns.data, buf, ps);
		} else if (ns.data.getClass() == Value.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Value.class), buf, ps);
			serializeValue((Value<?>) ns.data, buf, ps);
		}
	}

	protected void serializeValue(Value<?> data, byte[] buf, ArrayPointer ps) {
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
		} else if (data.get().getClass() == Byte.class) {
			BinaryConverter.writeByteToBytes(classByteLookup.get(Byte.class), buf, ps);
			BinaryConverter.writeByteToBytes((Byte) data.get(), buf, ps);
		} else {
			throw new SendableSerializationException(data.get().getClass().getCanonicalName()
					+ " can not be Serialized by BinaryObjectSerializer!");
		}
	}

	protected Value<?> deserializeValue(byte[] buf, ArrayPointer p) {
		
		
		//FIXME do not allocate new Values!
		
		Class<?> valueClass = classByte[BinaryConverter.readByteFromBytes(buf, p)];
		if (valueClass == Integer.class) {
			Value<Integer> v = new Value<Integer>();
			v.set(BinaryConverter.readIntFromBytes(buf, p));
			return v;
		} else if (valueClass == Long.class) {
			Value<Long> v = new Value<Long>();
			v.set(BinaryConverter.readLongFromBytes(buf, p));
			return v;
		}else 
		if (valueClass == Float.class) {
			Value<Float> v = new Value<Float>();
			v.set(BinaryConverter.readFloatFromBytes(buf, p));
			return v;
		}else 
		if (valueClass == Byte.class) {
			Value<Byte> v = new Value<Byte>();
			v.set(BinaryConverter.readByteFromBytes(buf, p));
			return v;
		}else 
		if (valueClass == Boolean.class) {
			Value<Boolean> v = new Value<Boolean>();
			v.set(BinaryConverter.readBooleanFromBytes(buf, p));
			return v;
		}else 
		if (valueClass == String.class) {
			Value<String> v = new Value<String>();
			v.set(BinaryConverter.readStringFromBytes(buf, p));
			return v;
		}else {
			throw new SendableSerializationException(valueClass+" cant be deserialized!");
		}
	}
	
	protected DataMap deserializeDataMap(byte[] bytes, ArrayPointer pd) {
		DataMap dm = new DataMap();
		Byte b = BinaryConverter.readByteFromBytes(bytes, pd);
		if(classByte[b] == MapEntry.class){
			MapEntry<AbstractSendableData<?>> me = new MapEntry<AbstractSendableData<?>>();
			me.setKey(BinaryConverter.readStringFromBytes(bytes, pd));
			me.setValue((AbstractSendableData<?>)deserializeAbstractSendable(bytes, pd));
		} else if(classByte[b] != DataMap.EOF.class){
			throw new SendableSerializationException("DataMap ended unexpectedly with byte "+b+" at position "+pd.pos());
		}
		return dm;
	}

	protected void serializeDataMap(DataMap data, byte[] buf2, ArrayPointer ps2) {
		//write header
		BinaryConverter.writeByteToBytes(classByteLookup.get(DataMap.class), buf, ps2);
		
		//FIXME remove iterator: use counter instead to avoid Iterator allocations
		
		for (MapEntry<? extends AbstractSendableData<?>> me : data) {
			//write header per mapEntry
			BinaryConverter.writeByteToBytes(classByteLookup.get(MapEntry.class), buf, ps2);
			BinaryConverter.writeStringToBytes(me.key(), buf2, ps2);
			serialzeAbstractSendable(me.value(), buf2, ps2);
		}
		BinaryConverter.writeByteToBytes(classByteLookup.get(DataMap.EOF.class), buf2, ps2);
	}
	
	protected ListNode<? extends AbstractSendable<?>> deserializeListNode(byte[] bytes, ArrayPointer pd) {
		//TODO avoid allocations
		Byte b = BinaryConverter.readByteFromBytes(bytes, pd);
//		System.out.println(classByte[b]);
		Byte classB = BinaryConverter.readByteFromBytes(bytes, pd);
//		System.out.println(classByte[classB]);
		
		SendableCacheFactory scf = new SendableCacheFactory();
		SendableProvider sp = new SendableProvider(scf);
		
		
		//read first header
		b = BinaryConverter.readByteFromBytes(bytes, pd);
		ListNode rootNode = sp.getListNode(deserializeAbstractSendable(bytes, pd));
		
		b = BinaryConverter.readByteFromBytes(bytes, pd);
		while (classByte[b] == ListNode.Header.class){		
			System.out.println(b);
			rootNode.append(sp.getListNode(deserializeAbstractSendable(bytes,pd)));
			b = BinaryConverter.readByteFromBytes(bytes, pd);
		}
		
		if(classByte[b] != ListNode.EOF.class){
			throw new SendableSerializationException("ListNode ended unexpectedly with byte "+b+"! That's disgusting.");
		}
		return rootNode;
	}
	
	protected void serializeListNode(ListNode<? extends AbstractSendable<?>> l, byte[] buf, ArrayPointer p) {
		//write header
		BinaryConverter.writeByteToBytes(classByteLookup.get(ListNode.class), buf, p);
		BinaryConverter.writeByteToBytes(classByteLookup.get(l.value().getClass()), buf, p);
		
		
		l.resetIterator();
		for (AbstractSendable<?> as : l) {
			System.out.println(as.getClass().getName());
			BinaryConverter.writeByteToBytes(classByteLookup.get(ListNode.Header.class), buf, p);
			serialzeAbstractSendable(as, buf, p);
		}
		BinaryConverter.writeByteToBytes(classByteLookup.get(ListNode.EOF.class), buf, p);
	}

	@Override
	public byte[] serializeMessage(Message m) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message deserializeMessage(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}



}
