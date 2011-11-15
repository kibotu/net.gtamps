package net.gtamps.shared.configuration;

import java.io.IOException;
import java.io.InputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XMLConfigLoader implements ConfigLoader {

	private final InputStream input;
	private final ConfigSource configSource;

	public XMLConfigLoader(final InputStream input) {
		this.input = input;
		// TODO
		this.configSource = new ConfigSource();
	}

	//TODO better exception handling
	@Override
	public Configuration loadConfig() throws RuntimeException {
		Document xmldoc;
		try {
			xmldoc = getDocument(this.input);
		} catch (final JDOMException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		} 
		final Configuration config = createConfiguration(xmldoc.getRootElement());
		return config;
	}

	private Configuration createConfiguration(final Element rootElement) {
		// TODO Auto-generated method stub
		return null;
	}

	private Document getDocument(final InputStream in) throws JDOMException, IOException {
		final SAXBuilder saxBuilder = new SAXBuilder();
		final Document doc = saxBuilder.build(in);
		return doc;
	}

}
