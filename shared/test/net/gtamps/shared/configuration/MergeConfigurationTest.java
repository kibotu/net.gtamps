package net.gtamps.shared.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MergeConfigurationTest {

    @Mock
    private Configuration baseConfig;

    @Mock
    private Configuration otherConfig;

    @Mock
    private MergeStrategy mergeStrategy;
    private MergeConfiguration mergeConfiguration;

    @Before
    public void createMergeConfiguration() throws Exception {
        mergeConfiguration = new MergeConfiguration(mergeStrategy, baseConfig);
    }

    @Test
    public void testMergeConfigurationMergeStrategy_shouldCreateEmptyConfig() {
        final MergeConfiguration m = new MergeConfiguration(mergeStrategy);
        assertEquals(0, m.getCount());
    }

    @Test
    public void testMergeConfigurationMergeStrategyConfiguration_whenNullBaseConfiguration_shouldCreateEmptyConfig() {
        final MergeConfiguration m = new MergeConfiguration(mergeStrategy, null);
        assertEquals(0, m.getCount());
    }

    @Test
    public void testMerge_shouldInvokeMergeStrategyWithCorrectParameterInCorrectOrder() {
        // run
        mergeConfiguration.merge(otherConfig);

        // assert
        verify(mergeStrategy).merge(baseConfig, otherConfig);
    }

}
