package net.gtamps.shared.configuration;

import org.junit.Before;
import org.junit.Test;

public class ConfigBuilderManualTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testWhole() {
		final ConfigBuilder testee = ConfigBuilder.buildConfig(new ConfigSource());
		testee.select("SINGLEBOOL").addValue(true);
		testee.select("key").addValue(false).addValue("megalo").addMap().select("innerkey").addValue(true).addValue(0);

		//add no values: this should collapse to nothing on build
		testee.select("mapsquish").addMap().select("something").addMap().select("haha").addMap().select("nix");


		// add empty subconfig that, before being removed on build, will produce a list,
		// which should however shrink back to the single boolean
		testee.select("SINGLEBOOL").addMap();

		testee.select("MULTISTRING").addValue("first");
		testee.select("NESTED_MAP").addMap().select("KEY_OF_NESTED_MAP").addValue(Float.POSITIVE_INFINITY);
		testee.select("MULTISTRING").addValue("second");

		// make a list of maps
		testee.select("CATEGORY").addMap().back().addMap();
		testee.select("CATEGORY").select("0").select("KEY_IN_FIRST_SUBCONF").addValue(0xFF);
		testee.select("CATEGORY").select("1").select("KEY_IN_SECOND_SUBCONF").addValue(0xFF & 0x10);

		final Configuration cfg = testee.getConfig();
		System.out.println(cfg);
	}

}
