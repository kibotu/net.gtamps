package net.gtamps.shared.configuration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigListBuilderTest {

	private static final String SAMPLE_STRING = "SAMPLE_STRING";

	@Mock
	private ConfigBuilder parent;

	@Mock
	private ConfigBuilder otherBuilder;

	@Mock
	private ConfigSource source;

	@Mock
	private Configuration sampleConfig;

	private ConfigListBuilder configListBuilder;

	@Before
	public void createConfigListBuilder() throws Exception {
		configListBuilder = new ConfigListBuilder(source, parent);
	}

	@Test
	public void testGetType_shouldReturnTypeOfConfigListTYPE() {
		assertSame(ConfigList.TYPE, configListBuilder.getType());
	}

	@Test
	public void testAddConfig_shouldReturnSelf() {
		assertSame(configListBuilder, configListBuilder.addConfig(null));
	}

	@Test
	public void testAddConfig_whenNotNull_shouldIncludeConfigInBuiltConfiguration() {
		// setup
		final Configuration testConfig = sampleConfig;
		when(testConfig.iterator()).thenReturn(getSingletonIterator(testConfig));

		// run
		configListBuilder.addConfig(testConfig);

		// assert
		final boolean found = itemIsInBuildOutput(testConfig, configListBuilder);
		assertTrue("test configuration was not added to final output", found);
	}

	@Test
	public void testAddBuilder_whenNotNull_shouldQueryBuilderOnGetConfig() {
		configListBuilder.addBuilder(otherBuilder);

		configListBuilder.getConfig();
		verify(otherBuilder).getBuild();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddBuilder_whenArgumentIsParent_shouldThrowIllegalArgumentException() {
		configListBuilder.addBuilder(configListBuilder.parent);
	}

	@Test
	public void testSelectConfigKey_whenSimpleStringKey_shouldBehaveLikeSelectString() {
		testSelectString_whenSimpleStringKey_shouldTreatStringAsIntIndex();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConfigListBuilder_whenSourceIsNull_shouldThrowIllegalArgumentException() {
		new ConfigListBuilder(null, parent);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSelectInt_whenNegative_shouldThrowIllegalArgumentException() {
		configListBuilder.select(-1);
	}

	@Test
	public void testAddValueInt_shouldIncludeElementInBuildOutput() {
		// setup
		final int testValue = 1;

		// run
		configListBuilder.addValue(testValue);

		// assert
		final boolean found = configListBuilder.getConfig().iterator().next().getInt() == testValue;
		assertTrue("test configuration was not added to final output", found);
	}

	@Test
	public void testAddValueFloat() {
		// setup
		final float testValue = 1f;

		// run
		configListBuilder.addValue(testValue);

		// assert
		final boolean found = configListBuilder.getConfig().iterator().next().getFloat() == testValue;
		assertTrue("test configuration was not added to final output", found);

	}

	@Test
	public void testAddValueBoolean() {
		// setup
		final boolean testValue = false;

		// run
		configListBuilder.addValue(testValue);

		// assert
		final boolean found = configListBuilder.getConfig().iterator().next().getBoolean() == testValue;
		assertTrue("test configuration was not added to final output", found);

	}

	@Test
	public void testAddValueString() {
		// setup
		final String testValue = SAMPLE_STRING;

		// run
		configListBuilder.addValue(testValue);

		// assert
		final boolean found = configListBuilder.getConfig().iterator().next().getString().equals(testValue);
		assertTrue("test configuration was not added to final output", found);

	}

	@Test
	public void testAddMap() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectString_whenSimpleStringKey_shouldTreatStringAsIntIndex() {
		// setup
		final int testIndex = 0;
		final String properIndex = Integer.toString(testIndex);
		configListBuilder.addValue(99);

		// run
		configListBuilder.select(properIndex);


		// setup
		final String noInt = "Not An Integer";

		// run and assert
		try {
			configListBuilder.select(noInt);
		} catch (final IllegalArgumentException e) {
			return;
		}
		fail("IllegalArgumentException expected for non-integer String argument");
	}

	@Test
	public void testGetConfig() {
		fail("Not yet implemented");
	}

	private boolean itemIsInBuildOutput(final Configuration testConfig, final ConfigListBuilder testeeBuilder) {
		final Iterator<Configuration> it = testeeBuilder.getConfig().iterator();
		boolean found = false;
		while(it.hasNext()) {
			if (it.next() == testConfig) {
				found = true;
			}
		}
		return found;
	}

	private <T> Iterator<T> getSingletonIterator(final T soleElement) {
		return Collections.singleton(soleElement).iterator();
	}



}
