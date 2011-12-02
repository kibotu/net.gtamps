package net.gtamps.shared.serializer.communication;

import static org.junit.Assert.*;

import net.gtamps.shared.serializer.communication.data.FloatData;

import org.junit.Test;

public class CompressedObjectSerializerTest {

	public Message createMessage(){
		Message m = new Message();
		Sendable sendable = new Sendable(SendableType.ACTION_ACCELERATE, new FloatData(0.9f));
		m.addSendable(sendable);
		return m;
	}
	
	@Test
	public void testSerialize_moreThanTwoBytes() {
		CompressedObjectSerializer cos = new CompressedObjectSerializer(1024);		
		byte[] serializedMessage = cos.serializeMessage(createMessage());
		assertTrue(serializedMessage.length>2);
	}
	
	@Test
	public void testSerializeAndDeserializeMessage_SameMessage() {
		CompressedObjectSerializer cos = new CompressedObjectSerializer(1024);
		ObjectSerializer os = new ObjectSerializer();
		Message m = createMessage();
		byte[] serializedMessage = cos.serializeMessage(m);
		byte[] serializedMessageUncompressed = os.serializeMessage(m);
		System.out.println("Uncompressed Output Length: "+serializedMessageUncompressed.length+" Bytes");
		System.out.println("Compressed Output Length:   "+serializedMessage.length+" Bytes");
		Message ma = cos.deserializeMessage(serializedMessage);
		
		assertEquals(m,ma);
	}
	
	@Test
	public void testSerializeAndDeserializeMessage_Different() {
		CompressedObjectSerializer cos = new CompressedObjectSerializer(1024);
		
		Message m = new Message();
		Sendable sendable = new Sendable(SendableType.ACTION_ACCELERATE, new FloatData(0.9f));
		Sendable otherSendable = new Sendable(SendableType.ACTION_ACCELERATE, new FloatData(0.1f));
		m.addSendable(sendable);
		
		byte[] serializedMessage = cos.serializeMessage(m);
		Message ma = cos.deserializeMessage(serializedMessage);
		
		assertNotNull(ma.sendables.get(0));
		
		assertNotSame(ma.sendables.get(0),otherSendable);
	}

}
