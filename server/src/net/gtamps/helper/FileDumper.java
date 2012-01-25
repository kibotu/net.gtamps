package net.gtamps.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileDumper {
	public static void writeBytesToFile(String filename , final byte[] bytes, final int length){
		try {
			final String basefilename = new String(filename);
			filename = filename+"000";
			File f = new File(filename);
			if(f.exists()){
				final int nr = Integer.parseInt(filename.substring(filename.length()-3));
				f = new File(filename+(nr+1));
			}
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
