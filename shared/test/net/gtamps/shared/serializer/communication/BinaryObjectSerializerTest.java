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
		final BinaryObjectSerializer bos = new BinaryObjectSerializer();

		final NewMessage nm = new NewMessage();

		final NewSendable ns = new NewSendable(SendableType.ACTION_ACCELERATE);
		ns.sessionId = "qwertzui";
		final DataMap dm = new DataMap();
		final MapEntry<AbstractSendableData<?>> me1 = new MapEntry<AbstractSendableData<?>>();
		final Value<Float> v = new Value<Float>();
		v.set(23456f);
		me1.set("zahl1", v);
		dm.add(me1);
		final MapEntry<AbstractSendableData<?>> me2 = new MapEntry<AbstractSendableData<?>>();
		final Value<String> str = new Value<String>();
		str.set("bu bu ba!");
		me2.set("string1", str);
		dm.add(me2);
		ns.data = dm;

		final NewSendable ns2 = new NewSendable(SendableType.GETMAPDATA);
		ns2.sessionId = "qwertzuiasdasd";
		final DataMap dm2 = new DataMap();
		final MapEntry<AbstractSendableData<?>> me21 = new MapEntry<AbstractSendableData<?>>();
		final Value<Float> v2 = new Value<Float>();
		v2.set(23456f);
		me21.set("zahl12", v2);
		dm2.add(me21);
		final MapEntry<AbstractSendableData<?>> me22 = new MapEntry<AbstractSendableData<?>>();
		final Value<String> str2 = new Value<String>();
		str2.set("bu bu ba!");
		me22.set("string1", str2);
		dm2.add(me22);
		ns2.data = dm2;

		ListNode<NewSendable> ln = new ListNode<NewSendable>();
		ln.set(ns);
		final ListNode<NewSendable> ln2 = new ListNode<NewSendable>();
		ln2.set(ns2);
		ln = ln.append(ln2);

		nm.sendables = ln;
		nm.setSessionId("MY MESSAGE SESSION ID");

		final byte[] serializedmessage = bos.serializeNewMessage(nm);
		assertEquals(nm, bos.deserializeNewMessage(serializedmessage));
	}

	@Test
	public void testSerialzeAbstractSendable() {
		final Float f = 123.23423432f;
		final Value<Float> vf = new Value<Float>();
		vf.set(f);

		final BinaryObjectSerializer bos = new BinaryObjectSerializer();
		final byte[] buf = new byte[1024];

		final ArrayPointer ps = new ArrayPointer();
		final ArrayPointer pd = new ArrayPointer();

		bos.serialzeAbstractSendable(vf, buf, ps);
		assertEquals(vf, bos.deserializeAbstractSendable(buf, pd));

	}

	@Test
	public void testSerialzeSendable() {
		final BinaryObjectSerializer bos = new BinaryObjectSerializer();
		final byte[] buf = new byte[1024];

		final ArrayPointer ps = new ArrayPointer();
		final ArrayPointer pd = new ArrayPointer();

		final NewSendable ns = new NewSendable(SendableType.ACTION_ACCELERATE);
		ns.sessionId = "qwertzui";

		final DataMap dm = new DataMap();
		final MapEntry<AbstractSendableData<?>> me1 = new MapEntry<AbstractSendableData<?>>();
		final Value<Float> v = new Value<Float>();
		v.set(23456f);
		me1.set("zahl1", v);
		dm.add(me1);
		final MapEntry<AbstractSendableData<?>> me2 = new MapEntry<AbstractSendableData<?>>();
		final Value<String> str = new Value<String>();
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
		final LinkedList<Value<?>> testValues = new LinkedList<Value<?>>();

		final Float f = 123.23423432f;
		final Value<Float> vf = new Value<Float>();
		vf.set(f);
		testValues.add(vf);

		final Long l = Long.MAX_VALUE;
		final Value<Long> vl = new Value<Long>();
		vl.set(l);
		testValues.add(vl);

		final Integer i = Integer.MIN_VALUE;
		final Value<Integer> vi = new Value<Integer>();
		vi.set(i);
		testValues.add(vi);

		final Byte b = 'F';
		final Value<Byte> vb = new Value<Byte>();
		vb.set(b);
		testValues.add(vb);

		final String s = "Random String";
		final Value<String> vs = new Value<String>();
		vs.set(s);
		testValues.add(vs);

		final Boolean bo = false;
		final Value<Boolean> vbo = new Value<Boolean>();
		vbo.set(bo);
		testValues.add(vbo);

		// init
		final BinaryObjectSerializer bos = new BinaryObjectSerializer();
		final byte[] buf = new byte[1024];

		final ArrayPointer ps = new ArrayPointer();
		final ArrayPointer pd = new ArrayPointer();

		for (final Value<?> tv : testValues) {
			// serialize
			bos.serializeValue(tv, buf, ps);

			// deserialize
			final Value<?> out = bos.deserializeValue(buf, pd);

			assertEquals(out, tv);
		}
		assertEquals(ps.pos(),pd.pos());
	}

	@Test
	public void testSerializeDataMap() {
		final BinaryObjectSerializer bos = new BinaryObjectSerializer();
		final byte[] buf = new byte[1024];

		final ArrayPointer ps = new ArrayPointer();
		final ArrayPointer pd = new ArrayPointer();

		final DataMap dm = new DataMap();

		final MapEntry<AbstractSendableData<?>> me1 = new MapEntry<AbstractSendableData<?>>();
		final Value<Float> v = new Value<Float>();
		v.set(23456f);
		me1.set("zahl1", v);
		dm.add(me1);

		final MapEntry<AbstractSendableData<?>> me2 = new MapEntry<AbstractSendableData<?>>();
		final Value<String> str = new Value<String>();
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
		final BinaryObjectSerializer bos = new BinaryObjectSerializer();
		final byte[] buf = new byte[1024];

		final ArrayPointer ps = new ArrayPointer();
		final ArrayPointer pd = new ArrayPointer();

		//create list nodes
		ListNode<Value<Float>> listNode = new ListNode<Value<Float>>();
		final Value<Float> v = new Value<Float>();
		v.set(23456f);
		listNode.set(v);

		final ListNode<Value<Float>> l2 = new ListNode<Value<Float>>();
		final Value<Float> v2 = new Value<Float>();
		v2.set(56789f);
		l2.set(v2);

		listNode = listNode.append(l2);

		bos.serializeListNode(listNode, buf, ps);

		//check if serialization was successful
		assertEquals(listNode, bos.deserializeListNode(buf, pd));
		//check if pointers have wrote and read the same amount of bytes
		assertEquals(ps.pos(), pd.pos());
	}

	public void testSerializeListNodeStrings() {
		// init
		final BinaryObjectSerializer bos = new BinaryObjectSerializer();
		final byte[] buf = new byte[1024];

		final ArrayPointer ps = new ArrayPointer();
		final ArrayPointer pd = new ArrayPointer();

		//create list nodes
		ListNode<Value<String>> listNode = new ListNode<Value<String>>();
		final Value<String> v = new Value<String>();
		v.set("erster string");
		listNode.set(v);

		final ListNode<Value<String>> l2 = new ListNode<Value<String>>();
		final Value<String> v2 = new Value<String>();
		v2.set("zweiter string");
		l2.set(v2);

		listNode = listNode.append(l2);

		bos.serializeListNode(listNode, buf, ps);

		assertEquals(listNode, bos.deserializeListNode(buf, pd));
		//check if pointers have wrote and read the same amount of bytes
		assertEquals(ps.pos(), pd.pos());
	}

	private void writeBufferToStOut(final byte[] bytes, final int bytecount){
		for(int i=0; i<bytecount; i++){
			System.out.print(bytes[i]+" ");
		}
		System.out.println("");
	}

}
