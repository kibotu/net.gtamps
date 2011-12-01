package net.gtamps.shared.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigLiteralStringTest {

	@Mock
	private AbstractConfigSource source;

	private final String string = "some_string";
	private ConfigLiteralString configLiteralString;

	@Before
	public void createConfigLiteralString() throws Exception {
		configLiteralString = new ConfigLiteralString(string, source);
	}

	@Test
	public void testConfigLiteralString_whenStringNull_shouldInitializeAsEmptyString() {
		final ConfigLiteralString cls = new ConfigLiteralString(null, source);
		assertEquals("", cls.getString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConfigLiteralString_whenSourceNull_expectException() {
		new ConfigLiteralString(string, null);
	}

	@Test
	public void testGetType_shouldReturnJavaLangString() {
		assertSame(java.lang.String.class, configLiteralString.getType());
	}

	@Test
	public void testGetString_shouldReturnValue() {
		assertEquals(string, configLiteralString.getString());
	}

}
