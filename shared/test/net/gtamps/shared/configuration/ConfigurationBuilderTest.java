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
		testee.select("SINGLEBOOL").addValue(true);

		// add empty subconfig that, before being removed on build, will produce a list,
		// which should however shrink back to the single boolean
		testee.select("SINGLEBOOL").addSubConfiguration();

		testee.select("MULTISTRING").addValue("first");
		testee.select("NESTED_MAP").addSubConfiguration().get().select("KEY_OF_NESTED_MAP").addValue(Float.POSITIVE_INFINITY);
		testee.select("MULTISTRING").addValue("second");

		// make a list of maps
		testee.select("CATEGORY_LIST").addSubConfiguration().addSubConfiguration();
		testee.get().select("0").get().select("KEY_IN_FIRST_SUBCONF").addValue(0xFF);
		testee.get().select("1").get().select("KEY_IN_SECOND_SUBCONF").addValue(0xFF & 0x10);

		final Configuration cfg = testee.fix().getConfiguration();
		System.out.println(cfg);
	}

}
