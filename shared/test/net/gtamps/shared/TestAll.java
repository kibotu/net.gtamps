package net.gtamps.shared;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	SharedObjectTest.class,
	net.gtamps.shared.configuration.ConfigListBuilderTest.class 
})
public class TestAll {
}