package net.gtamps.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileDumper {
	private static final String DUMP_DIR = "message_dumps/";

	public static void writeBytesToFile(String filename , final byte[] bytes, final int length){
		try {
			filename = DUMP_DIR+filename+("_"+System.currentTimeMillis()).substring(5);
			final File f = new File(filename);
			final FileOutputStream fos = new FileOutputStream(f);
			fos.write(bytes,0,length);
			fos.close();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static byte[] readBytesFromFile(final String filename){
		try {
			final FileInputStream fos = new FileInputStream(new File(filename));
			final byte[] b = new byte[fos.available()];
			fos.read(b);
			fos.close();
			return b;
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
