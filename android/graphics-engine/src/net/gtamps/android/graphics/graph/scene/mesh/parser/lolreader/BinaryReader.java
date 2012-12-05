package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

import android.content.res.Resources;
import net.gtamps.android.graphics.utils.Registry;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 10:17
 */
public class BinaryReader {

    private DataInputStream stream;
    private int readBytes;

    public BinaryReader(String resourceID) {
        readBytes = 0;
        final Resources resources = Registry.getContext().getResources();
        stream = new DataInputStream(new BufferedInputStream(resources.openRawResource(resources.getIdentifier(resourceID, null, resourceID.split(":")[0]))));
    }

    public void close() throws IOException {
        stream.close();
    }

    public int readInt() throws IOException {
        readBytes += 4;
        return stream.read() | (stream.read() << 8) | (stream.read() << 16) | (stream.read() << 24);
    }

    public int readChar() throws IOException {
        readBytes += 1;
        return stream.read();
    }

    public int readShort() throws IOException {
        readBytes += 2;
        return (stream.read() | (stream.read() << 8));
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public String readString(final int length) throws IOException {
        readBytes += length;
        final byte[] bytes = new byte[length];
        stream.read(bytes, 0, length);
        return new String(bytes).trim();
    }

    public void skip(int length) throws IOException {
        readBytes += length;
        stream.skip(length);
    }

    public void skipTo(int offset) throws IOException {
        int toSkipBytes = offset - readBytes;
        if (toSkipBytes > 0) {
            readBytes += toSkipBytes;
            stream.skip(toSkipBytes);
        }
    }
}
