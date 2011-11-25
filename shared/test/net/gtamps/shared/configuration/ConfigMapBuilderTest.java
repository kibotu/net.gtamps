package net.gtamps.shared.configuration;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigMapBuilderTest {

	private static final String COMPLEX_KEY_PART = "COMPLEX_KEY_PART";

	private static final String SIMPLE_KEY = "SIMPLE_KEY";

	@Mock
	private ConfigBuilder parent;

	@Mock
	private AbstractConfigSource source;

	@Mock
	private Configuration sampleConfig;

	private ConfigMapBuilder configMapBuilder;

	@Before
	public void createConfigMapBuilder() throws Exception {
		configMapBuilder = new ConfigMapBuilder(source, parent);
	}

	@Test
	public void testConfigMapBuilderSource_ValidSource_shouldNotSetAParent() {
		assertSame(null, (new ConfigMapBuilder(source)).parent);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConfigMapBuilder_NullSource_shouldThrowIllegalArgumentException() {
		new ConfigMapBuilder(null, parent);
	}

	@Test
	public void testGetType_shouldReturnConfigMapTYPE() {
		assertSame(ConfigMap.TYPE, configMapBuilder.getType());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddConfig_shouldThrowUnsupportedOperationException() {
		configMapBuilder.addConfig(sampleConfig);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddBuilder_shouldThrowUnsupportedOperationException() {
		configMapBuilder.addBuilder(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSelectConfigKey_whenNullArgument_shouldThrowIllegalArgumentException() {
		configMapBuilder.select((ConfigKey)null);
	}

	/**
	 * Selecting an invalid key should create and return a config builder
	 * that can be added to multiple times and that stays reachable under that key.
	 */
	@Test
	public void testSelectStringKey_whenInvalidKey_shouldReturnAndRetainUsableConfigBuilder() {
		final ConfigBuilder selected = configMapBuilder.select(SIMPLE_KEY);
		selected.addValue(true);
		selected.addValue(9);
		assertEquals(selected, configMapBuilder.select(SIMPLE_KEY));
	}

	@Test
	public void testSelectStringKey_whenComplexKey_shouldBehaveAsSimpleKeyOrThrowUnsupportedOperationException() {
		ConfigBuilder selected; 
		try {
			selected = configMapBuilder.select(createStandardComplexKey());
		} catch (final UnsupportedOperationException e) {
			return;
		}
		selected.addValue(true);
		selected.addValue(9);
		assertEquals(selected, configMapBuilder.select(SIMPLE_KEY));
	}

	private String createStandardComplexKey() {
		return SIMPLE_KEY + ConfigKey.DELIMITER + COMPLEX_KEY_PART;
	}

	@Test
	public void testGetConfig_whenNoElements_shouldReturnNull() {
		assertEquals(null, configMapBuilder.getConfig());
	}

	//	@Test
	//	public void testGetConfig_whenContainsElements_shouldReturnValidConfiguration() {
	//		fail("Not yet implemented");
	//	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddValueInt_shouldThrowUnsupportedOperationException() {
		configMapBuilder.addValue(0);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddValueFloat_shouldThrowUnsupportedOperationException() {
		configMapBuilder.addValue(0f);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddValueBoolean_shouldThrowUnsupportedOperationException() {
		configMapBuilder.addValue(true);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddValueString_shouldThrowUnsupportedOperationException() {
		configMapBuilder.addValue(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddMap_shouldThrowUnsupportedOperationException() {
		configMapBuilder.addMap();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSelectString_whenNull_shouldThrowIllegalArgumentException() {
		configMapBuilder.select((String)null);
	}

	@Test
	public void test_whenParentNotNull_shouldReturnParent() {
		assertSame(parent, configMapBuilder.back());
	}

	/**
	 * When parent is null, the configMapBuilder is the root ConfigBuilder,
	 * and back() should not move the building context nor return null.
	 * 
	 */
	@Test
	public void test_whenParentIsNull_shouldReturnSelf() {
		final ConfigMapBuilder localBuilder = new ConfigMapBuilder(source, null);
		assertSame(localBuilder, localBuilder.back());
	}

	@Test
	public void testConfigMapBuilder()
	{

	}

}
