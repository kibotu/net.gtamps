/*
 * created automatically; ALL MANUAL CHANGES TO THIS FILE WILL LIKELY BE OVERWRITTEN!
 * on Sat 2011-11-26 12:02:51 PM 
 * createTestSuite.py
 */

package net.gtamps.shared;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	net.gtamps.shared.SharedObjectTest.class,
	net.gtamps.shared.game.handler.HandlerTest.class,
	net.gtamps.shared.configuration.ConfigMapTest.class,
	net.gtamps.shared.configuration.ProtectedMergeStrategyTest.class,
	net.gtamps.shared.configuration.ConfigListBuilderTest.class,
	net.gtamps.shared.configuration.ConfigSourceTest.class,
	net.gtamps.shared.configuration.ConfigBuilderTest.class,
	net.gtamps.shared.configuration.ConfigMapBuilderTest.class,
	net.gtamps.shared.configuration.MergeConfigurationTest.class,
	net.gtamps.shared.configuration.conversion.XMLConfigLoaderTest.class,

})
public class TestAll {
}
