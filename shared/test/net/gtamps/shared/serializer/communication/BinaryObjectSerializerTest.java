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
	public void testSerializeMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeserializeMessage() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
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
		System.out.println("Write Pointer at: "+ps.pos());
		System.out.println("Read Pointer at: "+pd.pos());
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
	}

	@Test
	public void testSerializeListNode() {
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
		
		/*ListNode<Value<Float>> l2 = new ListNode<Value<Float>>();
		Value<Float> v2 = new Value<Float>();
		v2.set(56789f);
		l2.set(v2);
		
		listNode.append(l2);
		*/
		bos.serializeListNode(listNode, buf, ps);
		
		//jump over header
		//pd.set(1);
		System.out.println("Write Pointer at: "+ps.pos());
		
		assertEquals(listNode, bos.deserializeListNode(buf, pd));
		System.out.println("Read Pointer at: "+pd.pos());
	}

}
