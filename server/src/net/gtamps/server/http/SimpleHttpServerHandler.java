package net.gtamps.server.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.gtamps.GTAMultiplayerServer;
import net.gtamps.server.ControlCenter;
import net.gtamps.shared.game.GameData;
import net.gtamps.shared.game.ServerData;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public final class SimpleHttpServerHandler implements Runnable {

    public static String default_served_file = "index.html";
    
    private static final String RUNNING_GAMES_REQUEST_PATH = "/games/";
    
    private static final Pattern REQUEST_PATTERN = Pattern
            .compile("^GET (/.*) HTTP/1.[01]$");
    
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private final File documentDir;
    private final Socket socket;
    private final boolean log;

    public SimpleHttpServerHandler(final Socket socket, final File documentDir, final boolean log) {
        this.socket = socket;
        this.documentDir = documentDir;
        this.log = log;
    }

    private String readRequestPath() throws IOException {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            final String firstLine = reader.readLine();
            if (firstLine == null) {
                return null;
            }
            final Matcher matcher = REQUEST_PATTERN.matcher(firstLine);
            return matcher.matches() ? matcher.group(1) : null;
        } catch (final SocketException e) {
            //System.err.println("Socket exception: " + e);
            return null;
        }
    }

    private OutputStream sendResponseHeaders(final int status, final String message,
                                             final long len) throws IOException {
        try {
            final StringBuffer response = new StringBuffer();
            response.append("HTTP/1.0 ");
            response.append(status).append(' ').append(message).append("\r\n");
            response.append("Content-Length: ").append(len).append("\r\n\r\n");
            final OutputStream out = socket.getOutputStream();
            out.write(response.toString().getBytes());
            out.flush();
            return out;
        } catch (final SocketException e) {
            throw new IOException(e);
        }
    }

    private int sendErrorResponse(final int status, final String message)
            throws IOException {
        final OutputStream out = sendResponseHeaders(status, message,
                message.length());
        out.write(message.getBytes());
        out.flush();
        return status;
    }

    private long sendFile(final File file) throws IOException {
        final long len = file.length();
        final OutputStream out = sendResponseHeaders(200, "OK", len);
        final InputStream in = new FileInputStream(file);
        try {
            final byte[] buffer = new byte[1024];
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
    @Override
	public void run() {
        // initialize logging information
        final long time = System.currentTimeMillis();
        int status = 200;
        long len = 0;
        final String host = socket.getInetAddress().getHostName();
        String path = null;
        // handle request
        try {
            path = readRequestPath();
            if (path == null) {
                status = sendErrorResponse(400, "Bad Request");
            } else if (path.equals(RUNNING_GAMES_REQUEST_PATH)) {
            	len = sendString(getRunningGamesAsString());
            } else {
                if (path.equals("/")) {
                    path = default_served_file;
                }
                final File file = new File(documentDir, path);
                if (!file.getAbsolutePath().startsWith(
                        documentDir.getAbsolutePath())
                        || (file.exists() && (!file.isFile() || !file.canRead()))) {
                    // only allow readable files under document root
                    status = sendErrorResponse(403, "Forbidden");
                } else if (!file.exists()) {
                    status = sendErrorResponse(404, "Not Found");
                } else {
                    len = sendFile(file);
                }
            }
        } catch (final IOException e) {
            System.err.println("Error while serving request for [" + path
                    + "] from [" + host + "]: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (final IOException e) {
                System.err.println("Error while closing socket to " + host
                        + ": " + e.getMessage());
            }
        }
        if (log) {
            final StringBuffer sb = new StringBuffer();
            sb.append(DATE_FORMAT.format(new Date(time))).append(' ');
            sb.append(host).append(' ');
            sb.append(path == null ? "" : path).append(' ');
            sb.append(status).append(' ');
            sb.append(len).append(' ');
            sb.append(System.currentTimeMillis() - time);
            System.out.println(sb);
        }
    }

	private long sendString(final String sendMe) throws IOException {
		final byte[] bytes = sendMe.getBytes();
        final long len = bytes.length;
        final OutputStream out = sendResponseHeaders(200, "OK", len);
        final InputStream in = new ByteArrayInputStream(bytes);
        try {
            final byte[] buffer = new byte[1024];
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

	private String getRunningGamesAsString() {
		final ControlCenter cc  = GTAMultiplayerServer.getControlCenter();
		final ServerData srvData = GTAMultiplayerServer.getGameServerData();
		List<GameData> games = null;
		if (cc != null) {
			games = cc.getGames();
		}
		final Element serverElement = getServerElement(srvData);
		final Element gamesElement = getGamesElement(games);
		serverElement.addContent(gamesElement);
		final String string = xmlElementToDocString(serverElement);
		return string;
	}
	
	private String xmlElementToDocString(final Element element) {
		final Document doc = new Document(element);
		final XMLOutputter xmlOutput = new XMLOutputter();
		final String docString = xmlOutput.outputString(doc);
		return docString;
	}
	
	private Element getGamesElement(final List<GameData> games) {
		final Element gamesRootElement = new Element("GAMES");
		if (games != null) {
			for (final GameData gameData : games) {
				gamesRootElement.addContent(getGameDataElement(gameData));
			}
		}
		return gamesRootElement;
	}

	private Element getServerElement(final ServerData srvData) {
		return getElementFromObject("SERVER", srvData);
	}
	
	private Element getGameDataElement(final GameData gameData) {
		return getElementFromObject("GAME", gameData);
	}
	
	private Element getElementFromObject(final String name, final Object o) {
		final Element element = new Element(name);
		for (final Field field : o.getClass().getFields()) {
			try {
				element.setAttribute(field.getName(), field.get(o).toString());
			} catch (final IllegalArgumentException e) {
				System.err.println("Error translating GameData to XML");
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				System.err.println("Error translating GameData to XML");
				e.printStackTrace();
			}
		}
		return element;
	}

}
