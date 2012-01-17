package net.gtamps.shared.serializer.communication;

import static org.junit.Assert.*;

import java.util.LinkedList;

import net.gtamps.shared.serializer.communication.data.AbstractSendableData;
import net.gtamps.shared.serializer.communication.data.DataMap;
import net.gtamps.shared.serializer.communication.data.ListNode;
import net.gtamps.shared.serializer.communication.data.MapEntry;
import net.gtamps.shared.serializer.communication.data.Value;
import net.gtamps.shared.serializer.helper.ArrayPointer;

import org.junit.Test;

public class BinaryObjectSerializerTest {

	@Test
	public void testSerializeNewMessage() {
		BinaryObjectSerializer bos = new BinaryObjectSerializer();
		
		NewMessage nm = new NewMessage();
		
		NewSendable ns = new NewSendable(SendableType.ACTION_ACCELERATE);
		ns.sessionId = "qwertzui";
		DataMap dm = new DataMap();
		MapEntry<AbstractSendableData<?>> me1 = new MapEntry<AbstractSendableData<?>>();
		Value<Float> v = new Value<Float>();
		v.set(23456f);
		me1.set("zahl1", v);
		dm.add(me1);
		MapEntry<AbstractSendableData<?>> me2 = new MapEntry<AbstractSendableData<?>>();
		Value<String> str = new Value<String>();
		str.set("bu bu ba!");
		me2.set("string1", str);
		dm.add(me2);
		ns.data = dm;
		
		NewSendable ns2 = new NewSendable(SendableType.GETMAPDATA);
		ns2.sessionId = "qwertzuiasdasd";
		DataMap dm2 = new DataMap();
		MapEntry<AbstractSendableData<?>> me21 = new MapEntry<AbstractSendableData<?>>();
		Value<Float> v2 = new Value<Float>();
		v2.set(23456f);
		me21.set("zahl12", v2);
		dm2.add(me21);
		MapEntry<AbstractSendableData<?>> me22 = new MapEntry<AbstractSendableData<?>>();
		Value<String> str2 = new Value<String>();
		str2.set("bu bu ba!");
		me22.set("string1", str2);
		dm2.add(me22);
		ns2.data = dm2;
		
		ListNode<NewSendable> ln = new ListNode<NewSendable>();
		ln.set(ns);
		ListNode<NewSendable> ln2 = new ListNode<NewSendable>();
		ln2.set(ns2);
		ln.append(ln2);
		
		nm.sendables = ln;
		nm.setSessionId("MY MESSAGE SESSION ID");
		
		byte[] serializedmessage = bos.serializeNewMessage(nm);
		assertEquals(nm, bos.deserializeNewMessage(serializedmessage));
	}

	@Test
	public void testSerialzeAbstractSendable() {
		Float f = 123.23423432f;
		Value<Float> vf = new Value<Float>();
		vf.set(f);
		
		BinaryObjectSerializer bos = new BinaryObjectSerializer();
		byte[] buf = new byte[1024];

		ArrayPointer ps = new ArrayPointer();
		ArrayPointer pd = new ArrayPointer();
		
		bos.serialzeAbstractSendable(vf, buf, ps);
		assertEquals(vf, bos.deserializeAbstractSendable(buf, pd));
		
	}

	@Test
	public void testSerialzeSendable() {
		BinaryObjectSerializer bos = new BinaryObjectSerializer();
		byte[] buf = new byte[1024];

		ArrayPointer ps = new ArrayPointer();
		ArrayPointer pd = new ArrayPointer();
		
		NewSendable ns = new NewSendable(SendableType.ACTION_ACCELERATE);
		ns.sessionId = "qwertzui";
		
		DataMap dm = new DataMap();
		MapEntry<AbstractSendableData<?>> me1 = new MapEntry<AbstractSendableData<?>>();
		Value<Float> v = new Value<Float>();
		v.set(23456f);
		me1.set("zahl1", v);
		dm.add(me1);
		MapEntry<AbstractSendableData<?>> me2 = new MapEntry<AbstractSendableData<?>>();
		Value<String> str = new Value<String>();
		str.set("bu bu ba!");
		me2.set("string1", str);
		dm.add(me2);
		
		ns.data = dm;
		
		bos.serialzeSendable(ns, buf, ps);
//		writeBufferToStOut(buf, ps.pos());
		assertEquals(ns, bos.deserializeSendable(buf, pd));
	}

