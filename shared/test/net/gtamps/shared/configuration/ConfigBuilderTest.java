package net.gtamps.shared.configuration;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigBuilderTest {

	private static class MockConfigBuilderImplementation extends ConfigBuilder {
		protected MockConfigBuilderImplementation(final ConfigSource source) {
			super(source);
		}
		protected MockConfigBuilderImplementation(final ConfigSource source, final ConfigBuilder parent) {
			super(source, parent);
		}
		@Override
		public Class<?> getType() {
			return null;
		}
		@Override
		public ConfigBuilder addConfig(final Configuration config) {
			return null;
		}
		@Override
		protected ConfigBuilder addBuilder(final ConfigBuilder cb) throws UnsupportedOperationException {
			return null;
		}
		@Override
		protected ConfigBuilder select(final ConfigKey ckey) {
			return null;
		}
		@Override
		protected Configuration getBuild() {
			return null;
		}
		@Override
		public int getCount() {
			return 0;
		}
	}

	@Mock
	private ConfigBuilder parent;

	@Mock
	private ConfigSource source;
	private ConfigBuilder configBuilder;

	@Before
	public void createConfigBuilder() throws Exception {
		configBuilder = new MockConfigBuilderImplementation(source, parent);
	}

	@Test
	public void testBuildConfig_whenSourceNotNull_shouldReturnAUsableConfigBuilder() {
		ConfigBuilder.buildConfig(source).getType();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testBuildConfig_whenSourceIsNull_shouldThrowIllegalArgumentException() {
		ConfigBuilder.buildConfig(null);
	}


	@Test(expected=IllegalArgumentException.class)
	public void testConfigBuilderConfigSource_whenSourceIsNull_shouldThrowIllegalArgumentException() {
		new MockConfigBuilderImplementation(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConfigBuilderConfigSourceConfigBuilder_whenSourceIsNull_shouldThrowIllegalArgumentException() {
		new MockConfigBuilderImplementation(null, parent);
	}

	@Test
	public void testAddValueInt_callsAbstract_shouldWorkOK() {
		configBuilder.addValue(0);
	}

	@Test
	public void testAddValueFloat_callsAbstract_shouldWorkOK() {
		configBuilder.addValue(0f);
	}

	@Test
	public void testAddValueBoolean_callsAbstract_shouldWorkOK() {
		configBuilder.addValue(false);
	}

	@Test
	public void testAddValueString_whenNullArgument_shouldWorkOK() {
		configBuilder.addValue(null);
	}

	@Test
	public void testAddMap_callsAbstract_shouldWorkOK() {
		configBuilder.addMap();
	}

	@Test
	public void testAddMap_shouldReturnMapBuilderContext() {
		assertEquals(java.util.Map.class, configBuilder.addMap().getType());
	}

	@Test
	public void testSelectString_whenSimpleKey_shouldWorkOK() {
		configBuilder.select("SIMPLE_KEY_STRING");
	}

	@Test
	public void testSelectString_whenComplexKey_shouldWorkOKOrThrowUnsupportedOperationException() {
		try {
			configBuilder.select("SIMPLE_KEY_STRING" + ConfigKey.DELIMITER + "COMPLEX_KEY_SUFFIX");
		} catch (final UnsupportedOperationException e) {
			return;
		}
	}

	@Test
	public void testBack_whenActualParent_shouldReturnParent() {
		assertSame(parent, configBuilder.back());
	}

	/**
	 * When parent is null, the configMapBuilder is the root ConfigBuilder, 
	 */
	@Test
	public void testBack_whenNullParent_shouldReturnSelf() {
		final MockConfigBuilderImplementation localBuilder = new MockConfigBuilderImplementation(source);
		assertSame(localBuilder, localBuilder.back());
	}

	@Test
	public void testGetConfig_callsAbstract_shouldWorkOK() {
		configBuilder.getConfig();
	}

	@Test
	public void testToString_shouldReturnAString() {
		if (configBuilder.toString() == null) {
			fail("toString() must not return null");
		}
	}
}
