package net.gtamps.android.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

final public class HttpConnectionHandler {

    private static final String TAG = HttpConnectionHandler.class
            .getSimpleName();

    public enum RequestMethod {
        GET, POST
    }

    // static class
    private HttpConnectionHandler() {
    }

    /**
     * Opens up a connection to a certain HTTP Server via ARequest Method
     *
     * @param urlString
     * @param method
     * @return InputStream
     * @throws java.io.IOException
     */
    public static InputStream openHttpConnection(final String urlString,
                                                 final RequestMethod method) throws IOException {
        assert urlString != null;
        assert method != null;
        InputStream in = null;
        int response = -1;
        final URL url = new URL(urlString);
        final URLConnection conn = url.openConnection();
        if (!(conn instanceof HttpURLConnection)) {
            throw new IOException("Not an HTTP connection");
        }
        try {
            final HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod(method.name());
            httpConn.connect();

            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                Utils.log(HttpConnectionHandler.TAG, "" + response);
                in = httpConn.getInputStream();
            }
        } catch (final IOException e) {
            Utils.log(HttpConnectionHandler.TAG, "IOException:" + e.getMessage());
        }
        return in;
    }
}
