package net.gtamps.shared.serializer.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import net.gtamps.shared.serializer.communication.data.ListNode;

import org.junit.Test;

public class CompressedObjectSerializerTest {

	public NewMessage createMessage(){
		final NewMessage m = new NewMessage();
		final NewSendable sendable = new NewSendable(SendableType.ACTION_ACCELERATE, null);
		final ListNode<NewSendable> node = new ListNode<NewSendable>();
		node.set(sendable);
		m.addSendable(node);
		return m;
	}

	@Test
	public void testSerialize_moreThanTwoBytes() {
		final CompressedObjectSerializer cos = new CompressedObjectSerializer(1024);		
		final byte[] serializedMessage = cos.serializeNewMessage(createMessage());
		assertTrue(serializedMessage.length>2);
	}

	@Test
	public void testSerializeAndDeserializeMessage_SameMessage() {
		final CompressedObjectSerializer cos = new CompressedObjectSerializer(1024);
		final ObjectSerializer os = new ObjectSerializer();
		final NewMessage m = createMessage();
		final byte[] serializedMessage = cos.serializeNewMessage(m);
		final byte[] serializedMessageUncompressed = os.serializeNewMessage(m);
		System.out.println("Uncompressed Output Length: "+serializedMessageUncompressed.length+" Bytes");
		System.out.println("Compressed Output Length:   "+serializedMessage.length+" Bytes");
		final NewMessage ma = cos.deserializeNewMessage(serializedMessage);

		//		assertEquals(m.sendables.get(0).type,ma.sendables.get(0).type);
		assertEquals(m, ma);
	}

	@Test
	public void testSerializeAndDeserializeMessage_Different() {
		final CompressedObjectSerializer cos = new CompressedObjectSerializer(1024);

		final NewMessage m = new NewMessage();
		final NewSendable sendable = new NewSendable(SendableType.ACTION_ACCELERATE, null);
		final NewSendable otherSendable = new NewSendable(SendableType.ACTION_ACCELERATE, null);
		final ListNode<NewSendable> node = new ListNode<NewSendable>();
		node.set(sendable);
		m.addSendable(node);

		final byte[] serializedMessage = cos.serializeNewMessage(m);
		final NewMessage ma = cos.deserializeNewMessage(serializedMessage);


		assertNotSame(ma.sendables.iterator().next(), otherSendable);
	}

}
