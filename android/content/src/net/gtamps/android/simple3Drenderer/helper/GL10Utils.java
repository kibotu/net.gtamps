package net.gtamps.android.simple3Drenderer.helper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GL10Utils {
	public static FloatBuffer floatsToFloatBuffer(float[] f){
		ByteBuffer bb = ByteBuffer.allocateDirect(f.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = bb.asFloatBuffer();
		floatBuffer.put(f);
		floatBuffer.position(0);
		return floatBuffer;
	}
}
