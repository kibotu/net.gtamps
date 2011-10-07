package net.gtamps.server.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SimpleHttpServerHandler implements Runnable {
	
	public static String default_served_file = "index.html";
	
	private static final Pattern REQUEST_PATTERN = Pattern
			.compile("^GET (/.*) HTTP/1.[01]$");
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
	"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	private final File documentDir;
	private final Socket socket;
	private final boolean log;
	public SimpleHttpServerHandler(Socket socket, File documentDir, boolean log) {
		this.socket = socket;
		this.documentDir = documentDir;
		this.log = log;
	}
	private String readRequestPath() throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
			this.socket.getInputStream()));
			String firstLine = reader.readLine();
			if (firstLine == null) {
				return null;
			}
			Matcher matcher = REQUEST_PATTERN.matcher(firstLine);
			return matcher.matches() ? matcher.group(1) : null;
		} catch (SocketException e) {
			//System.err.println("Socket exception: " + e);
			return null;
		}
	}
	private OutputStream sendResponseHeaders(int status, String message,
	long len) throws IOException {
		try {
			StringBuffer response = new StringBuffer();
			response.append("HTTP/1.0 ");
			response.append(status).append(' ').append(message).append("\r\n");
			response.append("Content-Length: ").append(len).append("\r\n\r\n");
			OutputStream out = this.socket.getOutputStream();
			out.write(response.toString().getBytes());
			out.flush();
			return out;
		} catch (SocketException e) {
			throw new IOException(e);
		}
	}
	private int sendErrorResponse(int status, String message)
	throws IOException {
		OutputStream out = sendResponseHeaders(status, message,
		message.length());
		out.write(message.getBytes());
		out.flush();
		return status;
	}
	private long sendFile(File file) throws IOException {
		long len = file.length();
		OutputStream out = sendResponseHeaders(200, "OK", len);
		InputStream in = new FileInputStream(file);
		try {
			byte[] buffer = new byte[1024];
			int nread = 0;
			while ((nread = in.read(buffer)) > 0) {
				out.write(buffer, 0, nread);
			}
		} finally {
			in.close();
		}
		out.flush();
		return len;
	}
	// this is the main entry point into this handler
	public void run() {
        // initialize logging information
        long time = System.currentTimeMillis();
        int status = 200;
        long len = 0;
        String host = this.socket.getInetAddress().getHostName();
        String path = null;
        // handle request
        try {
            path = readRequestPath();
            if (path == null) {
                status = sendErrorResponse(400, "Bad Request");
            } else {
            	if(path.equals("/")){
            		path = default_served_file;
            	}
                File file = new File(this.documentDir, path);
                if (!file.getAbsolutePath().startsWith(
                        this.documentDir.getAbsolutePath())
                        || (file.exists() && (!file.isFile() || !file.canRead()))) {
                    // only allow readable files under document root
                    status = sendErrorResponse(403, "Forbidden");
                } else if (!file.exists()) {
                    status = sendErrorResponse(404, "Not Found");
                } else {
                    len = sendFile(file);
                }
            }
        } catch (IOException e) {
//            System.err.println("Error while serving request for [" + path
//                    + "] from [" + host + "]: " + e.getMessage());
//            e.printStackTrace();
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {
                System.err.println("Error while closing socket to " + host
                        + ": " + e.getMessage());
            }
        }
        if (this.log) {
            StringBuffer sb = new StringBuffer();
            sb.append(DATE_FORMAT.format(new Date(time))).append(' ');
            sb.append(host).append(' ');
            sb.append(path == null ? "" : path).append(' ');
            sb.append(status).append(' ');
            sb.append(len).append(' ');
            sb.append(System.currentTimeMillis() - time);
            System.out.println(sb);
        }
    }
}