	@Test
	public void testSerializeValue() {
		LinkedList<Value<?>> testValues = new LinkedList<Value<?>>();

		Float f = 123.23423432f;
		Value<Float> vf = new Value<Float>();
		vf.set(f);
		testValues.add(vf);
		
		Long l = Long.MAX_VALUE;
		Value<Long> vl = new Value<Long>();
		vl.set(l);
		testValues.add(vl);
		
		Integer i = Integer.MIN_VALUE;
		Value<Integer> vi = new Value<Integer>();
		vi.set(i);
		testValues.add(vi);

		Byte b = 'F';
		Value<Byte> vb = new Value<Byte>();
		vb.set(b);
		testValues.add(vb);
		
		String s = "Random String";
		Value<String> vs = new Value<String>();
		vs.set(s);
		testValues.add(vs);
		
		Boolean bo = false;
		Value<Boolean> vbo = new Value<Boolean>();
		vbo.set(bo);
		testValues.add(vbo);
		
		// init
		BinaryObjectSerializer bos = new BinaryObjectSerializer();
		byte[] buf = new byte[1024];

		ArrayPointer ps = new ArrayPointer();
		ArrayPointer pd = new ArrayPointer();
		
		for (Value<?> tv : testValues) {
			// serialize
			bos.serializeValue(tv, buf, ps);

			// deserialize
			Value<?> out = bos.deserializeValue(buf, pd);
			
			assertEquals(out, tv);
		}
		assertEquals(ps.pos(),pd.pos());
	}

	@Test
	public void testSerializeDataMap() {
		BinaryObjectSerializer bos = new BinaryObjectSerializer();
		byte[] buf = new byte[1024];

		ArrayPointer ps = new ArrayPointer();
		ArrayPointer pd = new ArrayPointer();

		DataMap dm = new DataMap();
		
		MapEntry<AbstractSendableData<?>> me1 = new MapEntry<AbstractSendableData<?>>();
		Value<Float> v = new Value<Float>();
		v.set(23456f);
		me1.set("zahl1", v);
		dm.add(me1);
		
		MapEntry<AbstractSendableData<?>> me2 = new MapEntry<AbstractSendableData<?>>();
		Value<String> str = new Value<String>();
		str.set("bu bu ba!");
		me2.set("string1", str);
		dm.add(me2);
		
		bos.serializeDataMap(dm, buf, ps);
		assertEquals(dm, bos.deserializeDataMap(buf, pd));
		assertEquals(ps.pos(), pd.pos());
	}

	@Test
	public void testSerializeListNode() {
		testSerializeListNodeFloats();
		testSerializeListNodeStrings();
	}
	
	public void testSerializeListNodeFloats() {
		// init
		BinaryObjectSerializer bos = new BinaryObjectSerializer();
		byte[] buf = new byte[1024];

		ArrayPointer ps = new ArrayPointer();
		ArrayPointer pd = new ArrayPointer();
		
		//create list nodes
		ListNode<Value<Float>> listNode = new ListNode<Value<Float>>();
		Value<Float> v = new Value<Float>();
		v.set(23456f);
		listNode.set(v);
		
		ListNode<Value<Float>> l2 = new ListNode<Value<Float>>();
		Value<Float> v2 = new Value<Float>();
		v2.set(56789f);
		l2.set(v2);
		
		listNode.append(l2);
		
		bos.serializeListNode(listNode, buf, ps);
		
		//check if serialization was successful
		assertEquals(listNode, bos.deserializeListNode(buf, pd));
		//check if pointers have wrote and read the same amount of bytes
		assertEquals(ps.pos(), pd.pos());
	}
	
	public void testSerializeListNodeStrings() {
		// init
		BinaryObjectSerializer bos = new BinaryObjectSerializer();
		byte[] buf = new byte[1024];

		ArrayPointer ps = new ArrayPointer();
		ArrayPointer pd = new ArrayPointer();
		
		//create list nodes
		ListNode<Value<String>> listNode = new ListNode<Value<String>>();
		Value<String> v = new Value<String>();
		v.set("erster string");
		listNode.set(v);
		
		ListNode<Value<String>> l2 = new ListNode<Value<String>>();
		Value<String> v2 = new Value<String>();
		v2.set("zweiter string");
		l2.set(v2);
		
		listNode.append(l2);
		
		bos.serializeListNode(listNode, buf, ps);
		
		assertEquals(listNode, bos.deserializeListNode(buf, pd));
		//check if pointers have wrote and read the same amount of bytes
		assertEquals(ps.pos(), pd.pos());
	}
	
	private void writeBufferToStOut(byte[] bytes, int bytecount){
		for(int i=0; i<bytecount; i++){
			System.out.print(bytes[i]+" ");
		}
		System.out.println("");
	}

}
