package net.gtamps.android.core.utils;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

final public class XmlLoader {

    private static final String TAG = XmlLoader.class.getSimpleName();
    private static final SAXBuilder saxBuilder = new SAXBuilder();

    // static
    private XmlLoader() {
    }

    /**
     * Basically loads a XML File by Resource Id.
     *
     * @param context
     * @param resourceId
     * @return document
     */
    public static Document loadXml(final Context context, final int resourceId) {
        assert context != null;
        Document document = null;
        try {
            document = XmlLoader.saxBuilder.build(new DataInputStream(new BufferedInputStream(context.getResources().openRawResource(resourceId))));
        } catch (final IOException e) {
            Utils.log(XmlLoader.TAG, e.getMessage());
        } catch (final NotFoundException e) {
            Utils.log(XmlLoader.TAG, e.getMessage());
        } catch (final JDOMException e) {
            Utils.log(XmlLoader.TAG, e.getMessage());
        }
        return document;
    }

    /**
     * Loads a XML Document by URL.
     *
     * @param url
     * @param method
     * @return document
     */
    public static Document getDocumentByUrl(final String url, final HttpConnectionHandler.RequestMethod method) {
        assert url != null;
        assert method != null;
        Document document = null;
        try {
            document = XmlLoader.saxBuilder.build(HttpConnectionHandler.openHttpConnection(url, method));
        } catch (final IOException e) {
            Utils.log(XmlLoader.TAG, "IOException:" + e.getMessage());
        } catch (final JDOMException e) {
            Utils.log(XmlLoader.TAG, "JDOMException: " + e.getMessage());
        }
        return document;
    }
}
