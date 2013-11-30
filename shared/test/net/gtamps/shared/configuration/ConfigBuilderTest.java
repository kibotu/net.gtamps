package net.gtamps.shared.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ConfigBuilderTest {

    /**
     * Argument matcher for ConfigLiteralBuilders
     */
    class isLiteralBuilderType extends ArgumentMatcher<ConfigBuilder> {
        Class<?> type;

        public isLiteralBuilderType(final Class<?> type) {
            this.type = type;
        }

        @Override
        public boolean matches(final Object argument) {
            return ((ConfigLiteralBuilder) argument).getType() == type;
        }
    }

    /**
     * Mock implementation of abstract testee; contains two sets that can be mocked
     * to verify testee methods which partially rely on local implementations
     */
    private static class MockConfigBuilderImplementation extends ConfigBuilder {
        private final Set<Configuration> configStore;
        private final Set<ConfigBuilder> builderStore;

        protected MockConfigBuilderImplementation(final AbstractConfigSource source, final Set<Configuration> configStore, final Set<ConfigBuilder> builderStore) {
            super(source);
            this.builderStore = builderStore;
            this.configStore = configStore;
        }

        protected MockConfigBuilderImplementation(final AbstractConfigSource source, final ConfigBuilder parent, final Set<Configuration> configStore, final Set<ConfigBuilder> builderStore) {
            super(source, parent);
            this.builderStore = builderStore;
            this.configStore = configStore;
        }

        protected MockConfigBuilderImplementation(final AbstractConfigSource source) {
            this(source, null, null);
        }

        protected MockConfigBuilderImplementation(final AbstractConfigSource source, final ConfigBuilder parent) {
            this(source, parent, null, null);
        }

        @Override
        public Class<?> getType() {
            return null;
        }

        @Override
        public ConfigBuilder addConfig(final Configuration config) {
            configStore.add(config);
            return this;
        }

        @Override
        protected ConfigBuilder addBuilder(final ConfigBuilder cb) throws UnsupportedOperationException {
            builderStore.add(cb);
            return this;
        }

        @Override
        protected ConfigBuilder select(final ConfigKey ckey) {
            return null;
        }

        @Override
        protected Configuration getBuild() {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }

    @Mock
    private ConfigBuilder parent;
    @Mock
    HashSet<Configuration> configStore;
    @Mock
    HashSet<ConfigBuilder> builderStore;
    @Mock
    private AbstractConfigSource source;
    private ConfigBuilder configBuilder;

    @Before
    public void createConfigBuilder() throws Exception {
        configBuilder = new MockConfigBuilderImplementation(source, parent, configStore, builderStore);
    }

    @Test
    public void testBuildConfig_whenSourceNotNull_shouldReturnAUsableConfigBuilder() {
        ConfigBuilder.buildConfig(source).getType();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildConfig_whenSourceIsNull_shouldThrowIllegalArgumentException() {
        ConfigBuilder.buildConfig(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testConfigBuilderConfigSource_whenSourceIsNull_shouldThrowIllegalArgumentException() {
        new MockConfigBuilderImplementation(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigBuilderConfigSourceConfigBuilder_whenSourceIsNull_shouldThrowIllegalArgumentException() {
        new MockConfigBuilderImplementation(null, parent);
    }

    @Test
    public void testAddValueInt_callsAbstract_shouldWorkOK() {
        configBuilder.addValue(0);
    }

    @Test
    public void testAddValueFloat_callsAbstract_shouldWorkOK() {
        configBuilder.addValue(0f);
    }

    @Test
    public void testAddValueBoolean_callsAbstract_shouldWorkOK() {
        configBuilder.addValue(false);
    }

    @Test
    public void testAddValueString_whenNullArgument_shouldWorkOK() {
        configBuilder.addValue((String) null);
    }

    @Test
    public void testAddValueString_whenIntArgument_shouldAddIntLiteralBuilder() {
        configBuilder.addValue(1);
        verify(builderStore).add(literalBuilderType(Integer.class));
    }

    @Test
    public void testAddValueString_whenFloatArgument_shouldAddFloatLiteralBuilder() {
        configBuilder.addValue(1f);
        verify(builderStore).add(literalBuilderType(Float.class));
    }

    @Test
    public void testAddValueString_whenBoolArgument_shouldAddBoolLiteralBuilder() {
        configBuilder.addValue(true);
        verify(builderStore).add(literalBuilderType(Boolean.class));
    }

    @Test
    public void testAddValueString_whenStringArgument_shouldAddStringLiteralBuilder() {
        configBuilder.addValue("string");
        verify(builderStore).add(literalBuilderType(String.class));
    }

    @Test
    public void testAddMap_callsAbstract_shouldWorkOK() {
        configBuilder.addMap();
    }

    @Test
    public void testAddMap_shouldReturnMapBuilderContext() {
        assertEquals(java.util.Map.class, configBuilder.addMap().getType());
    }

    @Test
    public void testSelectString_whenSimpleKey_shouldWorkOK() {
        configBuilder.select("SIMPLE_KEY_STRING");
    }

    @Test
    public void testSelectString_whenComplexKey_shouldWorkOKOrThrowUnsupportedOperationException() {
        try {
            configBuilder.select("SIMPLE_KEY_STRING" + ConfigKey.DELIMITER + "COMPLEX_KEY_SUFFIX");
        } catch (final UnsupportedOperationException e) {
            return;
        }
    }

    @Test
    public void testBack_whenActualParent_shouldReturnParent() {
        assertSame(parent, configBuilder.back());
    }

    /**
     * When parent is null, the configMapBuilder is the root ConfigBuilder,
     */
    @Test
    public void testBack_whenNullParent_shouldReturnSelf() {
        final MockConfigBuilderImplementation localBuilder = new MockConfigBuilderImplementation(source);
        assertSame(localBuilder, localBuilder.back());
    }

    @Test
    public void testGetConfig_callsAbstract_shouldWorkOK() {
        configBuilder.getConfig();
    }

    @Test
    public void testToString_shouldReturnAString() {
        if (configBuilder.toString() == null) {
            fail("toString() must not return null");
        }
    }

    private ConfigBuilder literalBuilderType(final Class<?> type) {
        return argThat(new isLiteralBuilderType(type));
    }


}
