package net.gtamps.shared.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigMapTest {

	private static final String COMPLEX_KEY_ELEMENT = "IS_COMPLEX";

	private static final String SIMPLE_SAMPLE_KEY = "key";

	@Mock
	private ConfigSource source;

	@Mock
	private Configuration config, configOther;


	//	@Mock
	//	private Collection<V> values;
	private ConfigMap configMap;

	@Before
	public void createConfigMap() throws Exception {
		configMap = new ConfigMap(source);
	}

	@Test
	public void testConfigMap_shouldSetSourceToPassedArgument() {
		assertSame(source, configMap.getSource());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConfigMap_whenNullArgument_shouldThrowIllegalArgumentException() {
		new ConfigMap(null);
	}

	@Test
	public void testConfigMap_shouldInitializeCountWithZero() {
		assertEquals(0, configMap.getCount());
	}


	@Test
	public void testGetCount_whenElementAdded_shouldIncreaseByOne() {
		//setup
		final int initialCount = configMap.getCount();
		configMap.putConfiguration(SIMPLE_SAMPLE_KEY, config);
		//run
		final int postAddCount = configMap.getCount();

		//assert
		assertEquals(initialCount + 1, postAddCount);
	}

	@Test
	public void testGetCount_whenElementRemoved_shouldDecreaseByOne() {
		//setup
		configMap.putConfiguration(SIMPLE_SAMPLE_KEY, config);
		final int initialCount = configMap.getCount();
		configMap.removeConfiguration(SIMPLE_SAMPLE_KEY);

		//run
		final int postCount = configMap.getCount();

		//assert
		assertEquals(initialCount - 1, postCount);
	}

	@Test
	public void testSelectString_whenSimpleKey_shouldReturnElementInsertedWithThatKey() {
		//setup
		final String key = SIMPLE_SAMPLE_KEY;
		final Configuration testConfig = config; 
		configMap.putConfiguration(key, testConfig);

		//run
		final Configuration selected = configMap.select(key);

		//assert
		assertSame(testConfig, selected);
	}

	@Test
	public void testSelectString_whenDeepKey_shouldReturnElementInsertedWithThatKey() {
		//setup
		final String key = SIMPLE_SAMPLE_KEY + ConfigKey.DELIMITER + COMPLEX_KEY_ELEMENT;
		final Configuration testConfig = config; 
		final ConfigMap deepMap = new ConfigMap(source);
		deepMap.putConfiguration(COMPLEX_KEY_ELEMENT, testConfig);
		configMap.putConfiguration(SIMPLE_SAMPLE_KEY, deepMap);

		//run
		final Configuration selected = configMap.select(key);

		//assert
		assertSame(testConfig, selected);
	}


	@Test(expected=IllegalArgumentException.class)
	public void testSelectInt_whenNegativeIndex_shouldThrowIllegalArgumentException() {
		configMap.select(-1);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testSelectInt_whenTooLargeIndex_shouldThrowIndexOutOfBoundsException() {
		assertEquals("test precondition not met; ", 0, configMap.getCount());
		configMap.select(0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetObject_shouldThrowIllegalArgumentException() {
		configMap.get(new Object());
	}

	@Test
	public void testGetString_shouldNotReturnNull() {
		if (configMap.getString() == null) {
			fail("getString() should not return 'null'");
		}
	}

	@Test
	public void testGetType_shouldReturnJavaUtilMap() {
		if (configMap.getType() != java.util.Map.class) {
			fail("getType() should return java.util.Map.class");
		}
	}

	@Test
	public void testGetSource_shouldNotReturnNull() {
		if (configMap.getString() == null) {
			fail("getString() should not return 'null'");
		}
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testEntrySet_shouldBeImmutable() {
		configMap.entrySet().add(null);
	}

	@Test
	public void testGetInt_shouldReturnNull() {
		assertEquals(null, configMap.getInt());
	}

	@Test
	public void testGetFloat_shouldReturnNull() {
		assertEquals(null, configMap.getFloat());
	}

	@Test
	public void testGetBoolean_shouldReturnNull() {
		assertEquals(null, configMap.getBoolean());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testGetKeys_shouldBeImmutable() {
		configMap.getKeys().add(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testIterator_shouldNotSupportRemove() {
		final Iterator<Configuration> it = getDefaultOneElementConfigMap().iterator();
		it.next();

		it.remove();
	}

	@Test
	public void testClone_shouldNotThrowCloneableNotImplemented() {
		configMap.clone();
	}

	@Test
	public void testClone_shouldReturnADeepCopy() {
		getOneElementConfigMap(SIMPLE_SAMPLE_KEY, config).clone();

		verify(config).clone();
	}


	private ConfigMap getDefaultOneElementConfigMap() {
		return getOneElementConfigMap(SIMPLE_SAMPLE_KEY, config);
	}

	private ConfigMap getOneElementConfigMap(final String key, final Configuration value) {
		final ConfigMap clone = configMap.clone();
		clone.putConfiguration(key, value);
		return clone; 
	}

}
