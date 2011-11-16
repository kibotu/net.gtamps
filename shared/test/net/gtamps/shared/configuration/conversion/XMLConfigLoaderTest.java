package net.gtamps.shared.configuration.conversion;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import net.gtamps.shared.configuration.Configuration;
import net.gtamps.shared.configuration.conversion.XMLConfigLoader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class XMLConfigLoaderTest {

	private static final String XML_FILE_PATH = "../assets/config/";
	private static final String XML_FILE_NAME = "EntityDefs.xml";

	//	@Mock
	//	private InputStream input;
	//
	//	@Mock
	//	private XMLGetter xmlGetter;
	private XMLConfigLoader xMLConfigLoader;

	@Before
	public void createXMLConfigLoader() throws Exception {
		final InputStream input = new BufferedInputStream(new FileInputStream(XML_FILE_PATH + XML_FILE_NAME));
		xMLConfigLoader = new XMLConfigLoader(input);
		//		xMLConfigLoader.xmlGetter = xmlGetter;
	}

	@Test
	public void testLoadConfig() {
		final Configuration config = xMLConfigLoader.loadConfig();
		System.out.println(config);
	}

}
