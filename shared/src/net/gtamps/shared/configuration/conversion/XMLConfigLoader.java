package net.gtamps.shared.configuration.conversion;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import net.gtamps.shared.configuration.ConfigBuilder;
import net.gtamps.shared.configuration.ConfigSource;
import net.gtamps.shared.configuration.Configuration;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XMLConfigLoader implements ConfigLoader {

	XMLGetter xmlGetter = new XMLGetter();

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
		Document xmlDocument;
		try {
			xmlDocument = xmlGetter.getDocument(this.input);
		} catch (final JDOMException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		} 
		final Configuration config = createConfiguration(xmlDocument);
		return config;
	}

	private Configuration createConfiguration(final Document xmlDocument) {
		final ConfigBuilder builderContext = ConfigBuilder.buildConfig(this.configSource);
		final Element rootElement = xmlDocument.getRootElement(); 
		return buildConfigFromElement(rootElement, builderContext);
	}

	@SuppressWarnings("unchecked")
	private Configuration buildConfigFromElement(final Element xmlElement, final ConfigBuilder builderContext) {
		assert builderContext.getType() == Map.class;
		final List<Attribute> attributeList = xmlElement.getAttributes();
		final List<Element> childrenList =  xmlElement.getChildren();
		for (final Attribute attribute : attributeList) {
			builderContext.select(attribute.getName()).addValue(attribute.getValue());
		}
		for (final Element childElement : childrenList) {
			final ConfigBuilder childContext = builderContext.select(childElement.getName()).addMap();
			buildConfigFromElement(childElement, childContext);
		}
		return builderContext.getConfig();
	}

	static class XMLGetter {

		SAXBuilder saxBuilder = null;

		public Document getDocument(final InputStream in) throws JDOMException, IOException {
			if (saxBuilder == null) {
				saxBuilder = new SAXBuilder();
			}
			final Document doc = this.saxBuilder.build(in);
			return doc;
		}
	}
}
