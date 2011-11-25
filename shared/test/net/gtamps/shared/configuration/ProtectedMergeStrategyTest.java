package net.gtamps.shared.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProtectedMergeStrategyTest {

	@Mock
	private Configuration baseConfig;
	@Mock
	private Configuration otherConfig;
	@Mock
	private Random valueGenerator;

	private final Set<String> sampleKeySet = getStringSet("A", "B");
	private final Set<String> disjointKeySet = getStringSet("X", "Y");
	private Set<String> mergedKeySet;
	private ProtectedMergeStrategy mergeStrategy;


	@Before
	public void createPriorityConfigMerger() throws Exception {
		mergeStrategy = new ProtectedMergeStrategy();
		mergedKeySet = getStringSet(sampleKeySet.toArray(new String[0]));
		mergedKeySet.addAll(disjointKeySet);
	}

	@Test
	public void testMerge_whenInstantiatedWithNull_shouldReturnOtherConfig() {
		// run
		final Configuration mergedConfig = mergeStrategy.merge(null, otherConfig);

		// assert
		assertSame(otherConfig, mergedConfig);
	}

	@Test
	public void testMerge_whenInstantiatedWithNullAndMergedWithNull_shouldReturnNull() {
		// run
		final Configuration mergedConfig = mergeStrategy.merge(null, null);

		// assert
		assertSame(null, mergedConfig);
	}

	@Test
	public void testMerge_whenMergedWithNull_shouldReturnBaseConfig() {
		final Configuration mergedConfig = mergeStrategy.merge(baseConfig, null);
		assertSame(baseConfig, mergedConfig);
	}

	@Test
	public void testMerge_whenMergedWithSelf_shouldReturnSelf() {
		final Configuration mergedConfig = mergeStrategy.merge(baseConfig, baseConfig);
		assertSame(baseConfig, mergedConfig);
	}

	@Test
	public void testMerge_whenMergedWithEqual_shouldReturnEqual() {
		// setup
		when(valueGenerator.nextFloat()).thenReturn((float)Math.random());
		@SuppressWarnings("unchecked")
		final Configuration realConfig = createSomeRealConfiguration(valueGenerator, sampleKeySet);
		final Configuration equalConfig = (Configuration) realConfig.clone();

		// run
		final Configuration mergedConfig = mergeStrategy.merge(realConfig, equalConfig);

		// assert
		assertEquals(realConfig, mergedConfig);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMerge_whenMergedWithSameKeysDifferentContent_shouldReturnEqualToOriginal() {
		// setup
		when(valueGenerator.nextFloat()).thenReturn(1f, getRandomFloatArray(sampleKeySet.size() * disjointKeySet.size()));
		final Configuration realConfig = createSomeRealConfiguration(valueGenerator, sampleKeySet, disjointKeySet);
		final Configuration modConfig = createSomeRealConfiguration(valueGenerator, sampleKeySet, disjointKeySet);

		// run
		final Configuration mergedConfig = mergeStrategy.merge(realConfig, modConfig);

		// assert
		assertEquals(realConfig, mergedConfig);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMerge_whenMergedWithNewKeys_shouldReturnUnion() {
		// setup
		when(valueGenerator.nextFloat()).thenReturn(1f);
		final Configuration realConfig = createSomeRealConfiguration(valueGenerator, sampleKeySet, sampleKeySet);
		final Configuration modConfig = createSomeRealConfiguration(valueGenerator, sampleKeySet, disjointKeySet);
		final Configuration expectedConfig = createSomeRealConfiguration(valueGenerator, sampleKeySet, mergedKeySet);

		// run
		final Configuration mergedConfig = mergeStrategy.merge(realConfig, modConfig);

		// assert
		assertEquals(expectedConfig, mergedConfig);
	}

	@Test
	public void testMerge_whenMergedWithNewSubKeys_shouldReturnEqualToOriginal() {
		// setup
		final Configuration realConfig = createSomeRealConfiguration(valueGenerator, sampleKeySet);
		final Configuration modConfig = createSomeRealConfiguration(valueGenerator, sampleKeySet, disjointKeySet);

		// run
		final Configuration mergedConfig = mergeStrategy.merge(realConfig, modConfig);

		// assert
		assertEquals(realConfig, mergedConfig);
	}

	private Float[] getRandomFloatArray(final int length) {
		final Float[] far = new Float[length];
		for (int i = 0; i < far.length; i++) {
			far[i] = (float) Math.random();
		}
		return far;
	}

	private Configuration createSomeRealConfiguration(final Random valueGenerator, final Set<String>...keySets) {
		final ConfigBuilder builder = ConfigBuilder.buildConfig(new ConfigSource());
		if (keySets.length == 1) {
			for (final String key : keySets[0]) {
				builder.select(key).addValue(valueGenerator.nextFloat()).back();
			}
		} else if (keySets.length > 1) {
			for (final String key : keySets[0]) {
				final Configuration tmpCfg = createSomeRealConfiguration(valueGenerator, Arrays.copyOfRange(keySets, 1, keySets.length));
				builder.select(key).addConfig(tmpCfg).back();
			}
		}
		return builder.getConfig();
	}

	private Set<String> getStringSet(final String... strings) {
		return new HashSet<String>(Arrays.asList(strings));
	}


}
