package net.gtamps.android.gtandroid.core.net;

import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.serializer.helper.BinaryConverter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class TcpStream implements IStream {

	private static final String TAG = TcpStream.class.getSimpleName();

	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;

	/**
	 * @see <a
	 *      href="http://stackoverflow.com/questions/2560083/how-to-change-internal-buffer-size-of-datainputstream">BufferSize</a>
	 */
	public TcpStream() {
		initSocket();
	}

	public void initSocket() {
		socket = new Socket();
		try {
			socket.setSendBufferSize(Config.SOCKET_MAX_SEND_BUFFER_SIZE);
			socket.setReceiveBufferSize(Config.SOCKET_MAX_RECEIVE_BUFFER_SIZE);
			socket.setKeepAlive(Config.SOCKET_KEEP_ALIVE_ENABLED);
			socket.setTcpNoDelay(Config.SOCKET_TCP_NO_DELAY);
			socket.setReuseAddress(true);
		} catch (SocketException e) {
			Logger.printException(this, e);
		}
	}

	@Override
	public boolean connect(String host, int port) {
		initSocket();
		if (socket.isConnected())
			return false;
		try {
			socket.connect(new InetSocketAddress(host, port), Config.SOCKET_TIMEOUT);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		} catch (ConnectException e) {
			Logger.i(this, "Connection refused...");
		} catch (java.net.SocketTimeoutException e) {
			Logger.e(this, "Socket Timout!");
		} catch (IOException e) {
			Logger.printException(this, e);
		}
		return socket.isConnected();
	}

	@Override
	public boolean disconnect() {
		if (!socket.isConnected())
			return false;
		try {
			input.close();
			output.close();
			socket.close();
		} catch (IOException e) {
			Logger.printException(this, e);
		}
		return !socket.isConnected();
	}

	@Override
	public boolean send(String message) {
		if (!socket.isConnected())
			return false;
		try {
			output.writeUTF(message);
			Logger.i(this, "has send string " + message.length());
		} catch (IOException e) {
			Logger.printException(this, e);
			return false;
		}
		return true;
	}

	byte[] lengthbytes = new byte[4];

	@Override
	public boolean send(byte[] message, int length) throws SocketException {
		if (!socket.isConnected())
			return false;

		try {

			BinaryConverter.writeIntToBytes(length, lengthbytes);
			output.write(lengthbytes);
			output.write(message, 0, length);
			// Logger.i(this, "has send bytes " + (temp.length));
		} catch (SocketException e){
			Logger.toast(this, "Connection lost, returning to Menu.");
			throw e;
		} catch (IOException e) {
			Logger.printException(this, e);
			return false;
		}
		return true;
	}

	@Override
	public boolean send(byte[] message) throws SocketException {
		return send(message, message.length);
	}

	@Override
	public boolean isConnected() {
		return socket.isConnected();
	}

	@Override
	public DataOutputStream getOutputStream() {
		return output;
	}

	@Override
	public DataInputStream getInputStream() {
		return input;
	}

	int length;
	byte[] lengthInteger = new byte[4];
	byte[] networkInputBuffer = new byte[Config.SOCKET_MAX_RECEIVE_BUFFER_SIZE];

	@Override
	public byte[] receive() {
		try {
			// if less than 4bytes (Integer) are available, wait a while.
			if (input.available() > 4) {
				input.readFully(lengthInteger, 0, 4);
				length = BinaryConverter.readIntFromBytes(lengthInteger);
				// byte[] response = new byte[length];
				// Logger.e(this, "has received " + lengthInteger[0] + " "+
				// lengthInteger[1] + " "+ lengthInteger[2] + " "+
				// lengthInteger[3]);
				// Logger.e(this, "has received " + length + " bytes");
				input.readFully(networkInputBuffer, 0, length);
				return networkInputBuffer;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
