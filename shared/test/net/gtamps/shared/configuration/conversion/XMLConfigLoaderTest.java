package net.gtamps.shared.configuration.conversion;

import net.gtamps.shared.configuration.ConfigBuilder;
import net.gtamps.shared.configuration.ConfigSource;
import net.gtamps.shared.configuration.Configuration;
import org.jdom.JDOMException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class XMLConfigLoaderTest {

    private static final String SIMPLE_CONFIG_STRING_XML = "" +
            "<X><CONFIG value=\"true\" more=\"false\"/></X>";

    private static final Configuration SIMPLE_CONFIG = ConfigBuilder.buildConfig(ConfigSource.EMPTY_SOURCE)
            .select("CONFIG").addMap() //
            .select("value").addValue(true).back().select("more").addValue(false).back().back().back() //
            .back().back()
            .getConfig();

    private static final String SAMPLE_CONFIG_STRING_XML = "" +
            "<GTACONFIG>" +
            "	<SERVER>" +
            "		<SETUP>" +
            "			<TCP port=\"8095\"/>" +
            "			<SERIALIZER class=\"net.gtamps.shared.serializer.communication.ObjectSerializer\"/>" +
            "		</SETUP>" +
            "	</SERVER>" +
            "	<game>" +
            "		<setup/>" +
            "		<entities>" +
            "			<entity name=\"car\" abstract=\"true\"/>" +
            "			<entity name=\"taxi\" extends=\"car\"/>" +
            "		</entities>" +
            "	</game>" +
            "</GTACONFIG>";

    private static final Configuration SAMPLE_CONFIG = ConfigBuilder.buildConfig(ConfigSource.EMPTY_SOURCE)
            //	.select("GTACONFIG").addMap()	// loader ignores root element!
            /**/.select("SERVER").addMap()    // each 'select(...)' or 'addMap()' moves the context one level deeper
            /**//**/.select("SETUP").addMap()// and requires one 'back()' each to get back to original building context
            /**//**//**/.select("TCP").addMap().select("port").addValue("8095").back().back().back() // one back for every select or addMap
            /**//**//**/.select("SERIALIZER").addMap().select("class").addValue("net.gtamps.shared.serializer.communication.ObjectSerializer").back().back().back()//
            /**//**/.back().back()    //
            /**/.back().back()        //
            /**/.select("game").addMap()    //
            /**//**/.select("setup").addMap().back().back()    //
            /**//**/.select("entities").addMap()    //
            /**//**//**/.select("entity").addMap().select("name").addValue("car").back().select("abstract").addValue("true").back().back().back()    //
            /**//**//**/.select("entity").addMap().select("name").addValue("taxi").back().select("extends").addValue("car").back().back().back()    //
            /**//**/.back().back()    //
            /**/.back().back()    //
            //	.back().back()	//
            .getConfig();


    @Test
    public void testLoadConfig_fromSimpleString_shouldMatchSimpleConfig() throws JDOMException, IOException {
        // setup
        final InputStream stringInput = generateInputStreamFromString(SIMPLE_CONFIG_STRING_XML);
        final XMLConfigLoader xmlConfigLoader = new XMLConfigLoader(stringInput, new ConfigSource("testing"));

        // run
        final Configuration loadedConfig = xmlConfigLoader.loadConfig();

        // assert
        assertEquals(SIMPLE_CONFIG, loadedConfig);
    }

    @Test
    public void testLoadConfig_fromString_shouldMatchSampleConfig() throws JDOMException, IOException {
        // setup
        final InputStream stringInput = generateInputStreamFromString(SAMPLE_CONFIG_STRING_XML);
        final XMLConfigLoader xmlConfigLoader = new XMLConfigLoader(stringInput, new ConfigSource("testing"));

        // run
        final Configuration loadedConfig = xmlConfigLoader.loadConfig();

        // assert
        assertEquals(SAMPLE_CONFIG, loadedConfig);
    }

    @SuppressWarnings("unused")
    private InputStream generateInputStreamFromFile(final File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    private InputStream generateInputStreamFromString(final String string) throws UnsupportedEncodingException {
        return new ByteArrayInputStream(string.getBytes("UTF-8"));
    }

}
