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
		testee.select("MULTISTRING").addValue("first");
		testee.select("NESTED_MAP").addSubConfiguration().getSelected().select("KEY_OF_NESTED_MAP").addValue(Float.POSITIVE_INFINITY);
		testee.select("MULTISTRING").addValue("second");
		testee.select("CATEGORY_LIST").addSubConfiguration().addSubConfiguration();
		testee.getSelected().select("0").select("KEY_IN_FIRST_SUBCONF").addValue(0xFF);
		testee.getSelected().select("1").select("KEY_IN_SECOND_SUBCONF").addValue(0xFF & 0x10);
		final Configuration cfg = testee.fix().getConfiguration();
		System.out.println(cfg);
	}

}
