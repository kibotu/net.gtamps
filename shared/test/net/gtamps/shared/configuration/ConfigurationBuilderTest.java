package net.gtamps.shared.configuration;

import org.junit.Before;
import org.junit.Test;

public class ConfigurationBuilderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testWhole() {
		final ConfigurationBuilder testee = ConfigurationBuilder.buildConfig(new ConfigSource());
		testee.select("SINGLEBOOL").addBool(true);
		testee.select("MULTISTRING").addString("first");
		testee.select("NESTED_MAP").addSubConfiguration().getSelected().select("KEY_OF_NESTED_MAP").addFloat(Float.POSITIVE_INFINITY);
		testee.select("MULTISTRING").addString("second");
		final Configuration cfg = testee.fix().getConfiguration();
		System.out.println(cfg);
	}

}
